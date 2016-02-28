package com.fr.plugin.chart.gauge.glyph;

import com.fr.chart.base.GlyphUtils;
import com.fr.chart.base.TextAttr;
import com.fr.chart.chartglyph.DataPoint;
import com.fr.chart.chartglyph.TextGlyph;
import com.fr.general.FRFont;
import com.fr.plugin.chart.attr.VanChartAttrHelper;
import com.fr.plugin.chart.base.AttrLabel;
import com.fr.plugin.chart.base.AttrLabelDetail;
import com.fr.plugin.chart.gauge.GaugeDetailStyle;
import com.fr.plugin.chart.glyph.axis.VanChartGaugeAxisGlyph;

import java.awt.*;
import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

/**
 * �ٷֱȿ̶Ȳ��Ǳ���ͼ�Ρ�
 */
public class SlotGaugeGlyph extends GaugeGlyph {
    private static final long serialVersionUID = -306297840432154041L;

    private static final double SLOT = 0.08;//�̶Ȳ�Բ���뾶ռ�뾶
    private static final double NEEDLE = 0.048;//ָ��뾶ռ�뾶

    private static final int START_ANGLE = 225;//��ʼ�Ƕ�
    private static final int EXTENT = -270;//��Χ
    private static final int END_ANGLE = START_ANGLE + EXTENT;//�����Ƕ�

    private Color valueColor = Color.black;
    private double percentLabelCenterY;
    private double valueLabelCenterY;

    public SlotGaugeGlyph(Rectangle2D bounds, AttrLabel attrLabel, GaugeDetailStyle gaugeDetailStyle, VanChartGaugeAxisGlyph gaugeAxisGlyph) {
        super(bounds, attrLabel, gaugeDetailStyle, gaugeAxisGlyph);
    }

    protected void initStartAngleAndExtent() {
        this.startAngle = START_ANGLE;
        this.extent = EXTENT;
    }

    public void doLayout(double cateLabelHeight, int resolution){
        super.doLayout(cateLabelHeight, resolution);
        if(attrLabel != null && attrLabel.isEnable()){
            boolean hasPercent = attrLabel.getAttrLabelDetail().getContent().hasLabelContent();
            boolean hasValue = attrLabel.getGaugeValueLabelDetail().getContent().hasLabelContent();
            double valueHeight = GaugeGlyphHelper.calculateTextDimension(valueLabelString,
                    attrLabel.getGaugeValueLabelDetail().getTextAttr().getFRFont(), resolution).getHeight();

            if(hasPercent && hasValue){
                double sumLabelHeight = cateLabelHeight + cateLabelHeight/2 + valueHeight;
                percentLabelCenterY = centerPoint.getY() - sumLabelHeight/2 + cateLabelHeight/2;
                valueLabelCenterY = percentLabelCenterY + cateLabelHeight + valueHeight/2;
            } else {
                percentLabelCenterY = centerPoint.getY();
                valueLabelCenterY = centerPoint.getY();
            }
        }
    }

    protected void drawInfo(Graphics2D g2d, int resolution){
        double temp = this.radius - this.radius * SLOT;
        double startX = centerPoint.getX() + temp * Math.cos(Math.PI * ((double)START_ANGLE / PIE));
        double startY = centerPoint.getY() - temp * Math.sin(Math.PI * ((double)START_ANGLE / PIE));
        double endX = centerPoint.getX() + temp * Math.cos(Math.PI * ((double)END_ANGLE / PIE));
        double endY = centerPoint.getY() - temp * Math.sin(Math.PI * ((double)END_ANGLE / PIE));
        Point2D startPoint = new Point2D.Double(startX, startY);
        Point2D endPoint = new Point2D.Double(endX, endY);

        if(valueList.isEmpty()){
            return;
        }
        double value = valueList.get(0).doubleValue();
        double minValue = gaugeAxisGlyph.getMinValue();
        double maxValue = gaugeAxisGlyph.getMaxValue();
        double valueExtent = Math.max(this.extent, this.extent/(maxValue - minValue) * (value - minValue));
        valueExtent = Math.min(valueExtent, 0);
        double valueAngle = this.startAngle + valueExtent;
        double valueX = centerPoint.getX() + temp * Math.cos(Math.PI * (valueAngle / PIE));
        double valueY = centerPoint.getY() - temp * Math.sin(Math.PI * (valueAngle / PIE));
        Point2D valuePoint = new Point2D.Double(valueX, valueY);
        valueColor = getValueColor(value, minValue, maxValue);

        drawSlot(g2d, startPoint, endPoint);//�̶Ȳ�

        drawValueArc(g2d, startPoint, valuePoint, valueExtent);//ϵ��ɫ

        drawNeedle(g2d, valuePoint);//ָ�룬��ֵ���ڵص�СԲ���

        drawCateOrPercentageLabel(g2d, resolution);//�ٷֱȱ�ǩ

        drawValueLabel(g2d, resolution);//ֵ��ǩ
    }

