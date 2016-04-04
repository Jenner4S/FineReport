package com.fr.plugin.chart.line;

import com.fr.base.Utils;
import com.fr.base.background.ColorBackground;
import com.fr.base.chart.chartdata.ChartData;
import com.fr.chart.base.AttrBackground;
import com.fr.chart.base.ChartConstants;
import com.fr.chart.base.ChartFunctionProcessor;
import com.fr.chart.base.ChartUtils;
import com.fr.chart.chartattr.Plot;
import com.fr.chart.chartdata.NormalChartData;
import com.fr.chart.chartglyph.ConditionAttr;
import com.fr.chart.chartglyph.ConditionCollection;
import com.fr.chart.chartglyph.DataPoint;
import com.fr.chart.chartglyph.PlotGlyph;
import com.fr.data.DataConstants;
import com.fr.data.condition.AbstractCondition;
import com.fr.data.condition.CommonCondition;
import com.fr.data.condition.JoinCondition;
import com.fr.data.condition.ListCondition;
import com.fr.data.core.Compare;
import com.fr.general.ComparatorUtils;
import com.fr.general.DateUtils;
import com.fr.general.Inter;
import com.fr.general.data.Condition;
import com.fr.plugin.chart.attr.AttrBand;
import com.fr.plugin.chart.attr.VanChartPlotType;
import com.fr.plugin.chart.attr.axis.AxisType;
import com.fr.plugin.chart.attr.axis.VanChartAxis;
import com.fr.plugin.chart.attr.plot.VanChartRectanglePlot;
import com.fr.plugin.chart.base.*;
import com.fr.plugin.chart.glyph.VanChartDataPoint;
import com.fr.plugin.chart.glyph.VanChartDataSeries;
import com.fr.stable.ArrayUtils;
import com.fr.stable.Constants;
import com.fr.stable.FormulaProvider;
import com.fr.stable.ParameterProvider;
import com.fr.stable.fun.FunctionProcessor;

import java.awt.*;

/**
 * Created by Mitisky on 15/11/5.
 */
public class VanChartLinePlot extends VanChartRectanglePlot {

    private static final long serialVersionUID = 2733137397945843905L;

    public VanChartLinePlot() {
        super();
    }
    public VanChartLinePlot(VanChartPlotType vanChartPlotType){
        super(vanChartPlotType);
    }

    /**
     * 创建相关PlotGlyph
     * @param chartData 图表相关的数据
     * @return 返回plotGlyph
     */
    public PlotGlyph createPlotGlyph(ChartData chartData) {
        VanChartLinePlotGlyph plotGlyph = new VanChartLinePlotGlyph();

        install4PlotGlyph(plotGlyph, chartData);
        installAxisGlyph(plotGlyph, chartData);

        return plotGlyph;
    }

    public AttrLabel getDefaultAttrLabel() {
        AttrLabel attrLabel = new AttrLabel();
        attrLabel.getAttrLabelDetail().setPosition(Constants.OUTSIDE);
        return attrLabel;
    }

    protected DataPoint createDataPoint() {
        return new VanChartLineDataPoint();
    }

    protected void dealDataSeriesCustomCondition(VanChartDataSeries dataSeries, ConditionCollection conditionCollection, ConditionCollection stackAndAxisCondition){
        super.dealDataSeriesCustomCondition(dataSeries, conditionCollection, stackAndAxisCondition);

        VanChartAttrLine attrLine = (VanChartAttrLine)conditionCollection.getCustomDataSeriesCondition(VanChartAttrLine.class, dataSeries);
        dataSeries.setAttrLine(attrLine);

        VanChartAttrMarker attrMarker = (VanChartAttrMarker)conditionCollection.getCustomDataSeriesCondition(VanChartAttrMarker.class, dataSeries);
        dataSeries.setMarker(attrMarker);

        dealDataSeriesBands(dataSeries, conditionCollection);
    }

    protected void dealDataSeriesStackAndAxisCondition(VanChartDataSeries dataSeries, ConditionCollection stackAndAxisCondition){
        if(isStackChart()){
            AttrSeriesStackAndAxis attrSeriesStackAndAxis = new AttrSeriesStackAndAxis();
            dataSeries.setAttrSeriesStackAndAxis(attrSeriesStackAndAxis);
        } else if(isCustomChart()){
            AttrSeriesStackAndAxis attrSeriesStackAndAxis = (AttrSeriesStackAndAxis)stackAndAxisCondition.getCustomDataSeriesCondition(AttrSeriesStackAndAxis.class, dataSeries);
            dataSeries.setAttrSeriesStackAndAxis(attrSeriesStackAndAxis);
        }
    }

