package com.fr.design.mainframe.chart;


import com.fr.base.BaseUtils;
import com.fr.chart.chartattr.Chart;
import com.fr.chart.chartattr.ChartCollection;
import com.fr.design.ChartTypeInterfaceManager;
import com.fr.design.beans.FurtherBasicBeanPane;
import com.fr.design.data.DesignTableDataManager;
import com.fr.design.data.tabledata.Prepare4DataSourceChange;
import com.fr.design.dialog.BasicPane;
import com.fr.design.gui.frpane.AttributeChangeListener;
import com.fr.design.gui.ibutton.UIHeadGroup;
import com.fr.design.gui.itabpane.TitleChangeListener;
import com.fr.design.mainframe.EastRegionContainerPane;
import com.fr.design.mainframe.TargetComponentContainer;
import com.fr.design.mainframe.chart.gui.ChartDataPane;
import com.fr.design.mainframe.chart.gui.ChartOtherPane;
import com.fr.design.mainframe.chart.gui.ChartStylePane;
import com.fr.design.mainframe.chart.gui.ChartTypePane;
import com.fr.general.ComparatorUtils;
import com.fr.general.FRLogger;
import com.fr.general.Inter;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ChartEditPane extends BasicPane implements AttributeChange,Prepare4DataSourceChange {
    public synchronized static ChartEditPane getInstance() {
        if (singleton == null) {
            singleton = new ChartEditPane();
        }
        return singleton;
    }

    private final static int CHANGE_MIN_TIME = 80;

    protected ChartCollection collection;
    protected boolean isDefaultPane = true;//�Ƿ���Ĭ�ϵĽ���

    private static ChartEditPane singleton;
    protected List<AbstractChartAttrPane> paneList;

    protected ChartTypePane typePane;
    protected ChartDataPane dataPane4SupportCell = null;
    protected ChartStylePane stylePane;
    protected ChartOtherPane otherPane;

    protected UIHeadGroup tabsHeaderIconPane;
    private ChartCollection lastCollection;
    protected CardLayout card;
    protected JPanel center;
    private TargetComponentContainer container = null;
    private TitleChangeListener titleChangeListener = null;

    private Calendar lastTime;

    protected ChartEditPane() {
        this.setLayout(new BorderLayout());

        paneList = new ArrayList<AbstractChartAttrPane>();
        typePane = new ChartTypePane();
        dataPane4SupportCell = new ChartDataPane(listener);
        dataPane4SupportCell.setSupportCellData(true);
        stylePane = new ChartStylePane(listener);
        otherPane = new ChartOtherPane();

        paneList.add(typePane);
        paneList.add(dataPane4SupportCell);
        paneList.add(stylePane);
        paneList.add(otherPane);

        createTabsPane();
        registerDSChangeListener();
    }

    //���������
    protected void createTabsPane() {
        Icon[] iconArray = new Icon[paneList.size()];
        card = new CardLayout();
        center = new JPanel(card);
        for (int i = 0; i < paneList.size(); i++) {
            AbstractChartAttrPane pane = paneList.get(i);
            iconArray[i] = BaseUtils.readIcon(pane.getIconPath());
            center.add(pane, pane.title4PopupWindow());
        }

        tabsHeaderIconPane = new UIHeadGroup(iconArray) {
            @Override
            public void tabChanged(int index) {
                paneList.get(index).populateBean(collection);
                paneList.get(index).addAttributeChangeListener(listener);
                card.show(center, paneList.get(index).title4PopupWindow());
                if (titleChangeListener != null) {
                    titleChangeListener.fireTitleChange(getSelectedTabName());
                }
            }
        };
        tabsHeaderIconPane.setNeedLeftRightOutLine(false);
        this.add(tabsHeaderIconPane, BorderLayout.NORTH);
        this.add(center, BorderLayout.CENTER);
    }

    AttributeChangeListener listener = new AttributeChangeListener() {
        @Override
        public void attributeChange() {
            if (lastTime != null && Calendar.getInstance().getTimeInMillis() - lastTime.getTimeInMillis() < CHANGE_MIN_TIME) {
                return;
            }
            AbstractChartAttrPane selectedPane = paneList.get(tabsHeaderIconPane.getSelectedIndex());
            selectedPane.update(collection);
            if (!ComparatorUtils.equals(collection, lastCollection)) {
                try {
                    lastCollection = (ChartCollection) collection.clone();
                } catch (CloneNotSupportedException e) {
                    FRLogger.getLogger().error("error in clone ChartEditPane");
                }
                if(ComparatorUtils.equals(selectedPane.title4PopupWindow(),PaneTitleConstants.CHART_STYLE_TITLE)){
                    dealWithStyleChange();
                }

                fire();
            }
        }
    };

    /**
     * ���¹������
     * @param currentChart ͼ��
     */
    public void reLayout(Chart currentChart){
        if(currentChart != null){
            int chartIndex = getSelectedChartIndex(currentChart);
            this.removeAll();
            this.setLayout(new BorderLayout());
            paneList = new ArrayList<AbstractChartAttrPane>();
            addTypeAndDataPane();

            boolean isDefault = true;
            String plotID = "";
            if(currentChart.getPlot() != null){
                plotID = currentChart.getPlot().getPlotID();
                isDefault = ChartTypeInterfaceManager.getInstance().isUseDefaultPane(plotID);
            }

            if(isDefault){
                paneList.add(stylePane);
                paneList.add(otherPane);
                this.isDefaultPane = true;
            }else{
                AbstractChartAttrPane[] otherPaneList = ChartTypeInterfaceManager.getInstance().getAttrPaneArray(plotID, listener);
                for(int i = 0; i < otherPaneList.length; i++){
                    otherPaneList[i].addAttributeChangeListener(listener);
                    paneList.add(otherPaneList[i]);
                }
                this.isDefaultPane = false;
            }
            createTabsPane();
            setSelectedTab();
        }
    }

    protected void addTypeAndDataPane() {
        paneList.add(typePane);
        paneList.add(dataPane4SupportCell);

    }

    protected void setSelectedTab() {
    }

    /**
     * �����Ƿ�֧�ֵ�Ԫ��
     */
    public void setSupportCellData(boolean supportCellData) {
        if (dataPane4SupportCell != null) {
            dataPane4SupportCell.setSupportCellData(supportCellData);
        }
    }

    /**
     * ����ѡ�е�tab����.
     */
    public String getSelectedTabName() {
        int index = Math.min(tabsHeaderIconPane.getSelectedIndex(), paneList.size() - 1);
        return paneList.get(index).title4PopupWindow();
    }

    /**
     * ��ӱ���仯�����¼�.
     *
     * @param titleChangeListener �����ChangeListener
     */
    public void addTitleChangeListener(TitleChangeListener titleChangeListener) {
        this.titleChangeListener = titleChangeListener;
    }

    /**
     * ��������.
     */
    public void setContainer(TargetComponentContainer container) {
        this.container = container;
    }

    @Override
    protected String title4PopupWindow() {
        return Inter.getLocText("FR-Chart-Property_Table");
    }

    public void populate(ChartCollection collection) {
        if (collection.getChartCount() <= 0) {
            return;
        }

        if(checkNeedsReLayout(collection.getSelectedChart())){
            reLayout(collection.getSelectedChart());
        }

        this.collection = collection;
        paneList.get(tabsHeaderIconPane.getSelectedIndex()).populateBean(collection);
        paneList.get(tabsHeaderIconPane.getSelectedIndex()).addAttributeChangeListener(listener);

        for (int i = 0; i < paneList.size(); i++) {
            paneList.get(i).registerChartEditPane(getCurrentChartEditPane());
        }
    }

    protected ChartEditPane getCurrentChartEditPane() {
        return this;
    }


    /**
     * ��Ӧ�¼�.
     */
    public void fire() {
        if (container != null && container.getEPane() != null) {
            container.getEPane().fireTargetModified();
        }
    }

    public int getSelectedChartIndex(Chart chart){
        int index = 0;
        if(typePane != null){
            FurtherBasicBeanPane[] paneList = typePane.getPaneList();
            for(; index < paneList.length; index++){
                if(paneList[index].accept(chart)){
                    return index;
                }
            }
        }
        return index;
    }

    //populate��ʱ�򿴿�Ҫ��Ҫ�ع����
    private boolean checkNeedsReLayout(Chart chart){
        if(chart != null){
            int lastIndex = typePane.getSelectedIndex();
            int currentIndex = getSelectedChartIndex(chart);
            boolean currentPane = true;
            if(chart.getPlot() != null){
                String plotID = chart.getPlot().getPlotID();
                currentPane = ChartTypeInterfaceManager.getInstance().isUseDefaultPane(plotID);
            }
            return (currentPane != isDefaultPane) || (!currentPane && lastIndex != currentIndex);
        }
        return false;
    }

    /**
     * ��ǰ�����Ƿ���Ĭ�ϵĽ���
     * @return �Ƿ���Ĭ�ϵĽ���
     */
    public boolean isDefaultPane(){
        return this.isDefaultPane;
    }

    /**
     * չ������Ӧid��pane.
     *
     * @param id ����ı�־.id
     */
    public void GoToPane(String... id) {
        this.setSelectedIndex(id);
        EastRegionContainerPane.getInstance().setWindow2PreferWidth();
    }

    /**
     * ����ѡ�е�id
     *
     * @param id ����ı�־.id
     */
    public void setSelectedIndex(String... id) {
        String firstid = id[0];
        for (int i = 0; i < paneList.size(); i++) {
            if (ComparatorUtils.equals(firstid, paneList.get(i).title4PopupWindow())) {
                tabsHeaderIconPane.setSelectedIndex(i);
                if (id.length >= 2) {
                    paneList.get(i).setSelectedByIds(1, id);
                }
                break;
            }
        }
    }

    protected void dealWithStyleChange(){

    }

    /**
     *��Ҫ����ͼ����������ж���ʽ�ı��Ƿ����Թ�������ȫ����ʽ��ť
     * @param isFromToolBar �Ƿ����Թ�����
     */
    public void styleChange(boolean isFromToolBar){

    }

    /**
     * ͼ�����������ʾѡ�е����
     */
    public void populateSelectedTabPane() {
        int index = tabsHeaderIconPane.getSelectedIndex();
        paneList.get(index).populateBean(collection);
        paneList.get(index).addAttributeChangeListener(listener);
    }

    /**
     * ���ݼ��ı���¼�����
     */
    public void registerDSChangeListener() {
        DesignTableDataManager.addDsChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                AbstractChartAttrPane attrPane = paneList.get(tabsHeaderIconPane.getSelectedIndex());
                attrPane.refreshChartDataPane(collection);
            }
        });
    }
}
