// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.chart;

import com.fr.chart.chartattr.ChartCollection;
import com.fr.chart.web.ChartHyperPoplink;
import com.fr.design.chart.gui.ChartComponent;
import com.fr.design.chart.series.SeriesCondition.impl.ChartHyperPopAttrPane;
import com.fr.design.gui.ibutton.UIHeadGroup;
import com.fr.design.mainframe.chart.gui.ChartDataPane;
import com.fr.design.mainframe.chart.gui.ChartOtherPane;
import com.fr.design.mainframe.chart.gui.ChartStylePane;
import com.fr.design.mainframe.chart.gui.ChartTypePane;
import java.util.ArrayList;
import java.util.List;

// Referenced classes of package com.fr.design.mainframe.chart:
//            ChartEditPane, AbstractChartAttrPane

public class ChartHyperEditPane extends ChartEditPane
{

    private ChartComponent useChartComponent;
    private ChartHyperPopAttrPane attrPane;

    public ChartHyperEditPane(int i)
    {
        paneList = new ArrayList();
        paneList.add(attrPane = new ChartHyperPopAttrPane(i));
        paneList.add(new ChartTypePane());
        dataPane4SupportCell = new ChartDataPane(listener);
        dataPane4SupportCell.setSupportCellData(false);
        paneList.add(dataPane4SupportCell);
        paneList.add(new ChartStylePane(listener));
        paneList.add(new ChartOtherPane());
        createTabsPane();
    }

    protected void addTypeAndDataPane()
    {
        paneList.add(attrPane);
        paneList.add(typePane);
        paneList.add(dataPane4SupportCell);
    }

    protected void setSelectedTab()
    {
        tabsHeaderIconPane.setSelectedIndex(1);
        card.show(center, getSelectedTabName());
        for(int i = 0; i < paneList.size(); i++)
            ((AbstractChartAttrPane)paneList.get(i)).registerChartEditPane(getCurrentChartEditPane());

    }

    protected ChartEditPane getCurrentChartEditPane()
    {
        return this;
    }

    public void useChartComponent(ChartComponent chartcomponent)
    {
        useChartComponent = chartcomponent;
    }

    public void fire()
    {
        if(useChartComponent != null)
        {
            useChartComponent.populate(collection);
            useChartComponent.reset();
        }
    }

    public void populateHyperLink(ChartHyperPoplink charthyperpoplink)
    {
        attrPane.populateBean(charthyperpoplink);
        populate((ChartCollection)charthyperpoplink.getChartCollection());
    }

    public void updateHyperLink(ChartHyperPoplink charthyperpoplink)
    {
        attrPane.updateBean(charthyperpoplink);
    }
}
