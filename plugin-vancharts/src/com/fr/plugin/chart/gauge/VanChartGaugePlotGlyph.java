package com.fr.plugin.chart.gauge;

import com.fr.chart.base.ChartBaseUtils;
import com.fr.chart.base.ChartConstants;
import com.fr.chart.chartglyph.DataPoint;
import com.fr.chart.chartglyph.DataSeries;
import com.fr.chart.chartglyph.MapHotAreaColor;
import com.fr.general.FRFont;
import com.fr.json.JSONArray;
import com.fr.json.JSONException;
import com.fr.json.JSONObject;
import com.fr.plugin.chart.attr.VanChartAttrHelper;
import com.fr.plugin.chart.base.AttrLabel;
import com.fr.plugin.chart.base.AttrLabelDetail;
import com.fr.plugin.chart.gauge.glyph.*;
import com.fr.plugin.chart.glyph.VanChartAxisPlotGlyphInterface;
import com.fr.plugin.chart.glyph.VanChartPlotGlyph;
import com.fr.plugin.chart.glyph.axis.VanChartGaugeAxisGlyph;
import com.fr.stable.StringUtils;
import com.fr.stable.web.Repository;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mitisky on 15/11/27.
 */
public class VanChartGaugePlotGlyph extends VanChartPlotGlyph implements VanChartAxisPlotGlyphInterface{

    private static final long serialVersionUID = 8281521012356811258L;

    //仅用于计算。
    private static final FRFont PERCENT_LABEL_AUTO_FONT = FRFont.getInstance("verdana", Font.BOLD, 27, Color.black);
    public static final int DEFAULT_COLOR_NUMBER = GaugeGlyph.DEFAULT_COLOR_NUMBER;//自动的颜色取系列的前三个颜色

    private GaugeStyle gaugeStyle = GaugeStyle.POINTER;

    private GaugeDetailStyle gaugeDetailStyle;

    private VanChartGaugeAxisGlyph gaugeAxisGlyph = new VanChartGaugeAxisGlyph();

    private AttrLabel attrLabel;//分类标签、百分比标签、值标签

    private List<GaugeGlyph> gaugeGlyphs = new ArrayList<GaugeGlyph>();//存储多个仪表盘图形对象

    public GaugeStyle getGaugeStyle() {
        return gaugeStyle;
    }

    public void setGaugeStyle(GaugeStyle gaugeStyle){
        this.gaugeStyle = gaugeStyle;
    }

    public void setGaugeDetailStyle(GaugeDetailStyle gaugeDetailStyle) {
        this.gaugeDetailStyle = gaugeDetailStyle;
    }

    public GaugeDetailStyle getGaugeDetailStyle() {
        return gaugeDetailStyle;
    }

    public void setGaugeAxisGlyph(VanChartGaugeAxisGlyph gaugeAxisGlyph) {
        this.gaugeAxisGlyph = gaugeAxisGlyph;
    }

    public VanChartGaugeAxisGlyph getGaugeAxisGlyph() {
        return gaugeAxisGlyph;
    }

    public void setAttrLabel(AttrLabel attrLabel){
        this.attrLabel = attrLabel;
    }

    public boolean isMultiPointer() {
        switch (gaugeStyle){
            case POINTER:
                return true;
            case POINTER_SEMI:
                return true;
            default:
                return false;
        }
    }

    /**
     *  对绘图区中的坐标轴部分进行布局,传过去图表边界计算限制区域
     * @param resolution 分辨率
     */
    public void layoutAxisGlyph(int resolution){
        double[] minMaxValue = getMinMaxValue();
        gaugeAxisGlyph.initMinMaxValue(minMaxValue[0], minMaxValue[1]);
    }


    //一个分类（即一个仪表盘）中没有有效数据则忽略这个分类
    private int getRealCategoryCount() {
        int cateCount = getCategoryCount();
        int realCateCount = 0;
        for(int cateIndex = 0; cateIndex < cateCount; cateIndex++){
            for(int seriesIndex = 0, seriesSize = getSeriesSize(); seriesIndex < seriesSize; seriesIndex++){
                DataPoint dataPoint = getSeries(seriesIndex).getDataPoint(cateIndex);
                if(!dataPoint.isValueIsNull()){
                    realCateCount++;
                    break;
                }
            }
        }
        return realCateCount;
    }

