package com.fr.plugin.chart.column;

import com.fr.base.background.ColorBackground;
import com.fr.base.background.ImageBackground;
import com.fr.chart.base.AttrBorder;
import com.fr.chart.base.ChartConstants;
import com.fr.chart.chartglyph.*;
import com.fr.general.ComparatorUtils;
import com.fr.json.JSONException;
import com.fr.json.JSONObject;
import com.fr.plugin.chart.attr.VanChartAttrHelper;
import com.fr.plugin.chart.attr.axis.AxisType;
import com.fr.plugin.chart.base.AttrSeriesImageBackground;
import com.fr.plugin.chart.base.AttrSeriesStackAndAxis;
import com.fr.plugin.chart.base.VanChartAttrTrendLine;
import com.fr.plugin.chart.glyph.VanChartDataSeries;
import com.fr.plugin.chart.glyph.VanChartRectanglePlotGlyph;
import com.fr.plugin.chart.glyph.axis.VanChartBaseAxisGlyph;
import com.fr.plugin.chart.glyph.axis.VanChartCategoryAxisGlyph;
import com.fr.stable.Constants;
import com.fr.stable.web.Repository;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Mitisky on 15/9/28.
 */
public class VanChartColumnPlotGlyph extends VanChartRectanglePlotGlyph {
    private static final long serialVersionUID = -1007991635958523854L;
    private static final double	LABEL_BAR_GAP = 6;
    private static final double LABEL_COLUMN_GAP = 3;

    private double seriesOverlapPercent = 20;
    private double categoryIntervalPercent = 20;
    private boolean fixedWidth = false;//是否固定宽度
    private int columnWidth = 0;//固定宽度大小
    private boolean filledWithImage = false;//是否填充图片

    private boolean isBar = false;

    //以下属性是存在condition里面的，专门拿出来是避免多个地方使用，循环遍历多次
    private AttrSeriesImageBackground defaultAttrSeriesImageBackground;

    public void setBar(boolean isBar) {
        this.isBar = isBar;
    }

    /**
     * 是否是条形图
     * @return 是否
     */
    public boolean isBar() {
        return isBar;
    }

    public void setSeriesOverlapPercent(double overlapPercent) {
        this.seriesOverlapPercent = overlapPercent;
    }

    public double getSeriesOverlapPercent() {
        return this.seriesOverlapPercent;
    }

    public void setCategoryIntervalPercent(double catePercent) {
        this.categoryIntervalPercent = catePercent;
    }

    public double getCategoryIntervalPercent() {
        return this.categoryIntervalPercent;
    }

    public void setColumnWidth(int columnWidth) {
        this.columnWidth = columnWidth;
    }

    public void setFilledWithImage(boolean filledWithImage) {
        this.filledWithImage = filledWithImage;
    }

    public void setFixedWidth(boolean fixedWidth) {
        this.fixedWidth = fixedWidth;
    }

    /**
     * 是否填充图片
     * @return 返回是否填充图片
     */
    public boolean isFilledWithImage() {
        return filledWithImage;
    }

    /**
     * 是否固定宽度
     * @return 返回是否固定宽度
     */
    public boolean isFixedWidth() {
        return fixedWidth;
    }

    private AttrSeriesImageBackground getDefaultAttrSeriesImageBackground() {
        if(defaultAttrSeriesImageBackground == null){
            ConditionAttr conditionAttr = getConditionCollection().getDefaultAttr();
            defaultAttrSeriesImageBackground = (AttrSeriesImageBackground)conditionAttr.getExisted(AttrSeriesImageBackground.class);
        }
        return defaultAttrSeriesImageBackground;
    }

    private AttrSeriesImageBackground getAttrSeriesImageBackground(VanChartColumnDataPoint dataPoint) {
        AttrSeriesImageBackground imageBackground = dataPoint.getImageBackground();
        if(imageBackground == null) {
            imageBackground = getDefaultAttrSeriesImageBackground();
        }
        return imageBackground;
    }

    protected void initXAxisLabelDrawPosition(VanChartBaseAxisGlyph axisGlyph) {
        if(!isBar()){
            setAxisLabelDrawBetween(axisGlyph);
        }
    }

