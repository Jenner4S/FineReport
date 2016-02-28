package com.fr.plugin.chart.area;

import com.fr.base.chart.chartdata.ChartData;
import com.fr.chart.base.ChartFunctionProcessor;
import com.fr.chart.chartattr.Plot;
import com.fr.chart.chartglyph.ConditionCollection;
import com.fr.chart.chartglyph.PlotGlyph;
import com.fr.general.ComparatorUtils;
import com.fr.general.Inter;
import com.fr.plugin.chart.attr.VanChartPlotType;
import com.fr.plugin.chart.attr.axis.VanChartAxis;
import com.fr.plugin.chart.base.AttrAreaSeriesFillColorBackground;
import com.fr.plugin.chart.base.AttrSeriesStackAndAxis;
import com.fr.plugin.chart.glyph.VanChartDataSeries;
import com.fr.plugin.chart.line.VanChartLinePlot;
import com.fr.stable.fun.FunctionProcessor;

/**
 * Created by Mitisky on 15/11/18.
 */
public class VanChartAreaPlot extends VanChartLinePlot{

    private static final long serialVersionUID = -5646135698046165505L;

    public VanChartAreaPlot(){
        super();
    }

    public VanChartAreaPlot(VanChartPlotType vanChartPlotType){
        super(vanChartPlotType);
    }

    /**
     * 创建相关PlotGlyph
     * @param chartData 图表相关的数据
     * @return 返回plotGlyph
     */
    public PlotGlyph createPlotGlyph(ChartData chartData) {
        VanChartAreaPlotGlyph plotGlyph = new VanChartAreaPlotGlyph();

        install4PlotGlyph(plotGlyph, chartData);
        installAxisGlyph(plotGlyph, chartData);

        return plotGlyph;
    }

    protected void dealDataSeriesCustomCondition(VanChartDataSeries dataSeries, ConditionCollection conditionCollection, ConditionCollection stackAndAxisCondition){
        super.dealDataSeriesCustomCondition(dataSeries, conditionCollection, stackAndAxisCondition);

        AttrAreaSeriesFillColorBackground fillColorBackground = (AttrAreaSeriesFillColorBackground)conditionCollection.getCustomDataSeriesCondition(AttrAreaSeriesFillColorBackground.class, dataSeries);
        dataSeries.setFillColorBackground(fillColorBackground);//面积图的填充色只对系列有效
    }

    protected void dealDataSeriesStackAndAxisCondition(VanChartDataSeries dataSeries, ConditionCollection stackAndAxisCondition){
        if(isCustomChart()){
            AttrSeriesStackAndAxis attrSeriesStackAndAxis = (AttrSeriesStackAndAxis)stackAndAxisCondition.getCustomDataSeriesCondition(AttrSeriesStackAndAxis.class, dataSeries);
            dataSeries.setAttrSeriesStackAndAxis(attrSeriesStackAndAxis);
            if(attrSeriesStackAndAxis != null && attrSeriesStackAndAxis.isPercentStacked()){
                VanChartAxis axis = getDefaultValueAxis(attrSeriesStackAndAxis);
                axis.setPercentage(true);
            }
        } else if(isStackChart()){
            AttrSeriesStackAndAxis attrSeriesStackAndAxis = new AttrSeriesStackAndAxis();
            dataSeries.setAttrSeriesStackAndAxis(attrSeriesStackAndAxis);
        } else if(isPercentStackChart()){
            AttrSeriesStackAndAxis attrSeriesStackAndAxis = new AttrSeriesStackAndAxis();
            attrSeriesStackAndAxis.setPercentStacked(true);
            dataSeries.setAttrSeriesStackAndAxis(attrSeriesStackAndAxis);
            VanChartAxis axis = getDefaultValueAxis();
            axis.setPercentage(true);
        }
    }

    /**
     * 返回Plot的类型名称,
     *
     * @return 类型名称.
     */
    public String getPlotName() {
        return Inter.getLocText("Plugin-ChartF_NewArea");
    }

    public String getPlotID(){
        return "VanChartAreaPlot";
    }

    /**
     * 比较 切换类型界面中的 大致Plot类型
     *
     * @param newPlot 新plot
     * @return 两者是否相符
     */
    public boolean matchPlotType(Plot newPlot) {
        return newPlot instanceof VanChartAreaPlot;
    }

    /**
     * 判断图表类型是否是obClass
     * @param obClass 传入对象
     * @return 是否是obClass对象
     */
    public boolean accept(Class<? extends Plot> obClass){
        return ComparatorUtils.equals(VanChartAreaPlot.class, obClass);
    }

    public boolean equals(Object ob) {
        return ob instanceof VanChartAreaPlot && super.equals(ob);
    }

    public FunctionProcessor getFunctionToRecord() {
        return ChartFunctionProcessor.AREA_VAN_CHARTS;
    }

}
