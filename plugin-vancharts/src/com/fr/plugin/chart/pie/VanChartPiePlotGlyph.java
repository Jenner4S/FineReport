package com.fr.plugin.chart.pie;

import com.fr.base.background.ColorBackground;
import com.fr.chart.base.*;
import com.fr.chart.chartglyph.*;
import com.fr.data.condition.Result;
import com.fr.general.ComparatorUtils;
import com.fr.json.JSONException;
import com.fr.json.JSONObject;
import com.fr.plugin.chart.attr.VanChartAttrHelper;
import com.fr.plugin.chart.base.AttrLabel;
import com.fr.plugin.chart.base.ChartRoseType;
import com.fr.plugin.chart.glyph.VanChartDataSeries;
import com.fr.plugin.chart.glyph.VanChartPieLegendItemIcon;
import com.fr.plugin.chart.glyph.VanChartPlotGlyph;
import com.fr.plugin.chart.glyph.VanChartPlotGlyphInterface;
import com.fr.stable.Constants;
import com.fr.stable.StringUtils;
import com.fr.stable.web.Repository;

import java.awt.*;
import java.awt.geom.*;
import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by Mitisky on 15/8/18.
 */
public class VanChartPiePlotGlyph extends VanChartPlotGlyph implements VanChartPlotGlyphInterface{

    private static final long serialVersionUID = 895972529922603488L;
    private static final int ANGLE = 360;
    //以下为计算中用到的临时变量
    protected transient double radius = 0;

    private transient ArrayList leftUp = new ArrayList();
    private transient ArrayList rightUp = new ArrayList();
    private transient ArrayList leftDown = new ArrayList();
    private transient ArrayList rightDown = new ArrayList();

    protected double radiusPercent = BEST_RAD;

    public static final double START_ANGLE = 90;

    protected static final double FIRST_QUA = 90;
    protected static final double SECOND_QUA = 180;
    protected static final double THRID_QUA = 270;
    private static final double FOUTH_QUA = 360;

    private static final double BEST_RAD = 0.9;
    protected static final double WHEN_MORE_RAD = 0.8;
    protected static final double MOVE_GAP = 0.1;

    protected static final double RADIO_PERCENT = 1.2;
    private static final double RADIUS_PERCENT_DIFF = 0.05;
    private static final double RADIUS_MIN_PERCENR = 0.5;

    private ChartRoseType roseType;

    private double innerRadiusPercent;

    private double startAngle;

    private double endAngle;

    private boolean supportRotation;

    protected boolean useDefaultNullData;

    public void setEndAngle(double endAngle) {
        this.endAngle = endAngle;
    }

    public void setInnerRadiusPercent(double innerRadiusPercent) {
        this.innerRadiusPercent = innerRadiusPercent;
    }

    public void setRoseType(ChartRoseType roseType) {
        this.roseType = roseType;
    }

    public void setStartAngle(double startAngle) {
        this.startAngle = startAngle;
    }

    public ChartRoseType getRoseType() {
        return roseType;
    }

    public double getStartAngle() {
        return startAngle;
    }

    public double getInnerRadiusPercent() {
        return innerRadiusPercent;
    }

    public double getEndAngle() {
        return endAngle;
    }

    public void setSupportRotation(boolean supportRotation) {
        this.supportRotation = supportRotation;
    }

    public void setUseDefaultNullData(boolean useDefaultNullData) {
        this.useDefaultNullData = useDefaultNullData;
    }
    /**
     * 返回是否支持旋转
     * @return 返回是否支持旋转
     */
    public boolean isSupportRotation() {
        return supportRotation;
    }

    protected LineMarkerIcon getLegendMarkerIcon(VanChartDataSeries dataSeries, Color[] colors) {
        VanChartPieLegendItemIcon itemIcon = new VanChartPieLegendItemIcon(innerRadiusPercent);

        this.dealMarkerIconCondition(itemIcon, dataSeries, colors);

        return itemIcon;
    }

    /**
     * 一个饼图 是一个分类. 每个扇形代表一个系列.
     *
     * @param resolution 分辨率
     */
    public void layoutDataSeriesGlyph(int resolution) {
        super.layoutDataSeriesGlyph(resolution);
        for (int categoryIndex = 0; categoryIndex < this.getCategoryCount(); categoryIndex++) {
            deal4EachCategory(categoryIndex, resolution);
        }
        //不对标签进行超出、覆盖处理。
    }

