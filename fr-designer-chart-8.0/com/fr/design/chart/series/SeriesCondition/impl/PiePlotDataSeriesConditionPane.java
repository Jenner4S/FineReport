// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.chart.series.SeriesCondition.impl;

import com.fr.chart.base.AttrBorder;
import com.fr.chart.chartattr.PiePlot;
import com.fr.design.chart.series.SeriesCondition.DataSeriesConditionPane;
import com.fr.design.chart.series.SeriesCondition.LabelBorderPane;
import java.util.Map;

public class PiePlotDataSeriesConditionPane extends DataSeriesConditionPane
{

    private static final long serialVersionUID = 0x6747fbe2bd946dfaL;

    public PiePlotDataSeriesConditionPane()
    {
    }

    protected void addBorderAction()
    {
        classPaneMap.put(com/fr/chart/base/AttrBorder, new LabelBorderPane(this));
    }

    public Class class4Correspond()
    {
        return com/fr/chart/chartattr/PiePlot;
    }
}
