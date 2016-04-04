// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.chart.axis;

import com.fr.chart.chartattr.Plot;
import com.fr.design.mainframe.chart.gui.style.axis.ChartRadarPane;

// Referenced classes of package com.fr.design.chart.axis:
//            ChartStyleAxisPane, AxisStyleObject

public class RadarChartStyleAxisPane extends ChartStyleAxisPane
{

    private static final long serialVersionUID = 0x7bc9ff8a993dd148L;

    public RadarChartStyleAxisPane(Plot plot)
    {
        super(plot);
    }

    public final AxisStyleObject[] createAxisStyleObjects(Plot plot)
    {
        return (new AxisStyleObject[] {
            new AxisStyleObject(CATE_AXIS, new ChartRadarPane())
        });
    }
}