    protected void initYAxisLabelDrawPosition(VanChartBaseAxisGlyph axisGlyph) {
        if(isBar()){
            setAxisLabelDrawBetween(axisGlyph);
        }
    }

    protected void initXAxisGlyphMinMaxValue(int axisIndex, VanChartBaseAxisGlyph axisGlyph) {
        if(isBar()){
            super.initYAxisGlyphMinMaxValue(axisIndex, axisGlyph);
        } else {
            super.initXAxisGlyphMinMaxValue(axisIndex, axisGlyph);
        }
    }

    protected void initYAxisGlyphMinMaxValue(int axisIndex, VanChartBaseAxisGlyph axisGlyph) {
        if(isBar()){
            super.initXAxisGlyphMinMaxValue(axisIndex, axisGlyph);
        } else {
            super.initYAxisGlyphMinMaxValue(axisIndex, axisGlyph);
        }

    }

    protected int getValueAxisIndex(AttrSeriesStackAndAxis attrSeriesStackAndAxis){
        return attrSeriesStackAndAxis == null ? 0 : (isBar ? attrSeriesStackAndAxis.getXAxisIndex() : attrSeriesStackAndAxis.getYAxisIndex());
    }

    protected int getCateAxisIndex(AttrSeriesStackAndAxis attrSeriesStackAndAxis){
        return attrSeriesStackAndAxis == null ? 0 : (isBar ? attrSeriesStackAndAxis.getYAxisIndex() : attrSeriesStackAndAxis.getXAxisIndex());
    }

    protected VanChartBaseAxisGlyph getCateAxis(int axisIndex){
        return isBar ? yAxisGlyphList.get(axisIndex) : xAxisGlyphList.get(axisIndex);
    }

    protected boolean isAdjustXAxisMinMaxValue(){
        return true;
    }

    /**
     * 布局图形对象
     * @param resolution 分辨率
     */
    public void layoutDataSeriesGlyph(int resolution) {
        super.layoutDataSeriesGlyph(resolution);
        Map<String,  List<List<Number>>> axis2SeriesMap = buildAxisMap(false);
        for(String axisName : axis2SeriesMap.keySet()){
            buildSingleAxisBars(axis2SeriesMap.get(axisName), resolution);
        }
        dealTrendLine();
    }

    private void dealTrendLine(){
        for(int seriesIndex = 0, seriesLen = getSeriesSize(); seriesIndex < seriesLen; seriesIndex++){
            VanChartDataSeries dataSeries = (VanChartDataSeries)getSeries(seriesIndex);
            VanChartAttrTrendLine attrTrendLine = getAttrTrendLine(dataSeries);
            if(attrTrendLine == null){
                //该系列堆积或者没有设置趋势线
                //改正：系列堆积的趋势线也显示
                continue;
            }
            int lineDataCount = calculateLineDataCount(dataSeries);
            double[] xVal = new double[lineDataCount];
            double[] yVal = new double[lineDataCount];

            lineDataCount = 0;
            VanChartBaseAxisGlyph valueAxisGlyph = getDataSeriesValueAxisGlyph(dataSeries);
            boolean hasAxisReversed = valueAxisGlyph.hasAxisReversed();
            boolean isPercent = valueAxisGlyph.isPercentage();
            for(int dataPointIndex = 0, dataCount = dataSeries.getDataPointCount(); dataPointIndex < dataCount; dataPointIndex++){
                DataPoint dataPoint = dataSeries.getDataPoint(dataPointIndex);
                if(dataPoint.isValueIsNull()){
                    continue;
                }
                if(dataPoint.getDrawImpl() == null){
                    continue;
                }
                calculateFittingData(isPercent, hasAxisReversed, dataPoint.getValue(), xVal, yVal, lineDataCount, dataPoint.getDrawImpl().getShape());
                lineDataCount++;
            }
            trendLineFitting(xVal, yVal, dataSeries);
        }
    }