    /**
     * 一个仪表盘 是一个分类。
     *
     * @param resolution 分辨率
     */
    public void layoutDataSeriesGlyph(int resolution) {
        super.layoutDataSeriesGlyph(resolution);
        int categoryCount = getRealCategoryCount();
        if(categoryCount < 1 || getSeriesSize() < 1){
            return;
        }
        double width = getEachWidth(categoryCount);
        double height = getEachHeight(categoryCount);
        double categoryLabelHeight = getCategoryLabelHeight(resolution);
        Color[] colors = ChartBaseUtils.createFillColorArray(getPlotFillStyle(), DEFAULT_COLOR_NUMBER);

        for (int categoryIndex = 0; categoryIndex < categoryCount; categoryIndex++) {
            initEachCategoryShape(categoryIndex, width, height, categoryLabelHeight, colors, resolution);
        }
    }

    // 计算每个仪表盘的整体宽度
    private double getEachWidth(int categoryCount) {
        Rectangle2D plotBounds = this.getBounds();
        if(gaugeDetailStyle.isHorizontalLayout()){
            return plotBounds.getWidth() / categoryCount;
        } else {
            return plotBounds.getWidth();
        }
    }

    //计算每个仪表盘的整体高度
    private double getEachHeight(int categoryCount) {
        Rectangle2D plotBounds = this.getBounds();
        if(gaugeDetailStyle.isHorizontalLayout()){
            return plotBounds.getHeight();
        } else {
            return plotBounds.getHeight() / categoryCount;
        }
    }

    //分类标签的高度。多指针类型的仪表盘计算圆心、半径会用到。
    private double getCategoryLabelHeight(int resolution) {
        if(attrLabel == null || !attrLabel.isEnable()){
            return 0;
        }
        String label;
        AttrLabelDetail cateLabelDetail = attrLabel.getAttrLabelDetail();
        if(isMultiPointer()){
            label = VanChartAttrHelper.getCategoryName(cateLabelDetail.getContent(), getSeries(0).getDataPoint(0));
        } else {
            label = VanChartAttrHelper.getPercent(cateLabelDetail.getContent(), getSeries(0).getDataPoint(0));
        }
        //这边刻度槽型仪表盘的值标签位置和百分比标签字体大小有关，当百分比标签为自动时，这边计算百分比标签高度时用一个默认的字号。
        FRFont frFont = cateLabelDetail.isCustom() ? cateLabelDetail.getTextAttr().getFRFont() : PERCENT_LABEL_AUTO_FONT;
        return GaugeGlyphHelper.calculateTextDimension(label, frFont, resolution).getHeight();
    }

    private void initEachCategoryShape(int categoryIndex, double width, double height, double cateLabelHeight, Color[] colors, int resolution) {
        Rectangle2D bounds;
        if(gaugeDetailStyle.isHorizontalLayout()){
            bounds = new Rectangle2D.Double(categoryIndex * width, 0, width, height);
        } else {
            bounds = new Rectangle2D.Double(0, categoryIndex * height, width, height);
        }
        GaugeGlyph gaugeGlyph = createGaugeGlyph(bounds);
        gaugeGlyphs.add(gaugeGlyph);

        gaugeGlyph.setDefaultColors(colors);
        MapHotAreaColor areaColors = getGaugeDetailStyle().getHotAreaColor();
        if(areaColors.getUseType() != 0){//自定义
            gaugeGlyph.setColorList(areaColors.getAreaColorList());
        }

        String gaugeValueLabel = StringUtils.EMPTY;
        for(int seriesIndex = 0, seriesSize = getSeriesSize(); seriesIndex < seriesSize; seriesIndex++){
            DataPoint dataPoint = getSeries(seriesIndex).getDataPoint(categoryIndex);
            if(dataPoint.isValueIsNull()){
                continue;
            }
            gaugeGlyph.addValue(dataPoint.getValue());

            if(attrLabel != null && attrLabel.isEnable()){
                String valueLabel = gaugeGlyph.getValueLabelWithDataPoint(dataPoint);
                if(StringUtils.isNotEmpty(valueLabel)){
                    if(StringUtils.isNotEmpty(gaugeValueLabel)){
                        gaugeValueLabel += VanChartAttrHelper.NEWLINE;
                    }
                    gaugeValueLabel += valueLabel;
                }
            }
        }

        if(attrLabel !=null && attrLabel.isEnable()){
            DataPoint temp = getSeries(0).getDataPoint(categoryIndex);
            gaugeGlyph.setCateOrPercentLabelString(temp);
            gaugeGlyph.setValueLabelString(gaugeValueLabel);
        }

        gaugeGlyph.doLayout(cateLabelHeight, resolution);
    }

    private GaugeGlyph createGaugeGlyph(Rectangle2D bounds) {
        switch (gaugeStyle){
            case POINTER:
                return new PointerGaugeGlyph(bounds, attrLabel, gaugeDetailStyle, gaugeAxisGlyph);
            case POINTER_SEMI:
                return new SemiPointerGaugeGlyph(bounds, attrLabel, gaugeDetailStyle, gaugeAxisGlyph);
            case RING:
                return new RingGaugeGlyph(bounds, attrLabel, gaugeDetailStyle, gaugeAxisGlyph);
            case SLOT:
                return new SlotGaugeGlyph(bounds, attrLabel, gaugeDetailStyle, gaugeAxisGlyph);
            default:
                return new ThermometerGaugeGlyph(bounds, attrLabel, gaugeDetailStyle, gaugeAxisGlyph);
        }
    }

