package com.fr.plugin.chart.glyph;

import com.fr.base.FRContext;
import com.fr.base.Utils;
import com.fr.chart.base.ChartBaseUtils;
import com.fr.chart.base.ChartConstants;
import com.fr.chart.base.ChartEquationType;
import com.fr.chart.base.LineStyleInfo;
import com.fr.chart.chartglyph.*;
import com.fr.general.ComparatorUtils;
import com.fr.json.JSONArray;
import com.fr.json.JSONException;
import com.fr.json.JSONObject;
import com.fr.plugin.chart.VanChartDateUtils;
import com.fr.plugin.chart.attr.axis.AxisType;
import com.fr.plugin.chart.base.AttrLabel;
import com.fr.plugin.chart.base.AttrSeriesStackAndAxis;
import com.fr.plugin.chart.base.VanChartAttrTrendLine;
import com.fr.plugin.chart.base.VanChartConstants;
import com.fr.plugin.chart.glyph.axis.VanChartBaseAxisGlyph;
import com.fr.plugin.chart.glyph.axis.VanChartCategoryAxisGlyph;
import com.fr.plugin.chart.glyph.axis.VanChartTimeAxisGlyph;
import com.fr.stable.web.Repository;

import java.awt.*;
import java.awt.geom.Dimension2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Rectangle2D;
import java.util.*;
import java.util.List;

/**
 * 矩形绘图区图表
 */
public abstract class VanChartRectanglePlotGlyph extends VanChartPlotGlyph implements VanChartAxisPlotGlyphInterface, VanChartDataSheetPlotGlyphInterface{
    private final static double GAP = 4;
    private static final long serialVersionUID = -8454856294551338692L;

    protected List<VanChartBaseAxisGlyph> xAxisGlyphList = new ArrayList<VanChartBaseAxisGlyph>();
    protected List<VanChartBaseAxisGlyph> yAxisGlyphList = new ArrayList<VanChartBaseAxisGlyph>();

    //以下属性是存在condition里面的，专门拿出来是避免多个地方使用，循环遍历多次
    private VanChartAttrTrendLine defaultAttrTrendLine;

    public List<VanChartBaseAxisGlyph> getYAxisGlyphList() {
        return yAxisGlyphList;
    }

    public List<VanChartBaseAxisGlyph> getXAxisGlyphList() {
        return xAxisGlyphList;
    }

    private VanChartAttrTrendLine getDefaultAttrTrendLine() {
        if(defaultAttrTrendLine == null){
            ConditionAttr attrList = getConditionCollection().getDefaultAttr();
            defaultAttrTrendLine = (VanChartAttrTrendLine)attrList.getExisted(VanChartAttrTrendLine.class);
        }
        return defaultAttrTrendLine;
    }

    protected VanChartAttrTrendLine getAttrTrendLine(VanChartDataSeries dataSeries) {
        VanChartAttrTrendLine attrTrendLine = dataSeries.getAttrTrendLine();
        if(attrTrendLine == null){
            attrTrendLine = getDefaultAttrTrendLine();
        }
        return attrTrendLine;
    }

    public void addXAxisGlyph(VanChartBaseAxisGlyph axisGlyph) {
        xAxisGlyphList.add(axisGlyph);
    }

    public void addYAxisGlyph(VanChartBaseAxisGlyph axisGlyph) {
        yAxisGlyphList.add(axisGlyph);
    }

    public int getBottomXAxisCount(){
        int i = 0;
        for(VanChartBaseAxisGlyph axisGlyph : xAxisGlyphList){
            if(axisGlyph != null && axisGlyph.getPosition() == VanChartConstants.AXIS_BOTTOM){
                i++;
            }
        }
        return i;
    }

    /**
     * 根据数据表和图例元素集合生成数据表Glyph，默认返回null
     * DataSheet显示的时候, 分类轴标签 不显示, 刻度线不显示  标题不显示.
     * 创建的时候只初始配置的属性，图例和x轴样式等布局的时候给
     * @param dataSheet 数据表
     * @param legendItems 图例集合(这边是null)，布局的时候再初始话items
     * @return 数据表Glyph
     * 此方法先过滤掉了部分图表 不能生成DataSheet.
     */
    public DataSheetGlyph createDataSheetGlyph(DataSheet dataSheet, LegendItem[] legendItems) {
        AxisGlyph cateAxisGlyph = this.getXAxisGlyphList().get(0);
        if (cateAxisGlyph != null) {
            cateAxisGlyph.notShowAllAttr();
        }

        return new VanChartDataSheetGlyph(dataSheet.getFont(), dataSheet.getFormat(), dataSheet);
    }

