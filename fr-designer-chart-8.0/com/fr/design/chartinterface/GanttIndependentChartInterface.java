// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.chartinterface;

import com.fr.chart.chartattr.Plot;
import com.fr.design.chart.fun.impl.AbstractIndependentChartUIWithAPILevel;
import com.fr.design.chart.series.SeriesCondition.impl.GanttPlotDataSeriesConditionPane;
import com.fr.design.condition.ConditionAttributesPane;
import com.fr.design.mainframe.chart.gui.ChartDataPane;
import com.fr.design.mainframe.chart.gui.data.report.AbstractReportDataContentPane;
import com.fr.design.mainframe.chart.gui.data.report.GanttPlotReportDataContentPane;
import com.fr.design.mainframe.chart.gui.data.table.AbstractTableDataContentPane;
import com.fr.design.mainframe.chart.gui.data.table.GanttPlotTableDataContentPane;
import com.fr.design.mainframe.chart.gui.type.AbstractChartTypePane;
import com.fr.design.mainframe.chart.gui.type.GanttPlotPane;

public class GanttIndependentChartInterface extends AbstractIndependentChartUIWithAPILevel
{

    public GanttIndependentChartInterface()
    {
    }

    public AbstractChartTypePane getPlotTypePane()
    {
        return new GanttPlotPane();
    }

    public AbstractTableDataContentPane getTableDataSourcePane(Plot plot, ChartDataPane chartdatapane)
    {
        return new GanttPlotTableDataContentPane(chartdatapane);
    }

    public AbstractReportDataContentPane getReportDataSourcePane(Plot plot, ChartDataPane chartdatapane)
    {
        return new GanttPlotReportDataContentPane(chartdatapane);
    }

    public ConditionAttributesPane getPlotConditionPane(Plot plot)
    {
        return new GanttPlotDataSeriesConditionPane();
    }

    public String getIconPath()
    {
        return "com/fr/design/images/form/toolbar/ChartF-Gantt.png";
    }
}