    //这个是画标签的时候用到的（标签的时候取系列色）,标记点曲系列色的时候也只看整个系列的颜色
    protected void setSeriesColor(VanChartDataPoint dataPoint, VanChartDataSeries dataSeries){
        dataPoint.setColor(dataSeries.getColor());
    }

    //处理series的bands属性
    protected void dealDataSeriesBands(VanChartDataSeries dataSeries, ConditionCollection conditionCollection){
        ConditionAttr defaultAttr = conditionCollection.getDefaultAttr();
        dataSeries.setEvalValue(true);
        java.util.List<ConditionAttr> conditionAttrList = conditionCollection.getAllAttrByResult(dataSeries, null);
        dataSeries.setEvalValue(false);
        for(ConditionAttr conditionAttr : conditionAttrList){
            if(conditionAttr != null && !ComparatorUtils.equals(conditionAttr, defaultAttr)){
                AttrBackground background = (AttrBackground)conditionAttr.getExisted(AttrBackground.class);
                AttrAreaSeriesFillColorBackground fillColorBackground = (AttrAreaSeriesFillColorBackground)conditionAttr.getExisted(AttrAreaSeriesFillColorBackground.class);
                boolean hasColorBackground = background != null && background.getSeriesBackground() instanceof ColorBackground;
                if(hasColorBackground || fillColorBackground != null){
                    dealSingleBands(dataSeries, conditionAttr, background, fillColorBackground);
                }
            }
        }
    }

    private void dealSingleBands(VanChartDataSeries dataSeries, ConditionAttr conditionAttr, AttrBackground attrBackground, AttrAreaSeriesFillColorBackground fillColorBackground){
        AbstractCondition condition = conditionAttr.getCondition();
        MinMaxEval minMaxEval = new MinMaxEval();
        if(condition instanceof ListCondition){
            int count = ((ListCondition) condition).getJoinConditionCount();
            if (count <= 0) {
                return;//没有设置条件属性
            }
            JoinCondition joinCondition;
            for (int i = 0; i < count; i++) {
                joinCondition = ((ListCondition) condition).getJoinCondition(i);
                int join = joinCondition.getJoin();
                if(joinCondition.getCondition() instanceof CommonCondition){
                    if (i == 0 || join == DataConstants.AND) {//第一个条件、AND条件
                        dealCommonCondition(joinCondition.getCondition(), minMaxEval);
                    } else if(join == DataConstants.OR) {//或者条件的情况处理,当成一个单独的条件属性
                        MinMaxEval temp = new MinMaxEval();
                        dealCommonCondition(joinCondition.getCondition(), temp);
                        addBands2DataSeries(temp, fillColorBackground, attrBackground, dataSeries);
                    }
                }
            }
        } else if(condition instanceof CommonCondition){
           dealCommonCondition(condition, minMaxEval);
        }

        addBands2DataSeries(minMaxEval, fillColorBackground, attrBackground, dataSeries);
    }

    private void addBands2DataSeries(MinMaxEval minMaxEval, AttrAreaSeriesFillColorBackground fillColorBackground, AttrBackground attrBackground, VanChartDataSeries dataSeries) {
        if((minMaxEval.hasMaxEval || minMaxEval.hasMinEval)){
            if(minMaxEval.maxEval == minMaxEval.minEval){
                return;
            }
            AttrBand bands = new AttrBand(minMaxEval.hasMinEval, minMaxEval.hasMaxEval, minMaxEval.minEval, minMaxEval.maxEval);
            if(fillColorBackground != null){
                bands.setFillColorBackground(fillColorBackground);
            }
            if(attrBackground != null && attrBackground.getSeriesBackground() instanceof ColorBackground){
                Color color = ((ColorBackground) attrBackground.getSeriesBackground()).getColor();
                bands.setColor(color);
            }
            dataSeries.addAttrBands(bands);
        }
    }

