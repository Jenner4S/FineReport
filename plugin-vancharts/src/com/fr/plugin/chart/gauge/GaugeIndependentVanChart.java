package com.fr.plugin.chart.gauge;

import com.fr.chart.chartattr.Chart;
import com.fr.chart.chartglyph.ConditionAttr;
import com.fr.general.Inter;
import com.fr.plugin.chart.vanchart.AbstractIndependentVanChartProvider;
import com.fr.plugin.chart.vanchart.VanChart;

/**
 * Created by Mitisky on 15/11/27.
 */
public class GaugeIndependentVanChart extends AbstractIndependentVanChartProvider {

    public static Chart[] GaugeVanChartTypes = {
            createVanChartGauge(GaugeStyle.POINTER),
            createVanChartGauge(GaugeStyle.POINTER_SEMI),
            createVanChartGauge(GaugeStyle.RING),
            createVanChartGauge(GaugeStyle.SLOT),
            createVanChartGauge(GaugeStyle.THERMOMETER)
    };

    public String getChartName(){
        return "Plugin-ChartF_NewGauge";
    }

    public String getChartUseName(){
        return Inter.getLocText("Plugin-ChartF_NewGauge");
    }

    public Chart[] getChartTypes(){
        return GaugeVanChartTypes;
    }

    private static Chart createVanChartGauge(GaugeStyle gaugeStyle) {
        VanChartGaugePlot GaugePlot = new VanChartGaugePlot(gaugeStyle);
        createDefaultPlotStyleAttr(GaugePlot);
        createDefaultCondition(GaugePlot);

        return new VanChart(GaugePlot);
    }

    //数据点提示、标签
    private static void createDefaultCondition(VanChartGaugePlot plot) {
        ConditionAttr defaultAttr = plot.getConditionCollection().getDefaultAttr();
        createDefaultTooltipCondition(defaultAttr, plot);
        defaultAttr.addDataSeriesCondition(plot.getDefaultAttrLabel());
    }
}
