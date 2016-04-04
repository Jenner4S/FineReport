package com.fr.plugin.chart.line;

import com.fr.base.background.ColorBackground;
import com.fr.chart.base.AttrColor;
import com.fr.chart.base.AttrLineStyle;
import com.fr.chart.base.ChartUtils;
import com.fr.chart.base.LineStyleInfo;
import com.fr.chart.chartglyph.*;
import com.fr.general.Background;
import com.fr.general.ComparatorUtils;
import com.fr.json.JSONException;
import com.fr.json.JSONObject;
import com.fr.plugin.chart.attr.AttrBand;
import com.fr.plugin.chart.attr.LineStyle;
import com.fr.plugin.chart.base.VanChartAttrLine;
import com.fr.plugin.chart.base.VanChartAttrMarker;
import com.fr.plugin.chart.glyph.VanChartBandGlyph;
import com.fr.plugin.chart.glyph.VanChartDataSeries;
import com.fr.plugin.chart.glyph.VanChartLineMarkerIcon;
import com.fr.plugin.chart.glyph.VanChartRectanglePlotGlyph;
import com.fr.plugin.chart.glyph.axis.VanChartBaseAxisGlyph;
import com.fr.plugin.chart.glyph.marker.CustomImageMarker;
import com.fr.stable.Constants;
import com.fr.stable.web.Repository;

import java.awt.*;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Mitisky on 15/11/10.
 */
public class VanChartLinePlotGlyph extends VanChartRectanglePlotGlyph {

    private static final long serialVersionUID = 3936503431906562228L;
    //仅用于画线的时候
    protected boolean isStartPoint = true;

    //以下属性是存在condition里面的，专门拿出来是避免多个地方使用，循环遍历多次
    private VanChartAttrLine defaultAttrLine;
    private VanChartAttrMarker defaultAttrMarker;

    private VanChartAttrLine getDefaultAttrLine() {
        if(defaultAttrLine == null){
            ConditionAttr conditionAttr = getConditionCollection().getDefaultAttr();
            defaultAttrLine = (VanChartAttrLine)conditionAttr.getExisted(VanChartAttrLine.class);
        }
        return defaultAttrLine;
    }

    private VanChartAttrMarker getDefaultAttrMarker() {
        if(defaultAttrMarker == null){
            ConditionAttr conditionAttr = getConditionCollection().getDefaultAttr();
            defaultAttrMarker = (VanChartAttrMarker)conditionAttr.getExisted(VanChartAttrMarker.class);
        }
        return defaultAttrMarker;
    }

    /**
     * 某个系列是否是光滑曲线
     * @param dataSeries 该系列
     * @return 是否光滑
     */
    protected boolean isCurve(VanChartDataSeries dataSeries){
        VanChartAttrLine attrLine = dataSeries.getAttrLine();
        if(attrLine == null) {
            attrLine = getDefaultAttrLine();
        }
        return attrLine != null && ComparatorUtils.equals(attrLine.getLineStyle(), LineStyle.CURVE);
    }

    protected boolean isStep(VanChartDataSeries dataSeries){
        VanChartAttrLine attrLine = dataSeries.getAttrLine();
        if(attrLine == null) {
            attrLine = getDefaultAttrLine();
        }
        return attrLine != null && ComparatorUtils.equals(attrLine.getLineStyle(), LineStyle.STEP);
    }

    protected boolean isNullValueBreak(VanChartDataSeries dataSeries){
        VanChartAttrLine attrLine = dataSeries.getAttrLine();
        if(attrLine == null) {
            attrLine = getDefaultAttrLine();
        }        return attrLine == null || attrLine.isNullValueBreak();
    }

    protected int getAttrLineWidth(VanChartDataSeries dataSeries){
        VanChartAttrLine attrLine = dataSeries.getAttrLine();
        if(attrLine == null) {
            attrLine = getDefaultAttrLine();
        }
        if(attrLine == null){
            attrLine = new VanChartAttrLine();
        }
        return attrLine.getLineWidth();
    }

    protected Color getAttrLineColor(VanChartDataSeries dataSeries){
        Color[] colors = createColors4Series();
        return getAttrLineColor(dataSeries, colors);
    }

    private VanChartAttrMarker getAttrMarker(VanChartLineDataPoint dataPoint) {
        VanChartAttrMarker attrMarker = dataPoint.getAttrMarker();
        if(attrMarker == null){
            attrMarker = getDefaultAttrMarker();
        }
        return attrMarker;
    }

