// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.chartinterface;

import com.fr.chart.chartattr.Plot;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.chart.fun.impl.AbstractIndependentChartUIWithAPILevel;
import com.fr.design.chart.series.SeriesCondition.DataSeriesConditionPane;
import com.fr.design.chart.series.SeriesCondition.impl.XYScatterPlotDataSeriesConditionPane;
import com.fr.design.condition.ConditionAttributesPane;
import com.fr.design.mainframe.chart.gui.ChartDataPane;
import com.fr.design.mainframe.chart.gui.ChartStylePane;
import com.fr.design.mainframe.chart.gui.data.report.AbstractReportDataContentPane;
import com.fr.design.mainframe.chart.gui.data.report.XYScatterPlotReportDataContentPane;
import com.fr.design.mainframe.chart.gui.data.table.AbstractTableDataContentPane;
import com.fr.design.mainframe.chart.gui.data.table.XYScatterPlotTableDataContentPane;
import com.fr.design.mainframe.chart.gui.style.series.XYScatterSeriesPane;
import com.fr.design.mainframe.chart.gui.type.AbstractChartTypePane;
import com.fr.design.mainframe.chart.gui.type.XYScatterPlotPane;

public class XYScatterIndependentChartInterface extends AbstractIndependentChartUIWithAPILevel
{

    public XYScatterIndependentChartInterface()
    {
    }

    public AbstractChartTypePane getPlotTypePane()
    {
        return new XYScatterPlotPane();
    }

    public AbstractTableDataContentPane getTableDataSourcePane(Plot plot, ChartDataPane chartdatapane)
    {
        return new XYScatterPlotTableDataContentPane(chartdatapane);
    }

    public AbstractReportDataContentPane getReportDataSourcePane(Plot plot, ChartDataPane chartdatapane)
    {
        return new XYScatterPlotReportDataContentPane(chartdatapane);
    }

    public BasicBeanPane getPlotSeriesPane(ChartStylePane chartstylepane, Plot plot)
    {
        return new XYScatterSeriesPane(chartstylepane, plot);
    }

    public ConditionAttributesPane getPlotConditionPane(Plot plot)
    {
        return ((ConditionAttributesPane) (plot.isSupportTrendLine() ? new XYScatterPlotDataSeriesConditionPane() : new DataSeriesConditionPane()));
    }

    public String getIconPath()
    {
        return "com/fr/design/images/form/toolbar/ChartF-XYScatter.png";
    }
}
