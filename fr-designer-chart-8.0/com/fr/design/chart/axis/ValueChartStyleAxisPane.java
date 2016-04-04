// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.chart.axis;

import com.fr.chart.chartattr.Plot;
import com.fr.design.mainframe.chart.gui.style.axis.ChartValuePane;

// Referenced classes of package com.fr.design.chart.axis:
//            ChartStyleAxisPane, AxisStyleObject

public class ValueChartStyleAxisPane extends ChartStyleAxisPane
{

    private static final long serialVersionUID = 0x7626afd72a70c0f3L;

    public ValueChartStyleAxisPane(Plot plot)
    {
        super(plot);
    }

    public final AxisStyleObject[] createAxisStyleObjects(Plot plot)
    {
        return (new AxisStyleObject[] {
            new AxisStyleObject(CATE_AXIS, new ChartValuePane())
        });
    }
}
