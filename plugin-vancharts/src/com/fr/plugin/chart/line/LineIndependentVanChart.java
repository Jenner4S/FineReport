package com.fr.plugin.chart.line;

import com.fr.chart.chartattr.Chart;
import com.fr.chart.chartglyph.ConditionAttr;
import com.fr.general.Inter;
import com.fr.plugin.chart.attr.VanChartAttrHelper;
import com.fr.plugin.chart.attr.VanChartPlotType;
import com.fr.plugin.chart.base.VanChartAttrLine;
import com.fr.plugin.chart.base.VanChartAttrMarker;
import com.fr.plugin.chart.vanchart.AbstractIndependentVanChartProvider;
import com.fr.plugin.chart.vanchart.VanChart;

/**
 * Created by Mitisky on 15/11/5.
 */
public class LineIndependentVanChart extends AbstractIndependentVanChartProvider {

    public static Chart[] LineVanChartTypes = {
            createVanChartLine(VanChartPlotType.NORMAL),
            createVanChartLine(VanChartPlotType.STACK),
            createVanChartLine(VanChartPlotType.CUSTOM)
    };

    public String getChartName(){
        return "Plugin-ChartF_NewLine";
    }

    public String getChartUseName(){
        return Inter.getLocText("Plugin-ChartF_NewLine");
    }

    public Chart[] getChartTypes(){
        return LineVanChartTypes;
    }

    private static Chart createVanChartLine(VanChartPlotType vanChartPlotType) {
        VanChartLinePlot linePlot = new VanChartLinePlot(vanChartPlotType);
        createDefaultPlotStyleAttr(linePlot);
        createDefaultCondition(linePlot);

        if(linePlot.isCustomChart()){
            createDefaultStackAndAxisCondition(linePlot);
        }
        linePlot.getDefaultYAxis().setMainGridColor(VanChartAttrHelper.DEFAULT_MAIN_GRID_COLOR);

        return new VanChart(linePlot);
    }

    //数据点提示、线型相关的、标记点
    private static void createDefaultCondition(VanChartLinePlot plot) {
        ConditionAttr defaultAttr = plot.getConditionCollection().getDefaultAttr();
        createDefaultTooltipCondition(defaultAttr, plot);
        defaultAttr.addDataSeriesCondition(new VanChartAttrMarker());
        defaultAttr.addDataSeriesCondition(new VanChartAttrLine());
    }
}