    /**
     * 布局数据表的时候，坐标轴左侧对绘图区的裁剪部分不够数据表使用，需要对绘图区左侧继续裁剪,
     * 因为是相对位置，所以只需要右侧的y坐标轴往左移就好了，x坐标轴需要计算unitLength等。
     * @param leftGap 绘图区左侧还需要裁剪leftGap
     */
    public void adjustAxisGlyphWithLeftGap(double leftGap){
        for(VanChartBaseAxisGlyph axisGlyph : xAxisGlyphList){
            if(axisGlyph != null){
                Rectangle2D temp = axisGlyph.getBounds();
                axisGlyph.calculateAxisGlyph(getBounds());
                axisGlyph.setBounds(new Rectangle2D.Double(
                        temp.getX(),
                        temp.getY(),
                        temp.getWidth() - leftGap,
                        temp.getHeight()
                ));
            }
        }
        for(VanChartBaseAxisGlyph axisGlyph : yAxisGlyphList){
            if(axisGlyph != null && axisGlyph.getPosition() == VanChartConstants.AXIS_RIGHT){
                Rectangle2D temp = axisGlyph.getBounds();
                axisGlyph.setBounds(new Rectangle2D.Double(
                        temp.getX() - leftGap,
                        temp.getY(),
                        temp.getWidth(),
                        temp.getHeight()
                ));
            }
        }
    }

    /**
     * 布局数据表的时候，需要对绘图区下方继续裁剪，
     * 下方x坐标轴也要进行平移等计算，y坐标轴需要计算unitLength等。
     * @param bottomGap 绘图区下方还需要裁剪rightGap
     */
    public void adjustAxisGlyphWithBottomGap(double bottomGap){
        for(VanChartBaseAxisGlyph axisGlyph : xAxisGlyphList){
            if(axisGlyph != null && axisGlyph.getPosition() == VanChartConstants.AXIS_BOTTOM){
                Rectangle2D temp = axisGlyph.getBounds();
                axisGlyph.finallyUpdateAxisGridLength(getBounds().getHeight());
                axisGlyph.setBounds(new Rectangle2D.Double(
                        temp.getX(),
                        temp.getY() - bottomGap,
                        temp.getWidth(),
                        temp.getHeight() - bottomGap
                ));
            }
        }
        for(VanChartBaseAxisGlyph axisGlyph : yAxisGlyphList){
            if(axisGlyph != null){
                Rectangle2D temp = axisGlyph.getBounds();
                axisGlyph.calculateAxisGlyph(getBounds());
                axisGlyph.setBounds(new Rectangle2D.Double(
                        temp.getX(),
                        temp.getY(),
                        temp.getWidth(),
                        temp.getHeight() - bottomGap
                ));
            }
        }

        dealOnZeroAxisGlyphLocation();
    }


    /**
     *  对绘图区中的坐标轴部分进行布局,传过去图表边界计算限制区域
     * @param chartOriginalBounds 原始图表边界
     * @param resolution 分辨率
     */
    public void layoutAxisGlyph(Rectangle2D chartOriginalBounds, int resolution){
        //绘图区在裁剪之前的边界，保存下，裁剪过程中会用到。
        Rectangle2D plotZeroBounds = this.getBounds();
        //根据坐标轴对此plotBounds剪切，然后再赋值给plotGlyph
        Rectangle2D leftPlotBounds = this.getBounds();

        //从最后一个横轴坐标轴开始布局,对坐标轴依次裁剪
        for(int i = xAxisGlyphList.size() - 1; i >= 0; i--){
            VanChartBaseAxisGlyph axisGlyph = xAxisGlyphList.get(i);
            initXAxisGlyphMinMaxValue(i, axisGlyph);//初始化最大最小值
            initXAxisLabelDrawPosition(axisGlyph);//初始化标签是否画在两刻度线中间
            initAxisGlyphBoundsAndZeroBoundsAndOriginalChartBounds(chartOriginalBounds, axisGlyph, plotZeroBounds);
            dealPlotBoundsWithAxisGlyph(axisGlyph, leftPlotBounds, resolution);
            dealPlotBoundsWidthAxisInHorizontal(axisGlyph, leftPlotBounds);
            initAxisGlyphStartPoint(axisGlyph, leftPlotBounds);
        }

        for(int i = yAxisGlyphList.size() - 1; i >= 0; i--){
            VanChartBaseAxisGlyph axisGlyph = yAxisGlyphList.get(i);
            initYAxisGlyphMinMaxValue(i, axisGlyph);
            initYAxisLabelDrawPosition(axisGlyph);
            initAxisGlyphBoundsAndZeroBoundsAndOriginalChartBounds(chartOriginalBounds, axisGlyph, plotZeroBounds);
            dealPlotBoundsWithAxisGlyph(axisGlyph, leftPlotBounds, resolution);
            dealPlotBoundsWidthAxisInHorizontal(axisGlyph, leftPlotBounds);
            initAxisGlyphStartPoint(axisGlyph, leftPlotBounds);
        }

        //全部裁剪完之后的plotBounds再对坐标轴所有和长度, 位置有关的信息处理
        for(VanChartBaseAxisGlyph axisGlyph : xAxisGlyphList){
            calculateAxisGlyphAttr(axisGlyph, leftPlotBounds);
        }
        for(VanChartBaseAxisGlyph axisGlyph : yAxisGlyphList){
            calculateAxisGlyphAttr(axisGlyph, leftPlotBounds);
        }

        //坐标轴长度等布局好后再处理0值对齐的坐标轴的起始位置
        dealOnZeroAxisGlyphLocation();

        setBounds(leftPlotBounds);
    }

