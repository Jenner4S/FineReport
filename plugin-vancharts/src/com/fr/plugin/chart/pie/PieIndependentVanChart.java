package com.fr.plugin.chart.pie;

import com.fr.chart.chartattr.Chart;
import com.fr.chart.chartglyph.ConditionAttr;
import com.fr.general.Inter;
import com.fr.plugin.chart.PiePlot4VanChart;
import com.fr.plugin.chart.base.ChartRoseType;
import com.fr.plugin.chart.vanchart.AbstractIndependentVanChartProvider;
import com.fr.plugin.chart.vanchart.VanChart;

/**
 * Created by Mitisky on 15/8/4.
 */
public class PieIndependentVanChart extends AbstractIndependentVanChartProvider {
    public static Chart[] newPieChartTypes = {
            createNewPieChart(ChartRoseType.PIE),
            createNewPieChart(ChartRoseType.PIE_SAME_ARC),
            createNewPieChart(ChartRoseType.PIE_DIFFERENT_ARC)
    };

    public String getChartName(){
        return "Plugin-ChartF_NewPie";
    }

    public String getChartUseName(){
        return Inter.getLocText("Plugin-ChartF_NewPie");
    }

    public Chart[] getChartTypes(){
        return newPieChartTypes;
    }

    private static Chart createNewPieChart(ChartRoseType roseType) {
        PiePlot4VanChart piePlot = new PiePlot4VanChart();
        piePlot.setRoseType(roseType);

        createDefaultPlotStyleAttr(piePlot);
        createDefaultCondition(piePlot);

        return new VanChart(piePlot);
    }


    private static void createDefaultCondition(PiePlot4VanChart plot) {
        ConditionAttr defaultAttr = plot.getConditionCollection().getDefaultAttr();
        createDefaultTooltipCondition(defaultAttr, plot);
        createDefaultSeriesBorderCondition(defaultAttr);
    }

}