    private VanChartAttrMarker getAttrMarker(VanChartDataSeries dataSeries) {
        VanChartAttrMarker attrMarker = dataSeries.getMarker();
        if(attrMarker == null){
            attrMarker = getDefaultAttrMarker();
        }
        return attrMarker;
    }

    //默认的series的bands属性，即坐标轴的最大最小值。为了补足bands属性最大值最小值只设置了一个的问题
    protected void initDataSeriesBandsDefaultMinMaxValue(double min, double max, List<Number> seriesList4TheAxis){
        for(Number seriesIndex : seriesList4TheAxis){
            VanChartDataSeries dataSeries = (VanChartDataSeries)getSeries(seriesIndex.intValue());
            dataSeries.initBandsDefaultMinMaxValue(min, max);
        }
    }

    protected LineMarkerIcon getLegendMarkerIcon(VanChartDataSeries dataSeries, Color[] colors) {
        VanChartLineMarkerIcon itemIcon = new VanChartLineMarkerIcon();

        dealMarkerStyle(itemIcon, dataSeries, colors);

        return itemIcon;
    }

    private void dealMarkerStyle(VanChartLineMarkerIcon itemIcon, VanChartDataSeries dataSeries, Color[] colors) {
        VanChartAttrMarker attrMarker = getAttrMarker(dataSeries);
        Marker marker = MarkerFactory.createMarker(attrMarker.getMarkerType().getType());
        marker.setPlotBackground(Marker.SCATTER_PLOT_BACKROUNG);
        ColorBackground colorBackground = ColorBackground.getInstance(getAttrLineColor(dataSeries, colors));
        if(attrMarker.isCommon()){//图例中的标记点颜色和线一样，都是和系列色一样
            marker.setBackground(colorBackground);
        }
        itemIcon.setLineStyle(Constants.LINE_MEDIUM);//图例的线型默认，不随着系列的宽度改变而改变
        itemIcon.setBackground(colorBackground);
        itemIcon.setMarker(marker);
    }

    /**
     * 布局图形对象
     * @param resolution 分辨率
     */
    public void layoutDataSeriesGlyph(int resolution) {
        super.layoutDataSeriesGlyph(resolution);
        Map<String, List<List<Number>>> axis2SeriesMap = buildAxisMap(false);
        for(String axisName : axis2SeriesMap.keySet()){
            buildSingleAxisLines(axis2SeriesMap.get(axisName), resolution);
        }
    }

    //一个横坐标上的所有系列
    protected void buildSingleAxisLines(List<List<Number>> axisMap, int resolution){
        for(List<Number> allSeriesIndexInBar : axisMap){
            Map<String, Number> prePositiveSumValueInSameCateValue = new HashMap<String, Number>();//存相同cateValue下的已经画的正向柱子的数值和
            Map<String, Number> preNegativeSumValueInSameCateValue = new HashMap<String, Number>();//存相同cateValue下的已经画的负向柱子的数值和
            Point2D[] lastPositivePoints = null;
            Point2D[] lastNegativePoints = null;
            VanChartBaseAxisGlyph XAxisGlyph = null, YAxisGlyph = null;
            for(Number seriesIndex : allSeriesIndexInBar){//堆积在一起的系列
                //这边排序的原因是：横轴为值轴或者时间轴
                VanChartDataSeries dataSeries = (VanChartDataSeries) getSeries(seriesIndex.intValue());
                if(XAxisGlyph == null){
                    XAxisGlyph = getDataSeriesCateAxisGlyph(dataSeries);
                }
                if(YAxisGlyph == null){
                    YAxisGlyph = getDataSeriesValueAxisGlyph(dataSeries);
                }
                if(lastPositivePoints == null){
                    lastPositivePoints = new Point2D[dataSeries.getDataPointCount()];
                }
                if(lastNegativePoints == null){
                    lastNegativePoints = new Point2D[dataSeries.getDataPointCount()];
                }
                //不同系列可能分类不同的
                int[] cateIndexArray = sortCateValue(XAxisGlyph, dataSeries);
                buildSingleLine(dataSeries, XAxisGlyph, YAxisGlyph, lastPositivePoints, lastNegativePoints,
                        prePositiveSumValueInSameCateValue, preNegativeSumValueInSameCateValue, cateIndexArray, resolution);
            }
        }
    }