    //�̶Ȳ�
    private void drawSlot(Graphics2D g2d, Point2D startPoint, Point2D endPoint) {
        Color slotColor = GaugeGlyphHelper.getColor(gaugeDetailStyle.getSlotBackgroundColor());
        double temp = radius * SLOT;
        GaugeGlyphHelper.drawArcStroke(g2d, centerPoint, radius - temp * 2, radius, START_ANGLE, EXTENT, slotColor);
        GaugeGlyphHelper.drawCircle(g2d, startPoint, temp, slotColor);
        GaugeGlyphHelper.drawCircle(g2d, endPoint, temp, slotColor);
    }

    //ϵ��ɫ
    private void drawValueArc(Graphics2D g2d, Point2D startPoint, Point2D valuePoint, double extent) {
        double temp = radius * SLOT;
        GaugeGlyphHelper.drawArcStroke(g2d, centerPoint, radius - temp * 2, radius, START_ANGLE, extent, valueColor);
        GaugeGlyphHelper.drawCircle(g2d, startPoint, temp, valueColor);
        GaugeGlyphHelper.drawCircle(g2d, valuePoint, temp, valueColor);
    }

    //ָ�룬��ֵ���ڵص�СԲ���
    private void drawNeedle(Graphics2D g2d, Point2D valuePoint) {
        GaugeGlyphHelper.drawCircle(g2d, valuePoint, radius * NEEDLE, gaugeDetailStyle.getNeedleColor());
    }

    //�Զ�ʱ����ʽ
    protected void setCateOrPercentLabelAutoFont(TextAttr textAttr){
        textAttr.setFRFont(FRFont.getInstance("verdana", Font.BOLD, 27, valueColor));
    }

    //�������Ƶ�λ�úʹ�С
    protected Rectangle2D getCateOrPercentLabelBounds(int position, Dimension2D dim) {
        double x = centerPoint.getX() - dim.getWidth()/2;
        return new Rectangle2D.Double(x, percentLabelCenterY - dim.getHeight()/2, dim.getWidth(), dim.getHeight());
    }

    // ֵ��ǩ
    protected void drawValueLabel(Graphics2D g2d, int resolution) {
        if(attrLabel != null && attrLabel.isEnable()){
            AttrLabelDetail valueLabelDetail = attrLabel.getGaugeValueLabelDetail();
            TextAttr valueLabelAttr = valueLabelDetail.getTextAttr();
            Dimension2D dim = GlyphUtils.calculateTextDimensionWithNoRotation(valueLabelString, valueLabelAttr, resolution);
            Rectangle2D labelBounds = getValueLabelBounds(valueLabelDetail.getPosition(), dim);
            TextGlyph textGlyph = new TextGlyph(valueLabelString, valueLabelAttr);
            textGlyph.setBounds(labelBounds);
            textGlyph.draw(g2d, resolution);
        }
    }

    //ֵ��ǩ��λ�úʹ�С
    protected Rectangle2D getValueLabelBounds(int position, Dimension2D dim) {
        double x = centerPoint.getX() - dim.getWidth()/2;
        return new Rectangle2D.Double(x, valueLabelCenterY - dim.getHeight()/2, dim.getWidth(), dim.getHeight());
    }

    public String getValueLabelWithDataPoint(DataPoint dataPoint) {
        return VanChartAttrHelper.getSlotCateAndValue(attrLabel.getGaugeValueLabelDetail().getContent(), dataPoint);
    }

}
