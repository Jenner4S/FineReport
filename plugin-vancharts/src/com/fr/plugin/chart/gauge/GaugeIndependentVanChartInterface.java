package com.fr.plugin.chart.gauge;

import com.fr.chart.chartattr.Plot;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.chart.fun.impl.AbstractIndependentChartUI;
import com.fr.design.gui.frpane.AttributeChangeListener;
import com.fr.design.mainframe.chart.AbstractChartAttrPane;
import com.fr.design.mainframe.chart.gui.ChartDataPane;
import com.fr.design.mainframe.chart.gui.ChartStylePane;
import com.fr.design.mainframe.chart.gui.data.report.AbstractReportDataContentPane;
import com.fr.design.mainframe.chart.gui.data.report.CategoryPlotReportDataContentPane;
import com.fr.design.mainframe.chart.gui.data.report.MeterPlotReportDataContentPane;
import com.fr.design.mainframe.chart.gui.data.table.AbstractTableDataContentPane;
import com.fr.design.mainframe.chart.gui.data.table.CategoryPlotTableDataContentPane;
import com.fr.design.mainframe.chart.gui.data.table.MeterPlotTableDataContentPane;
import com.fr.design.mainframe.chart.gui.type.AbstractChartTypePane;
import com.fr.plugin.chart.designer.other.VanChartOtherPane;
import com.fr.plugin.chart.designer.style.VanChartStylePane;

/**
 * Created by Mitisky on 15/11/27.
 */
public class GaugeIndependentVanChartInterface extends AbstractIndependentChartUI {
    @Override
    public String getIconPath() {
        return "com/fr/design/images/form/toolbar/ChartF-Meter.png";
    }

    @Override
    public AbstractChartTypePane getPlotTypePane() {
        return new VanChartGaugePlotPane();
    }

    public AbstractTableDataContentPane getTableDataSourcePane(Plot plot, ChartDataPane parent){
        if(plot instanceof VanChartGaugePlot){
            VanChartGaugePlot gaugePlot = (VanChartGaugePlot)plot;
            switch (gaugePlot.getGaugeStyle()){
                case POINTER:
                    return new CategoryPlotTableDataContentPane(parent);
                case POINTER_SEMI:
                    return new CategoryPlotTableDataContentPane(parent);
                default:
                    break;
            }
        }
        return new MeterPlotTableDataContentPane(parent);
    }

    public AbstractReportDataContentPane getReportDataSourcePane(Plot plot, ChartDataPane parent){
        if(plot instanceof VanChartGaugePlot){
            VanChartGaugePlot gaugePlot = (VanChartGaugePlot)plot;
            switch (gaugePlot.getGaugeStyle()){
                case POINTER:
                    return new CategoryPlotReportDataContentPane(parent);
                case POINTER_SEMI:
                    return new CategoryPlotReportDataContentPane(parent);
                default:
                    break;
            }
        }
        return new MeterPlotReportDataContentPane(parent);
    }

    public BasicBeanPane<Plot> getPlotSeriesPane(ChartStylePane parent, Plot plot){
        return new VanChartGaugeSeriesPane(parent, plot);
    }

    /**
     * ͼ������Խ�������
     * @return ���Խ���
     */
    public AbstractChartAttrPane[] getAttrPaneArray(AttributeChangeListener listener){
        VanChartStylePane stylePane = new VanChartStylePane(listener);
        VanChartOtherPane otherPane = new VanChartOtherPane(listener);
        return new AbstractChartAttrPane[]{stylePane, otherPane};
    }

    /**
     * �Ƿ�ʹ��Ĭ�ϵĽ��棬Ϊ�˱�����������л�
     * @return �Ƿ�ʹ��Ĭ�ϵĽ���
     */
    public boolean isUseDefaultPane(){
        return false;
    }
}