    private void deal4EachCategory(int categoryIndex, int resolution) {
        initLabelData(categoryIndex);
        dealWithDataSeries(categoryIndex, resolution);
    }

    private void initLabelData(int categoryIndex) {
        radius = getPieDimension(categoryIndex) / 2;
        getSeriesCountWithQuadrant(categoryIndex);
    }

    // 计算直径
    private double getPieDimension(int categoryIndex) {
        Rectangle2D plotBounds = this.getBounds();
        // 直径
        double eachWidth = plotBounds.getWidth() / this.getCategoryCount();

        double resultD = 0;
        if (eachWidth > plotBounds.getHeight()) {
            resultD = plotBounds.getHeight() / RADIO_PERCENT;
        } else {
            resultD = eachWidth / RADIO_PERCENT;
        }
        resultD = deal4BestRad(resultD, categoryIndex);
        return resultD;
    }

    private double getSeriesStartAngle(int categoryIndex, int seriesIndex) {
        double startAngle = START_ANGLE - getStartAngle();
        double wholeAngle = getWholeAngle();
        for (int i = 0; i < seriesIndex; i++) {
            startAngle -= getPercent(i, categoryIndex) * wholeAngle;
        }
        return startAngle;
    }

    private double getHalfAngle(double seriesStartAngle, int seriesIndex, int categoryIndex) {
        return (seriesStartAngle - getPercent(seriesIndex, categoryIndex) * getWholeAngle() / 2 + ANGLE * 2) % ANGLE;
    }

    /**
     * 值对应的角度  比*
     */
    private double getPercent(int seriesIndex, int categoryIndex) {
        if(ComparatorUtils.equals(roseType, ChartRoseType.PIE_SAME_ARC)){
            return 1/(double)getSeriesSize();
        }

        double totalValue = getTotalValue(categoryIndex); // 确定大于 还是小于的.
        if (totalValue <= 0) {
            return 0;
        }
        DataSeries dataSeries = this.getSeries(seriesIndex);
        DataPoint dataPoint = dataSeries.getDataPoint(categoryIndex);
        return Math.abs(dataPoint.getValue()) / totalValue;
    }


    // 得到某个分类的总值.  categoryIndex
    private double getTotalValue(int categoryIndex) {
        double totalValue = 0;
        int seriesSize = this.getSeriesSize();
        for (int seriesIndex = 0; seriesIndex < seriesSize; seriesIndex++) {
            DataSeries dataSeries = this.getSeries(seriesIndex);
            DataPoint dataPoint = dataSeries.getDataPoint(categoryIndex);
            totalValue += Math.abs(dataPoint.getValue());
        }

        return totalValue;
    }

    private double getWholeAngle() {
        double wholeAngle = (endAngle - startAngle + ANGLE) % ANGLE;
        return wholeAngle == 0 ? ANGLE : wholeAngle;
    }

    private Shape getDataPointShape(Point2D centerPoint, double seriesStartAngle, double radius, int seriesIndex, int categoryIndex) {

        double innerRadius = getInnerRadius();
        double outerRadius = getOuterRadius(radius, categoryIndex, getSeries(seriesIndex).getDataPoint(categoryIndex));

        GeneralPath arcPath = new GeneralPath();
        double extent = getPercent(seriesIndex, categoryIndex) * getWholeAngle();

        Arc2D outerArc = new Arc2D.Double(
                centerPoint.getX() - outerRadius,
                centerPoint.getY() - outerRadius,
                2 * outerRadius, 2 * outerRadius, seriesStartAngle - extent, extent, Arc2D.OPEN
        );

        Arc2D innerArc = new Arc2D.Double(
                centerPoint.getX() - innerRadius,
                centerPoint.getY() - innerRadius,
                2 * innerRadius, 2 * innerRadius, seriesStartAngle, -extent, Arc2D.OPEN
        );

        arcPath.moveTo((float)outerArc.getStartPoint().getX(), (float)outerArc.getStartPoint().getY());
        arcPath.append(outerArc, true);
        if(Math.floor(Math.abs(extent/ANGLE)) >= 1){//如果是整圆，就不用两边连的线了，边框直接就是内外圆。
            arcPath.moveTo((float)innerArc.getStartPoint().getX(), (float)innerArc.getStartPoint().getY());
        } else {
            arcPath.lineTo((float)innerArc.getStartPoint().getX(), (float)innerArc.getStartPoint().getY());
        }
        arcPath.append(innerArc, true);
        arcPath.closePath();

        return arcPath;
    }

