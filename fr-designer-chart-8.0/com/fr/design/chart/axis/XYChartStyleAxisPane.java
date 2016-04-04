// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.chart.axis;

import com.fr.chart.chartattr.Plot;
import com.fr.design.mainframe.chart.gui.style.axis.ChartValuePane;
import com.fr.general.Inter;

// Referenced classes of package com.fr.design.chart.axis:
//            BinaryChartStyleAxisPane, AxisStyleObject

public class XYChartStyleAxisPane extends BinaryChartStyleAxisPane
{

    public XYChartStyleAxisPane(Plot plot)
    {
        super(plot);
    }

    protected AxisStyleObject getXAxisPane(Plot plot)
    {
        return new AxisStyleObject(Inter.getLocText("ChartF-X_Axis"), new ChartValuePane());
    }

    protected AxisStyleObject getYAxisPane(Plot plot)
    {
        return new AxisStyleObject(Inter.getLocText("ChartF-Y_Axis"), new ChartValuePane());
    }
}
