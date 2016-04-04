// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.chart.axis;

import com.fr.chart.chartattr.Plot;
import com.fr.design.mainframe.chart.gui.style.axis.*;

// Referenced classes of package com.fr.design.chart.axis:
//            ChartStyleAxisPane, AxisStyleObject

public class TernaryChartStyleAxisPane extends ChartStyleAxisPane
{

    private static final long serialVersionUID = 0x77dc35e5b84a8d4L;

    public TernaryChartStyleAxisPane(Plot plot)
    {
        super(plot);
    }

    public AxisStyleObject[] createAxisStyleObjects(Plot plot)
    {
        return (new AxisStyleObject[] {
            new AxisStyleObject(CATE_AXIS, new ChartCategoryPane()), new AxisStyleObject(getValueAxisName(), new ChartValuePane()), new AxisStyleObject(getSecondValueAxisName(), new ChartSecondValuePane())
        });
    }

    protected String getValueAxisName()
    {
        return VALUE_AXIS;
    }

    protected String getSecondValueAxisName()
    {
        return SECOND_AXIS;
    }
}