    /**
     * 计算数据点的占比。仪表盘的百分比为：每个点的数值比上坐标轴的最大值。
     */
    public void calculateDataPointPercentValue(){
        double min = gaugeAxisGlyph.getMinValue();
        double max = gaugeAxisGlyph.getMaxValue();
        int seriesCount = getSeriesSize();
        int cateCount = getCategoryCount();

        for(int seriesIndex = 0; seriesIndex < seriesCount; seriesIndex++){
            DataSeries dataSeries = getSeries(seriesIndex);
            for(int cateIndex = 0; cateIndex < cateCount; cateIndex++){
                DataPoint dataPoint = dataSeries.getDataPoint(cateIndex);
                double value = dataPoint.getValue();
                if (dataPoint.isValueIsNull()) {
                    continue;
                }
                if(max == min){
                    dataPoint.setPercentValue(0);
                } else {
                    dataPoint.setPercentValue((value - min)/(max - min));
                }
            }
        }
    }

    public void draw(Graphics g, int resolution) {
        drawInfo(g);
        Graphics2D g2d = (Graphics2D) g;

        Shape oldClip = g2d.getClip();
        //如果没有进行clip，才需要clip
        if(oldClip == null){
            resetClip(g2d);
        }

        g2d.translate(getBounds().getX(), getBounds().getY());

        for(GaugeGlyph gaugeGlyph : gaugeGlyphs){
            gaugeGlyph.draw(g, resolution);
        }

        g2d.translate(-getBounds().getX(), -getBounds().getY());
        g2d.setClip(oldClip);
    }

    public String getChartType(){
        return "gauge";
    }

    /**
     * X坐标轴写入js
     * @param js json对象
     * @param repo 请求
     * @throws JSONException 抛错
     */
    public void addXAxisJSON(JSONObject js, Repository repo) throws JSONException{
    }

    /**
     * Y坐标轴写入js
     * @param js json对象
     * @param repo 请求
     * @throws JSONException 抛错
     */
    public void addYAxisJSON(JSONObject js, Repository repo) throws JSONException{
        JSONArray list = new JSONArray();

        list.put(getGaugeAxisGlyph().toJSONObject(repo));

        js.put("yAxis", list);
    }

    /**
     * 为每个系列创建颜色数组
     * @return 返回颜色数组.
     */
    public Color[] createColors4Series() {
        if(getPlotFillStyle().getColorStyle() == ChartConstants.COLOR_GRADIENT){
            return ChartBaseUtils.createFillColorArray(getPlotFillStyle(), DEFAULT_COLOR_NUMBER);
        }
        return ChartBaseUtils.createFillColorArray(getPlotFillStyle(), DEFAULT_COLOR_NUMBER);
    }


        /**
         * 系列写入json
         * @param js  json对象
         * @param repo 请求
         * @throws com.fr.json.JSONException 抛错
         */
    public void addSeriesJSON(JSONObject js, Repository repo) throws JSONException {
        addSeriesJSONWithCate2Series(js, repo);
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

        js.put("style", getGaugeStyle().getStyle());

        js.put("layout", getGaugeDetailStyle().isHorizontalLayout() ? "horizontal" : "vertical");

        MapHotAreaColor areaColors = getGaugeDetailStyle().getHotAreaColor();
        if(areaColors.getUseType() != 0){//自定义
            js.put("bands", getGaugeDetailStyle().getBandsArray());
        }

        //指针、枢纽、底盘等。
        getGaugeDetailStyle().addDetailStyleJSON(js, getGaugeStyle());

        return js;
    }

    protected void addLabelJSON(JSONObject js, AttrLabel attrLabel, Repository repo) throws JSONException {
        if(isMultiPointer()){
            js.put("seriesLabel", attrLabel.toJSONObject(repo));
        } else {
            js.put("percentageLabel", attrLabel.toJSONObject(repo));
        }
        js.put("valueLabel", attrLabel.toGaugeValueLabelJSONObject());
    }

    protected void addNullLabelJSON(JSONObject js) throws JSONException{
        JSONObject nullLabelJSON = new JSONObject();
        nullLabelJSON.put("enabled", false);
        if(isMultiPointer()){
            js.put("seriesLabel", nullLabelJSON);
        } else {
            js.put("percentageLabel", nullLabelJSON);
        }
        js.put("valueLabel", nullLabelJSON);
    }
}
