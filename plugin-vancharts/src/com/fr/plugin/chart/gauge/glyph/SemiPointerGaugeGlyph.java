package com.fr.plugin.chart.gauge.glyph;

import com.fr.plugin.chart.base.AttrLabel;
import com.fr.plugin.chart.gauge.GaugeDetailStyle;
import com.fr.plugin.chart.glyph.axis.VanChartGaugeAxisGlyph;
import com.fr.stable.Constants;

import java.awt.*;
import java.awt.geom.Arc2D;
import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

/**
 * 多指针仪表盘图形。180度。
 */
public class SemiPointerGaugeGlyph extends PointerGaugeGlyph {
    private static final long serialVersionUID = -4853556539610118272L;
    private static final double HINGE_BACKGROUND = 0.11;//枢纽背景半径占仪表盘半径0.16.
    private static final double HINGE = 0.055;//枢纽占比
    private static final double HINGE_GAP = 0.14;//半圆下面空出一段距离
    private static final int START_ANGLE = 180;//起始角度
    private static final int EXTENT = -180;//范围

    private static final double ANGLE1 = 135;
    private static final double ANGLE2 = 45;

    private static final double ANGLE3 = 8;

    public SemiPointerGaugeGlyph(Rectangle2D bounds, AttrLabel attrLabel, GaugeDetailStyle gaugeDetailStyle, VanChartGaugeAxisGlyph gaugeAxisGlyph) {
        super(bounds, attrLabel, gaugeDetailStyle, gaugeAxisGlyph);
    }

    protected void initStartAngleAndExtent() {
        this.startAngle = START_ANGLE;
        this.extent = EXTENT;
    }

    protected void calculateRadiusAndCenterPoint(double width, double height, double cateLabelHeight){
        double temp = height - cateLabelHeight - CATEGORY_LABEL_GAP;
        radius = Math.min(width/2, temp/(1 + HINGE_GAP));
        radius = radius > GAP ? radius - GAP : radius;

        double x = width/2;
        double y = height/2 + radius/2;
        if(attrLabel != null && attrLabel.isEnable()){
            int position = attrLabel.getAttrLabelDetail().getPosition();
            if(position == Constants.BOTTOM){//下方
                double gap = cateLabelHeight + CATEGORY_LABEL_GAP + HINGE_GAP * radius;
                if(gap > (height - y)){//下面显示不下
                    y = height - gap;
                }
            } else {//上方
                double gap = cateLabelHeight + CATEGORY_LABEL_GAP + radius;
                if(gap > y){//上面显示不下
                    y = gap;
                }
            }
        }
        centerPoint = new Point2D.Double(x, y);
    }

    protected void setFirstBigBounds() {
        bigBounds.setFrame(centerPoint.getX() - radius, centerPoint.getY() - radius, radius * 2, radius - radius * HINGE_BACKGROUND - oneLineHeight * VALUE_GAP);
    }

    //分类名称的位置和大小
    protected Rectangle2D getCateOrPercentLabelBounds(int position, Dimension2D dim){
        double x = centerPoint.getX() - dim.getWidth()/2;
        double y = position == Constants.BOTTOM ? centerPoint.getY() + radius * HINGE_GAP + CATEGORY_LABEL_GAP - dim.getHeight()/PROPORTION
                : centerPoint.getY() - radius - CATEGORY_LABEL_GAP - dim.getHeight();
        return new Rectangle2D.Double(x, y, dim.getWidth(), dim.getHeight());
    }

    //180到0度
    protected void calculateValueBackgroundBounds(Rectangle2D labelBounds, double valueAngle){
        double safe = labelBounds.getHeight()/PROPORTION;
        Rectangle2D safeBounds = new Rectangle2D.Double(labelBounds.getX() - safe, labelBounds.getY() - safe,
                labelBounds.getWidth() + safe * 2, labelBounds.getHeight() + safe * 2);
        if(valueAngle >= ANGLE2 && valueAngle <= ANGLE1){
            if(safeBounds.getMaxY() > bigBounds.getY()){
                bigBounds.setFrame(bigBounds.getX(), safeBounds.getMaxY(), bigBounds.getWidth(), bigBounds.getHeight() - (safeBounds.getMaxY() - bigBounds.getY()));
            }
        } else if(valueAngle > ANGLE1){
            if(bigBounds.getX() < safeBounds.getMaxX()){
                bigBounds.setFrame(safeBounds.getMaxX(), bigBounds.getY(), bigBounds.getWidth(), bigBounds.getHeight());
            }
        } else if(valueAngle < ANGLE2) {
            if(bigBounds.getMaxX() > safeBounds.getX()){
                bigBounds.setFrame(bigBounds.getX(), bigBounds.getY(), safeBounds.getX() - bigBounds.getX(), bigBounds.getHeight());
            }
        }
    }

    //底盘背景（不是一个圆）
    protected void drawPaneBackground(Graphics2D g2d){
        GaugeGlyphHelper.drawArc(g2d, centerPoint, radius, startAngle + ANGLE3, extent - ANGLE3 * 2, gaugeDetailStyle.getPaneBackgroundColor(), Arc2D.OPEN);
    }

    protected double getHingeBackgroundSize() {
        return HINGE_BACKGROUND;
    }

    protected double getHingeSize() {
        return HINGE;
    }
}