    private double getInnerRadius(){
        return radius * getInnerRadiusPercent()/VanChartAttrHelper.PERCENT;
    }

    private double getOuterRadius(double maxRadius, int categoryIndex, DataPoint dataPoint){
        if(ComparatorUtils.equals(roseType, ChartRoseType.PIE)){
            return maxRadius;
        }
        return getInnerRadius() + (maxRadius - getInnerRadius())/getMaxValue(categoryIndex) * dataPoint.getValue();
    }

    // 得到某个分类的绝对值最大值.  categoryIndex
    private double getMaxValue(int categoryIndex) {
        double maxValue = 0;
        int seriesSize = this.getSeriesSize();
        for (int seriesIndex = 0; seriesIndex < seriesSize; seriesIndex++) {
            DataSeries dataSeries = this.getSeries(seriesIndex);
            VanChartPieDataPoint dataPoint = (VanChartPieDataPoint) dataSeries.getDataPoint(categoryIndex);
            maxValue = Math.max(Math.abs(dataPoint.getValue()), maxValue);
        }

        return maxValue;
    }

    private ShapeGlyph calculateShapeGlyph4DifferentSub(VanChartPieDataPoint dataPoint) {
        Shape arc2D = getArc2D(dataPoint);
        ShapeGlyph shapeGlyph = new ShapeGlyph(arc2D);

        changeInfoWithCondition(shapeGlyph.getGeneralInfo(), dataPoint);

        return shapeGlyph;
    }

    private Shape getArc2D(DataPoint dataPoint) {
        int seriesIndex = dataPoint.getSeriesIndex();
        int categoryIndex = dataPoint.getCategoryIndex();
        double seriesStartAngle = getSeriesStartAngle(categoryIndex, seriesIndex) % FOUTH_QUA;
        Point2D centerPoint = getCenterPoint(categoryIndex);
        return getDataPointShape(centerPoint, seriesStartAngle, radius, seriesIndex, categoryIndex);
    }

    private Point2D getCenterPoint(int categoryIndex) {
        double eachWidth = getBounds().getWidth() / this.getCategoryCount();
        double normalX = categoryIndex * eachWidth + eachWidth / 2;
        return new Point2D.Double(normalX, getBounds().getHeight() / 2);
    }

    private boolean isOutsideLabel(int seriesIndex, int categoryIndex) {
        AttrLabel attrLabel = (AttrLabel) getConditionCollection().getDataSeriesCondition(AttrLabel.class, getSeries(seriesIndex).getDataPoint(categoryIndex));
        if(attrLabel == null){
            return false;
        }
        return (attrLabel.getPosition() == Constants.OUTSIDE || attrLabel.isShowGuidLine());
    }

    private boolean isGuideLineShow(DataPoint dataPoint) {
        TextGlyph textGlyph = dataPoint.getDataLabel();
        if (textGlyph == null) {
            return false;
        }

        AttrLabel attrLabel = (AttrLabel) getConditionCollection().getDataSeriesCondition(AttrLabel.class, dataPoint);

        String pieLabel = textGlyph.getText();
        return pieLabel != null && attrLabel.isShowGuidLine() && textGlyph.getBounds() != null;
    }

    private Dimension2D getLabelDim(DataPoint dataPoint, int resolution) {
        if (dataPoint.getDataLabel() == null || StringUtils.isEmpty(dataPoint.getDataLabel().getText())) {
            return new Dimension(0, 0);
        }
        AttrLabel attrLabel =
                (AttrLabel) getConditionCollection().getDataSeriesCondition(AttrLabel.class, dataPoint, null);
        TextAttr textAttr = attrLabel.getTextAttr();
        if(!attrLabel.isCustom()){
            textAttr.setFRFont(VanChartAttrHelper.DEFAULT_LABEL_FONT);
        }
        return GlyphUtils.calculateTextDimensionWithNoRotation(dataPoint.getDataLabel().getText(), textAttr, resolution);

    }

    private boolean isSmallerThan(PointAndAngle pointAndAngle,PointAndAngle minPoint){
        return getExtend(pointAndAngle.pieDataPoint) < getExtend(minPoint.pieDataPoint);
    }

    private double getExtend(DataPoint dataPoint){
        return getPercent(dataPoint.getSeriesIndex(), dataPoint.getCategoryIndex()) * getWholeAngle();
    }

