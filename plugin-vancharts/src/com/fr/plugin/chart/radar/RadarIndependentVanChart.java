package com.fr.plugin.chart.radar;

import com.fr.chart.chartattr.Chart;
import com.fr.chart.chartglyph.ConditionAttr;
import com.fr.general.Inter;
import com.fr.plugin.chart.attr.VanChartPlotType;
import com.fr.plugin.chart.base.AttrAreaSeriesFillColorBackground;
import com.fr.plugin.chart.base.VanChartAttrLine;
import com.fr.plugin.chart.base.VanChartAttrMarker;
import com.fr.plugin.chart.vanchart.AbstractIndependentVanChartProvider;
import com.fr.plugin.chart.vanchart.VanChart;

/**
 * Created by Mitisky on 15/12/28.
 */
public class RadarIndependentVanChart extends AbstractIndependentVanChartProvider {
    public static Chart[] RadarVanChartTypes = {
            createVanChartRadar(VanChartPlotType.NORMAL),
            createVanChartRadar(VanChartPlotType.STACK)
    };

    public String getChartName(){
        return "Plugin-ChartF_NewRadar";
    }

    public String getChartUseName(){
        return Inter.getLocText("Plugin-ChartF_NewRadar");
    }

    public Chart[] getChartTypes(){
        return RadarVanChartTypes;
    }

    private static Chart createVanChartRadar(VanChartPlotType plotType) {
        VanChartRadarPlot RadarPlot = new VanChartRadarPlot(plotType);
        createDefaultPlotStyleAttr(RadarPlot);
        createDefaultCondition(RadarPlot);

        return new VanChart(RadarPlot);
    }

    //数据点提示、标记点、线、填充颜色、边框
    private static void createDefaultCondition(VanChartRadarPlot plot) {
        ConditionAttr defaultAttr = plot.getConditionCollection().getDefaultAttr();
        createDefaultTooltipCondition(defaultAttr, plot);
        if(plot.isNormalChart()){
            defaultAttr.addDataSeriesCondition(new VanChartAttrMarker());
            defaultAttr.addDataSeriesCondition(new VanChartAttrLine());
            AttrAreaSeriesFillColorBackground fillColorBackground = new AttrAreaSeriesFillColorBackground();
            fillColorBackground.setAlpha(0);
            defaultAttr.addDataSeriesCondition(fillColorBackground);
        } else {
            createDefaultSeriesBorderCondition(defaultAttr);
            createDefaultSeriesAlphaCondition(defaultAttr);
        }
    }
}