    //坐标轴长度等布局好后再处理0值对齐的坐标轴的起始位置
    private void dealOnZeroAxisGlyphLocation() {
        double defaultXAxisZeroPoint = getDefaultXAxisGlyph().getPoint2D(0).getX();
        double defaultYAxisZeroPoint = getDefaultYAxisGlyph().getPoint2D(0).getY();
        for(VanChartBaseAxisGlyph axisGlyph : xAxisGlyphList){
            dealOnZeroAxisGlyphLocation(axisGlyph, defaultYAxisZeroPoint);
        }
        for(VanChartBaseAxisGlyph axisGlyph : yAxisGlyphList){
            dealOnZeroAxisGlyphLocation(axisGlyph, defaultXAxisZeroPoint);
        }
    }

    protected void initAxisGlyphBoundsAndZeroBoundsAndOriginalChartBounds(Rectangle2D chartOriginalBounds, VanChartBaseAxisGlyph axisGlyph, Rectangle2D zeroBounds) {
        if (axisGlyph != null) {
            axisGlyph.setChartBounds(chartOriginalBounds);
            axisGlyph.setBounds(zeroBounds);
            axisGlyph.setPlotZeroBounds(zeroBounds);
        }
    }

    protected void dealPlotBoundsWithAxisGlyph(VanChartBaseAxisGlyph axisGlyph, Rectangle2D plotLeftBounds, int resolution) {
        if (axisGlyph != null) {
            axisGlyph.dealPlotBoundsWithAxisLabel(plotLeftBounds, resolution);
        }
    }

    protected void dealPlotBoundsWidthAxisInHorizontal(VanChartBaseAxisGlyph axisGlyph, Rectangle2D lastPlotBounds) {
        if (axisGlyph != null) {
            axisGlyph.dealPlotBoundsWithLabelInHorizontal(lastPlotBounds);
        }
    }

    protected void calculateAxisGlyphAttr(VanChartBaseAxisGlyph axisGlyph, Rectangle2D endPlotBounds) {
        if (axisGlyph != null) {
            axisGlyph.calculateAxisGlyph(endPlotBounds);
        }
    }

    protected void initAxisGlyphStartPoint(VanChartBaseAxisGlyph axisGlyph, Rectangle2D endPlotBounds) {
        if(axisGlyph != null) {
            axisGlyph.initAxisGlyphStartPoint(endPlotBounds);
        }
    }

    protected void dealOnZeroAxisGlyphLocation(VanChartBaseAxisGlyph axisGlyph, double verticalZeroPoint) {
        if(axisGlyph != null) {
            axisGlyph.dealOnZeroAxisGlyphLocation(verticalZeroPoint);
        }
    }

    protected void initXAxisLabelDrawPosition(VanChartBaseAxisGlyph axisGlyph) {
        setAxisLabelDrawBetween(axisGlyph);
    }

    protected void initYAxisLabelDrawPosition(VanChartBaseAxisGlyph axisGlyph) {
    }

    protected void setAxisLabelDrawBetween(VanChartBaseAxisGlyph axisGlyph) {
        axisGlyph.setDrawBetweenTick(ComparatorUtils.equals(axisGlyph.getVanAxisType(), AxisType.AXIS_CATEGORY));
    }

    protected void initXAxisGlyphMinMaxValue(int axisIndex, VanChartBaseAxisGlyph axisGlyph) {
        if(ComparatorUtils.equals(axisGlyph.getVanAxisType(), AxisType.AXIS_VALUE)){
            //数值轴初始最大最小值
            initCateAxisGlyphMinMaxValue(axisIndex, axisGlyph, false);
        } else if(ComparatorUtils.equals(axisGlyph.getVanAxisType(), AxisType.AXIS_TIME)){
            initCateAxisGlyphMinMaxValue(axisIndex, axisGlyph, true);
        }
    }

    protected void initYAxisGlyphMinMaxValue(int axisIndex, VanChartBaseAxisGlyph axisGlyph) {
        initValueAxisGlyphMinMaxValue(axisIndex, axisGlyph);
    }

    //不用考虑堆积
    private void initCateAxisGlyphMinMaxValue(int axisIndex, VanChartBaseAxisGlyph axisGlyph, boolean isTime){
        if(axisGlyph.isPercentage()){
            axisGlyph.initMinMaxValue(0, 1);
            return;
        }
        double[] minMaxCateValue = {isTime ? Double.MAX_VALUE : 0, -Double.MAX_VALUE};

        boolean hasData = false;
        int seriesSize = getSeriesSize();
        for(int i = 0; i < seriesSize; i++){
            VanChartDataSeries dataSeries = (VanChartDataSeries)getSeries(i);
            AttrSeriesStackAndAxis attrSeriesStackAndAxis = dataSeries.getAttrSeriesStackAndAxis();
            int seriesAxisIndex = attrSeriesStackAndAxis == null ? 0 : attrSeriesStackAndAxis.getXAxisIndex();
            if(axisIndex == seriesAxisIndex){
                hasData = true;
                minMaxCateValue = isTime ? getSeriesMinMaxCateDateValue(dataSeries, minMaxCateValue) : getSeriesMinMaxCateValue(dataSeries, minMaxCateValue);
            }
        }

        if(!hasData && !isTime){
            minMaxCateValue[0] = 0;
            minMaxCateValue[1] = 1;
        }

        axisGlyph.initMinMaxValue(minMaxCateValue[0], minMaxCateValue[1], isAdjustXAxisMinMaxValue());
    }

