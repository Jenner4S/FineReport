package com.fr.plugin.chart.column;

import com.fr.chart.base.ChartConstants;
import com.fr.chart.chartattr.Chart;
import com.fr.chart.chartglyph.ConditionAttr;
import com.fr.chart.chartglyph.ConditionCollection;
import com.fr.data.DataConstants;
import com.fr.data.condition.CommonCondition;
import com.fr.data.condition.ListCondition;
import com.fr.data.core.Compare;
import com.fr.general.Inter;
import com.fr.plugin.chart.attr.VanChartAttrHelper;
import com.fr.plugin.chart.attr.VanChartPlotType;
import com.fr.plugin.chart.attr.axis.VanChartAxis;
import com.fr.plugin.chart.base.AttrSeriesStackAndAxis;
import com.fr.plugin.chart.vanchart.AbstractIndependentVanChartProvider;
import com.fr.plugin.chart.vanchart.VanChart;

/**
 * Created by Mitisky on 15/9/24.
 */
public class ColumnIndependentVanChart extends AbstractIndependentVanChartProvider {

    public static Chart[] ColumnVanChartTypes = {
            createVanChartColumn(VanChartPlotType.NORMAL),
            createVanChartColumn(VanChartPlotType.STACK),
            createVanChartColumn(VanChartPlotType.STACK_BY_PERCENT),
            createVanChartColumn(VanChartPlotType.CUSTOM)
    };

    public String getChartName(){
        return "Plugin-ChartF_NewColumn";
    }

    public String getChartUseName(){
        return Inter.getLocText("Plugin-ChartF_NewColumn");
    }

    public Chart[] getChartTypes(){
        return ColumnVanChartTypes;
    }

    private static Chart createVanChartColumn(VanChartPlotType vanChartPlotType) {
        VanChartColumnPlot columnPlot = new VanChartColumnPlot(vanChartPlotType);
        createDefaultPlotStyleAttr(columnPlot);
        createDefaultCondition(columnPlot);

        VanChartAxis defaultValueAxis = columnPlot.getDefaultYAxis();
        if(columnPlot.isCustomChart()){
            createDefaultStackAndAxisCondition(columnPlot);
        } else if(columnPlot.isPercentStackChart()){
            setDefaultAxisPercentAndFormat(defaultValueAxis);
        }
        defaultValueAxis.setMainGridColor(VanChartAttrHelper.DEFAULT_MAIN_GRID_COLOR);

        return new VanChart(columnPlot);
    }

    private static void createDefaultStackAndAxisCondition(VanChartColumnPlot plot) {
        plot.getYAxisList().add(VanChartAttrHelper.createDefaultY2Axis());

        ConditionCollection conditionCollection = new ConditionCollection();
        ConditionAttr conditionAttr = new ConditionAttr();
        conditionAttr.setName(Inter.getLocText("Plugin-ChartF_StackAndSeries") + "1");

        ListCondition condition2 = new ListCondition();
        condition2.addJoinCondition(DataConstants.AND, new CommonCondition(ChartConstants.SERIES_INDEX, Compare.EQUALS, new Integer(2)));
        condition2.addJoinCondition(DataConstants.OR, new CommonCondition(ChartConstants.SERIES_INDEX, Compare.EQUALS, new Integer(3)));
        conditionAttr.setCondition(condition2);

        conditionCollection.addConditionAttr(conditionAttr);
        AttrSeriesStackAndAxis seriesStackAndAxis = new AttrSeriesStackAndAxis();
        conditionAttr.addDataSeriesCondition(seriesStackAndAxis);
        seriesStackAndAxis.setXAxisIndex(0);
        seriesStackAndAxis.setYAxisIndex(1);
        seriesStackAndAxis.setStacked(true);
        seriesStackAndAxis.setPercentStacked(false);

        plot.setStackAndAxisCondition(conditionCollection);
    }

    //数据点提示、系列边框
    private static void createDefaultCondition(VanChartColumnPlot plot) {
        ConditionAttr defaultAttr = plot.getConditionCollection().getDefaultAttr();
        createDefaultTooltipCondition(defaultAttr, plot);
        createDefaultSeriesBorderCondition(defaultAttr);
        createDefaultSeriesAlphaCondition(defaultAttr);
    }
}