    private void calculateFittingData(boolean isPercent, boolean hasReversed, double value, double[] xVal, double[] yVal, int lineDataCount, Shape shape) {
        if(shape == null){
            return;//不在绘图区范围
        }
        // 计算拟合数据,即为bar上沿或下沿中心
        RoundRectangle2D bar = (RoundRectangle2D) shape;
        boolean flag = (value > 0) != hasReversed;
        if(isPercent) {//百分比的话都是取正向的。
            flag = true;
        }
        if(isBar()){
            if (flag) {//右
                xVal[lineDataCount] = bar.getX() + bar.getWidth();
                yVal[lineDataCount] = bar.getCenterY();
            } else {//左
                xVal[lineDataCount] = bar.getX();
                yVal[lineDataCount] = bar.getCenterY();
            }
        } else {
            if (flag) {//上沿
                xVal[lineDataCount] = bar.getCenterX();
                yVal[lineDataCount] = bar.getY();
            } else {//下沿
                xVal[lineDataCount] = bar.getCenterX();
                yVal[lineDataCount] = bar.getY() + bar.getHeight();
            }
        }
    }

    protected void initTrendLineGlyph(TrendLineGlyph newTrendLine, double[] xVal, double[] yVal) {
        if(isBar()){
            int length = xVal.length - 1;
            LinearEquation linearEquation = (LinearEquation)newTrendLine.getEquation();
            newTrendLine.fitting(yVal, xVal);
            double x1 = linearEquation.execute(yVal[0]);
            double x2 = linearEquation.execute(yVal[length]);
            GeneralPath path = new GeneralPath();
            path.moveTo(x1, yVal[0]);
            path.lineTo(x2, yVal[length]);
            path.closePath();
            newTrendLine.setGeneralPath(path);
        } else {
            super.initTrendLineGlyph(newTrendLine, xVal, yVal);
        }
    }

    protected VanChartBaseAxisGlyph getDataSeriesValueAxisGlyph(VanChartDataSeries dataSeries){
        return isBar() ? xAxisGlyphList.get(getXAxisGlyphIndex(dataSeries.getAttrSeriesStackAndAxis()))
                : yAxisGlyphList.get(getYAxisGlyphIndex(dataSeries.getAttrSeriesStackAndAxis()));
    }

    protected VanChartBaseAxisGlyph getDataSeriesCateAxisGlyph(VanChartDataSeries dataSeries){
        return isBar() ? yAxisGlyphList.get(getYAxisGlyphIndex(dataSeries.getAttrSeriesStackAndAxis()))
                : xAxisGlyphList.get(getXAxisGlyphIndex(dataSeries.getAttrSeriesStackAndAxis()));
    }

    //处理一个横轴上的所有柱子
    private void buildSingleAxisBars(List<List<Number>> axisMap, int resolution){

        int seriesIndex = axisMap.get(0).get(0).intValue();
        VanChartDataSeries dataSeries = (VanChartDataSeries)getSeries(seriesIndex);
        VanChartBaseAxisGlyph categoryAxisGlyph = getDataSeriesCateAxisGlyph(dataSeries);

        if( ComparatorUtils.equals(categoryAxisGlyph.getVanAxisType(), AxisType.AXIS_CATEGORY)){
            buildNormalBars(axisMap, (VanChartCategoryAxisGlyph)categoryAxisGlyph, resolution);
        } else {
            buildDoubleValueBars(axisMap, categoryAxisGlyph, resolution);
        }
    }