    /**
     * 柱形图的横轴是数值或者日期时，自动计算坐标轴最大值最小值之后， 最小值减一个mainUnit，最大值加，以放下一根柱子
     * @return 是否调整自动计算后的最大值最小值
     */
    protected boolean isAdjustXAxisMinMaxValue(){
        return false;
    }

    public double[] getSeriesMinMaxCateValue(DataSeries dataSeries, double[] minMaxCateValue){
        for(int i = 0, len = dataSeries.getDataPointCount(); i < len; i++){
            DataPoint dataPoint = dataSeries.getDataPoint(i);
            Number number = Utils.string2Number(dataPoint.getCategoryName());
            if(number != null){
                double xValue = number.doubleValue();
                minMaxCateValue[0] = Math.min(minMaxCateValue[0], xValue);
                minMaxCateValue[1] = Math.max(minMaxCateValue[1], xValue);
            }
        }
        return minMaxCateValue;
    }

    private double[] getSeriesMinMaxCateDateValue(DataSeries dataSeries, double[] minMaxCateValue) {
        for(int i = 0, len = dataSeries.getDataPointCount(); i < len; i++){
            DataPoint dataPoint = dataSeries.getDataPoint(i);
            Date date = VanChartDateUtils.object2Date(dataPoint.getCategoryName(), true);
            if(date != null){
                double dateInt = ChartBaseUtils.date2Int(date, ChartConstants.SECOND_TYPE);
                minMaxCateValue[0] = Math.min(minMaxCateValue[0], dateInt);
                minMaxCateValue[1] = Math.max(minMaxCateValue[1], dateInt);
            }
        }
        return minMaxCateValue;
    }

    /**
     * 数值轴初始最大最小值（考虑堆积）
     * @param axisIndex 坐标轴下标
     * @param axisGlyph 坐标轴图形
     */
    private void initValueAxisGlyphMinMaxValue(int axisIndex, VanChartBaseAxisGlyph axisGlyph){
        if(axisGlyph.isPercentage()){
            axisGlyph.initMinMaxValue(0, 1);
            return;
        }
        double minValue = Double.MAX_VALUE, maxValue = -minValue;
        Map<String, Map<String, Number>> dataMap = new HashMap<String, Map<String, Number>>();

        int seriesSize = getSeriesSize();
        List<Number> seriesList4TheAxis = new ArrayList<Number>();
        for(int seriesIndex = 0; seriesIndex < seriesSize; seriesIndex++){
            VanChartDataSeries dataSeries = (VanChartDataSeries)getSeries(seriesIndex);
            AttrSeriesStackAndAxis attrSeriesStackAndAxis = dataSeries.getAttrSeriesStackAndAxis();
            int valueIndex = getValueAxisIndex(attrSeriesStackAndAxis);
            VanChartBaseAxisGlyph cateAxis = getCateAxis(getCateAxisIndex(attrSeriesStackAndAxis));
            if(axisIndex == valueIndex){
                getSeriesValue(dataMap, dataSeries, attrSeriesStackAndAxis, cateAxis);
                seriesList4TheAxis.add(seriesIndex);
            }
        }

        for(String tempKey : dataMap.keySet()){
            Map<String, Number> data = dataMap.get(tempKey);
            for(String cateName : data.keySet()){
                double value = data.get(cateName).doubleValue();
                minValue = Math.min(value, minValue);
                maxValue = Math.max(value, maxValue);
            }
        }

        axisGlyph.initMinMaxValue(minValue, maxValue);
        initDataSeriesBandsDefaultMinMaxValue(axisGlyph.getMinValue(), axisGlyph.getMaxValue(), seriesList4TheAxis);
    }

    //堆积在哪条值轴上
    protected int getValueAxisIndex(AttrSeriesStackAndAxis attrSeriesStackAndAxis){
        return attrSeriesStackAndAxis == null ? 0 : attrSeriesStackAndAxis.getYAxisIndex();
    }

    //堆积在哪条分类轴上面
    protected int getCateAxisIndex(AttrSeriesStackAndAxis attrSeriesStackAndAxis){
        return attrSeriesStackAndAxis == null ? 0 :attrSeriesStackAndAxis.getXAxisIndex();
    }

    protected VanChartBaseAxisGlyph getCateAxis(int axisIndex){
        return xAxisGlyphList.get(axisIndex);
    }

