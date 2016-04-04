// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.chart.series.SeriesCondition.impl;

import com.fr.chart.base.AttrTrendLine;
import com.fr.chart.chartattr.StockPlot;
import com.fr.design.chart.series.SeriesCondition.DataSeriesConditionPane;
import com.fr.design.chart.series.SeriesCondition.TrendLinePane;
import java.util.Map;

public class StockPlotDataSeriesConditionPane extends DataSeriesConditionPane
{

    private static final long serialVersionUID = 0x12b80d1cffd3c173L;

    public StockPlotDataSeriesConditionPane()
    {
    }

    protected void addTrendLineAction()
    {
        classPaneMap.put(com/fr/chart/base/AttrTrendLine, new TrendLinePane(this));
    }

    public Class class4Correspond()
    {
        return com/fr/chart/chartattr/StockPlot;
    }
}
