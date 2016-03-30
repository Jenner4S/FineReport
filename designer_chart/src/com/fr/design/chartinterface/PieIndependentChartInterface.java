package com.fr.design.chartinterface;

import com.fr.chart.chartattr.Plot;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.chart.fun.impl.AbstractIndependentChartUIWithAPILevel;
import com.fr.design.chart.series.SeriesCondition.impl.Pie3DPlotDataSeriesConditionPane;
import com.fr.design.chart.series.SeriesCondition.impl.PiePlotDataSeriesConditionPane;
import com.fr.design.condition.ConditionAttributesPane;
import com.fr.design.mainframe.chart.gui.ChartDataPane;
import com.fr.design.mainframe.chart.gui.ChartStylePane;
import com.fr.design.mainframe.chart.gui.data.report.AbstractReportDataContentPane;
import com.fr.design.mainframe.chart.gui.data.report.PiePlotReportDataContentPane;
import com.fr.design.mainframe.chart.gui.data.table.AbstractTableDataContentPane;
import com.fr.design.mainframe.chart.gui.data.table.PiePlotTableDataContentPane;
import com.fr.design.mainframe.chart.gui.style.series.Pie2DSeriesPane;
import com.fr.design.mainframe.chart.gui.style.series.Pie3DSeriesPane;
import com.fr.design.mainframe.chart.gui.type.AbstractChartTypePane;
import com.fr.design.mainframe.chart.gui.type.PiePlotPane;

/**
 * Created by eason on 15/4/21.
 */
public class PieIndependentChartInterface extends AbstractIndependentChartUIWithAPILevel {

    public AbstractChartTypePane getPlotTypePane(){
        return new PiePlotPane();
    }

    public AbstractTableDataContentPane getTableDataSourcePane(Plot plot, ChartDataPane parent){
        return new PiePlotTableDataContentPane(parent);
    }

    public AbstractReportDataContentPane getReportDataSourcePane(Plot plot, ChartDataPane parent){
        return new PiePlotReportDataContentPane(parent);
    }

    public BasicBeanPane<Plot> getPlotSeriesPane(ChartStylePane parent, Plot plot){
        return plot.isSupport3D() ? new Pie3DSeriesPane(parent, plot) : new Pie2DSeriesPane(parent, plot);
    }

    public ConditionAttributesPane getPlotConditionPane(Plot plot){
        return plot.isSupport3D() ? new Pie3DPlotDataSeriesConditionPane() : new PiePlotDataSeriesConditionPane();
    }

    /**
     *图标路径
     * @return 图标路径
     */
    public String getIconPath(){
        return "com/fr/design/images/form/toolbar/ChartF-Pie.png";
    }

}