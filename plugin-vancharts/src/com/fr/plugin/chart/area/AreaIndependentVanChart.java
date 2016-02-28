package com.fr.plugin.chart.area;

import com.fr.chart.chartattr.Chart;
import com.fr.chart.chartglyph.ConditionAttr;
import com.fr.general.Inter;
import com.fr.plugin.chart.attr.VanChartAttrHelper;
import com.fr.plugin.chart.attr.VanChartPlotType;
import com.fr.plugin.chart.attr.axis.VanChartAxis;
import com.fr.plugin.chart.base.AttrAreaSeriesFillColorBackground;
import com.fr.plugin.chart.base.VanChartAttrLine;
import com.fr.plugin.chart.base.VanChartAttrMarker;
import com.fr.plugin.chart.vanchart.AbstractIndependentVanChartProvider;
import com.fr.plugin.chart.vanchart.VanChart;

/**
 * Created by Mitisky on 15/11/18.
 */
public class AreaIndependentVanChart extends AbstractIndependentVanChartProvider {

    public static Chart[] AreaVanChartTypes = {
            createVanChartArea(VanChartPlotType.NORMAL),
            createVanChartArea(VanChartPlotType.STACK),
            createVanChartArea(VanChartPlotType.STACK_BY_PERCENT),
            createVanChartArea(VanChartPlotType.CUSTOM)
    };

    public String getChartName(){
        return "Plugin-ChartF_NewArea";
    }

    public String getChartUseName(){
        return Inter.getLocText("Plugin-ChartF_NewArea");
    }

    public Chart[] getChartTypes(){
        return AreaVanChartTypes;
    }

    private static Chart createVanChartArea(VanChartPlotType vanChartPlotType) {
        VanChartAreaPlot areaPlot = new VanChartAreaPlot(vanChartPlotType);

        createDefaultPlotStyleAttr(areaPlot);

        createDefaultCondition(areaPlot);

        VanChartAxis defaultValueAxis = areaPlot.getDefaultYAxis();
        if(areaPlot.isCustomChart()){
            createDefaultStackAndAxisCondition(areaPlot);
        } else if(areaPlot.isPercentStackChart()){
            setDefaultAxisPercentAndFormat(defaultValueAxis);
        }
        defaultValueAxis.setMainGridColor(VanChartAttrHelper.DEFAULT_MAIN_GRID_COLOR);

        return new VanChart(areaPlot);
    }

    //数据点提示、线型相关的、标记点、填充色
    protected static void createDefaultCondition(VanChartAreaPlot plot) {
        ConditionAttr defaultAttr = plot.getConditionCollection().getDefaultAttr();
        AbstractIndependentVanChartProvider.createDefaultTooltipCondition(defaultAttr, plot);
        defaultAttr.addDataSeriesCondition(new VanChartAttrMarker());
        defaultAttr.addDataSeriesCondition(new VanChartAttrLine());
        defaultAttr.addDataSeriesCondition(new AttrAreaSeriesFillColorBackground());
    }

}