    private void buildDoubleValueBars(List<List<Number>> axisMap, VanChartBaseAxisGlyph xAxisGlyph, int resolution){
        double barCount = getAllPointsCount();
        if(barCount > 0){
            double barWidth = fixedWidth ? columnWidth : xAxisGlyph.getAxisLength()/barCount/3.0;
            double XCrossValue = xAxisGlyph.getCrossValueInPlot();

            for(List<Number> allSeriesIndexInBar : axisMap){
                Map<String, Number> prePositiveSumValueInSameCateValue = new HashMap<String, Number>();//存相同cateValue下的已经画的正向柱子的数值和
                Map<String, Number> preNegativeSumValueInSameCateValue = new HashMap<String, Number>();//存相同cateValue下的已经画的负向柱子的数值和
                for(Number seriesIndex : allSeriesIndexInBar){
                    VanChartDataSeries dataSeries = (VanChartDataSeries)getSeries(seriesIndex.intValue());
                    VanChartBaseAxisGlyph valueAxisGlyph = getDataSeriesValueAxisGlyph(dataSeries);
                    double crossValue = valueAxisGlyph.getCrossValueInPlot();

                    for(int pointIndex = 0, len = dataSeries.getDataPointCount(); pointIndex < len; pointIndex++){
                        VanChartColumnDataPoint dataPoint = (VanChartColumnDataPoint)dataSeries.getDataPoint(pointIndex);
                        double cateValue = getCateValue(xAxisGlyph, dataPoint, true);
                        double barStart = getBarXValueStart(cateValue, XCrossValue, xAxisGlyph) - barWidth/2.0;
                        double realValue = valueAxisGlyph.isPercentage() ? dataPoint.getPercentValue() : dataPoint.getValue();
                        double sumValue;
                        Number preSumValue;
                        if(realValue > 0){
                            preSumValue = prePositiveSumValueInSameCateValue.get(String.valueOf(cateValue));
                            sumValue = realValue + (preSumValue == null ? 0 : preSumValue.doubleValue());
                            prePositiveSumValueInSameCateValue.put(String.valueOf(cateValue), sumValue);
                        } else {
                            preSumValue = preNegativeSumValueInSameCateValue.get(String.valueOf(cateValue));
                            sumValue = realValue + (preSumValue == null ? 0 : preSumValue.doubleValue());
                            preNegativeSumValueInSameCateValue.put(String.valueOf(cateValue), sumValue);
                        }
                        double valueLength = valueAxisGlyph.get2ValueLength(realValue, crossValue);
                        double valueStart = getBarValueStart(sumValue, preSumValue == null ? crossValue : preSumValue.doubleValue(), valueAxisGlyph);
                        AttrBorder attrBorder = getAttrBorder(dataPoint);
                        buildDataPointDrawImpl(valueAxisGlyph, dataPoint, getBarShape(barStart, barWidth, valueStart, valueLength, attrBorder.getRoundRadius()), resolution);
                    }
                }
            }

        }
    }

    private int getAllPointsCount() {
        if(getSeriesSize() > 0){
            DataSeries dataSeries = getSeries(0);
            if(dataSeries.getDataPointCount() > 0){
                return getSeriesSize() * dataSeries.getDataPointCount();
            }
        }
        return 0;
    }

