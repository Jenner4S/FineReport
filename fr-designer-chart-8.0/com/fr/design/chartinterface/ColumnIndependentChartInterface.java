// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.chartinterface;

import com.fr.chart.chartattr.Plot;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.chart.fun.impl.AbstractIndependentChartUIWithAPILevel;
import com.fr.design.chart.series.SeriesCondition.impl.*;
import com.fr.design.condition.ConditionAttributesPane;
import com.fr.design.mainframe.chart.gui.ChartDataPane;
import com.fr.design.mainframe.chart.gui.ChartStylePane;
import com.fr.design.mainframe.chart.gui.data.report.AbstractReportDataContentPane;
import com.fr.design.mainframe.chart.gui.data.report.CategoryPlotMoreCateReportDataContentPane;
import com.fr.design.mainframe.chart.gui.data.table.AbstractTableDataContentPane;
import com.fr.design.mainframe.chart.gui.data.table.CategoryPlotMoreCateTableDataContentPane;
import com.fr.design.mainframe.chart.gui.style.series.*;
import com.fr.design.mainframe.chart.gui.type.AbstractChartTypePane;
import com.fr.design.mainframe.chart.gui.type.ColumnPlotPane;

public class ColumnIndependentChartInterface extends AbstractIndependentChartUIWithAPILevel
{

    public ColumnIndependentChartInterface()
    {
    }

    public AbstractChartTypePane getPlotTypePane()
    {
        return new ColumnPlotPane();
    }

    public AbstractTableDataContentPane getTableDataSourcePane(Plot plot, ChartDataPane chartdatapane)
    {
        return new CategoryPlotMoreCateTableDataContentPane(chartdatapane);
    }

    public AbstractReportDataContentPane getReportDataSourcePane(Plot plot, ChartDataPane chartdatapane)
    {
        return new CategoryPlotMoreCateReportDataContentPane(chartdatapane);
    }

    public BasicBeanPane getPlotSeriesPane(ChartStylePane chartstylepane, Plot plot)
    {
        return ((BasicBeanPane) (plot.isSupport3D() ? new Bar3DSeriesPane(chartstylepane, plot) : new Bar2DSeriesPane(chartstylepane, plot)));
    }

    public ConditionAttributesPane getPlotConditionPane(Plot plot)
    {
        return ((ConditionAttributesPane) (plot.isSupport3D() ? new Bar3DPlotDataSeriesConditionPane() : plot.isSupportTrendLine() ? new Bar2DTrendLineDSConditionPane() : new BarPlotDataSeriesConditionPane()));
    }

    public String getIconPath()
    {
        return "com/fr/design/images/form/toolbar/ChartF-Column.png";
    }
}
