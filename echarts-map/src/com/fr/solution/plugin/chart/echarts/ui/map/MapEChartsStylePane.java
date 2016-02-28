package com.fr.solution.plugin.chart.echarts.ui.map;

import com.fr.chart.chartattr.ChartCollection;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.dialog.BasicPane;
import com.fr.design.dialog.MultiTabPane;
import com.fr.design.event.UIObserver;
import com.fr.design.event.UIObserverListener;
import com.fr.design.gui.frpane.AttributeChangeListener;
import com.fr.design.mainframe.chart.AbstractChartAttrPane;
import com.fr.design.mainframe.chart.PaneTitleConstants;
import com.fr.solution.plugin.chart.echarts.base.NewChart;

import javax.swing.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MapEChartsStylePane extends AbstractChartAttrPane implements UIObserver {


    /**
	 * 
	 */
	private static final long serialVersionUID = 2878869718193908164L;

	private KindOfTabPane kindOfTabPane;

    private /*EChartsTitlePane*/MapEChartsTitlePane titlePane;
    private BasicPane dataRangePane;
    private BasicPane mapNameRef;
//    private /*EChartsLengendPane*/ BasicPane lengendPane;
    private AttributeChangeListener listener;
    private UIObserverListener uiObserverListener;
    private NewChart chart;

    public MapEChartsStylePane(AttributeChangeListener listener) {
        this.listener = listener;
        initComponents();
    }

	private void initComponents() {
        setLayout(new BorderLayout());
        titlePane =	new MapEChartsTitlePane(this);	// new EChartsTitlePane(this);
        
        dataRangePane = new BasicPane() {
            @Override
            protected String title4PopupWindow() {
                return "\u6570\u636E\u8303\u56F4";
            }
        };
        mapNameRef = new BasicPane() {
            @Override
            protected String title4PopupWindow() {
                return "\u533A\u57DF\u540D\u5BF9\u5E94";
            }
        };
        
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
        kindOfTabPane = new KindOfTabPane();
        content.add(kindOfTabPane, BorderLayout.CENTER);
        return content;
    }

    public String getIconPath() {
        return "com/fr/solution/plugin/chart/echarts/images/toolbar_item.png";
    }


    @Override
    public void populate(ChartCollection collection) {
        this.chart = (NewChart) collection.getSelectedChart();
        this.remove(leftContentPane);
        initContentPane();
        this.removeAttributeChangeListener();
        kindOfTabPane.populateBean(chart);
        this.addAttributeChangeListener(listener);
        this.initAllListeners();
    }

    @Override
    public void update(ChartCollection collection) {
        kindOfTabPane.updateBean((NewChart) collection.getSelectedChart());
    }

    public String title4PopupWindow() {
        return PaneTitleConstants.CHART_STYLE_TITLE;
    }

    public void registerChangeListener(UIObserverListener listener) {
        this.uiObserverListener = listener;
    }

    public boolean shouldResponseChangeListener() {
        return true;
    }

    public class KindOfTabPane extends MultiTabPane<NewChart> {

		private static final long serialVersionUID = 868742523298491337L;

		@Override
        protected void initLayout() {
            JPanel tabPanel = new JPanel(new BorderLayout());
            tabPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 10, getBackground()));
            tabPanel.add(tabPane, BorderLayout.CENTER);

            setLayout(new BorderLayout(0, 4));
            add(tabPanel, BorderLayout.NORTH);
            add(centerPane, BorderLayout.CENTER);
        }

        @Override
        protected void dealWithTabChanged(int index) {
            dealWithChosenPane(index);
            cardLayout.show(centerPane, NameArray[index]);
            tabChanged();
        }

        private void dealWithChosenPane(int index) {
            if (index == 0) {
                return;
            }

            BasicPane chosenPane = paneList.get(index);
            centerPane.remove(index);
            paneList.remove(index);

            if (chosenPane == dataRangePane) {
                chosenPane = new MapEChartsDataRangePane(MapEChartsStylePane.this);
            }/*else if(chosenPane == mapNameRef){
            	chosenPane = new MapNameRefPane(MapEChartsStylePane.this);
            }else */if(chosenPane == titlePane){
            	chosenPane = new MapEChartsTitlePane(MapEChartsStylePane.this);
            }

            initSelfListener(chosenPane);
            centerPane.add(chosenPane, chosenPane.getTitle(), index);
            paneList.add(index, chosenPane);
        }

		@Override
        protected void tabChanged() {
            MapEChartsStylePane.this.removeAttributeChangeListener();
            ((BasicBeanPane<NewChart>) paneList.get(tabPane.getSelectedIndex())).populateBean(chart);
            MapEChartsStylePane.this.addAttributeChangeListener(listener);
        }

        @Override
        protected List<BasicPane> initPaneList() {
            java.util.List<BasicPane> paneList = new ArrayList<BasicPane>();
            paneList.add(titlePane);
            paneList.add(dataRangePane);
//            paneList.add(mapNameRef);
            return paneList;
        }

        @SuppressWarnings("unchecked")
		@Override
        public void populateBean(NewChart ob) {
            if (chart == null || kindOfTabPane.getSelectedIndex() == -1) {
                return;
            }
            ((BasicBeanPane<NewChart>) paneList.get(kindOfTabPane.getSelectedIndex())).populateBean(chart);
        }

		@Override
        public NewChart updateBean() {
            if (chart == null) {
                return null;
            }
            ((BasicBeanPane<NewChart>) paneList.get(kindOfTabPane.getSelectedIndex())).updateBean(chart);
            return chart;
        }

		@Override
        public void updateBean(NewChart ob) {
            ((BasicBeanPane<NewChart>) paneList.get(kindOfTabPane.getSelectedIndex())).updateBean(ob);
        }

        @Override
        public boolean accept(Object ob) {
            return true;
        }

        @Override
        public String title4PopupWindow() {
            return "Tab";
        }

        @Override
        public void reset() {

        }
    }
}