package com.fr.plugin.chart.gauge.glyph;

import com.fr.chart.base.AreaColor;
import com.fr.chart.base.GlyphUtils;
import com.fr.chart.base.TextAttr;
import com.fr.chart.chartglyph.DataPoint;
import com.fr.chart.chartglyph.SpecialGlyph;
import com.fr.plugin.chart.attr.VanChartAttrHelper;
import com.fr.plugin.chart.base.AttrLabel;
import com.fr.plugin.chart.base.AttrLabelDetail;
import com.fr.plugin.chart.gauge.GaugeDetailStyle;
import com.fr.plugin.chart.glyph.axis.VanChartGaugeAxisGlyph;
import com.fr.stable.Constants;

import java.awt.*;
import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mitisky on 15/12/10.
 */
public abstract class GaugeGlyph extends SpecialGlyph {
    private static final long serialVersionUID = -6454068778509007571L;
    public static final int DEFAULT_COLOR_NUMBER = 3;//自动的颜色取系列的前三个颜色
    public static final int CATEGORY_LABEL_GAP = 20;//分类名称距离圆盘20。

    protected static final int GAP = 5;//两个圆盘之间的空隙是10.
    protected static final double PIE = 180;

    private static final int START_ANGLE = 90;//起始角度
    private static final int EXTENT = 360;//范围

    protected Rectangle2D bounds;//整体的一个边界
    protected Point2D centerPoint;//圆心
    protected double radius;//半径

    protected int startAngle; //仪表盘起始角度，即最小值处
    protected int extent; //仪表盘角度范围，多指的是指刻度范围。百分比的是指底盘的那个环的范围。

    protected AttrLabel attrLabel;//分类标签、百分比标签、值标签

    protected GaugeDetailStyle gaugeDetailStyle;//系列的一些属性，包括指针颜色，底盘颜色等。

    protected VanChartGaugeAxisGlyph gaugeAxisGlyph;//坐标轴的一些属性，包括最大最小值，和主要次要刻度单位等。

    protected Color[] defaultColors = new Color[DEFAULT_COLOR_NUMBER];//刻度和配色设置自动时，取配色的前三个

    protected java.util.List colorList = new ArrayList();//自定义的刻度和配色（bands）。如果是自动的，数组为空，则使用defaultColors。

    protected String cateOrPercentLabelString;//分类标签

    protected String valueLabelString;//值标签

    protected List<Number> valueList = new ArrayList<Number>();//多指针仪表盘是值的集合，百分比类是含有一个值的集合

    public void setDefaultColors(Color[] defaultColors){
        this.defaultColors = defaultColors;
    }

    public void setColorList(java.util.List colorList) {
        this.colorList = colorList;
    }

    public void setCateOrPercentLabelString(DataPoint dataPoint) {
        this.cateOrPercentLabelString = getCateOrPercentLabelWithDataPoint(dataPoint);
    }

    public void setValueLabelString(String valueLabelString) {
        this.valueLabelString = valueLabelString;
    }

    public void addValue(Double value){
        valueList.add(value);
    }

    public GaugeGlyph(Rectangle2D bounds, AttrLabel attrLabel, GaugeDetailStyle gaugeDetailStyle, VanChartGaugeAxisGlyph gaugeAxisGlyph) {
        this.bounds = bounds;
        this.attrLabel = attrLabel;
        this.gaugeDetailStyle = gaugeDetailStyle;
        this.gaugeAxisGlyph = gaugeAxisGlyph;
    }

    public void doLayout(double cateLabelHeight, int resolution){
        initStartAngleAndExtent();
        calculateRadiusAndCenterPoint(bounds.getWidth(), bounds.getHeight(), cateLabelHeight);
    }

    protected void initStartAngleAndExtent() {
        this.startAngle = START_ANGLE;
        this.extent = EXTENT;
    }

    protected void calculateRadiusAndCenterPoint(double width, double height, double cateLabelHeight) {
        radius = Math.min(width, height)/2;
        radius = radius > GAP ? radius - GAP : radius;
        centerPoint = new Point2D.Double(width / 2, height / 2);
    }

    public void draw(java.awt.Graphics graphics, int resolution){
        if(radius <= 0){
            return;
        }
        Graphics2D g2d = (Graphics2D) graphics;

        g2d.translate(bounds.getX(), bounds.getY());

        Paint oldPaint = g2d.getPaint();
        Stroke oldStroke = g2d.getStroke();
        drawInfo(g2d, resolution);
        g2d.setStroke(oldStroke);
        g2d.setPaint(oldPaint);

        g2d.translate(-bounds.getX(), -bounds.getY());
    }