    public void getSeriesValue( Map<String, Map<String, Number>> dataMap, VanChartDataSeries dataSeries,
                                AttrSeriesStackAndAxis attrSeriesStackAndAxis, VanChartBaseAxisGlyph cateAxisGlyph) {
        boolean isTime = ComparatorUtils.equals(cateAxisGlyph.getVanAxisType(), AxisType.AXIS_TIME);
        String seriesName = dataSeries.getSeriesName();
        if(attrSeriesStackAndAxis == null || !attrSeriesStackAndAxis.isStacked()){
            if(dataMap.get(seriesName) == null){
                dataMap.put(seriesName, new HashMap<String, Number>());
            }
            Map<String, Number> dataList = dataMap.get(seriesName);
            for(int i = 0, size = dataSeries.getDataPointCount(); i < size; i++){
                dataList.put(dataSeries.getDataPoint(i).getCategoryName(), dataSeries.getDataPoint(i).getValue());
            }
        } else {
            String stackPositiveKey = attrSeriesStackAndAxis.getStackID() + "POSITIVE";
            String stackNegativeKey = attrSeriesStackAndAxis.getStackID() + "NEGATIVE";
            if(dataMap.get(stackPositiveKey) == null){
                dataMap.put(stackPositiveKey, new HashMap<String, Number>());
            }
            if(dataMap.get(stackNegativeKey) == null){
                dataMap.put(stackNegativeKey, new HashMap<String, Number>());
            }
            Map<String, Number> positiveList = dataMap.get(stackPositiveKey);
            Map<String, Number> negativeList = dataMap.get(stackNegativeKey);
            for(int i = 0, size = dataSeries.getDataPointCount(); i < size; i++){
                double value = dataSeries.getDataPoint(i).getValue();
                String cateName = dataSeries.getDataPoint(i).getCategoryName();
                if(isTime){
                    Date date = VanChartDateUtils.object2Date(cateName, true);
                    if(date != null){
                        double intDate = ChartBaseUtils.date2Int(date, ((VanChartTimeAxisGlyph) cateAxisGlyph).getMainType().getTimeType());
                        if(value > 0){
                            Number number = positiveList.get(String.valueOf(intDate));
                            positiveList.put(String.valueOf(intDate), dataSeries.getDataPoint(i).getValue() + (number == null ? 0 :number.doubleValue()));
                        } else {
                            Number number = negativeList.get(String.valueOf(intDate));
                            negativeList.put(String.valueOf(intDate), dataSeries.getDataPoint(i).getValue() + (number == null ? 0 :number.doubleValue()));
                        }
                        continue;
                    }
                }
                //非时间型和转化时间失败都会走到这儿
                if(value > 0){
                    Number number = positiveList.get(cateName);
                    positiveList.put(cateName, dataSeries.getDataPoint(i).getValue() + (number == null ? 0 :number.doubleValue()));
                } else {
                    Number number = negativeList.get(cateName);
                    negativeList.put(cateName, dataSeries.getDataPoint(i).getValue() + (number == null ? 0 :number.doubleValue()));
                }
            }
        }
    }

    //默认的series的bands属性，即坐标轴的最大最小值。为了补足bands属性最大值最小值只设置了一个的问题。折线图和面积图画图的时候用到。
    protected void initDataSeriesBandsDefaultMinMaxValue(double min, double max, List<Number> seriesList4TheAxis){
    }

    //所有不堆积的放在一起，主要是算百分比
    //改正：最后一个可能是所有不堆积的。例外情况：堆积柱形图、百分比堆积柱形图、自定义柱形图所有系列分别堆积等
    protected Map<String,  List<List<Number>>> buildAxisMap( boolean combineUnStack){
        Map<String, List<Number>> tempMap = new HashMap<String, List<Number>>();
        //下面这个循环将柱子坐在哪个坐标轴上归了下类
        for(int seriesIndex = 0, seriesSize = getSeriesSize(); seriesIndex < seriesSize; seriesIndex++){
            VanChartDataSeries dataSeries = (VanChartDataSeries)getSeries(seriesIndex);
            String axisName = getDataSeriesCateAxisGlyph(dataSeries).getVanAxisName();

            List<Number> seriesIndexList = tempMap.get(axisName);
            if(seriesIndexList == null){
                seriesIndexList = new ArrayList<Number>();
                tempMap.put(axisName, seriesIndexList);
            }
            seriesIndexList.add(seriesIndex);
        }

        return buildLocation2SeriesMap(tempMap, combineUnStack);
    }

    private Map<String,  List<List<Number>>> buildLocation2SeriesMap(Map<String, List<Number>> tempMap, boolean combineUnStack){
        //按坐标轴归类之后，每个坐标轴中再根据堆积值归类
        Map<String, List<List<Number>>> location2SeriesMap = new HashMap<String, List<List<Number>>>();//<position, { barIndex1:{S1,S2},barIndex2:{S3} }>

        for(String axisName : tempMap.keySet()){
            Map<String, Number> stackMap = new HashMap<String, Number>();//堆积的系列为一根柱子，stackID或seriesName，柱子下标
            int barIndex = 0;
            List<Number> seriesList = tempMap.get(axisName);
            List<List<Number>> locationMap = new ArrayList<List<Number>>();
            if(seriesList == null || seriesList.size() < 1){
                continue;
            }
            for(int index = 0 ,len = seriesList.size(); index < len; index++){
                VanChartDataSeries dataSeries = (VanChartDataSeries)getSeries(seriesList.get(index).intValue());
                String key = combineUnStack ? "combineUnStack" : dataSeries.getSeriesName();//把不堆积的放到一个里面，为了方便算百分比
                AttrSeriesStackAndAxis attrSeriesStackAndAxis = dataSeries.getAttrSeriesStackAndAxis();
                if(attrSeriesStackAndAxis != null && attrSeriesStackAndAxis.isStacked()){
                    key = attrSeriesStackAndAxis.getStackID();
                }

                if(stackMap.get(key) != null){
                    locationMap.get(stackMap.get(key).intValue()).add(seriesList.get(index));
                } else {
                    stackMap.put(key, barIndex);
                    List<Number> list = new ArrayList<Number>();
                    list.add(seriesList.get(index));
                    locationMap.add(barIndex, list);
                    barIndex++;
                }
            }
            location2SeriesMap.put(axisName, locationMap);
        }

        return location2SeriesMap;
    }

