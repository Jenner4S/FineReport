// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.chart.series.SeriesCondition.impl;

import com.fr.chart.base.AttrContents;
import com.fr.chart.chartattr.MapPlot;
import com.fr.design.chart.series.SeriesCondition.DataSeriesConditionPane;
import com.fr.design.chart.series.SeriesCondition.LabelContentsPane;
import java.util.Map;

public class MapPlotDataSeriesConditionPane extends DataSeriesConditionPane
{

    private static final long serialVersionUID = 0x3352f97f2bf28f1dL;

    public MapPlotDataSeriesConditionPane()
    {
    }

    protected void addBasicAction()
    {
        classPaneMap.put(com/fr/chart/base/AttrContents, new LabelContentsPane(this, class4Correspond()));
    }

    public Class class4Correspond()
    {
        return com/fr/chart/chartattr/MapPlot;
    }
}