    //处理标签在内时的标签bounds
    private void dealInsideLabelBounds(DataPoint dataPoint, int resolution) {
        Rectangle2D labelBounds = new Rectangle2D.Double();
        int seriesIndex = dataPoint.getSeriesIndex();
        int categoryIndex = dataPoint.getCategoryIndex();
        Point2D centerPoint = getCenterPoint(categoryIndex);
        double tg_height = getLabelDim(dataPoint, resolution).getHeight();
        double tg_width = getLabelDim(dataPoint, resolution).getWidth();
        double time = 1.0 / 2.0;
        double outerRadius = getOuterRadius(radius, categoryIndex, dataPoint);
        double innerRadius = getInnerRadius();
        TextGlyph textGlyph = dataPoint.getDataLabel();
        double seriesStartAngle = getSeriesStartAngle(categoryIndex, seriesIndex) % ANGLE;
        double centerAngle = getHalfAngle(seriesStartAngle, seriesIndex, categoryIndex) % ANGLE;

        double tmpX = (outerRadius - innerRadius) * Math.cos(Math.PI * centerAngle / SECOND_QUA);
        double tmpY = (outerRadius - innerRadius) * Math.sin(Math.PI * centerAngle / SECOND_QUA);
        double startX = centerPoint.getX() + innerRadius * Math.cos(Math.PI * centerAngle / SECOND_QUA);
        double startY = centerPoint.getY() - innerRadius * Math.sin(Math.PI * centerAngle / SECOND_QUA);

        double rx = startX + tmpX * time;
        double ry = startY - tmpY * time;
        double centerLabelX = rx - tg_width / 2.0;
        double centerLabelY = ry - tg_height / 2.0;
        labelBounds.setRect(centerLabelX, centerLabelY, tg_width, tg_height);
        textGlyph.setBounds(labelBounds);
    }

    //返回分类号为categoryIndex的饼图四象限的系列计数，跳过空值。
    private void getSeriesCountWithQuadrant(int categoryIndex) {
        clearList();
        for (int i = 0; i < this.getSeriesSize(); i++) {
            if (this.getSeries(i).getDataPoint(categoryIndex).isValueIsNull()) {
                continue;
            }
            if(!this.isOutsideLabel(i,categoryIndex)){
                continue;
            }
            DataSeries dataSeries = this.getSeries(i);
            VanChartPieDataPoint dataPoint = (VanChartPieDataPoint)dataSeries.getDataPoint(categoryIndex);
            double centerAngle = getHalfAngle(getSeriesStartAngle(categoryIndex, i), i, categoryIndex) % FOUTH_QUA;
            DecimalFormat decimal = new DecimalFormat("######0.00");
            centerAngle = Double.valueOf(decimal.format(centerAngle)).doubleValue();
            if (centerAngle >= FIRST_QUA && centerAngle <= SECOND_QUA) {
                leftUp.add(new PointAndAngle(dataPoint, centerAngle));
            } else if (centerAngle > SECOND_QUA && centerAngle <= THRID_QUA) {
                leftDown.add(new PointAndAngle(dataPoint, centerAngle));
            } else if (centerAngle > THRID_QUA && centerAngle <= FOUTH_QUA) {
                rightDown.add(new PointAndAngle(dataPoint, centerAngle));
            } else {
                rightUp.add(new PointAndAngle(dataPoint, centerAngle));
            }
        }
        sortList();
    }

    private void clearList() {
        leftUp.clear();
        leftDown.clear();
        rightUp.clear();
        rightDown.clear();
    }

    private void sortList() {
        bubbleSort(leftUp);
        bubbleSort(leftDown);
        bubbleSort(rightUp);
        bubbleSort(rightDown);
    }

    //冒泡排序
    private void bubbleSort(ArrayList list) {
        PointAndAngle[] array = (PointAndAngle[]) list.toArray(new PointAndAngle[]{});
        int low = 0;
        int high = array.length - 1; //设置变量的初始值
        PointAndAngle tmp;
        int j;
        while (low < high) {
            for (j = low; j < high; ++j) { //正向冒泡,找到最大者
                if (array[j].centerAngle > array[j + 1].centerAngle) {
                    tmp = array[j];
                    array[j] = array[j + 1];
                    array[j + 1] = tmp;
                }
            }
            --high; //修改high值, 前移一位
            for (j = high; j > low; --j) { //反向冒泡,找到最小者
                if (array[j].centerAngle < array[j - 1].centerAngle) {
                    tmp = array[j];
                    array[j] = array[j - 1];
                    array[j - 1] = tmp;
                }
            }
            ++low; //修改low值,后移一位
        }
        list.clear();
        for (PointAndAngle pointAndAngle : array) {
            list.add(pointAndAngle);
        }
    }

