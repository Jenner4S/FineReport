// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.chart.series.SeriesCondition.impl;

import com.fr.chart.base.AttrAlpha;
import com.fr.chart.base.AttrColor;
import com.fr.chart.chartattr.GanttPlot;
import com.fr.design.chart.series.SeriesCondition.*;
import java.util.Map;

public class GanttPlotDataSeriesConditionPane extends DataSeriesConditionPane
{

    private static final long serialVersionUID = 0x2382e112f5ca1bbeL;

    public GanttPlotDataSeriesConditionPane()
    {
    }

    protected void addBasicAction()
    {
        classPaneMap.put(com/fr/chart/base/AttrAlpha, new LabelAlphaPane(this));
    }

    protected void addStyleAction()
    {
        classPaneMap.put(com/fr/chart/base/AttrColor, new LabelColorPane(this));
    }

    protected void addBorderAction()
    {
    }

    protected void addTrendLineAction()
    {
    }

    public Class class4Correspond()
    {
        return com/fr/chart/chartattr/GanttPlot;
    }
}