    protected void buildSingleLine(VanChartDataSeries dataSeries, VanChartBaseAxisGlyph xAxisGlyph, VanChartBaseAxisGlyph yAxisGlyph,
                                   Point2D[] lastPositivePoints, Point2D[] lastNegativePoints,
                                Map<String, Number> prePositiveSumValueInSameCateValue,
                                Map<String, Number> preNegativeSumValueInSameCateValue,int[] cateIndexArray, int resolution) {
        Color seriesColor = getAttrLineColor(dataSeries);//系列色，考虑条件显示和所有系列那边的配色
        GeneralPath linePaths = new GeneralPath(GeneralPath.WIND_NON_ZERO);
        GeneralPath curvePaths = new GeneralPath(GeneralPath.WIND_NON_ZERO);

        //这边clone之后存在另一个对象的原因是：
        //dealLine4AllSeries会改变lastPositivePoints的值，initDataSeriesGlyph这个里面要用改变之前的值
        Point2D[] lastPositivePointsClone = lastPositivePoints.clone();
        Point2D[] lastNegativePointsClone = lastNegativePoints.clone();

        dealLine4AllSeries(dataSeries, xAxisGlyph, yAxisGlyph, prePositiveSumValueInSameCateValue, preNegativeSumValueInSameCateValue,
                lastPositivePoints, lastNegativePoints, seriesColor, linePaths, curvePaths, cateIndexArray, resolution);

        initDataSeriesGlyph(dataSeries, linePaths, curvePaths, seriesColor, yAxisGlyph, cateIndexArray, lastPositivePoints, lastPositivePointsClone, lastNegativePointsClone);
    }

    protected void initDataSeriesGlyph(VanChartDataSeries dataSeries, GeneralPath linePaths, GeneralPath curvePaths, Color seriesColor, VanChartBaseAxisGlyph yAxisGlyph
                                       , int[] cateIndexArray, Point2D[] currentPositivePoints, Point2D[] lastPositivePoints, Point2D[] lastNegativePoints) {
        if (isCurve(dataSeries)) {
            ChartUtils.curveTo(linePaths, curvePaths);
        }
        dealLinePath(linePaths, dataSeries, seriesColor, yAxisGlyph);
    }

    //给横轴排个序
    protected int[] sortCateValue(VanChartBaseAxisGlyph axisGlyph, VanChartDataSeries dataSeries) {
        if(getSeriesSize() < 1){
            return new int[0];
        }
        int cateLen = dataSeries.getDataPointCount();
        int[] cateIndexArray = new int[cateLen];//存放横轴下标，之后排序会打乱
        double[] cateValueArray = new double[cateLen];//存放横轴数值，排序时不会变化
        for(int cateIndex = 0; cateIndex < cateLen; cateIndex++){
            DataPoint dataPoint = dataSeries.getDataPoint(cateIndex);
            double cateValue = getCateValue(axisGlyph, dataPoint, false);
            cateIndexArray[cateIndex] = cateIndex;
            cateValueArray[cateIndex] = cateValue;
        }
        int low = 0;
        int high = cateLen - 1;
        int temp;
        int j;
        while (low < high) {
            for (j = low; j < high; ++j) {
                if(cateValueArray[cateIndexArray[j]] > cateValueArray[cateIndexArray[j+1]]){
                    temp = cateIndexArray[j];
                    cateIndexArray[j] = cateIndexArray[j+1];
                    cateIndexArray[j+1] = temp;
                }
            }
            --high;
            for (j = high; j > low; --j) {
                if(cateValueArray[cateIndexArray[j]] < cateValueArray[cateIndexArray[j-1]]){
                    temp = cateIndexArray[j];
                    cateIndexArray[j] = cateIndexArray[j-1];
                    cateIndexArray[j-1] = temp;
                }
            }
            ++low;
        }
        return cateIndexArray;
    }