    private void buildNormalBars(List<List<Number>> axisMap, VanChartCategoryAxisGlyph xAxisGlyph, int resolution){
        int categoryCount = xAxisGlyph.getCategoryCount();
        int barCount = axisMap.size();

        double unitLength = xAxisGlyph.getUnitLen();//每个分类区间的宽度
        double cateGap = unitLength * categoryIntervalPercent/VanChartAttrHelper.PERCENT;
        double defaultBarWidth = (unitLength - cateGap)/barCount;//柱子+gap
        double realBarWidth = fixedWidth ? columnWidth : defaultBarWidth * (1 - seriesOverlapPercent/VanChartAttrHelper.PERCENT);//每个柱子宽度

        for(int cateIndex = 0; cateIndex < categoryCount; cateIndex++){//每个分类
            double cateStart = getCateStart(unitLength, xAxisGlyph, categoryCount, cateIndex);
            for(int barIndex = 0; barIndex < barCount; barIndex++){//一个分类中的每个柱子
                List<Number> allSeriesIndexInBar = axisMap.get(barIndex);//一根柱子上的所有点
                double barCenterPos = (cateStart + cateGap/2.0 + barIndex * defaultBarWidth + defaultBarWidth/2.0);
                double barStart = (barCenterPos - realBarWidth/2.0);

                VanChartDataSeries dataSeries = (VanChartDataSeries)getSeries(allSeriesIndexInBar.get(0).intValue());
                VanChartBaseAxisGlyph valueAxisGlyph = getDataSeriesValueAxisGlyph(dataSeries);
                double crossValue = valueAxisGlyph.getCrossValueInPlot();

                if(allSeriesIndexInBar.size() == 1){
                    VanChartColumnDataPoint dataPoint = (VanChartColumnDataPoint)dataSeries.getDataPoint(cateIndex);
                    double realValue = valueAxisGlyph.isPercentage() ? dataPoint.getPercentValue() : dataPoint.getValue();
                    double valueLength = valueAxisGlyph.get2ValueLength(realValue, crossValue);
                    double valueStart = getBarValueStart(realValue, crossValue, valueAxisGlyph);
                    AttrBorder attrBorder = getAttrBorder(dataPoint);
                    buildDataPointDrawImpl(valueAxisGlyph, dataPoint, getBarShape(barStart, realBarWidth, valueStart, valueLength, attrBorder.getRoundRadius()), resolution);
                } else {
                    double pre_positiveSum= 0, pre_negativeSum = 0;
                    for(Number seriesIndex : allSeriesIndexInBar){
                        VanChartColumnDataPoint dataPoint = (VanChartColumnDataPoint)getSeries(seriesIndex.intValue()).getDataPoint(cateIndex);
                        double realValue = valueAxisGlyph.isPercentage() ? dataPoint.getPercentValue() : dataPoint.getValue();
                        double valueStart, valueLength = 0;
                        if(realValue > 0){
                            valueLength = valueAxisGlyph.get2ValueLength(realValue + pre_positiveSum, pre_positiveSum);
                            valueStart = getBarValueStart(realValue + pre_positiveSum, pre_positiveSum, valueAxisGlyph);
                            pre_positiveSum += realValue;
                        } else {
                            valueLength = valueAxisGlyph.get2ValueLength(realValue + pre_negativeSum, pre_negativeSum);
                            valueStart = getBarValueStart(realValue + pre_negativeSum, pre_negativeSum, valueAxisGlyph);
                            pre_negativeSum += realValue;
                        }
                        AttrBorder attrBorder = getAttrBorder(dataPoint);
                        buildDataPointDrawImpl(valueAxisGlyph, dataPoint, getBarShape(barStart, realBarWidth, valueStart, valueLength, attrBorder.getRoundRadius()), resolution);
                    }
                }
            }
        }
    }

    //该分类开始坐标
    private double getCateStart(double unitLength, VanChartBaseAxisGlyph xAxisGlyph, int categoryCount, int cateIndex) {
        if(isBar()){
            if(xAxisGlyph.hasAxisReversed()){
                return unitLength * cateIndex;
            } else {
                return unitLength * (categoryCount - cateIndex - 1);
            }
        } else {
            if(xAxisGlyph.hasAxisReversed()){
                return unitLength * (categoryCount - cateIndex - 1);
            } else {
                return unitLength * cateIndex;
            }
        }
    }

    private double getBarValueStart(double value, double crossValue, VanChartBaseAxisGlyph valueAxisGlyph) {
        if (value > crossValue) {
            if (isBar()) {
                if(valueAxisGlyph.hasAxisReversed()){
                    return valueAxisGlyph.getPointInBounds(value).getX();
                }else{
                    return valueAxisGlyph.getPointInBounds(crossValue).getX();
                }
            } else {
                if(valueAxisGlyph.hasAxisReversed()){
                    return valueAxisGlyph.getPointInBounds(crossValue).getY();
                }else{
                    return valueAxisGlyph.getPointInBounds(value).getY();
                }
            }
        } else {
            if (isBar()) {
                if(valueAxisGlyph.hasAxisReversed()){
                    return valueAxisGlyph.getPointInBounds(crossValue).getX();
                }else{
                    return valueAxisGlyph.getPointInBounds(value).getX();
                }
            } else {
                if(valueAxisGlyph.hasAxisReversed()){
                    return valueAxisGlyph.getPointInBounds(value).getY();
                }else{
                    return valueAxisGlyph.getPointInBounds(crossValue).getY();
                }
            }
        }
    }

    private double getBarXValueStart(double value, double crossValue, VanChartBaseAxisGlyph valueAxisGlyph) {
        if (isBar()) {//x轴是值轴，不在范围内就画出去。
            return valueAxisGlyph.getPoint2D(value).getY();
        } else {
            return valueAxisGlyph.getPoint2D(value).getX();
        }
    }

