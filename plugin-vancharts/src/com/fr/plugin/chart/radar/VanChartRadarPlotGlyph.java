package com.fr.plugin.chart.radar;

import com.fr.chart.base.AttrAlpha;
import com.fr.chart.base.AttrLineStyle;
import com.fr.chart.chartglyph.*;
import com.fr.json.JSONException;
import com.fr.json.JSONObject;
import com.fr.plugin.chart.area.VanChartAreaPlotGlyph;
import com.fr.plugin.chart.glyph.VanChartDataSeries;
import com.fr.plugin.chart.glyph.axis.VanChartBaseAxisGlyph;
import com.fr.plugin.chart.glyph.axis.VanChartRadarAxisGlyph;
import com.fr.plugin.chart.glyph.marker.VanChartMarkerGlyph;
import com.fr.stable.web.Repository;

import java.awt.*;
import java.awt.geom.*;
import java.util.*;

/**
 * Created by Mitisky on 15/12/28.
 */
public class VanChartRadarPlotGlyph extends VanChartAreaPlotGlyph {

    private static final long serialVersionUID = 3690467196318699246L;

    private VanChartRadarAxisGlyph radarAxisGlyph;
    private RadarType radarType;
    private boolean isColumnType;

    public void setRadarAxisGlyph(VanChartRadarAxisGlyph radarAxisGlyph) {
        this.radarAxisGlyph = radarAxisGlyph;
    }

    public void setRadarType(RadarType radarType) {
        this.radarType = radarType;
    }

    public void setColumnType(boolean isColumnType) {
        this.isColumnType = isColumnType;
    }

    /**
     *  对绘图区中的坐标轴部分进行布局,传过去图表边界计算限制区域
     *  雷达图的坐标轴布局不是常规坐标轴图形布局，单拿出来。
     * @param chartOriginalBounds 原始图表边界
     * @param resolution 分辨率
     */
    public void layoutAxisGlyph(Rectangle2D chartOriginalBounds, int resolution){
        initYAxisGlyphMinMaxValue(0, radarAxisGlyph.getDataSeriesValueAxisGlyph());
        radarAxisGlyph.doLayout(new Rectangle2D.Double(0, 0, getBounds().getWidth(), getBounds().getHeight()), resolution);
    }

    //堆积柱形雷达图的图例和柱形图一样，普通雷达图图例和折线图一样。
    protected LineMarkerIcon getLegendMarkerIcon(VanChartDataSeries dataSeries, Color[] colors) {
        if(isColumnType){
            LineMarkerIcon itemIcon = new LineMarkerIcon();
            dealMarkerIconCondition(itemIcon, dataSeries, colors);
            return itemIcon;
        } else {
            return super.getLegendMarkerIcon(dataSeries, colors);
        }
    }

    //默认的series的bands属性，即坐标轴的最大最小值。为了补足bands属性最大值最小值只设置了一个的问题
    protected void initDataSeriesBandsDefaultMinMaxValue(double min, double max, java.util.List<Number> seriesList4TheAxis){
    }

    protected void initDataSeriesGlyph(VanChartDataSeries dataSeries, GeneralPath linePaths, GeneralPath areaPaths, Color seriesColor,
                                       VanChartBaseAxisGlyph yAxisGlyph
            , int[] cateIndexArray, Point2D[] currentPositivePoints, Point2D[] lastPositivePoints, Point2D[] lastNegativePoints) {
        dealLinePath(areaPaths, linePaths, dataSeries, seriesColor, yAxisGlyph);
    }

    protected void dealBands(GeneralPath areaPaths, GeneralPath linePaths, VanChartDataSeries dataSeries, Color seriesColor,
                             VanChartBaseAxisGlyph yAxisGlyph, AttrLineStyle attrLineStyle) {
    }

    //面积图没有光滑曲线，因为空值断开时，线和填充颜色的路径不一样，所以分开。
    protected void dealLine4AllSeries(VanChartDataSeries dataSeries, VanChartBaseAxisGlyph xAxisGlyph, VanChartBaseAxisGlyph yAxisGlyph,
                                      Map<String, Number> prePositiveSumValueInSameCateValue, Map<String, Number> preNegativeSumValueInSameCateValue,
                                      Point2D[] lastPositivePoints, Point2D[] lastNegativePoints,
                                      Color seriesColor, GeneralPath linePaths, GeneralPath areaPaths, int[] cateIndexArray, int resolution) {
        if(isColumnType){
            dealStackColumn4AllSeries(dataSeries, prePositiveSumValueInSameCateValue, preNegativeSumValueInSameCateValue, cateIndexArray, resolution);
        } else {
            double centerValue = yAxisGlyph.getMinValue();
            dealNormalLine4AllSeries(dataSeries, seriesColor, linePaths, areaPaths, cateIndexArray, centerValue, resolution);
        }
    }

