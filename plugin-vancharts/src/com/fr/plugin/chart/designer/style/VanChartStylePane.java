package com.fr.plugin.chart.designer.style;

import com.fr.chart.chartattr.Chart;
import com.fr.chart.chartattr.ChartCollection;
import com.fr.chart.chartattr.Plot;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.dialog.BasicPane;
import com.fr.design.dialog.MultiTabPane;
import com.fr.design.event.UIObserver;
import com.fr.design.event.UIObserverListener;
import com.fr.design.gui.frpane.AttributeChangeListener;
import com.fr.design.mainframe.chart.PaneTitleConstants;
import com.fr.design.mainframe.chart.gui.ChartStylePane;
import com.fr.design.mainframe.chart.gui.style.legend.AutoSelectedPane;
import com.fr.design.mainframe.chart.gui.style.series.ChartSeriesPane;
import com.fr.general.ComparatorUtils;
import com.fr.general.Inter;
import com.fr.plugin.chart.designer.style.axis.VanChartAxisPane;
import com.fr.plugin.chart.designer.style.background.VanChartAreaPane;
import com.fr.plugin.chart.designer.style.datasheet.VanChartDataSheetPane;
import com.fr.plugin.chart.designer.style.label.VanChartLabelPane;
import com.fr.plugin.chart.designer.style.tooltip.VanChartTooltipPane;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class VanChartStylePane extends ChartStylePane implements UIObserver {

    private static final long serialVersionUID = 186776958263021761L;
    private TabPane stylePane;
    private Chart chart;
    private AttributeChangeListener listener;
    private VanChartTitlePane chartTitlePane;
    private BasicPane chartLegendPane;
    private BasicPane chartSeriesPane;
    private BasicPane chartDataLabelPane;
    private BasicPane chartAreaPane;
    private BasicPane chartTooltipPane;
    private BasicPane chartAxisPane;
    private VanChartDataSheetPane chartDataSheetPane;
    private UIObserverListener uiObserverListener;

    public VanChartStylePane(AttributeChangeListener listener) {
        super();
        this.listener = listener;
        initListPanes();
    }

    private void initListPanes(){
        chartTitlePane = new VanChartTitlePane(VanChartStylePane.this);
        chartLegendPane = new BasicPane() {
            @Override
            protected String title4PopupWindow() {
                return PaneTitleConstants.CHART_STYLE_LEGNED_TITLE;
            }
        };
        chartSeriesPane = new BasicPane() {
            @Override
            protected String title4PopupWindow() {
                return PaneTitleConstants.CHART_STYLE_SERIES_TITLE;
            }
        };
        chartDataLabelPane = new BasicPane() {
            @Override
            protected String title4PopupWindow() {
                return PaneTitleConstants.CHART_STYLE_LABEL_TITLE;
            }
        } ;
        chartAreaPane = new BasicPane() {
            @Override
            protected String title4PopupWindow() {
                return PaneTitleConstants.CHART_STYLE_AREA_TITLE;
            }
        } ;
        chartTooltipPane = new BasicPane() {
            @Override
            protected String title4PopupWindow() {
                return Inter.getLocText("Plugin-Chart_Tooltip");
            }
        };
        chartAxisPane = new BasicPane() {
            @Override
            protected String title4PopupWindow() {
                return PaneTitleConstants.CHART_STYLE_AXIS_TITLE;
            }
        };
        chartDataSheetPane = new VanChartDataSheetPane();
    }

    protected void initSelfListener(Container parentComponent) {
        for (int i = 0; i < parentComponent.getComponentCount(); i++) {
            Component tmpComp = parentComponent.getComponent(i);
            if (tmpComp instanceof Container) {
                initListener((Container) tmpComp);
            }
            if (tmpComp instanceof UIObserver) {
                ((UIObserver) tmpComp).registerChangeListener(uiObserverListener);
            }
        }
    }

    @Override
    protected JPanel createContentPane() {
        JPanel content = new JPanel(new BorderLayout());
        if (chart == null) {
            return content;
        }
        stylePane = new TabPane();
        content.add(stylePane, BorderLayout.CENTER);
        return content;
    }

    @Override
    public void populate(ChartCollection collection) {
        this.chart = collection.getSelectedChart();
        this.remove(leftContentPane);
        initContentPane();
        this.removeAttributeChangeListener();
        stylePane.populateBean(chart);
        this.addAttributeChangeListener(listener);
        this.initAllListeners();
    }


    @Override
    public void update(ChartCollection collection) {
        stylePane.updateBean(collection.getSelectedChart());
    }

    /**
     * 返回对应的图片路径.
     */
    public String getIconPath() {
        return "com/fr/design/images/chart/ChartStyle.png";
    }

    /**
     * 返回界面的标题名称
     * @return  返回名称.
     */
    public String title4PopupWindow() {
        return PaneTitleConstants.CHART_STYLE_TITLE;
    }

    /**
     * 设置选中的界面id
     */
    public void setSelectedByIds(int level, String... id) {
        stylePane.setSelectedByIds(level, id);
    }

    /**
     * 注册事件监听器
     * @param listener 观察者监听事件
     */
    public void registerChangeListener(UIObserverListener listener) {
        this.uiObserverListener = listener;
    }

    /**
     * 是否相应事件
     * @return 是
     */
    public boolean shouldResponseChangeListener() {
        return true;
    }

    class TabPane extends MultiTabPane<Chart> {

        protected void dealWithTabChanged(int index) {
            dealWithChosenPane(index);
            cardLayout.show(centerPane, NameArray[index]);
            tabChanged();
        }

        private void dealWithChosenPane(int index){
            //第一个总是标题
            if(index == 0){
                return;
            }

            BasicPane chosenPane = paneList.get(index);
            centerPane.remove(index);
            paneList.remove(index);
            if(ComparatorUtils.equals(chosenPane, chartLegendPane)){
                chartLegendPane = new VanChartLegendPane(VanChartStylePane.this);
                chosenPane = chartLegendPane;
            } else if(ComparatorUtils.equals(chosenPane,chartSeriesPane)){
                chartSeriesPane = new ChartSeriesPane(VanChartStylePane.this);
                chosenPane = chartSeriesPane;
            } else if(ComparatorUtils.equals(chosenPane,chartDataLabelPane)){
                chartDataLabelPane = new VanChartLabelPane(VanChartStylePane.this);
                chosenPane = chartDataLabelPane;
            } else if(ComparatorUtils.equals(chosenPane,chartAreaPane)){
                chartAreaPane = new VanChartAreaPane(chart.getPlot(), VanChartStylePane.this);
                chosenPane = chartAreaPane;
            } else if(ComparatorUtils.equals(chosenPane,chartTooltipPane)){
                chartTooltipPane = new VanChartTooltipPane(VanChartStylePane.this);
                chosenPane = chartTooltipPane;
            } else if(ComparatorUtils.equals(chosenPane,chartAxisPane)){
                chartAxisPane = new VanChartAxisPane(VanChartStylePane.this);
                chosenPane = chartAxisPane;
            } else if(ComparatorUtils.equals(chosenPane,chartDataSheetPane)){
                chartDataSheetPane = new VanChartDataSheetPane();
                chosenPane = chartDataSheetPane;
            }

            initSelfListener(chosenPane);
            centerPane.add(chosenPane,chosenPane.getTitle(),index);
            paneList.add(index,chosenPane);
        }

        @Override
        protected void tabChanged() {
            VanChartStylePane.this.removeAttributeChangeListener();
            ((BasicBeanPane<Chart>) paneList.get(tabPane.getSelectedIndex())).populateBean(chart);
            VanChartStylePane.this.addAttributeChangeListener(listener);
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
        protected java.util.List<BasicPane> initPaneList() {
            java.util.List<BasicPane> paneList = new ArrayList<BasicPane>();
            Plot plot = chart.getPlot();

            paneList.add(chartTitlePane);

            if(plot.isSupportLegend()){
                paneList.add(chartLegendPane);
            }

            paneList.add(chartDataLabelPane);

            paneList.add(chartSeriesPane);

            if(plot.isHaveAxis()){
                paneList.add(chartAxisPane);
                if(plot.isSupportDataSheet()) {
                    paneList.add(chartDataSheetPane);
                }
            }

            paneList.add(chartAreaPane);

            paneList.add(chartTooltipPane);

            return paneList;
        }

        @Override
        public void populateBean(Chart chart) {
            if (chart == null || stylePane.getSelectedIndex() == -1) {
                return;
            }
            ((BasicBeanPane<Chart>) paneList.get(stylePane.getSelectedIndex())).populateBean(chart);

        }

        @Override
        public Chart updateBean() {
            if (chart == null) {
                return null;
            }
            ((BasicBeanPane<Chart>) paneList.get(stylePane.getSelectedIndex())).updateBean(chart);
            return chart;
        }

        @Override
        public void updateBean(Chart ob) {
            ((BasicBeanPane<Chart>) paneList.get(stylePane.getSelectedIndex())).updateBean(ob);
        }

        /**
         * 接受界面的类型
         */
        @Override
        public boolean accept(Object ob) {
            return true;
        }

        /**
         * 界面标题
         */
        @Override
        public String title4PopupWindow() {
            return "";
        }

        /**
         * 重置
         */
        public void reset() {

        }

        /**
         * 设置选中的界面id
         */
        public void setSelectedByIds(int level, String... id) {
            tabPane.setSelectedIndex(-1);
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
    }
}