    private void dealWithDataSeries(int categoryIndex, int resolution) {
        int seriesSize = this.getSeriesSize();
        for (int seriesIndex = 0; seriesIndex < seriesSize; seriesIndex++) {
            DataSeries dataSeries = this.getSeries(seriesIndex);
            VanChartPieDataPoint dataPoint = (VanChartPieDataPoint)dataSeries.getDataPoint(categoryIndex);
            if (dataPoint.isValueIsNull()) {
                continue;
            }
            ShapeGlyph shapeGlyph = calculateShapeGlyph4DifferentSub(dataPoint);
            dataPoint.setDrawImpl(shapeGlyph);

            if (getSubType() == ChartConstants.PIE_NORMAL) {
                this.dealPlotStyle(this.plotStyle, dataPoint);
            }
        }
        dealWithCategoryLabels(categoryIndex, resolution);
    }

    private int getSubType() {
        return ChartConstants.PIE_NORMAL;
    }

    private boolean isExistOuterLabels(){
        return !leftUp.isEmpty() || !leftDown.isEmpty() || !rightUp.isEmpty() || !rightDown.isEmpty() ;
    }

    private void dealWithCategoryLabels(int categoryIndex, int resolution){
        if (isExistOuterLabels()) {
            dealOutSideLabels(categoryIndex, resolution);
            //处理标签重叠
            dealLabelBoundsOverLay(leftUp,resolution,0,90,180,true);
            dealLabelBoundsOverLay(leftDown,resolution,0,180,270,true);
            dealLabelBoundsOverLay(rightUp,resolution,0,0,90,false);
            dealLabelBoundsOverLay(rightDown,resolution,0,270,360,false);
        }
        //处理内部标签
        dealInsideLabels(categoryIndex, resolution);
    }

    private void dealInsideLabels(int categoryIndex, int resolution) {
        for (int seriesIndex = 0; seriesIndex < this.getSeriesSize(); seriesIndex++) {
            DataSeries dataSeries = this.getSeries(seriesIndex);
            VanChartPieDataPoint dataPoint = (VanChartPieDataPoint) dataSeries.getDataPoint(categoryIndex);
            if (dataPoint.isValueIsNull()) {
                continue;
            }
            if(this.isOutsideLabel(seriesIndex,categoryIndex)){
                continue;
            }
            TextGlyph textGlyph = dataPoint.getDataLabel();
            if (textGlyph != null && StringUtils.isNotEmpty(textGlyph.getText())) {
                dealInsideLabelBounds(dataPoint, resolution);
            }
        }
    }

    private void dealOutSideLabels(int categoryIndex, int resolution) {
        double maxDiff = 0;
        maxDiff = Math.max(maxDiff, dealEachArea(leftUp, resolution));
        maxDiff = Math.max(maxDiff, dealEachArea(leftDown, resolution));
        maxDiff = Math.max(maxDiff, dealEachArea(rightUp, resolution));
        maxDiff = Math.max(maxDiff, dealEachArea(rightDown, resolution));
        if (maxDiff == 0) {
            return;
        }
        this.radiusPercent -= RADIUS_PERCENT_DIFF;
        if (this.radiusPercent >= RADIUS_MIN_PERCENR) {
            deal4EachCategory(categoryIndex, resolution);
        }
    }

    private double dealEachArea(ArrayList pointArray, int resolution) {
        double maxX = this.getBounds().getX() + this.getBounds().getWidth();
        double minX = this.getBounds().getX();
        double diff = 0;
        checkPointsShowLabel(pointArray, resolution);
        //接下来是所有要显示label的数据点

        for (int i = 0; i < pointArray.size(); i++) {
            Rectangle2D labelBounds = new Rectangle2D.Double();
            PointAndAngle pointAndAngle = (PointAndAngle) pointArray.get(i);
            if (pointAndAngle.pieDataPoint.getDataLabel() == null) {
                continue;
            }
            dealOutsideLabelBoundsWhenNormal(labelBounds, pointAndAngle, resolution);
            if (labelBounds.getX() < minX) {
                diff = minX - labelBounds.getX();
            }
            if (labelBounds.getX() + labelBounds.getWidth() > maxX) {
                diff = labelBounds.getX() + labelBounds.getWidth() - maxX;
            }
        }
        return diff;
    }