    private void dealNormalLine4AllSeries(VanChartDataSeries dataSeries, Color seriesColor, GeneralPath linePaths,
                                          GeneralPath areaPaths, int[] cateIndexArray, double centerValue, int resolution) {
        isStartPoint = true; // 是否是起始点, 判断值是否为空, 是否是第一个分类
        boolean isNullValueBreak = isNullValueBreak(dataSeries);
        Point2D firstPoint = null;//第一个不是空值的数据点
        for (int cateIndex : cateIndexArray) {
            VanChartRadarDataPoint dataPoint = (VanChartRadarDataPoint)dataSeries.getDataPoint(cateIndex);
            double value = dataPoint.getValue();
            if(dataPoint.isValueIsNull()){
                value = centerValue;
            }
            Point2D p = radarAxisGlyph.getPoint2DWithCateIndexAndValue(cateIndex, value);
            dealAreaPoint4EveryPoint(areaPaths, p, firstPoint);

            if (dataPoint.isValueIsNull()) {
                if (isNullValueBreak) {
                    isStartPoint = true;  //空值断开
                }
                continue;
            } else if(firstPoint == null){
                firstPoint = p;
            }

            dealLinePoint4EveryDataPoint(linePaths, new GeneralPath(), (float)p.getX(), (float)p.getY(), false, false, 0);
            VanChartMarkerGlyph markerGlyph = new VanChartMarkerGlyph(){
                protected boolean isNeedBorderBG() {
                    return false;
                }
            };
            initMarkerGlyph(markerGlyph, dataPoint, (float)p.getX(), (float)p.getY(), seriesColor);
            dealDataPointLabel(dataPoint, value, resolution);
        }
        if(firstPoint != null){
            dealAreaPoint4EveryPoint(areaPaths, firstPoint, firstPoint);
            dealLinePoint4EveryDataPoint(linePaths, new GeneralPath(), (float)firstPoint.getX(), (float)firstPoint.getY(), false, false, 0);
        }
    }

    private void dealAreaPoint4EveryPoint(GeneralPath areaPaths, Point2D p, Point2D firstPoint) {
        if(firstPoint == null){
            areaPaths.moveTo(p.getX(), p.getY());
        } else {
            areaPaths.lineTo(p.getX(), p.getY());
        }
    }

    private void dealStackColumn4AllSeries(VanChartDataSeries dataSeries, Map<String, Number> prePositiveSumValueInSameCateValue,
                                           Map<String, Number> preNegativeSumValueInSameCateValue, int[] cateIndexArray, int resolution) {
        for (int cateIndex : cateIndexArray) {
            VanChartRadarDataPoint dataPoint = (VanChartRadarDataPoint)dataSeries.getDataPoint(cateIndex);
            double value = dataPoint.getValue();
            double lastSumValue = getLastSumValue(cateIndex, value, prePositiveSumValueInSameCateValue, preNegativeSumValueInSameCateValue);
            double sumValue = getSumValue(cateIndex, value, prePositiveSumValueInSameCateValue, preNegativeSumValueInSameCateValue);

            Shape shape = radarAxisGlyph.getArcShapeBetweenTwoValues(lastSumValue, sumValue, cateIndex);
            ShapeGlyph shapeGlyph = new ShapeGlyph(shape);

            changeInfoWithCondition(shapeGlyph.getGeneralInfo(), dataPoint);

            dataPoint.setDrawImpl(shapeGlyph);

            dealDataPointLabel(dataPoint, sumValue, resolution);
        }
    }

    /**
     * 处理数据点的标签的边框
     *
     * @param dataPoint  数据点
     * @param resolution 分辨率
     */
    private void dealDataPointLabel(DataPoint dataPoint, double realValue, int resolution) {
        TextGlyph labelGlyph = dataPoint.getDataLabel();
        if(labelGlyph == null) {
            return;
        }

        if (notDealDataPointLabel(dataPoint, labelGlyph)) {
            labelGlyph.setBounds(null);
        } else {
            Dimension2D labelPreDim = labelGlyph.preferredDimension(resolution);

            Rectangle2D bounds = isColumnType ? radarAxisGlyph.getColumnTypeLabelBounds(labelPreDim, dataPoint.getCategoryIndex(), realValue)
                    : radarAxisGlyph.getNormalLabelBounds(labelPreDim, dataPoint.getCategoryIndex(), realValue);

            labelGlyph.setBounds(bounds);
        }
    }

    private double getLastSumValue(double cateValue, double value, Map<String, Number> prePositiveSumValueInSameCateValue,
                                   Map<String, Number> preNegativeSumValueInSameCateValue) {
        if(value > 0){
            Number sum = prePositiveSumValueInSameCateValue.get(String.valueOf(cateValue));
            return sum == null ? 0 : sum.doubleValue();
        } else {
            Number sum = preNegativeSumValueInSameCateValue.get(String.valueOf(cateValue));
            return sum == null ? 0 : sum.doubleValue();
        }
    }

    /**
     * 趋势线拟合计算
     */
    protected void trendLineFitting(double[] xVal, double[] yVal, DataSeries dataSeries) {
    }

    //雷达图画系列的时候不能超出坐标轴x轴包围的圆形或多边形区域。
    public void drawShape4Series(Graphics g, int resolution) {
        Shape oldShape = g.getClip();
        g.setClip(radarAxisGlyph.getShape());
        super.drawShape4Series(g, resolution);
        g.setClip(oldShape);
    }

    protected void drawAxis(Graphics g, int resolution) {
        radarAxisGlyph.draw(g, resolution);
    }

    public  String getChartType(){
        return "radar";
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
        js.put("type", radarType.getType());
        js.put("columnType", isColumnType);

        AttrAlpha attrAlpha = getDefaultAttrAlpha();
        if(attrAlpha != null){
            js.put("fillColorOpacity", attrAlpha.getAlpha());
        }
        return js;
    }

}
