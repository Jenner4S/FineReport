// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.chart.series.SeriesCondition.impl;

import com.fr.chart.base.AttrBorder;
import com.fr.chart.chartattr.Bar2DPlot;
import com.fr.design.chart.series.SeriesCondition.DataSeriesConditionPane;
import com.fr.design.chart.series.SeriesCondition.LabelBorderPane;
import java.util.Map;

public class BarPlotDataSeriesConditionPane extends DataSeriesConditionPane
{

    private static final long serialVersionUID = 0x6d7521214d4308d1L;

    public BarPlotDataSeriesConditionPane()
    {
    }

    protected void addBorderAction()
    {
        classPaneMap.put(com/fr/chart/base/AttrBorder, new LabelBorderPane(this));
    }

    public Class class4Correspond()
    {
        return com/fr/chart/chartattr/Bar2DPlot;
    }
}
