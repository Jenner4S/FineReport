package com.fr.solution.plugin.chart.echarts;

import com.fr.chart.chartattr.Plot;
import com.fr.design.chart.fun.impl.AbstractIndependentChartUI;
import com.fr.design.gui.frpane.AttributeChangeListener;
import com.fr.design.mainframe.chart.AbstractChartAttrPane;
import com.fr.design.mainframe.chart.gui.ChartDataPane;
import com.fr.design.mainframe.chart.gui.data.report.AbstractReportDataContentPane;
import com.fr.design.mainframe.chart.gui.data.table.AbstractTableDataContentPane;
import com.fr.design.mainframe.chart.gui.type.AbstractChartTypePane;
import com.fr.solution.plugin.chart.echarts.ui.map.CategoryPlotReportDataContentPane;
import com.fr.solution.plugin.chart.echarts.ui.map.CategoryPlotTableDataContentPane;
import com.fr.solution.plugin.chart.echarts.ui.map.ChineseChartTypePane;
import com.fr.solution.plugin.chart.echarts.ui.map.MapEChartsStylePane;

/**
 * Created by richie on 16/1/29.
 */
public class ChineseMapUI extends AbstractIndependentChartUI  {
    private AbstractChartAttrPane stylePane;

	@Override
    public AbstractChartTypePane getPlotTypePane() {
        return  new  ChineseChartTypePane();
    }

    public AbstractChartAttrPane[] getAttrPaneArray(AttributeChangeListener listener){
        if (stylePane == null) {
            stylePane = new MapEChartsStylePane(listener);
            
        }
        return new AbstractChartAttrPane[]{stylePane};
    }
    @Override
    public AbstractTableDataContentPane getTableDataSourcePane(Plot plot, ChartDataPane parent) {
//        return new ChineseMapTableDataContentPane(parent);
    	return new CategoryPlotTableDataContentPane(parent);
    }

    @Override
    public AbstractReportDataContentPane getReportDataSourcePane(Plot plot, ChartDataPane parent) {
//    	return new ChineseMapTableDataContentPane(parent);
//        return new ChineseMapReportDataContentPane(parent);
        return new CategoryPlotReportDataContentPane(parent);
//    	return null;
        
        
    }
    
    @Override
    public String getIconPath() {
        return "com/fr/solution/plugin/chart/echarts/images/map/chinese.png";
    }

    @Override
    public boolean isUseDefaultPane() {
        return false;
    }
}
