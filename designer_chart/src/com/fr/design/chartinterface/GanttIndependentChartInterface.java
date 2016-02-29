package com.fr.design.chartinterface;

import com.fr.chart.chartattr.Plot;
import com.fr.design.chart.fun.impl.AbstractIndependentChartUI;
import com.fr.design.chart.series.SeriesCondition.impl.GanttPlotDataSeriesConditionPane;
import com.fr.design.condition.ConditionAttributesPane;
import com.fr.design.mainframe.chart.gui.ChartDataPane;
import com.fr.design.mainframe.chart.gui.data.report.AbstractReportDataContentPane;
import com.fr.design.mainframe.chart.gui.data.report.GanttPlotReportDataContentPane;
import com.fr.design.mainframe.chart.gui.data.table.AbstractTableDataContentPane;
import com.fr.design.mainframe.chart.gui.data.table.GanttPlotTableDataContentPane;
import com.fr.design.mainframe.chart.gui.type.AbstractChartTypePane;
import com.fr.design.mainframe.chart.gui.type.GanttPlotPane;

/**
 * Created by eason on 15/4/21.
 */
public class GanttIndependentChartInterface extends AbstractIndependentChartUI {

    public AbstractChartTypePane getPlotTypePane(){
        return new GanttPlotPane();
    }

    public AbstractTableDataContentPane getTableDataSourcePane(Plot plot, ChartDataPane parent){
        return new GanttPlotTableDataContentPane(parent);
    }

    public AbstractReportDataContentPane getReportDataSourcePane(Plot plot, ChartDataPane parent){
        return new GanttPlotReportDataContentPane(parent);
    }

    public ConditionAttributesPane getPlotConditionPane(Plot plot){
        return new GanttPlotDataSeriesConditionPane();
    }

    /**
     *图标路径
     * @return 图标路径
     */
    public String getIconPath(){
        return "com/fr/design/images/form/toolbar/ChartF-Gantt.png";
    }

}