// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.chart.series.SeriesCondition.impl;

import com.fr.chart.base.AttrColor;
import com.fr.chart.chartattr.BubblePlot;
import com.fr.design.chart.series.SeriesCondition.DataSeriesConditionPane;
import com.fr.design.chart.series.SeriesCondition.LabelColorPane;
import java.util.Map;

public class BubblePlotDataSeriesConditionPane extends DataSeriesConditionPane
{

    public BubblePlotDataSeriesConditionPane()
    {
    }

    protected void addStyleAction()
    {
        classPaneMap.put(com/fr/chart/base/AttrColor, new LabelColorPane(this));
    }

    public Class class4Correspond()
    {
        return com/fr/chart/chartattr/BubblePlot;
    }
}
