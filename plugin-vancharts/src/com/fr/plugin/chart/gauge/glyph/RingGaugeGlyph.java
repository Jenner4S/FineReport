package com.fr.plugin.chart.gauge.glyph;

import com.fr.chart.base.TextAttr;
import com.fr.general.FRFont;
import com.fr.plugin.chart.base.AttrLabel;
import com.fr.plugin.chart.gauge.GaugeDetailStyle;
import com.fr.plugin.chart.glyph.axis.VanChartGaugeAxisGlyph;

import java.awt.*;
import java.awt.geom.Dimension2D;
import java.awt.geom.Rectangle2D;

/**
 * 百分比圆环仪表盘。
 */
public class RingGaugeGlyph extends GaugeGlyph {

    private static final long serialVersionUID = -6367293164920991590L;
    private static final double INNER_BACKGROUND = 0.8;//内底盘半径占半径

    private Color valueColor = Color.black;
    private double percentLabelCenterY;
    private double valueLabelCenterY;

    public RingGaugeGlyph(Rectangle2D bounds, AttrLabel attrLabel, GaugeDetailStyle gaugeDetailStyle, VanChartGaugeAxisGlyph gaugeAxisGlyph) {
        super(bounds, attrLabel, gaugeDetailStyle, gaugeAxisGlyph);
    }

    public void doLayout(double cateLabelHeight, int resolution){
        super.doLayout(cateLabelHeight, resolution);
        if(attrLabel != null && attrLabel.isEnable()){
            boolean hasPercent = attrLabel.getAttrLabelDetail().getContent().hasLabelContent();
            boolean hasValue = attrLabel.getGaugeValueLabelDetail().getContent().hasLabelContent();
            double valueHeight = GaugeGlyphHelper.calculateOneLineHeight(attrLabel.getGaugeValueLabelDetail().getTextAttr().getFRFont());
            if(hasPercent && hasValue){
                double sum = cateLabelHeight + cateLabelHeight/2 + valueHeight;
                percentLabelCenterY = centerPoint.getY() - sum/2 + cateLabelHeight/2;
                valueLabelCenterY = percentLabelCenterY + cateLabelHeight + valueHeight/2;
            } else{
                percentLabelCenterY = centerPoint.getY();
                valueLabelCenterY = centerPoint.getY();
            }
        }
    }

    protected void drawInfo(Graphics2D g2d, int resolution){
        drawPaneBackground(g2d);//底盘背景

        drawPaneBorder(g2d);//底盘的外描边

        drawValueArc(g2d);//表示占比的圆环，其实是个圆，被内底盘覆盖了，显得是个圆环。

        drawInnerPaneBackground(g2d);//内底盘背景

        drawInnerPaneBorder(g2d);//内底盘的内外描边

        drawCateOrPercentageLabel(g2d, resolution);//百分比标签

        drawValueLabel(g2d, resolution);//值标签
    }

    //底盘的外描边
    private void drawPaneBorder(Graphics2D g2d) {
        GaugeGlyphHelper.drawCircleStroke(g2d, centerPoint, radius - 2, radius, new Color(0,0,0,12));
    }

    //画系列部分
    private void drawValueArc(Graphics2D g2d) {
        if(valueList.isEmpty()){
            return;
        }
        double minValue = gaugeAxisGlyph.getMinValue();
        double maxValue = gaugeAxisGlyph.getMaxValue();
        double value = valueList.get(0).doubleValue();
        valueColor = getValueColor(value, minValue, maxValue);
        if(value < minValue){
            return;
        }
        double valueExtent = this.extent/(maxValue - minValue) * (value - minValue);

        if(!gaugeDetailStyle.isAntiClockWise()){
            valueExtent = -valueExtent;
        }
        GaugeGlyphHelper.drawArc(g2d, centerPoint, radius, startAngle, valueExtent, valueColor);
    }

    //内底盘背景
    private void drawInnerPaneBackground(Graphics2D g2d) {
        GaugeGlyphHelper.drawCircle(g2d, centerPoint, radius * INNER_BACKGROUND, gaugeDetailStyle.getInnerPaneBackgroundColor());
    }

    //内底盘的内外描边
    private void drawInnerPaneBorder(Graphics2D g2d) {
        double innerRadius = radius * INNER_BACKGROUND;
        GaugeGlyphHelper.drawCircleStroke(g2d, centerPoint, innerRadius, innerRadius + 4, new Color(0,0,0,12));//外描边
        GaugeGlyphHelper.drawCircleShadeStroke(g2d, centerPoint, innerRadius - 3, innerRadius, new Color(221,221,221), new Color(255,255,255));//内描边
    }

    //自动时的样式
    protected void setCateOrPercentLabelAutoFont(TextAttr textAttr){
        textAttr.setFRFont(FRFont.getInstance("verdana", Font.BOLD, 18, valueColor));
    }

    //分类名称的位置和大小
    protected Rectangle2D getCateOrPercentLabelBounds(int position, Dimension2D dim) {
        double x = centerPoint.getX() - dim.getWidth()/2;
        return new Rectangle2D.Double(x, percentLabelCenterY - dim.getHeight()/2, dim.getWidth(), dim.getHeight());
    }

    //值标签的位置和大小
    protected Rectangle2D getValueLabelBounds(int position, Dimension2D dim) {
        double x = centerPoint.getX() - dim.getWidth()/2;
        return new Rectangle2D.Double(x, valueLabelCenterY - dim.getHeight()/2, dim.getWidth(), dim.getHeight());
    }

}
