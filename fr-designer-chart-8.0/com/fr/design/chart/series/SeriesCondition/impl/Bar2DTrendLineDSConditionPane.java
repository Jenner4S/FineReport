// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.chart.series.SeriesCondition.impl;

import com.fr.chart.base.AttrTrendLine;
import com.fr.design.chart.series.SeriesCondition.TrendLinePane;
import java.util.Map;

// Referenced classes of package com.fr.design.chart.series.SeriesCondition.impl:
//            BarPlotDataSeriesConditionPane

public class Bar2DTrendLineDSConditionPane extends BarPlotDataSeriesConditionPane
{

    private static final long serialVersionUID = 0xae478d48829be198L;

    public Bar2DTrendLineDSConditionPane()
    {
    }

    protected void addTrendLineAction()
    {
        classPaneMap.put(com/fr/chart/base/AttrTrendLine, new TrendLinePane(this));
    }
}