    /*
  * 处理标签在外且系列标签一开始不调整时候的位置
  */
    private void dealOutsideLabelBoundsWhenNormal(Rectangle2D labelBounds, PointAndAngle pointAndAngle, int resolution) {
        Point2D centerPoint = calculateLabelBoundsWithAngle(pointAndAngle.centerAngle,pointAndAngle,resolution,labelBounds);
        pointAndAngle.pieDataPoint.getDataLabel().setBounds(labelBounds);
        if (isGuideLineShow(pointAndAngle.pieDataPoint)) {
            dealWidthLine(centerPoint, pointAndAngle);
        }
    }

    private Point2D calculateLabelBoundsWithAngle(double centerAngle,PointAndAngle pointAndAngle, int resolution,Rectangle2D labelBounds){
        int categoryIndex = pointAndAngle.pieDataPoint.getCategoryIndex();
        Point2D centerPoint = getCenterPoint(categoryIndex);
        Dimension2D dim = getLabelDim(pointAndAngle.pieDataPoint, resolution);
        double tg_height = dim.getHeight();
        double tg_width = dim.getWidth();
        double R = plotStyle != ChartConstants.STYLE_3D?(this.radius * RADIO_PERCENT):this.radius;
        double rx = centerPoint.getX() + R * Math.cos(Math.PI * centerAngle/ SECOND_QUA);
        double ry = centerPoint.getY() - R * Math.sin(Math.PI * centerAngle / SECOND_QUA);
        double littleLineLength = (plotStyle != ChartConstants.STYLE_3D ? this.radius : (this.radius/RADIO_PERCENT)) * MOVE_GAP + 2;
        // 垂直角度 左右分开
        if (pointAndAngle.centerAngle >= FIRST_QUA && pointAndAngle.centerAngle <= THRID_QUA) {
            labelBounds.setRect(rx-littleLineLength-tg_width, ry - tg_height / 2, tg_width, tg_height);
        } else {
            labelBounds.setRect(rx+littleLineLength, ry - tg_height / 2, tg_width, tg_height);
        }
        return centerPoint;
    }


    private void checkPointsShowLabel(ArrayList pointArray, int resolution) {
        if(pointArray == null || pointArray.isEmpty()){
            return;
        }
        double allUsedHeight = 0; //是否显示不下
        for (int i = 0; i < pointArray.size(); i++) {
            PointAndAngle pointAndAngle = (PointAndAngle) pointArray.get(i);
            TextGlyph textGlyph = pointAndAngle.pieDataPoint.getDataLabel();
            if (textGlyph != null && StringUtils.isNotEmpty(textGlyph.getText())) {
                allUsedHeight += getLabelDim(pointAndAngle.pieDataPoint, resolution).getHeight();
            }
        }
        if (allUsedHeight > this.radius) {
            removeAngleExtendMinPoint(pointArray);
            checkPointsShowLabel(pointArray, resolution);
        }
    }

    private void removeAngleExtendMinPoint(ArrayList pointArray) {
        if (pointArray == null || pointArray.isEmpty()) {
            return;
        }
        PointAndAngle minDataPoint = (PointAndAngle) pointArray.get(0);
        for (int i = 1; i < pointArray.size(); i++) {
            PointAndAngle pointAndAngle = (PointAndAngle) pointArray.get(i);
            if (isSmallerThan(pointAndAngle,minDataPoint)) {
                minDataPoint = pointAndAngle;
            }
        }
        pointArray.remove(minDataPoint);
    }

    private void dealLabelBoundsOverLay(ArrayList pointArray,int resolution,int changedTimes,double minAngle,double maxAngle,boolean isLeft){
        if(changedTimes>=2){
            return;
        }
        GeneralPath boundsPath = new GeneralPath();
        boolean isSuccess = true;
        for (int i = 0; i < pointArray.size(); i++) {
            PointAndAngle pointAndAngle = (PointAndAngle) pointArray.get(i);
            if (pointAndAngle.pieDataPoint.getDataLabel() == null ||
                    StringUtils.isEmpty(pointAndAngle.pieDataPoint.getDataLabel().getText())) {
                continue;
            }
            Rectangle2D labelBounds = pointAndAngle.pieDataPoint.getDataLabel().getBounds();
            if(boundsPath.intersects(labelBounds)){
                if(!adjustLabelBoundsY(boundsPath,pointAndAngle,resolution,minAngle,maxAngle,isLeft)){
                    isSuccess = false;
                    break;
                }
            }else{
                boundsPath.append(labelBounds,false);
            }
        }
        if(isSuccess){
            return;
        }
        dealLabelBoundsOverLay(pointArray,resolution,changedTimes+1, minAngle,maxAngle,!isLeft);
    }