    private void buildDataPointDrawImpl(VanChartBaseAxisGlyph valueAxisGlyph, VanChartColumnDataPoint dataPoint, RectangularShape shape, int resolution){
        if (dataPoint.isValueIsNull()) {
            return;
        }
        ShapeGlyph shapeGlyph;

        AttrSeriesImageBackground attrSeriesImageBackground = getAttrSeriesImageBackground(dataPoint);
        if(attrSeriesImageBackground != null){
            shapeGlyph = new ColumnImageShapeGlyph(getBarImageDirection(dataPoint, valueAxisGlyph));
        } else {
            shapeGlyph = new ShapeGlyph();
        }

        dataPoint.setDrawImpl(shapeGlyph);

        if(this.isOutPlotBounds(shape)) {
            shapeGlyph.setShape(null);
            dataPoint.setDataLabel(null);
            return;
        }

        shapeGlyph.setShape(shape);

        changeInfoWithCondition(shapeGlyph.getGeneralInfo(), dataPoint);
        if(attrSeriesImageBackground != null){//填充的图片优先显示
            shapeGlyph.getGeneralInfo().changeStyleAttrBackground(attrSeriesImageBackground);
        }

        addChartStyle4DataPoint(dataPoint, shapeGlyph, shape);
        dealDataPointLabel(dataPoint, resolution);
    }

    private Direction getBarImageDirection(DataPoint dataPoint, VanChartBaseAxisGlyph valueAxisGlyph) {
        if (dataPoint.getValue() > valueAxisGlyph.getCrossValue()) {
            if (isBar()) {
                if(valueAxisGlyph.hasAxisReversed()){
                    return Direction.BAR_TOP_RIGHT;
                }else{
                    return Direction.BAR_TOP_LEFT;
                }
            } else {
                if(valueAxisGlyph.hasAxisReversed()){
                    return Direction.COLUMN_LEFT_TOP;
                }else{
                    return Direction.COLUMN_LEFT_BOTTOM;
                }
            }
        } else {
            if (isBar()) {
                if(valueAxisGlyph.hasAxisReversed()){
                    return Direction.BAR_TOP_LEFT;
                }else{
                    return Direction.BAR_TOP_RIGHT;
                }
            } else {
                if(valueAxisGlyph.hasAxisReversed()){
                    return Direction.COLUMN_LEFT_BOTTOM;
                }else{
                    return Direction.COLUMN_LEFT_TOP;
                }
            }
        }

    }

    /**
     * 根据标签的位置Position返回特定DataPoint的标签边界,
     * @param position 标签位置,  柱形外侧, 柱形内侧, 居中
     * @param labelPreDim 标签预计算的大小
     * @param dataPointRect 系列点DataPoint的图形边界
     */
    protected Rectangle2D getDataPointLabelBoundsWithPosition(Dimension2D labelPreDim, Rectangle2D dataPointRect, int position, DataPoint dataPoint){
        VanChartDataSeries dataSeries = (VanChartDataSeries)getSeries(dataPoint.getSeriesIndex());
        VanChartBaseAxisGlyph axisGlyph = getDataSeriesValueAxisGlyph(dataSeries);
        if (this.isBar()) {
            return getLabelBoundsInHorizontal(labelPreDim, dataPointRect, position, axisGlyph);
        } else {
            return getLabelBoundsInVertical(labelPreDim, dataPointRect, position, axisGlyph);
        }
    }

