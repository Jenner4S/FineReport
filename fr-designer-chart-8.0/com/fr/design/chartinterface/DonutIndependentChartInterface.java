// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.chartinterface;

import com.fr.chart.chartattr.Plot;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.chart.fun.impl.AbstractIndependentChartUIWithAPILevel;
import com.fr.design.chart.series.SeriesCondition.DataSeriesConditionPane;
import com.fr.design.chart.series.SeriesCondition.impl.Donut2DPlotDataSeriesConditionPane;
import com.fr.design.condition.ConditionAttributesPane;
import com.fr.design.mainframe.chart.gui.ChartDataPane;
import com.fr.design.mainframe.chart.gui.ChartStylePane;
import com.fr.design.mainframe.chart.gui.data.report.AbstractReportDataContentPane;
import com.fr.design.mainframe.chart.gui.data.report.CategoryPlotReportDataContentPane;
import com.fr.design.mainframe.chart.gui.data.table.AbstractTableDataContentPane;
import com.fr.design.mainframe.chart.gui.data.table.CategoryPlotTableDataContentPane;
import com.fr.design.mainframe.chart.gui.style.series.Donut2DSeriesPane;
import com.fr.design.mainframe.chart.gui.type.AbstractChartTypePane;
import com.fr.design.mainframe.chart.gui.type.DonutPlotPane;

public class DonutIndependentChartInterface extends AbstractIndependentChartUIWithAPILevel
{

    public DonutIndependentChartInterface()
    {
    }

    public AbstractChartTypePane getPlotTypePane()
    {
        return new DonutPlotPane();
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
        return plot.isSupport3D() ? null : new Donut2DSeriesPane(chartstylepane, plot);
    }

    public ConditionAttributesPane getPlotConditionPane(Plot plot)
    {
        return ((ConditionAttributesPane) (plot.isSupport3D() ? new DataSeriesConditionPane() : new Donut2DPlotDataSeriesConditionPane()));
    }

    public String getIconPath()
    {
        return "com/fr/design/images/form/toolbar/ChartF-Donut.png";
    }
}