    protected abstract void drawInfo(Graphics2D g2d, int resolution);

    //底盘背景
    protected void drawPaneBackground(Graphics2D g2d){
        GaugeGlyphHelper.drawCircle(g2d, centerPoint, radius, gaugeDetailStyle.getPaneBackgroundColor());
    }

    //分类标签
    protected void drawCateOrPercentageLabel(Graphics2D g2d, int resolution) {
        if(attrLabel != null && attrLabel.isEnable()){
            AttrLabelDetail cateLabelDetail = attrLabel.getAttrLabelDetail();
            if(!cateLabelDetail.isCustom()){
                setCateOrPercentLabelAutoFont(cateLabelDetail.getTextAttr());
            }
            Dimension2D dim = GlyphUtils.calculateTextDimensionWithNoRotation(cateOrPercentLabelString, cateLabelDetail.getTextAttr(), resolution);
            Rectangle2D labelBounds = getCateOrPercentLabelBounds(cateLabelDetail.getPosition(), dim);
            GlyphUtils.drawStrings(g2d, cateOrPercentLabelString, cateLabelDetail.getTextAttr(), labelBounds, resolution);
        }
    }

    //自动时的样式
    protected void setCateOrPercentLabelAutoFont(TextAttr textAttr){
    }

    //分类名称的位置和大小
    protected Rectangle2D getCateOrPercentLabelBounds(int position, Dimension2D dim){
        double x = centerPoint.getX() - dim.getWidth()/2;
        double y = position == Constants.BOTTOM ? centerPoint.getY() + radius + CATEGORY_LABEL_GAP
                : centerPoint.getY() - radius - CATEGORY_LABEL_GAP;
        return new Rectangle2D.Double(x, y, dim.getWidth(), dim.getHeight());
    }

    // 值标签
    protected void drawValueLabel(Graphics2D g2d, int resolution) {
        if(attrLabel != null && attrLabel.isEnable()){
            AttrLabelDetail valueLabelDetail = attrLabel.getGaugeValueLabelDetail();
            TextAttr valueLabelAttr = valueLabelDetail.getTextAttr();
            Dimension2D dim = GlyphUtils.calculateTextDimensionWithNoRotation(valueLabelString, valueLabelAttr, resolution);
            Rectangle2D labelBounds = getValueLabelBounds(valueLabelDetail.getPosition(), dim);
            GaugeGlyphHelper.drawStrings(g2d, valueLabelString, valueLabelAttr, labelBounds, resolution);
        }
    }

    //值标签的位置和大小
    protected Rectangle2D getValueLabelBounds(int position, Dimension2D dim) {
        double x = centerPoint.getX() - dim.getWidth()/2;
        double y = centerPoint.getY() + (radius - dim.getHeight())/2;
        return new Rectangle2D.Double(x, y, dim.getWidth(), dim.getHeight());
    }

    protected Color getValueColor(double tickValue, double minValue, double maxValue) {
        for(Object ob : colorList) {
            AreaColor areaColor = (AreaColor)ob;
            double min = areaColor.getMinNum();
            double max = areaColor.getMaxNum();
            boolean temp1 = tickValue >= min && tickValue <= max;
            boolean temp2 = tickValue <= min && tickValue >= max;
            if(temp1 || temp2){
                return areaColor.getAreaColor();
            }
        }
        return getDefaultValueColor(tickValue, minValue, maxValue);
    }

    protected Color getDefaultValueColor(double tickValue, double minValue, double maxValue) {
        if(tickValue >= maxValue){
            return defaultColors[2];
        } else if(tickValue <= minValue){
            return defaultColors[0];
        } else {
            double gap = (maxValue - minValue)/DEFAULT_COLOR_NUMBER;
            for(int i = 0; i < DEFAULT_COLOR_NUMBER; i++){
                double min = minValue + gap * i;
                double max = minValue + gap * (i + 1);
                if(tickValue >= min && tickValue <= max){
                    return defaultColors[i];
                }
            }
        }
        return defaultColors[0];
    }

    public String getCateOrPercentLabelWithDataPoint(DataPoint dataPoint) {
        return VanChartAttrHelper.getPercent(attrLabel.getAttrLabelDetail().getContent(), dataPoint);
    }

    public String getValueLabelWithDataPoint(DataPoint dataPoint) {
        return VanChartAttrHelper.getCateAndValue(attrLabel.getGaugeValueLabelDetail().getContent(), dataPoint);
    }

    public Shape getShape(){
        return bounds;
    }
}
