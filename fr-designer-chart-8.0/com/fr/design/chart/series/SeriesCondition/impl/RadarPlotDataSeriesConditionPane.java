// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.chart.series.SeriesCondition.impl;

import com.fr.chart.base.*;
import com.fr.chart.chartattr.RadarPlot;
import com.fr.design.chart.series.SeriesCondition.*;
import java.util.Map;

public class RadarPlotDataSeriesConditionPane extends DataSeriesConditionPane
{

    private static final long serialVersionUID = 0x3960d614ee4f24fcL;

    public RadarPlotDataSeriesConditionPane()
    {
    }

    protected void addStyleAction()
    {
        classPaneMap.put(com/fr/chart/base/AttrColor, new LabelColorPane(this));
        classPaneMap.put(com/fr/chart/base/AttrLineStyle, new LabelLineStylePane(this));
        classPaneMap.put(com/fr/chart/base/AttrMarkerType, new LineMarkerTypePane(this));
    }

    public Class class4Correspond()
    {
        return com/fr/chart/chartattr/RadarPlot;
    }
}
