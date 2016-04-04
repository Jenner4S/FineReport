// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.chart.series.SeriesCondition.impl;

import com.fr.chart.base.AttrAxisPosition;
import com.fr.design.chart.series.SeriesCondition.DataSeriesConditionPane;
import com.fr.design.chart.series.SeriesCondition.LabelAxisPositionPane;
import java.util.Map;

public class CustomPlotDataSeriesConditionPane extends DataSeriesConditionPane
{

    private static final long serialVersionUID = 0xaaca4184790fa6acL;

    public CustomPlotDataSeriesConditionPane()
    {
    }

    protected void addAxisPositionAction()
    {
        classPaneMap.put(com/fr/chart/base/AttrAxisPosition, new LabelAxisPositionPane(this));
    }
}
