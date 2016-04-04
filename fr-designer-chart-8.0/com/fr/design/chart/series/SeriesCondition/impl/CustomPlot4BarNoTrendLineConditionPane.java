// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.chart.series.SeriesCondition.impl;

import com.fr.chart.base.AttrAxisPosition;
import com.fr.design.chart.series.SeriesCondition.LabelAxisPositionPane;
import java.util.Map;

// Referenced classes of package com.fr.design.chart.series.SeriesCondition.impl:
//            BarPlotDataSeriesConditionPane

public class CustomPlot4BarNoTrendLineConditionPane extends BarPlotDataSeriesConditionPane
{

    private static final long serialVersionUID = 0x9f666abf1a4425bcL;

    public CustomPlot4BarNoTrendLineConditionPane()
    {
    }

    protected void addAxisPositionAction()
    {
        classPaneMap.put(com/fr/chart/base/AttrAxisPosition, new LabelAxisPositionPane(this));
    }
}
