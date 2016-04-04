// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.chart.axis;

import com.fr.chart.chartattr.Plot;
import com.fr.design.mainframe.chart.gui.style.ChartAxisValueTypePane;
import com.fr.design.mainframe.chart.gui.style.axis.ChartCategoryPane;
import com.fr.general.Inter;

// Referenced classes of package com.fr.design.chart.axis:
//            BinaryChartStyleAxisPane, AxisStyleObject

public class GanntChartStyleAxisPane extends BinaryChartStyleAxisPane
{

    public GanntChartStyleAxisPane(Plot plot)
    {
        super(plot);
    }

    protected AxisStyleObject getXAxisPane(Plot plot)
    {
        ChartCategoryPane chartcategorypane = new ChartCategoryPane();
        chartcategorypane.getAxisValueTypePane().removeTextAxisPane();
        return new AxisStyleObject(Inter.getLocText("Chart_Date_Axis"), chartcategorypane);
    }

    protected AxisStyleObject getYAxisPane(Plot plot)
    {
        return new AxisStyleObject(CATE_AXIS, new ChartCategoryPane());
    }
}
