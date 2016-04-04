// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.chart.series.SeriesCondition.impl;

import com.fr.chart.base.AttrAxisPosition;
import com.fr.design.chart.series.SeriesCondition.LabelAxisPositionPane;
import java.util.Map;

// Referenced classes of package com.fr.design.chart.series.SeriesCondition.impl:
//            Bar2DTrendLineDSConditionPane

public class CustomPlot4BarTrendLineConditionPane extends Bar2DTrendLineDSConditionPane
{

    private static final long serialVersionUID = 0x486315cb4a42ca3cL;

    public CustomPlot4BarTrendLineConditionPane()
    {
    }

    protected void addAxisPositionAction()
    {
        classPaneMap.put(com/fr/chart/base/AttrAxisPosition, new LabelAxisPositionPane(this));
    }
}