    private void dealLinePath(GeneralPath linePaths, VanChartDataSeries dataSeries, Color seriesColor, VanChartBaseAxisGlyph yAxisGlyph) {
        FoldLine foldLine = new FoldLine(linePaths);
        dataSeries.setDrawImpl(foldLine);

        LineStyleInfo info = foldLine.getLineStyleInfo();

        AttrLineStyle attrLineStyle = new AttrLineStyle(getAttrLineWidth(dataSeries));
        info.changeStyleAttrLineStyle(attrLineStyle);
        info.changeStyleAttrColor(new AttrColor(seriesColor));

        List<AttrBand> bansList = dataSeries.getBands();
        for(AttrBand band : bansList){
            double min_y = yAxisGlyph.getPointInBounds(band.getMinEval()).getY();
            double max_y = yAxisGlyph.getPointInBounds(band.getMaxEval()).getY();
            Rectangle2D clipBounds = new Rectangle2D.Double(0, Math.min(min_y, max_y), getBounds().getWidth(), Math.abs(min_y - max_y));
            VanChartBandGlyph bandGlyph = new VanChartBandGlyph(linePaths, clipBounds);
            LineStyleInfo bandInfo = bandGlyph.getLineStyleInfo();
            bandInfo.changeStyleAttrLineStyle(attrLineStyle);
            bandInfo.changeStyleAttrColor(new AttrColor(band.getColor()));
            dataSeries.addBandGlyph(bandGlyph);
        }
    }

    protected void dealLine4AllSeries(VanChartDataSeries dataSeries, VanChartBaseAxisGlyph xAxisGlyph, VanChartBaseAxisGlyph yAxisGlyph,
                                    Map<String, Number> prePositiveSumValueInSameCateValue, Map<String, Number> preNegativeSumValueInSameCateValue,
                                    Point2D[] lastPositivePoints, Point2D[] lastNegativePoints,
                                    Color seriesColor, GeneralPath linePaths, GeneralPath curvePaths, int[] cateIndexArray, int resolution) {
        VanChartBaseAxisGlyph valueAxisGlyph = getDataSeriesValueAxisGlyph(dataSeries);
        boolean isPercentStack = valueAxisGlyph.isPercentage();
        int lineDataCount = calculateLineDataCount(dataSeries);
        double[] xVal = new double[lineDataCount],  yVal = new double[lineDataCount];
        lineDataCount = 0;
        isStartPoint = true; // 是否是起始点, 判断值是否为空, 是否是第一个分类
        boolean isCurve = isCurve(dataSeries), isStep = isStep(dataSeries), isNullValueBreak = isNullValueBreak(dataSeries);
        double defaultY = yAxisGlyph.getPoint2D(0).getY();
        for (int cateIndex : cateIndexArray) {
            VanChartLineDataPoint dataPoint = (VanChartLineDataPoint)dataSeries.getDataPoint(cateIndex);
            double cateValue = getCateValue(xAxisGlyph, dataPoint, true);
            float point_x = (float) xAxisGlyph.getPoint2D(cateValue).getX();
            if(lastNegativePoints[cateIndex] == null){
                lastNegativePoints[cateIndex] = new Point2D.Double(point_x, defaultY);
            }
            if(lastPositivePoints[cateIndex] == null){
                lastPositivePoints[cateIndex] = new Point2D.Double(point_x, defaultY);
            }
            if (dataPoint.isValueIsNull()) {
                if (isNullValueBreak) {
                    isStartPoint = true;  //空值断开
                }
                continue;
            }
            double value = isPercentStack ? dataPoint.getPercentValue() : dataPoint.getValue();
            if(this.isDataPointXNotInPlotBounds(point_x)) {
                dataPoint.setDataLabel(null);
                continue;
            }
            double sumValue = getSumValue(cateValue, value, prePositiveSumValueInSameCateValue, preNegativeSumValueInSameCateValue);
            float point_y = (float)yAxisGlyph.getBounds().getY() + (float) yAxisGlyph.getPoint2D(sumValue).getY();
            xVal[lineDataCount] = point_x;// 记录拟合数据
            yVal[lineDataCount] = point_y;
            if(value < 0){
                lastNegativePoints[cateIndex] = new Point2D.Double(point_x, point_y);
            } else {
                lastPositivePoints[cateIndex] = new Point2D.Double(point_x, point_y);
            }
            float last_point_y = (float)(isStartPoint ? 0 : yVal[lineDataCount - 1]);//垂直折线要用到
            lineDataCount++;
            dealLinePoint4EveryDataPoint(linePaths, curvePaths, point_x, point_y, isCurve, isStep, last_point_y);
            initMarkerGlyph(dataPoint, point_x, point_y, seriesColor);
            dealDataPointLabel(dataPoint, resolution);
        }
        trendLineFitting(xVal, yVal, dataSeries);// 拟合计算
    }

