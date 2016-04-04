// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.chart.series.SeriesCondition.impl;

import com.fr.chart.base.AttrAxisPosition;
import com.fr.design.chart.series.SeriesCondition.LabelAxisPositionPane;
import java.util.Map;

// Referenced classes of package com.fr.design.chart.series.SeriesCondition.impl:
//            AreaPlotDataSeriesCondtionPane

public class CustomPlot4AreaConditionPane extends AreaPlotDataSeriesCondtionPane
{

    private static final long serialVersionUID = 0x604941d8bfa71683L;

    public CustomPlot4AreaConditionPane()
    {
    }

    protected void addAxisPositionAction()
    {
        classPaneMap.put(com/fr/chart/base/AttrAxisPosition, new LabelAxisPositionPane(this));
    }
}