    private boolean adjustLabelBoundsY(GeneralPath boundsPath,PointAndAngle pointAndAngle,int resolution,double minAngle,double maxAngle,boolean isLeft){
        Rectangle2D labelBounds = pointAndAngle.pieDataPoint.getDataLabel().getBounds();
        double centerAngle = pointAndAngle.centerAngle;
        while(boundsPath.intersects(labelBounds)){
            if (isLeft) {
                centerAngle += 1;
            } else{
                centerAngle -= 1;
            }
            calculateLabelBoundsWithAngle(centerAngle, pointAndAngle,resolution,labelBounds);
        }
        pointAndAngle.pieDataPoint.getDataLabel().setBounds(labelBounds);

        boundsPath.append(labelBounds,false);
        if(centerAngle <minAngle || centerAngle > maxAngle){
            return false;
        }

        if(!isGuideLineShow(pointAndAngle.pieDataPoint)){
            return true;
        }
        GeneralPath linePath = new GeneralPath();
        double pointX = 0;
        double pointY = 0;
        if (pointAndAngle.centerAngle >= FIRST_QUA && pointAndAngle.centerAngle <= THRID_QUA) {
            pointX = labelBounds.getX()+labelBounds.getWidth()+2;
        } else{
            pointX = labelBounds.getX()-2;
        }
        pointY = labelBounds.getY()+labelBounds.getHeight()/2;
        linePath.moveTo(pointX,pointY);
        if (pointAndAngle.centerAngle >= FIRST_QUA && pointAndAngle.centerAngle <= THRID_QUA) {
            pointX += MOVE_GAP*this.radius;
        } else{
            pointX -= MOVE_GAP*this.radius;
        }
        linePath.lineTo(pointX,pointY);
        Point2D centerPoint = getCenterPoint(pointAndAngle.pieDataPoint.getCategoryIndex());
        double R = this.radius + 2;
        pointX = centerPoint.getX() + R * Math.cos(Math.PI * pointAndAngle.centerAngle / SECOND_QUA);
        pointY = centerPoint.getY() - R * Math.sin(Math.PI * pointAndAngle.centerAngle / SECOND_QUA);
        linePath.lineTo(pointX,pointY);
        pointAndAngle.pieDataPoint.setLeadLine(linePath);
        return true;
    }


    // 计算合适的饼图半径 考虑牵引线
    private double deal4BestRad(double resultD, int categoryIndex) {
        AttrLabel attrLabel = (AttrLabel)getConditionCollection().getDataSeriesCondition(AttrLabel.class, this.getSeries(0).getDataPoint(categoryIndex));
        if (attrLabel != null && attrLabel.getPosition() == Constants.OUTSIDE) {
            resultD = resultD * radiusPercent;
            if (this.getCategoryCount() > 1) {
                resultD = resultD * WHEN_MORE_RAD;
            }
        }
        return resultD;
    }

    //牵引线
    private void dealWidthLine(Point2D centerPoint, PointAndAngle pointAndAngle) {
        GeneralPath path = new GeneralPath();
        double outerRadius = getOuterRadius(radius, pointAndAngle.pieDataPoint.getCategoryIndex(), pointAndAngle.pieDataPoint);
        double rx = centerPoint.getX() + (outerRadius + 2) * Math.cos(Math.PI * pointAndAngle.centerAngle / SECOND_QUA);
        double ry = centerPoint.getY() - (outerRadius + 2) * Math.sin(Math.PI * pointAndAngle.centerAngle / SECOND_QUA);
        path.moveTo(rx, ry);
        double textRadius = radius * RADIO_PERCENT;
        rx = centerPoint.getX() + textRadius * Math.cos(Math.PI * pointAndAngle.centerAngle / SECOND_QUA);
        ry = centerPoint.getY() - textRadius * Math.sin(Math.PI * pointAndAngle.centerAngle / SECOND_QUA);
        path.lineTo(rx, ry);
        if (pointAndAngle.centerAngle >= FIRST_QUA && pointAndAngle.centerAngle <= THRID_QUA) {
            rx -= radius * MOVE_GAP;
        } else {
            rx += radius * MOVE_GAP;
        }
        path.lineTo(rx, ry);
        pointAndAngle.pieDataPoint.setLeadLine(path);
    }