    //获取某个系列所在的值轴
    protected VanChartBaseAxisGlyph getDataSeriesValueAxisGlyph(VanChartDataSeries dataSeries){
        return yAxisGlyphList.get(getYAxisGlyphIndex(dataSeries.getAttrSeriesStackAndAxis()));
    }

    //获取某个系列所在的分类轴
    protected VanChartBaseAxisGlyph getDataSeriesCateAxisGlyph(VanChartDataSeries dataSeries){
        return xAxisGlyphList.get(getXAxisGlyphIndex(dataSeries.getAttrSeriesStackAndAxis()));
    }

    //获取x轴下标
    protected int getXAxisGlyphIndex(AttrSeriesStackAndAxis attrSeriesStackAndAxis){
        return attrSeriesStackAndAxis == null ? 0 : attrSeriesStackAndAxis.getXAxisIndex();
    }

    //获取y轴下标
    protected int getYAxisGlyphIndex(AttrSeriesStackAndAxis attrSeriesStackAndAxis){
        return attrSeriesStackAndAxis == null ? 0 : attrSeriesStackAndAxis.getYAxisIndex();
    }

    public VanChartBaseAxisGlyph getDefaultXAxisGlyph() {
        return xAxisGlyphList.get(0);
    }

    public VanChartBaseAxisGlyph getDefaultYAxisGlyph() {
        return yAxisGlyphList.get(0);
    }

    /**
     * 处理数据点的标签的边框
     *
     * @param dataPoint  数据点
     * @param resolution 分辨率
     */
    protected void dealDataPointLabel(VanChartDataPoint dataPoint, int resolution) {
        TextGlyph labelGlyph = dataPoint.getDataLabel();
        if(labelGlyph == null) {
            return;
        }

        if (notDealDataPointLabel(dataPoint, labelGlyph)) {
            labelGlyph.setBounds(null);
        } else {
            Dimension2D labelPreDim = labelGlyph.preferredDimension(resolution);
            Rectangle2D dataPointRect = dataPoint.getShape().getBounds2D();
            AttrLabel attrLabel = getAttrLabel(dataPoint);
            int position = attrLabel.getPosition();
            Rectangle2D bounds = getDataPointLabelBoundsWithPosition(labelPreDim, dataPointRect, position, dataPoint);
            labelGlyph.setBounds(bounds);
        }
    }


    //没有设置标签位置时，折线
    protected Rectangle2D getDataPointLabelBoundsWithPosition(Dimension2D labelPreDim, Rectangle2D dataPointRect, int position, DataPoint dataPoint){
        double labelX = dataPointRect.getX() + (dataPointRect.getWidth() - labelPreDim.getWidth()) / 2.0;
        double labelY = dataPointRect.getY() - labelPreDim.getHeight() - GAP;
        return new Rectangle2D.Double(labelX, labelY, labelPreDim.getWidth(), labelPreDim.getHeight());
    }

    /**
     * 计算数据点的占比
     */
    public void calculateDataPointPercentValue(){
        if(getSeriesSize() < 1){
            return;
        }
        int pointSize = getSeries(0).getDataPointCount();
        Map<String,  List<List<Number>>> axis2SeriesList = buildAxisMap(true);
        for(String axisName : axis2SeriesList.keySet()){
            List<List<Number>> barList = axis2SeriesList.get(axisName);

            VanChartBaseAxisGlyph axisGlyph = getAxisGlyphFromAxisName(axisName);
            AxisType axisType = axisGlyph.getVanAxisType();
            if(ComparatorUtils.equals(axisType, AxisType.AXIS_CATEGORY)){
                for (int j = 0; j < pointSize; j++) {
                    for(List<Number> seriesList : barList){
                        double total = 0;
                        for(Number seriesIndex : seriesList){
                            total += Math.abs(getSeries(seriesIndex.intValue()).getDataPoint(j).getValue());
                        }
                        for(Number seriesIndex : seriesList){
                            double value = Math.abs(this.getSeries(seriesIndex.intValue()).getDataPoint(j).getValue());
                            if (total != 0) {
                                this.getSeries(seriesIndex.intValue()).getDataPoint(j).setPercentValue(value / total);
                            }
                        }
                    }
                }
            } else {
                boolean isTime = ComparatorUtils.equals(axisType, AxisType.AXIS_TIME);
                calculateDoubleValueDataPointPercentValue(barList, isTime, axisGlyph);
            }
        }
    }

