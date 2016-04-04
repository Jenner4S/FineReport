// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.chart.series.SeriesCondition.impl;

import com.fr.chart.base.*;
import com.fr.chart.chartattr.XYScatterPlot;
import com.fr.design.chart.series.SeriesCondition.*;
import java.util.Map;

public class XYScatterPlotDataSeriesConditionPane extends DataSeriesConditionPane
{

    private static final long serialVersionUID = 0xdc94b69cf80d97b8L;

    public XYScatterPlotDataSeriesConditionPane()
    {
    }

    protected void addStyleAction()
    {
        classPaneMap.put(com/fr/chart/base/AttrColor, new LabelColorPane(this));
        classPaneMap.put(com/fr/chart/base/AttrLineStyle, new LabelLineStylePane(this));
        classPaneMap.put(com/fr/chart/base/AttrMarkerType, new LineMarkerTypePane(this));
    }

    protected void addTrendLineAction()
    {
        classPaneMap.put(com/fr/chart/base/AttrTrendLine, new TrendLinePane(this));
    }

    public Class class4Correspond()
    {
        return com/fr/chart/chartattr/XYScatterPlot;
    }
}