    private Rectangle2D getLabelBoundsInVertical(Dimension2D dim, Rectangle2D rect, int position, VanChartBaseAxisGlyph axisGlyph) {
        double labelY;
        double y = axisGlyph.getPoint2D(0).getY();
        boolean axisReversed = axisGlyph.hasAxisReversed();
        boolean isNegative = axisReversed ? rect.getMinY() < y : rect.getMaxY() > y;
        isNegative = isNegative ^ axisReversed;
        if(isNegative){
            switch(position) {
                case Constants.INSIDE : {
                    labelY = rect.getY() + rect.getHeight() - LABEL_COLUMN_GAP - dim.getHeight(); break;
                }
                case Constants.OUTSIDE : {
                    labelY = rect.getY() + rect.getHeight() + LABEL_COLUMN_GAP; break;
                }
                default : {
                    labelY =  rect.getY() + (rect.getHeight() - dim.getHeight()) / 2.0; break;
                }
            }
        }else{
            switch(position) {
                case Constants.INSIDE : {
                    labelY = rect.getY() + LABEL_COLUMN_GAP; break;
                }
                case Constants.OUTSIDE : {
                    labelY = rect.getY() - dim.getHeight() - LABEL_COLUMN_GAP; break;
                }
                default : {
                    labelY =  rect.getY() + (rect.getHeight() - dim.getHeight()) / 2.0; break;
                }
            }
        }
        return new Rectangle2D.Double(rect.getX() + (rect.getWidth() - dim.getWidth()) / 2.0,
                labelY, dim.getWidth(), dim.getHeight());
    }

    private Rectangle2D getLabelBoundsInHorizontal(Dimension2D dim, Rectangle2D rect, int position, VanChartBaseAxisGlyph axisGlyph) {
        double labelX;
        double x = axisGlyph.getPoint2D(0).getX();
        boolean axisReversed = axisGlyph.hasAxisReversed();
        boolean isNegative = axisReversed ? rect.getMaxX() > x : rect.getMinX() < x;
        isNegative = isNegative ^ axisReversed;
        if(isNegative){
            switch(position) {
                case Constants.INSIDE : {
                    labelX = rect.getX() + LABEL_BAR_GAP; break;
                }
                case Constants.OUTSIDE : {
                    labelX = rect.getX() - dim.getWidth() - LABEL_BAR_GAP; break;
                }
                default  : {
                    labelX = rect.getX() + (rect.getWidth() - dim.getWidth())/2 ; break;
                }
            }
        }else{
            switch(position) {
                case Constants.INSIDE : {
                    labelX = rect.getX() + rect.getWidth() - dim.getWidth() - LABEL_BAR_GAP; break;
                }
                case Constants.OUTSIDE : {
                    labelX = rect.getX() + rect.getWidth() + LABEL_BAR_GAP; break;
                }
                default  : {
                    labelX = rect.getX() + (rect.getWidth() - dim.getWidth())/2 ; break;
                }
            }
        }
        return new Rectangle2D.Double(labelX, rect.getY() + (rect.getHeight() - dim.getHeight())/2,
                dim.getWidth(), dim.getHeight());
    }

    private RectangularShape getBarShape(double cateStart, double cateLength, double valueStart, double valueLength, double roundRadius) {
        if (isBar()) {
            return new RoundRectangle2D.Double(valueStart, cateStart, valueLength, cateLength, roundRadius, roundRadius);
        } else {
            return new RoundRectangle2D.Double(cateStart, valueStart, cateLength, valueLength, roundRadius, roundRadius);
        }
    }

    private boolean isOutPlotBounds(RectangularShape pointBounds) {
        Rectangle2D plotBounds = this.getBounds();

        double minX = pointBounds.getX();
        double maxX = pointBounds.getMaxX();

        double minY = pointBounds.getY();
        double maxY = pointBounds.getMaxY();

        if(isBar()){
            if (maxY < 0 || minY > plotBounds.getHeight() + 1) {
                return true;
            }
        } else {
            if (maxX < 0 || minX > plotBounds.getWidth() + 1) {
                return true;
            }
        }
        return false;
    }

    private void addChartStyle4DataPoint(VanChartColumnDataPoint dataPoint, ShapeGlyph shapeGlyph, RectangularShape shape) {
        //产品确认，只有系列背景是纯色的时候才加载风格，图片做背景的不管
        int plotStyle = this.plotStyle;
        if(shapeGlyph.getBackground() instanceof ColorBackground) {
            Color seriesColor = (((ColorBackground) shapeGlyph.getBackground())).getColor();
            ChartStyle dataPointStyle = null;
            VanChartBaseAxisGlyph axisGlyph = getDataSeriesValueAxisGlyph((VanChartDataSeries)getSeries(dataPoint.getSeriesIndex()));
            boolean axisReversed = axisGlyph.hasAxisReversed();

            if(plotStyle == ChartConstants.STYLE_SHADE){
                dataPointStyle = new ColumnTopDownShadeStyle(seriesColor, shape, this.isBar, true, axisReversed);
            }

            if(dataPointStyle!=null && getAttrAlpha(dataPoint) != null){
                dataPointStyle.setAlpha(shapeGlyph.getAlpha());
            }

            if(dataPointStyle !=null && getAttrBorder(dataPoint) != null){
                dataPointStyle.setBorderWidth(VanChartAttrHelper.getAxisLineStyle(shapeGlyph.getBorderStyle()));
                dataPointStyle.setBorderColor(shapeGlyph.getBorderColor() == null ? new Color(0,0,0,0) : shapeGlyph.getBorderColor());
            }

            if(dataPointStyle != null){
                dataPoint.setDataPointStyle(dataPointStyle);
            }
        }
    }