    private void calculateDoubleValueDataPointPercentValue(List<List<Number>> barList, boolean isTime, VanChartBaseAxisGlyph axisGlyph) {
        int pointSize = getSeries(0).getDataPointCount();
        for(int barIndex = 0, barLen = barList.size(); barIndex < barLen; barIndex++){
            List<Number> seriesList = barList.get(barIndex);

            boolean isStack;
            VanChartDataSeries dataSeries = (VanChartDataSeries)getSeries(seriesList.get(0).intValue());//一根柱子上堆积情况一样，第一个代表
            if(dataSeries.getAttrSeriesStackAndAxis() == null){
                isStack = false;
            } else {
                isStack = dataSeries.getAttrSeriesStackAndAxis().isStacked();
            }
            if((barIndex == barLen - 1 && !isStack) || seriesList.size() == 1){//最后一个bar放的是所有不堆积的,百分比为1。或者一根柱子上只有一个系列的
                //改正：最后一个可能是所有不堆积的。例外情况：堆积柱形图、百分比堆积柱形图、自定义柱形图所有系列分别堆积等
                setDoubleValueDataPointDefaultPercentValue(seriesList, pointSize);
                continue;
            }

            Map<String, List<Number>> stackOnSameCateNameList = new HashMap<String, List<Number>>();//具有相同cateName的系列下标放在一起

            //先将堆积在一根柱子上的所有系列的point遍历一遍，具有相同cateName的系列下标放在一起
            for(Number seriesIndex : seriesList){
                for(int cateIndex = 0; cateIndex < pointSize; cateIndex++){
                    String cateName = getSeries(seriesIndex.intValue()).getDataPoint(cateIndex).getCategoryName();
                    double doubleCate;
                    if(isTime){
                        Date date = VanChartDateUtils.object2Date(cateName, true);
                        if(date == null){
                            continue;
                        }
                        doubleCate = ChartBaseUtils.date2Int(date, ((VanChartTimeAxisGlyph) axisGlyph).getMainType().getTimeType());
                    } else {
                        Number number = Utils.objectToNumber(cateName, true);
                        if(number == null){
                            continue;
                        }
                        doubleCate = number.doubleValue();
                    }
                    List<Number> series = stackOnSameCateNameList.get(String.valueOf(doubleCate));
                    if(series == null){
                        series = new ArrayList<Number>();
                        stackOnSameCateNameList.put(String.valueOf(doubleCate), series);
                    }
                    series.add(seriesIndex);
                }
            }
            //把设置成堆积且确实堆积到了一起的点求和后算平均值
           setDoubleValueDataPointPercentValue(seriesList, pointSize, isTime, axisGlyph, stackOnSameCateNameList);
        }
    }

    private void setDoubleValueDataPointDefaultPercentValue(List<Number> seriesList, int pointSize) {
        for(Number seriesIndex : seriesList){
            for(int cateIndex = 0; cateIndex < pointSize; cateIndex++){
                getSeries(seriesIndex.intValue()).getDataPoint(cateIndex).setPercentValue(1);
            }
        }
    }

    private void setDoubleValueDataPointPercentValue(List<Number> seriesList, int pointSize, boolean isTime, VanChartBaseAxisGlyph axisGlyph, Map<String, List<Number>> stackOnSameCateNameList) {
        //把设置成堆积且确实堆积到了一起的点求和后算平均值
        for(Number seriesIndex : seriesList){
            for(int cateIndex = 0; cateIndex < pointSize; cateIndex++){
                DataPoint dataPoint = getSeries(seriesIndex.intValue()).getDataPoint(cateIndex);
                String cateName = dataPoint.getCategoryName();
                double doubleCate;
                if(isTime){
                    Date date = VanChartDateUtils.object2Date(cateName, true);
                    if(date == null){
                        continue;
                    }
                    doubleCate = ChartBaseUtils.date2Int(date, ((VanChartTimeAxisGlyph) axisGlyph).getMainType().getTimeType());
                } else {
                    Number number = Utils.objectToNumber(cateName, true);
                    if(number == null){
                        continue;
                    }
                    doubleCate = number.doubleValue();
                }
                List<Number> series = stackOnSameCateNameList.get(String.valueOf(doubleCate));
                double total = 0;
                for(Number index : series){
                    total += Math.abs(getSeries(index.intValue()).getDataPoint(cateIndex).getValue());
                }
                dataPoint.setPercentValue(Math.abs(dataPoint.getValue()/total));
            }
        }
    }

    private VanChartBaseAxisGlyph getAxisGlyphFromAxisName(String axisName){
        for(VanChartBaseAxisGlyph axisGlyph : xAxisGlyphList){
            if(ComparatorUtils.equals(axisGlyph.getVanAxisName(), axisName)){
                return axisGlyph;
            }
        }
        for(VanChartBaseAxisGlyph axisGlyph : yAxisGlyphList){
            if(ComparatorUtils.equals(axisGlyph.getVanAxisName(), axisName)){
                return axisGlyph;
            }
        }
        return getDefaultXAxisGlyph();
    }

