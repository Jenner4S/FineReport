package com.fr.design.chartinterface;

import com.fr.chart.chartattr.Plot;
import com.fr.design.chart.fun.impl.AbstractIndependentChartUI;
import com.fr.design.chart.series.SeriesCondition.impl.RadarPlotDataSeriesConditionPane;
import com.fr.design.condition.ConditionAttributesPane;
import com.fr.design.mainframe.chart.gui.ChartDataPane;
import com.fr.design.mainframe.chart.gui.data.report.AbstractReportDataContentPane;
import com.fr.design.mainframe.chart.gui.data.report.CategoryPlotReportDataContentPane;
import com.fr.design.mainframe.chart.gui.data.table.AbstractTableDataContentPane;
import com.fr.design.mainframe.chart.gui.data.table.CategoryPlotTableDataContentPane;
import com.fr.design.mainframe.chart.gui.type.AbstractChartTypePane;
import com.fr.design.mainframe.chart.gui.type.RadarPlotPane;

/**
 * Created by eason on 15/4/21.
 */
public class RadarIndependentChartInterface extends AbstractIndependentChartUI {

    public AbstractChartTypePane getPlotTypePane(){
        return new RadarPlotPane();
    }

    public AbstractTableDataContentPane getTableDataSourcePane(Plot plot, ChartDataPane parent){
        return new CategoryPlotTableDataContentPane(parent);
    }

    public AbstractReportDataContentPane getReportDataSourcePane(Plot plot, ChartDataPane parent){
        return new CategoryPlotReportDataContentPane(parent);
    }

    public ConditionAttributesPane getPlotConditionPane(Plot plot){
        return new RadarPlotDataSeriesConditionPane();
    }


    /**
     *ͼ��·��
     * @return ͼ��·��
     */
    public String getIconPath(){
        return "com/fr/design/images/form/toolbar/ChartF-Radar.png";
    }

}
