// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.chart.gui.style.axis;

import com.fr.chart.chartattr.Axis;
import com.fr.chart.chartattr.Plot;
import com.fr.design.mainframe.chart.gui.style.AbstractChartTabPane;

public abstract class ChartAxisUsePane extends AbstractChartTabPane
{

    public ChartAxisUsePane()
    {
    }

    public abstract void populateBean(Axis axis, Plot plot);

    public abstract void updateBean(Axis axis, Plot plot);
}
