// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.chart.fun.impl;

import com.fr.chart.chartattr.Plot;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.chart.fun.IndependentChartUIProvider;
import com.fr.design.chart.series.SeriesCondition.DataSeriesConditionPane;
import com.fr.design.condition.ConditionAttributesPane;
import com.fr.design.gui.frpane.AttributeChangeListener;
import com.fr.design.mainframe.chart.AbstractChartAttrPane;
import com.fr.design.mainframe.chart.gui.ChartStylePane;
import com.fr.general.ComparatorUtils;

public abstract class AbstractIndependentChartUI
    implements IndependentChartUIProvider
{

    public AbstractIndependentChartUI()
    {
    }

    public int currentAPILevel()
    {
        return -1;
    }

    public AbstractChartAttrPane[] getAttrPaneArray(AttributeChangeListener attributechangelistener)
    {
        return new AbstractChartAttrPane[0];
    }

    public boolean isUseDefaultPane()
    {
        return true;
    }

    public BasicBeanPane getPlotSeriesPane(ChartStylePane chartstylepane, Plot plot)
    {
        return getPlotSeriesPane();
    }

    public BasicBeanPane getPlotSeriesPane()
    {
        return null;
    }

    public boolean equals(Object obj)
    {
        return obj != null && ComparatorUtils.equals(obj.getClass(), getClass());
    }

    public ConditionAttributesPane getPlotConditionPane(Plot plot)
    {
        return new DataSeriesConditionPane();
    }
}
