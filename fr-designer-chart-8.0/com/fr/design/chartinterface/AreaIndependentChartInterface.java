// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.chartinterface;

import com.fr.chart.chartattr.Plot;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.chart.fun.impl.AbstractIndependentChartUIWithAPILevel;
import com.fr.design.chart.series.SeriesCondition.DataSeriesConditionPane;
import com.fr.design.chart.series.SeriesCondition.impl.Area3DPlotDataSeriesConditionPane;
import com.fr.design.chart.series.SeriesCondition.impl.AreaPlotDataSeriesCondtionPane;
import com.fr.design.condition.ConditionAttributesPane;
import com.fr.design.mainframe.chart.gui.ChartDataPane;
import com.fr.design.mainframe.chart.gui.ChartStylePane;
import com.fr.design.mainframe.chart.gui.data.report.AbstractReportDataContentPane;
import com.fr.design.mainframe.chart.gui.data.report.CategoryPlotReportDataContentPane;
import com.fr.design.mainframe.chart.gui.data.table.AbstractTableDataContentPane;
import com.fr.design.mainframe.chart.gui.data.table.CategoryPlotTableDataContentPane;
import com.fr.design.mainframe.chart.gui.style.series.*;
import com.fr.design.mainframe.chart.gui.type.AbstractChartTypePane;
import com.fr.design.mainframe.chart.gui.type.AreaPlotPane;

public class AreaIndependentChartInterface extends AbstractIndependentChartUIWithAPILevel
{

    public AreaIndependentChartInterface()
    {
    }

    public AbstractChartTypePane getPlotTypePane()
    {
        return new AreaPlotPane();
    }

    public AbstractTableDataContentPane getTableDataSourcePane(Plot plot, ChartDataPane chartdatapane)
    {
        return new CategoryPlotTableDataContentPane(chartdatapane);
    }

    public AbstractReportDataContentPane getReportDataSourcePane(Plot plot, ChartDataPane chartdatapane)
    {
        return new CategoryPlotReportDataContentPane(chartdatapane);
    }

    public BasicBeanPane getPlotSeriesPane(ChartStylePane chartstylepane, Plot plot)
    {
        return ((BasicBeanPane) (plot.isSupport3D() ? new Area3DSeriesPane(chartstylepane, plot) : new AreaSeriesPane(chartstylepane, plot)));
    }

    public ConditionAttributesPane getPlotConditionPane(Plot plot)
    {
        return ((ConditionAttributesPane) (plot.isSupport3D() ? new Area3DPlotDataSeriesConditionPane() : new AreaPlotDataSeriesCondtionPane()));
    }

    public String getIconPath()
    {
        return "com/fr/design/images/form/toolbar/ChartF-Area.png";
    }
}
