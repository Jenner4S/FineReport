// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.chart.axis;

import com.fr.design.mainframe.chart.gui.style.axis.ChartAxisUsePane;

public class AxisStyleObject
{

    private String name;
    private ChartAxisUsePane axisStylePane;

    public AxisStyleObject(String s, ChartAxisUsePane chartaxisusepane)
    {
        name = s;
        axisStylePane = chartaxisusepane;
    }

    public String getName()
    {
        return name;
    }

    public ChartAxisUsePane getAxisStylePane()
    {
        return axisStylePane;
    }
}
