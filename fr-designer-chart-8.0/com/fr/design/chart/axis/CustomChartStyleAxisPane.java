// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.chart.axis;

import com.fr.chart.chartattr.Plot;
import com.fr.general.Inter;

// Referenced classes of package com.fr.design.chart.axis:
//            TernaryChartStyleAxisPane

public class CustomChartStyleAxisPane extends TernaryChartStyleAxisPane
{

    public CustomChartStyleAxisPane(Plot plot)
    {
        super(plot);
    }

    protected String getValueAxisName()
    {
        return Inter.getLocText("Main_Axis");
    }

    protected String getSecondValueAxisName()
    {
        return Inter.getLocText("Second_Axis");
    }
}
