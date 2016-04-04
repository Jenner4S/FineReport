// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.chart;

import com.fr.chart.chartattr.ChartCollection;
import com.fr.design.gui.frpane.AbstractAttrNoScrollPane;

// Referenced classes of package com.fr.design.mainframe.chart:
//            ChartEditPane

public abstract class AbstractChartAttrPane extends AbstractAttrNoScrollPane
{

    public AbstractChartAttrPane()
    {
    }

    public abstract void populate(ChartCollection chartcollection);

    public abstract void update(ChartCollection chartcollection);

    public void populateBean(ChartCollection chartcollection)
    {
        if(chartcollection == null)
        {
            return;
        } else
        {
            removeAttributeChangeListener();
            populate(chartcollection);
            return;
        }
    }

    public void registerChartEditPane(ChartEditPane charteditpane)
    {
    }

    public void refreshChartDataPane(ChartCollection chartcollection)
    {
    }
}
