// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.chartinterface;

import com.fr.chart.chartattr.Plot;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.chart.fun.impl.AbstractIndependentChartUIWithAPILevel;
import com.fr.design.mainframe.chart.gui.ChartDataPane;
import com.fr.design.mainframe.chart.gui.ChartStylePane;
import com.fr.design.mainframe.chart.gui.data.report.AbstractReportDataContentPane;
import com.fr.design.mainframe.chart.gui.data.report.MeterPlotReportDataContentPane;
import com.fr.design.mainframe.chart.gui.data.table.AbstractTableDataContentPane;
import com.fr.design.mainframe.chart.gui.data.table.MeterPlotTableDataContentPane;
import com.fr.design.mainframe.chart.gui.style.series.MeterSeriesPane;
import com.fr.design.mainframe.chart.gui.type.AbstractChartTypePane;
import com.fr.design.mainframe.chart.gui.type.MeterPlotPane;

public class MeterIndependentChartInterface extends AbstractIndependentChartUIWithAPILevel
{

    public MeterIndependentChartInterface()
    {
    }

    public AbstractChartTypePane getPlotTypePane()
    {
        return new MeterPlotPane();
    }

    public AbstractTableDataContentPane getTableDataSourcePane(Plot plot, ChartDataPane chartdatapane)
    {
        return new MeterPlotTableDataContentPane(chartdatapane);
    }

    public AbstractReportDataContentPane getReportDataSourcePane(Plot plot, ChartDataPane chartdatapane)
    {
        return new MeterPlotReportDataContentPane(chartdatapane);
    }

    public BasicBeanPane getPlotSeriesPane(ChartStylePane chartstylepane, Plot plot)
    {
        return new MeterSeriesPane(chartstylepane, plot);
    }

    public String getIconPath()
    {
        return "com/fr/design/images/form/toolbar/ChartF-Meter.png";
    }
}