    //渐变风格
    private void dealPlotStyle(int plotStyle, VanChartPieDataPoint dataPoint) {
        ShapeGlyph shapeGlyph = (ShapeGlyph) dataPoint.getDrawImpl();
        Shape shape = dataPoint.getShape();
        if (shapeGlyph.getBackground() instanceof ColorBackground) {
            Color seriesColor = (((ColorBackground) shapeGlyph.getBackground())).getColor();
            double innerRadius = getInnerRadius();
            double outerRadius = getOuterRadius(radius, dataPoint.getCategoryIndex(), dataPoint);
            Point2D centerPoint = getCenterPoint(dataPoint.getCategoryIndex());
            ChartStyle dataPointStyle = null;
            if (plotStyle == ChartConstants.STYLE_SHADE) {// 上下渐变
                dataPointStyle = new RoseTopDownShadeStyle(innerRadius,outerRadius,centerPoint,seriesColor,shape,true);
            }

            if (dataPointStyle != null) {
                if(getAttrAlpha(dataPoint) != null){
                    dataPointStyle.setAlpha(shapeGlyph.getAlpha());
                }
                if(getAttrBorder(dataPoint) != null){
                    dataPointStyle.setBorderWidth(VanChartAttrHelper.getAxisLineStyle(shapeGlyph.getBorderStyle()));
                    dataPointStyle.setBorderColor(shapeGlyph.getBorderColor() == null ? new Color(0,0,0,0) : shapeGlyph.getBorderColor());
                }
                dataPoint.setDataPointStyle(dataPointStyle);
            }
        }
    }

    /**
     * 判断是否对某个Result进行条件设置
     * @param result 需要判断的对象
     * @param targetClass 是否存在的条件
     * @return 是则返回true
     */
    public boolean isExistAttrByResult(Result result,Class targetClass){
        return getConditionCollection() != null && getConditionCollection().getAttrByResult(result, targetClass) != null;
    }

    /**
     * 计算数据点的占比，用的是各个数值的绝对值.
     */
    public void calculateDataPointPercentValue(){
        int seriesSize = this.getSeriesSize();
        if (seriesSize > 0) {
            int cateCount = this.getSeries(0).getDataPointCount();
            for (int cateIndex = 0; cateIndex < cateCount; cateIndex++) {
                double total = 0;
                for (int seriesIndex = 0; seriesIndex < seriesSize; seriesIndex++) {
                    total += Math.abs(this.getSeries(seriesIndex).getDataPoint(cateIndex).getValue());
                }
                for (int i = 0; i < seriesSize; i++) {
                    DataPoint dataPoint = this.getSeries(i).getDataPoint(cateIndex);
                    double value = Math.abs(dataPoint.getValue());
                    if (total != 0) {
                        dataPoint.setPercentValue(value / total);
                    }
                }
            }
        }
    }

    /**
     * 获取 plotOptions的JSON对象
     * @param repo 请求
     * @param isJsDraw 动态展示
     * @return 返回json
     * @throws JSONException 抛出问题
     */
    public JSONObject getPlotOptionsJSON(Repository repo, boolean isJsDraw) throws JSONException {
        JSONObject js = super.getPlotOptionsJSON(repo, isJsDraw);

        if(!ComparatorUtils.equals(getRoseType(), ChartRoseType.PIE)){
            js.put("roseType",getRoseType().getRoseType());
        }

        js.put("innerRadius", getInnerRadiusPercent() + VanChartAttrHelper.STRING_PERCENT);
        js.put("startAngle", getStartAngle());
        js.put("endAngle", getEndAngle());
        js.put("rotatable", isSupportRotation());

        return js;
    }

    public String getChartType() {
        return "pie";
    }

    /**
     * 系列写入json
     * @param js  json对象
     * @param repo 请求
     * @throws JSONException 抛错
     */
    public void addSeriesJSON(JSONObject js, Repository repo) throws JSONException {
        if(this.useDefaultNullData) {
            addNullDataSeriesJSON(js);
            return;
        }
        addSeriesJSONWithCate2Series(js, repo);
    }

    /**
     * 数据没有配置时，系列写入json
     * @param js json对象
     * @throws JSONException 抛错
     */
    public void addNullDataSeriesJSON(JSONObject js) throws JSONException{
        js.put("series", new ArrayList());
    }

    private class PointAndAngle {
        public double centerAngle;
        public VanChartPieDataPoint pieDataPoint;

        public PointAndAngle(VanChartPieDataPoint pieDataPoint, double centerAngle) {
            this.pieDataPoint = pieDataPoint;
            this.centerAngle = centerAngle;
        }
    }

}