    private void dealCommonCondition(Condition condition, MinMaxEval minMaxEval){
        CommonCondition commonCondition = (CommonCondition)condition;
        String columnName = commonCondition.getColumnName();
        if(ComparatorUtils.equals(columnName, ChartConstants.VALUE)
                || ArrayUtils.contains(ChartConstants.PROJECT_ID_KEYS, columnName)){
            Compare comparator = commonCondition.getCompare();
            Object value = comparator.getValue();
            if (value instanceof ParameterProvider) {
                value = ((ParameterProvider) value).getValue();
            }
            if (value instanceof FormulaProvider) {
                value = ((FormulaProvider) value).getResult();
            }
            Number number = Utils.objectToNumber(value, false);
            if(number == null){
                return;
            }
            if(comparator.getOp() == Compare.LESS_THAN || comparator.getOp() == Compare.LESS_THAN_OR_EQUAL){
                minMaxEval.minEval = number.doubleValue();
                minMaxEval.hasMinEval = true;
            } else if(comparator.getOp() == Compare.GREATER_THAN || comparator.getOp() == Compare.GREATER_THAN_OR_EQUAL){
                minMaxEval.maxEval = number.doubleValue();
                minMaxEval.hasMaxEval = true;
            }
        }
    }

    private class MinMaxEval{
        //只设置了大于条件的参照值，小于条件的参照值为数值最大值。
        //只设置了小于条件的参照值，大于条件的参照值为数值最小值。
        double minEval = Double.MAX_VALUE, maxEval = - Double.MAX_VALUE;//条件中的大于小于的参照值
        boolean hasMinEval = false, hasMaxEval = false;//是否有
        private MinMaxEval(){}

    }

    protected void dealDataPointCustomCondition(VanChartDataPoint dataPoint, ConditionCollection conditionCollection){
        super.dealDataPointCustomCondition(dataPoint, conditionCollection);

        VanChartAttrMarker attrMarker = (VanChartAttrMarker)conditionCollection.getCustomDataSeriesCondition(VanChartAttrMarker.class, dataPoint);
        ((VanChartLineDataPoint)dataPoint).setAttrMarker(attrMarker);
    }

    private static final Object[] LINE_SERIES_NAME = {
            Inter.getLocText("FR-Chart-Data_Series") + "1",
            Inter.getLocText("FR-Chart-Data_Series") + "2",
    };

    private static final Object[][] LINE_VALUE = {
            {"5", "40", "18", "14", "70", "85"},
            {"36", "75", "53", "57", "40", "45"}
    };

    /*
* 基本图表的默认横轴日期数据
*/
    public static final Object[] AXIS_DATE = {
            DateUtils.createDate(2001, 01, 03),
            DateUtils.createDate(2001, 01, 01),
            DateUtils.createDate(2001, 01, 07),
            DateUtils.createDate(2001, 01, 13),
            DateUtils.createDate(2001, 01, 05),
            DateUtils.createDate(2001, 01, 11),
    };


    public static final Object[] AXIS_VALUE = {
            "10",
            "20",
            "30",
            "35",
            "26",
            "55"
    };

    public static final Object[] AXIS_CATEGORY = ChartUtils.LONG_CATEGORY_STRING;

    /**
     * 返回柱形图默认的图表数据
     * @return  返回默认数据。
     */
    public ChartData defaultChartData() {
        VanChartAxis axis = getDefaultXAxis();
        if(ComparatorUtils.equals(axis.getAxisType(), AxisType.AXIS_CATEGORY)){
            return new NormalChartData(AXIS_CATEGORY, LINE_SERIES_NAME, LINE_VALUE);
        } else if(ComparatorUtils.equals(axis.getAxisType(), AxisType.AXIS_VALUE)) {
            return new NormalChartData(AXIS_VALUE, LINE_SERIES_NAME, LINE_VALUE);
        } else {
            return new NormalChartData(AXIS_DATE, LINE_SERIES_NAME, LINE_VALUE);
        }
    }


    /**
     * 返回Plot的类型名称,
     *
     * @return 类型名称.
     */
    public String getPlotName() {
        return Inter.getLocText("Plugin-ChartF_NewLine");
    }

    public String getPlotID(){
        return "VanChartLinePlot";
    }

    /**
     * 比较 切换类型界面中的 大致Plot类型
     *
     * @param newPlot 新plot
     * @return 两者是否相符
     */
    public boolean matchPlotType(Plot newPlot) {
        return newPlot instanceof VanChartLinePlot;
    }

    /**
     * 判断图表类型是否是obClass
     * @param obClass 传入对象
     * @return 是否是obClass对象
     */
    public boolean accept(Class<? extends Plot> obClass){
        return ComparatorUtils.equals(VanChartLinePlot.class, obClass);
    }

    public boolean equals(Object ob) {
        return ob instanceof VanChartLinePlot && super.equals(ob);
    }

    public FunctionProcessor getFunctionToRecord() {
        return ChartFunctionProcessor.LINE_VAN_CHARTS;
    }
}
