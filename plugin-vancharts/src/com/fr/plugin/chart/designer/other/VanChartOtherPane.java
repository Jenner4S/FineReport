package com.fr.plugin.chart.designer.other;

import com.fr.chart.chartattr.Chart;
import com.fr.chart.chartattr.ChartCollection;
import com.fr.design.dialog.BasicPane;
import com.fr.design.dialog.MultiTabPane;
import com.fr.design.gui.frpane.AttributeChangeListener;
import com.fr.design.mainframe.chart.AbstractChartAttrPane;
import com.fr.design.mainframe.chart.ChartEditPane;
import com.fr.design.mainframe.chart.PaneTitleConstants;
import com.fr.design.mainframe.chart.gui.style.legend.AutoSelectedPane;
import com.fr.general.ComparatorUtils;
import com.fr.stable.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;


public class VanChartOtherPane extends AbstractChartAttrPane {
    private static final long serialVersionUID = 5538256824676045807L;
    protected TabPane otherPane;
    AttributeChangeListener listener;

    protected boolean hasConditionPane = false;

    public VanChartOtherPane(AttributeChangeListener listener){
        super();
        this.listener = listener;
    }

    @Override
    protected JPanel createContentPane() {
        JPanel content = new JPanel(new BorderLayout());
        otherPane = new TabPane();
        content.add(otherPane, BorderLayout.CENTER);
        return content;
    }

    @Override
    public void populate(ChartCollection collection) {
        hasConditionPane = collection.getSelectedChart().getPlot().isSupportDataSeriesCondition();
        this.remove(leftContentPane);
        initContentPane();
        this.removeAttributeChangeListener();
        otherPane.populateBean(collection);
        this.addAttributeChangeListener(listener);
        this.initAllListeners();
    }

    /**
     * 注册 切换按钮的 改变事件, 和超链区分.
     * @param currentChartEditPane  当前图表编辑界面.
     */
    public void registerChartEditPane(ChartEditPane currentChartEditPane) {
        otherPane.registerChartEditPane(currentChartEditPane);
    }

    @Override
    public void update(ChartCollection collection) {
        otherPane.updateBean(collection);
    }

    @Override
    public String getIconPath() {
        return "com/fr/design/images/chart/InterAttr.png";
    }

    /**
     * 界面标题
     * @return 返回标题.
     */
    public String title4PopupWindow() {
        return PaneTitleConstants.CHART_OTHER_TITLE;
    }

    /**
     * 设置选中的界面id
     */
    public void setSelectedByIds(int level, String... id) {
        otherPane.setSelectedByIds(level, id);
    }

    protected class TabPane extends MultiTabPane<ChartCollection> {
        private static final long serialVersionUID = 8602189467609052748L;
        protected VanChartInteractivePane interactivePane;
        protected VanChartConditionAttrPane conditionAttrPane;

        @Override
        protected java.util.List<BasicPane> initPaneList() {
            java.util.List<BasicPane> paneList = new ArrayList<BasicPane>();
            interactivePane = new VanChartInteractivePane();
            paneList.add(interactivePane);

            if(hasConditionPane){
                conditionAttrPane = new VanChartConditionAttrPane();
                paneList.add(conditionAttrPane);
            }

            return paneList;
        }

        @Override
        protected void initLayout() {
            JPanel tabPanel = new JPanel(new BorderLayout());
            tabPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 10, getBackground()));
            tabPanel.add(tabPane, BorderLayout.CENTER);
            this.setLayout(new BorderLayout(0, 4));
            this.add(tabPanel, BorderLayout.NORTH);
            this.add(centerPane, BorderLayout.CENTER);
        }

        @Override
        public void populateBean(ChartCollection ob) {
            interactivePane.populateBean(ob.getSelectedChart());
            if(conditionAttrPane != null){
                conditionAttrPane.populateBean(ob.getSelectedChart());
            }
        }

        @Override
        public void updateBean(ChartCollection ob) {
            if(ob.getSelectedChart() == null) {
                return;
            }
            Chart chart = ob.getSelectedChart();
            interactivePane.updateBean(chart);
            if(conditionAttrPane != null){
                conditionAttrPane.updateBean(chart);
            }
        }

        /**
         * 设置选中的界面id
         */
        public void setSelectedByIds(int level, String... id) {
            for (int i = 0; i < paneList.size(); i++) {
                if (ComparatorUtils.equals(id[level], NameArray[i])) {
                    tabPane.setSelectedIndex(i);
                    tabPane.tabChanged(i);
                    if (id.length >= level + 2) {
                        ((AutoSelectedPane)paneList.get(i)).setSelectedIndex(id[level + 1]);
                    }
                    break;
                }
            }
        }

        /**
         * 注册 切换按钮的切换事件.
         * @param currentChartEditPane 当前编辑的图表编辑界面.
         */
        public void registerChartEditPane(ChartEditPane currentChartEditPane) {
//			chartSwitchPane.registerChartEditPane(currentChartEditPane);
        }

        @Override
        public boolean accept(Object ob) {
            return false;
        }

        @Override
        public String title4PopupWindow() {
            return StringUtils.EMPTY;
        }

        @Override
        public void reset() {

        }

        @Override
        public ChartCollection updateBean() {
            return null;
        }
    }
}
