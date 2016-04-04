// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.chart.series.SeriesCondition.impl;

import com.fr.chart.base.AttrBorder;
import com.fr.chart.base.AttrColor;
import com.fr.chart.chartattr.Area3DPlot;
import com.fr.design.chart.series.SeriesCondition.*;
import java.util.Map;

public class Area3DPlotDataSeriesConditionPane extends DataSeriesConditionPane
{

    private static final long serialVersionUID = 0xa50e66ee0fccf929L;

    public Area3DPlotDataSeriesConditionPane()
    {
    }

    protected void addStyleAction()
    {
        classPaneMap.put(com/fr/chart/base/AttrColor, new LabelColorPane(this));
    }

    protected void addBorderAction()
    {
        classPaneMap.put(com/fr/chart/base/AttrBorder, new LabelBorderPane(this));
    }

    public Class class4Correspond()
    {
        return com/fr/chart/chartattr/Area3DPlot;
    }
}
