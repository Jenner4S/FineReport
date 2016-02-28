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
     * �������PlotGlyph
     * @param chartData ͼ����ص�����
     * @return ����plotGlyph
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
        dataSeries.setFillColorBackground(fillColorBackground);//���ͼ�����ɫֻ��ϵ����Ч
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
     * ����Plot����������,
     *
     * @return ��������.
     */
    public String getPlotName() {
        return Inter.getLocText("Plugin-ChartF_NewArea");
    }

    public String getPlotID(){
        return "VanChartAreaPlot";
    }

    /**
     * �Ƚ� �л����ͽ����е� ����Plot����
     *
     * @param newPlot ��plot
     * @return �����Ƿ����
     */
    public boolean matchPlotType(Plot newPlot) {
        return newPlot instanceof VanChartAreaPlot;
    }

    /**
     * �ж�ͼ�������Ƿ���obClass
     * @param obClass �������
     * @return �Ƿ���obClass����
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