    protected double getSumValue(double cateValue, double value, Map<String, Number> prePositiveSumValueInSameCateValue,
                               Map<String, Number> preNegativeSumValueInSameCateValue){
        double sumValue;
        if(value > 0){
            Number sum = prePositiveSumValueInSameCateValue.get(String.valueOf(cateValue));
            sumValue = value + (sum == null ? 0 : sum.doubleValue());
            prePositiveSumValueInSameCateValue.put(String.valueOf(cateValue), sumValue);
        } else {
            Number sum = preNegativeSumValueInSameCateValue.get(String.valueOf(cateValue));
            sumValue = value + (sum == null ? 0 : sum.doubleValue());
            preNegativeSumValueInSameCateValue.put(String.valueOf(cateValue), sumValue);
        }
        return sumValue;
    }

    protected void dealLinePoint4EveryDataPoint(GeneralPath linePaths, GeneralPath curvePaths, float point_x, float point_y,
                                              boolean isSeriesCurve, boolean isSeriesStep, float last_point_y) {
        if (isSeriesCurve) {//光滑曲线
            if (isStartPoint) {
                ChartUtils.curveTo(linePaths, curvePaths);
                curvePaths.reset();
                curvePaths.moveTo(point_x, point_y);
                linePaths.moveTo(point_x, point_y);
                isStartPoint = false;
            } else {
                curvePaths.lineTo(point_x, point_y);
            }
        } else if(isSeriesStep){//垂直折线
            if (this.isStartPoint) {
                linePaths.moveTo(point_x, point_y);
                this.isStartPoint = false;
            } else {
                linePaths.lineTo(point_x, last_point_y);
                linePaths.lineTo(point_x, point_y);
            }
        } else {//普通折线
            if (isStartPoint) {
                linePaths.moveTo(point_x, point_y);
                isStartPoint = false;
            } else {
                linePaths.lineTo(point_x, point_y);
            }
        }
    }

    protected void initMarkerGlyph(VanChartLineDataPoint dataPoint, float point_x, float point_y, Color seriesColor) {
        initMarkerGlyph(new MarkerGlyph(), dataPoint, point_x, point_y, seriesColor);
    }

    protected void initMarkerGlyph(MarkerGlyph markerGlyph, VanChartLineDataPoint dataPoint, float point_x, float point_y, Color seriesColor) {
        VanChartAttrMarker attrMarker = getAttrMarker(dataPoint);
        Marker marker = attrMarker.getMarkerType().getMarker();
        if(attrMarker.isCommon()){
            if(attrMarker.getColorBackground() != null){//先找给这个标记点设置的颜色
                marker.setBackground(attrMarker.getColorBackground());
            } else {//再找该系列的颜色
                marker.setBackground(ColorBackground.getInstance(seriesColor));
            }
            double radius = attrMarker.getRadius();
            marker.setSize(radius);
            markerGlyph.setShape(new Rectangle2D.Double(point_x - radius, point_y - radius, radius * 2, radius * 2));
        } else if(attrMarker.getImageBackground() != null){
            double width = attrMarker.getWidth();
            double height = attrMarker.getHeight();
            marker = new CustomImageMarker(width, height);
            marker.setBackground(attrMarker.getImageBackground());
            markerGlyph.setShape(new Rectangle2D.Double(point_x - width/2, point_y - height/2, width, height));
        }
        marker.setPlotBackground(getMarkerBackground());
        markerGlyph.setMarker(marker);
        dataPoint.setDrawImpl(markerGlyph);
    }

    private Background getMarkerBackground() {
        if(this.getBackground() instanceof ColorBackground){//绘图区有颜色背景
            return this.getBackground();
        } else if(this.getBackground() == null && getWholeChartBackground() instanceof ColorBackground){//绘图区没有背景且图表区有颜色背景
            return this.getWholeChartBackground();
        } else {
            return null;
        }
    }

    public String getChartType(){
        return "line";
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

        VanChartAttrLine attrLine = getDefaultAttrLine();
        VanChartAttrMarker attrMarker = getDefaultAttrMarker();
        if(attrLine != null){
            attrLine.addToJSONObject(js);
        }

        if(attrMarker != null){
            js.put("marker", attrMarker.toJSONObject(repo));
        }

        return js;
    }
}
