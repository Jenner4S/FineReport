// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.chart.gui.active.action;

import com.fr.base.chart.BaseChart;
import com.fr.chart.chartattr.Axis;
import com.fr.chart.chartattr.ChartCollection;
import com.fr.chart.chartglyph.AxisGlyph;
import com.fr.design.actions.UpdateAction;
import com.fr.design.chart.gui.ChartComponent;

public abstract class ChartComponentAction extends UpdateAction
{

    protected ChartComponent chartComponent;

    public ChartComponentAction(ChartComponent chartcomponent)
    {
        chartComponent = chartcomponent;
    }

    public void reset()
    {
        chartComponent.reset();
    }

    public void repaint()
    {
        chartComponent.repaint();
    }

    public BaseChart getEditingChart()
    {
        return chartComponent.getEditingChart();
    }

    public ChartCollection getChartCollection()
    {
        return chartComponent.getChartCollection();
    }

    public Axis getActiveAxis()
    {
        return chartComponent.getActiveAxis();
    }

    public AxisGlyph getActiveAxisGlyph()
    {
        return chartComponent.getActiveAxisGlyph();
    }
}
