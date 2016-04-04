// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.chart;

import com.fr.base.BaseUtils;
import com.fr.chart.chartattr.Chart;
import com.fr.chart.chartattr.ChartCollection;
import com.fr.chart.chartattr.Plot;
import com.fr.design.ChartTypeInterfaceManager;
import com.fr.design.beans.FurtherBasicBeanPane;
import com.fr.design.data.DesignTableDataManager;
import com.fr.design.data.tabledata.Prepare4DataSourceChange;
import com.fr.design.designer.TargetComponent;
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
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.util.ArrayList;
import java.util.Calendar;
import javax.swing.Icon;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

// Referenced classes of package com.fr.design.mainframe.chart:
//            AbstractChartAttrPane, AttributeChange, PaneTitleConstants

public class ChartEditPane extends BasicPane
    implements AttributeChange, Prepare4DataSourceChange
{

    private static final int CHANGE_MIN_TIME = 80;
    protected ChartCollection collection;
    protected boolean isDefaultPane;
    private static ChartEditPane singleton;
    protected java.util.List paneList;
    protected ChartTypePane typePane;
    protected ChartDataPane dataPane4SupportCell;
    protected ChartStylePane stylePane;
    protected ChartOtherPane otherPane;
    protected UIHeadGroup tabsHeaderIconPane;
    private ChartCollection lastCollection;
    protected CardLayout card;
    protected JPanel center;
    private TargetComponentContainer container;
    private TitleChangeListener titleChangeListener;
    private Calendar lastTime;
    AttributeChangeListener listener;

    public static synchronized ChartEditPane getInstance()
    {
        if(singleton == null)
            singleton = new ChartEditPane();
        return singleton;
    }

    protected ChartEditPane()
    {
        isDefaultPane = true;
        dataPane4SupportCell = null;
        container = null;
        titleChangeListener = null;
        listener = new AttributeChangeListener() {

            final ChartEditPane this$0;

            public void attributeChange()
            {
                if(lastTime != null && Calendar.getInstance().getTimeInMillis() - lastTime.getTimeInMillis() < 80L)
                    return;
                AbstractChartAttrPane abstractchartattrpane = (AbstractChartAttrPane)paneList.get(tabsHeaderIconPane.getSelectedIndex());
                abstractchartattrpane.update(collection);
                if(!ComparatorUtils.equals(collection, lastCollection))
                {
                    try
                    {
                        lastCollection = (ChartCollection)collection.clone();
                    }
                    catch(CloneNotSupportedException clonenotsupportedexception)
                    {
                        FRLogger.getLogger().error("error in clone ChartEditPane");
                    }
                    if(ComparatorUtils.equals(abstractchartattrpane.title4PopupWindow(), PaneTitleConstants.CHART_STYLE_TITLE))
                        dealWithStyleChange();
                    fire();
                }
            }

            
            {
                this$0 = ChartEditPane.this;
                super();
            }
        }
;
        setLayout(new BorderLayout());
        paneList = new ArrayList();
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

    protected void createTabsPane()
    {
        Icon aicon[] = new Icon[paneList.size()];
        card = new CardLayout();
        center = new JPanel(card);
        for(int i = 0; i < paneList.size(); i++)
        {
            AbstractChartAttrPane abstractchartattrpane = (AbstractChartAttrPane)paneList.get(i);
            aicon[i] = BaseUtils.readIcon(abstractchartattrpane.getIconPath());
            center.add(abstractchartattrpane, abstractchartattrpane.title4PopupWindow());
        }

        tabsHeaderIconPane = new UIHeadGroup(aicon) {

            final ChartEditPane this$0;

            public void tabChanged(int j)
            {
                ((AbstractChartAttrPane)paneList.get(j)).populateBean(collection);
                ((AbstractChartAttrPane)paneList.get(j)).addAttributeChangeListener(listener);
                card.show(center, ((AbstractChartAttrPane)paneList.get(j)).title4PopupWindow());
                if(titleChangeListener != null)
                    titleChangeListener.fireTitleChange(getSelectedTabName());
            }

            
            {
                this$0 = ChartEditPane.this;
                super(aicon);
            }
        }
;
        tabsHeaderIconPane.setNeedLeftRightOutLine(false);
        add(tabsHeaderIconPane, "North");
        add(center, "Center");
    }

    public void reLayout(Chart chart)
    {
        if(chart != null)
        {
            int i = getSelectedChartIndex(chart);
            removeAll();
            setLayout(new BorderLayout());
            paneList = new ArrayList();
            addTypeAndDataPane();
            boolean flag = true;
            String s = "";
            if(chart.getPlot() != null)
            {
                s = chart.getPlot().getPlotID();
                flag = ChartTypeInterfaceManager.getInstance().isUseDefaultPane(s);
            }
            if(flag)
            {
                paneList.add(stylePane);
                paneList.add(otherPane);
                isDefaultPane = true;
            } else
            {
                AbstractChartAttrPane aabstractchartattrpane[] = ChartTypeInterfaceManager.getInstance().getAttrPaneArray(s, listener);
                for(int j = 0; j < aabstractchartattrpane.length; j++)
                {
                    aabstractchartattrpane[j].addAttributeChangeListener(listener);
                    paneList.add(aabstractchartattrpane[j]);
                }

                isDefaultPane = false;
            }
            createTabsPane();
            setSelectedTab();
        }
    }

    protected void addTypeAndDataPane()
    {
        paneList.add(typePane);
        paneList.add(dataPane4SupportCell);
    }

    protected void setSelectedTab()
    {
    }

    public void setSupportCellData(boolean flag)
    {
        if(dataPane4SupportCell != null)
            dataPane4SupportCell.setSupportCellData(flag);
    }

    public String getSelectedTabName()
    {
        int i = Math.min(tabsHeaderIconPane.getSelectedIndex(), paneList.size() - 1);
        return ((AbstractChartAttrPane)paneList.get(i)).title4PopupWindow();
    }

    public void addTitleChangeListener(TitleChangeListener titlechangelistener)
    {
        titleChangeListener = titlechangelistener;
    }

    public void setContainer(TargetComponentContainer targetcomponentcontainer)
    {
        container = targetcomponentcontainer;
    }

    protected String title4PopupWindow()
    {
        return Inter.getLocText("FR-Chart-Property_Table");
    }

    public void populate(ChartCollection chartcollection)
    {
        if(chartcollection.getChartCount() <= 0)
            return;
        if(checkNeedsReLayout(chartcollection.getSelectedChart()))
            reLayout(chartcollection.getSelectedChart());
        collection = chartcollection;
        ((AbstractChartAttrPane)paneList.get(tabsHeaderIconPane.getSelectedIndex())).populateBean(chartcollection);
        ((AbstractChartAttrPane)paneList.get(tabsHeaderIconPane.getSelectedIndex())).addAttributeChangeListener(listener);
        for(int i = 0; i < paneList.size(); i++)
            ((AbstractChartAttrPane)paneList.get(i)).registerChartEditPane(getCurrentChartEditPane());

    }

    protected ChartEditPane getCurrentChartEditPane()
    {
        return this;
    }

    public void fire()
    {
        if(container != null && container.getEPane() != null)
            container.getEPane().fireTargetModified();
    }

    public int getSelectedChartIndex(Chart chart)
    {
        int i = 0;
        if(typePane != null)
        {
            for(FurtherBasicBeanPane afurtherbasicbeanpane[] = typePane.getPaneList(); i < afurtherbasicbeanpane.length; i++)
                if(afurtherbasicbeanpane[i].accept(chart))
                    return i;

        }
        return i;
    }

    private boolean checkNeedsReLayout(Chart chart)
    {
        if(chart != null)
        {
            int i = typePane.getSelectedIndex();
            int j = getSelectedChartIndex(chart);
            boolean flag = true;
            if(chart.getPlot() != null)
            {
                String s = chart.getPlot().getPlotID();
                flag = ChartTypeInterfaceManager.getInstance().isUseDefaultPane(s);
            }
            return flag != isDefaultPane || !flag && i != j;
        } else
        {
            return false;
        }
    }

    public boolean isDefaultPane()
    {
        return isDefaultPane;
    }

    public transient void GoToPane(String as[])
    {
        setSelectedIndex(as);
        EastRegionContainerPane.getInstance().setWindow2PreferWidth();
    }

    public transient void setSelectedIndex(String as[])
    {
        String s = as[0];
        int i = 0;
        do
        {
            if(i >= paneList.size())
                break;
            if(ComparatorUtils.equals(s, ((AbstractChartAttrPane)paneList.get(i)).title4PopupWindow()))
            {
                tabsHeaderIconPane.setSelectedIndex(i);
                if(as.length >= 2)
                    ((AbstractChartAttrPane)paneList.get(i)).setSelectedByIds(1, as);
                break;
            }
            i++;
        } while(true);
    }

    protected void dealWithStyleChange()
    {
    }

    public void styleChange(boolean flag)
    {
    }

    public void populateSelectedTabPane()
    {
        int i = tabsHeaderIconPane.getSelectedIndex();
        ((AbstractChartAttrPane)paneList.get(i)).populateBean(collection);
        ((AbstractChartAttrPane)paneList.get(i)).addAttributeChangeListener(listener);
    }

    public void registerDSChangeListener()
    {
        DesignTableDataManager.addDsChangeListener(new ChangeListener() {

            final ChartEditPane this$0;

            public void stateChanged(ChangeEvent changeevent)
            {
                AbstractChartAttrPane abstractchartattrpane = (AbstractChartAttrPane)paneList.get(tabsHeaderIconPane.getSelectedIndex());
                abstractchartattrpane.refreshChartDataPane(collection);
            }

            
            {
                this$0 = ChartEditPane.this;
                super();
            }
        }
);
    }




}