    /**
     * 获取 plotOptions的JSON对象
     * @param repo 请求
     * @param isJsDraw 动态展示
     * @return 返回json
     * @throws com.fr.json.JSONException 抛出问题
     */
    public JSONObject getPlotOptionsJSON(Repository repo, boolean isJsDraw) throws JSONException {
        JSONObject js = super.getPlotOptionsJSON(repo, isJsDraw);

        AttrSeriesImageBackground attrSeriesImageBackground = getDefaultAttrSeriesImageBackground();
        if(isFilledWithImage() && attrSeriesImageBackground != null){
            VanChartAttrHelper.addImageBackgroundAndWidthAndHeight(js, attrSeriesImageBackground, repo);
        }

        js.put("gap", getSeriesOverlapPercent() + VanChartAttrHelper.STRING_PERCENT);
        js.put("categoryGap", getCategoryIntervalPercent() + VanChartAttrHelper.STRING_PERCENT);

        if(isFixedWidth()){
            js.put("width", columnWidth);
        }

        return js;
    }

    public String getChartType(){
        return isBar ? "bar" : "column";
    }

    //柱形图填充图片可能为 COLUMN_LEFT_BOTTOM COLUMN_LEFT_TOP
    //条形图填充图片可能为 BAR_TOP_LEFT BAR_TOP_RIGHT
    private enum Direction {
        COLUMN_LEFT_BOTTOM,
        COLUMN_LEFT_TOP,
        BAR_TOP_LEFT,
        BAR_TOP_RIGHT
    }

    //柱子堆积方向为柱子生长方向
    private class ColumnImageShapeGlyph extends ShapeGlyph {
        private Direction direction;
        private ColumnImageShapeGlyph(Direction direction){
            this.direction = direction;
        }

        protected void paintBackground(Graphics2D g2d, Shape shape) {
            if (getBackground() != null && getBackground() instanceof ImageBackground) {
                Shape clipShape = g2d.getClip();
                g2d.setClip(shape);
                getBackground().paint(g2d, getPaintBounds(shape));
                g2d.setClip(clipShape);
            }
        }

        private Shape getPaintBounds(Shape shape) {
            ImageBackground imageBackground = (ImageBackground)getBackground();
            Rectangle2D bounds = shape.getBounds2D();
            switch (this.direction){
                case COLUMN_LEFT_BOTTOM:
                    int singleImageHeight = imageBackground.getImage().getHeight(new JPanel());
                    double newHeight = Math.ceil(bounds.getHeight()/singleImageHeight) * singleImageHeight;
                    return new Rectangle2D.Double(bounds.getX(), bounds.getY() - (newHeight - bounds.getHeight()), bounds.getWidth(), newHeight);
                case COLUMN_LEFT_TOP:
                    return shape;//默认就是这种布局，所以不用做改变
                case BAR_TOP_LEFT:
                    return shape;//默认就是这种布局，所以不用做改变
                case BAR_TOP_RIGHT:
                    int singleImageWidth = imageBackground.getImage().getWidth(new JPanel());
                    double newWidth = Math.ceil(bounds.getWidth()/singleImageWidth) * singleImageWidth;
                    return new Rectangle2D.Double(bounds.getX() - (newWidth - bounds.getWidth()), bounds.getY(), newWidth, bounds.getHeight());
                default:
                    return shape;
            }
        }
    }
}