    protected double getCateValue(VanChartBaseAxisGlyph axisGlyph, DataPoint dataPoint, boolean justHalf) {
        if(ComparatorUtils.equals(axisGlyph.getVanAxisType(), AxisType.AXIS_CATEGORY)){
            return dataPoint.getCategoryIndex() + ((axisGlyph.isDrawBetweenTick() && justHalf) ? VanChartCategoryAxisGlyph.HALF : 0);
        } else if(ComparatorUtils.equals(axisGlyph.getVanAxisType(), AxisType.AXIS_VALUE)){
            Number number = Utils.string2Number(dataPoint.getCategoryName());
            if(number != null){
                return number.doubleValue();
            }
        } else {
            Date date = VanChartDateUtils.object2Date(dataPoint.getCategoryName(), true);
            if(date != null){
                return ChartBaseUtils.date2Int(date, ((VanChartTimeAxisGlyph) axisGlyph).getMainType().getTimeType());
            }
        }
        return 0;
    }

    // 拟合数据,排除空值
    protected int calculateLineDataCount(DataSeries dataSeries) {
        int lineDataCount = 0;
        for (int i = 0; i < dataSeries.getDataPointCount(); i++) {
            if (!dataSeries.getDataPoint(i).isValueIsNull()) {
                lineDataCount++;
            }
        }
        return lineDataCount;
    }

    /**
     * 趋势线拟合计算
     */
    protected void trendLineFitting(double[] xVal, double[] yVal, VanChartDataSeries dataSeries) {
        dataSeries.clearTrendLineGlyph();
        VanChartAttrTrendLine attrTrendLine = getAttrTrendLine(dataSeries);

        //只有一个有效点的时候不显示趋势线
        if (attrTrendLine != null && xVal.length > 1) {
            TrendLineGlyph newTrendLine = new TrendLineGlyph(ChartEquationType.LINEAR, 6, 5);
            try {
                newTrendLine.setLineStyleInfo((LineStyleInfo)attrTrendLine.getLineStyleInfo().clone());
            } catch (CloneNotSupportedException e) {
                FRContext.getLogger().error(e.getMessage(), e);
            }

            initTrendLineGlyph(newTrendLine, xVal, yVal);

            dataSeries.addTrendLineGlyph(newTrendLine);
        }
    }

    protected void initTrendLineGlyph(TrendLineGlyph newTrendLine, double[] xVal, double[] yVal) {
        int length = xVal.length - 1;
        newTrendLine.fitting(xVal, yVal);
        LinearEquation linearEquation = (LinearEquation)newTrendLine.getEquation();
        double y1 = linearEquation.execute(xVal[0]);
        double y2 = linearEquation.execute(xVal[length]);
        GeneralPath path = new GeneralPath();
        path.moveTo(xVal[0], y1);
        path.lineTo(xVal[length], y2);
        path.closePath();
        newTrendLine.setGeneralPath(path);
    }


    public void drawInfo(Graphics g, int resolution) {
        super.drawInfo(g, resolution);

        Graphics2D g2d = (Graphics2D)g;
        g2d.translate(getBounds().getX(), getBounds().getY());
        drawAxis(g, resolution);
        g2d.translate(-getBounds().getX(), -getBounds().getY());

    }

    protected void drawAxis(Graphics g, int resolution) {
        for(VanChartBaseAxisGlyph axisGlyph : xAxisGlyphList){
            if (axisGlyph != null) {
                axisGlyph.draw(g, resolution);
            }
        }
        for(VanChartBaseAxisGlyph axisGlyph : yAxisGlyphList){
            if (axisGlyph != null) {
                axisGlyph.draw(g, resolution);
            }
        }
    }

    /**
     * X坐标轴写入js
     * @param js json对象
     * @param repo 请求
     * @throws JSONException 抛错
     */
    public void addXAxisJSON(JSONObject js, Repository repo) throws JSONException{
        JSONArray list = new JSONArray();
        for (AxisGlyph axisGlyph : xAxisGlyphList){
            list.put(axisGlyph.toJSONObject(repo));
        }
        js.put("xAxis", list);
    }

    /**
     * Y坐标轴写入js
     * @param js json对象
     * @param repo 请求
     * @throws JSONException 抛错
     */
    public void addYAxisJSON(JSONObject js, Repository repo) throws JSONException{
        JSONArray list = new JSONArray();
        for (AxisGlyph axisGlyph : yAxisGlyphList){
            list.put(axisGlyph.toJSONObject(repo));
        }
        js.put("yAxis", list);
    }

    /**
     * 系列写入json
     * @param js  json对象
     * @param repo 请求
     * @throws JSONException 抛错
     */
    public void addSeriesJSON(JSONObject js, Repository repo) throws JSONException{
        int seriesCount = getSeriesSize();

        JSONArray list = new JSONArray();
        for (int i = 0; i < seriesCount; i++) {
            list.put(this.getSeries(i).toJSONObject(repo));
        }

        js.put("series", list);
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

        VanChartAttrTrendLine attrTrendLine = getDefaultAttrTrendLine();
        if(attrTrendLine != null && attrTrendLine.getLineStyleInfo().getAttrLineStyle().getLineStyle() > 0){
            js.put("trendLine", attrTrendLine.toJSONObject(repo));
        }

        return js;
    }

}
