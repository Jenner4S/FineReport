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
    private boolean fixedWidth = false;//�Ƿ�̶����
    private int columnWidth = 0;//�̶���ȴ�С
    private boolean filledWithImage = false;//�Ƿ����ͼƬ

    private boolean isBar = false;

    //���������Ǵ���condition����ģ�ר���ó����Ǳ������ط�ʹ�ã�ѭ���������
    private AttrSeriesImageBackground defaultAttrSeriesImageBackground;

    public void setBar(boolean isBar) {
        this.isBar = isBar;
    }

    /**
     * �Ƿ�������ͼ
     * @return �Ƿ�
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
     * �Ƿ����ͼƬ
     * @return �����Ƿ����ͼƬ
     */
    public boolean isFilledWithImage() {
        return filledWithImage;
    }

    /**
     * �Ƿ�̶����
     * @return �����Ƿ�̶����
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
     * ����ͼ�ζ���
     * @param resolution �ֱ���
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
                //��ϵ�жѻ�����û������������
                //������ϵ�жѻ���������Ҳ��ʾ
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
            return;//���ڻ�ͼ����Χ
        }
        // �����������,��Ϊbar���ػ���������
        RoundRectangle2D bar = (RoundRectangle2D) shape;
        boolean flag = (value > 0) != hasReversed;
        if(isPercent) {//�ٷֱȵĻ�����ȡ����ġ�
            flag = true;
        }
        if(isBar()){
            if (flag) {//��
                xVal[lineDataCount] = bar.getX() + bar.getWidth();
                yVal[lineDataCount] = bar.getCenterY();
            } else {//��
                xVal[lineDataCount] = bar.getX();
                yVal[lineDataCount] = bar.getCenterY();
            }
        } else {
            if (flag) {//����
                xVal[lineDataCount] = bar.getCenterX();
                yVal[lineDataCount] = bar.getY();
            } else {//����
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

    //����һ�������ϵ���������
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
                Map<String, Number> prePositiveSumValueInSameCateValue = new HashMap<String, Number>();//����ͬcateValue�µ��Ѿ������������ӵ���ֵ��
                Map<String, Number> preNegativeSumValueInSameCateValue = new HashMap<String, Number>();//����ͬcateValue�µ��Ѿ����ĸ������ӵ���ֵ��
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

        double unitLength = xAxisGlyph.getUnitLen();//ÿ����������Ŀ��
        double cateGap = unitLength * categoryIntervalPercent/VanChartAttrHelper.PERCENT;
        double defaultBarWidth = (unitLength - cateGap)/barCount;//����+gap
        double realBarWidth = fixedWidth ? columnWidth : defaultBarWidth * (1 - seriesOverlapPercent/VanChartAttrHelper.PERCENT);//ÿ�����ӿ��

        for(int cateIndex = 0; cateIndex < categoryCount; cateIndex++){//ÿ������
            double cateStart = getCateStart(unitLength, xAxisGlyph, categoryCount, cateIndex);
            for(int barIndex = 0; barIndex < barCount; barIndex++){//һ�������е�ÿ������
                List<Number> allSeriesIndexInBar = axisMap.get(barIndex);//һ�������ϵ����е�
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

    //�÷��࿪ʼ����
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
        if (isBar()) {//x����ֵ�ᣬ���ڷ�Χ�ھͻ���ȥ��
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
        if(attrSeriesImageBackground != null){//����ͼƬ������ʾ
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
     * ���ݱ�ǩ��λ��Position�����ض�DataPoint�ı�ǩ�߽�,
     * @param position ��ǩλ��,  �������, �����ڲ�, ����
     * @param labelPreDim ��ǩԤ����Ĵ�С
     * @param dataPointRect ϵ�е�DataPoint��ͼ�α߽�
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
        //��Ʒȷ�ϣ�ֻ��ϵ�б����Ǵ�ɫ��ʱ��ż��ط��ͼƬ�������Ĳ���
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
     * ��ȡ plotOptions��JSON����
     * @param repo ����
     * @param isJsDraw ��̬չʾ
     * @return ����json
     * @throws com.fr.json.JSONException �׳�����
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

    //����ͼ���ͼƬ����Ϊ COLUMN_LEFT_BOTTOM COLUMN_LEFT_TOP
    //����ͼ���ͼƬ����Ϊ BAR_TOP_LEFT BAR_TOP_RIGHT
    private enum Direction {
        COLUMN_LEFT_BOTTOM,
        COLUMN_LEFT_TOP,
        BAR_TOP_LEFT,
        BAR_TOP_RIGHT
    }

    //���Ӷѻ�����Ϊ������������
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
                    return shape;//Ĭ�Ͼ������ֲ��֣����Բ������ı�
                case BAR_TOP_LEFT:
                    return shape;//Ĭ�Ͼ������ֲ��֣����Բ������ı�
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
