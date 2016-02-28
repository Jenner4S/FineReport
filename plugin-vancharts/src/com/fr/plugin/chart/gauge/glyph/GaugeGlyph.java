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
    public static final int DEFAULT_COLOR_NUMBER = 3;//�Զ�����ɫȡϵ�е�ǰ������ɫ
    public static final int CATEGORY_LABEL_GAP = 20;//�������ƾ���Բ��20��

    protected static final int GAP = 5;//����Բ��֮��Ŀ�϶��10.
    protected static final double PIE = 180;

    private static final int START_ANGLE = 90;//��ʼ�Ƕ�
    private static final int EXTENT = 360;//��Χ

    protected Rectangle2D bounds;//�����һ���߽�
    protected Point2D centerPoint;//Բ��
    protected double radius;//�뾶

    protected int startAngle; //�Ǳ�����ʼ�Ƕȣ�����Сֵ��
    protected int extent; //�Ǳ��̽Ƕȷ�Χ����ָ����ָ�̶ȷ�Χ���ٷֱȵ���ָ���̵��Ǹ����ķ�Χ��

    protected AttrLabel attrLabel;//�����ǩ���ٷֱȱ�ǩ��ֵ��ǩ

    protected GaugeDetailStyle gaugeDetailStyle;//ϵ�е�һЩ���ԣ�����ָ����ɫ��������ɫ�ȡ�

    protected VanChartGaugeAxisGlyph gaugeAxisGlyph;//�������һЩ���ԣ����������Сֵ������Ҫ��Ҫ�̶ȵ�λ�ȡ�

    protected Color[] defaultColors = new Color[DEFAULT_COLOR_NUMBER];//�̶Ⱥ���ɫ�����Զ�ʱ��ȡ��ɫ��ǰ����

    protected java.util.List colorList = new ArrayList();//�Զ���Ŀ̶Ⱥ���ɫ��bands����������Զ��ģ�����Ϊ�գ���ʹ��defaultColors��

    protected String cateOrPercentLabelString;//�����ǩ

    protected String valueLabelString;//ֵ��ǩ

    protected List<Number> valueList = new ArrayList<Number>();//��ָ���Ǳ�����ֵ�ļ��ϣ��ٷֱ����Ǻ���һ��ֵ�ļ���

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

    //���̱���
    protected void drawPaneBackground(Graphics2D g2d){
        GaugeGlyphHelper.drawCircle(g2d, centerPoint, radius, gaugeDetailStyle.getPaneBackgroundColor());
    }

    //�����ǩ
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

    //�Զ�ʱ����ʽ
    protected void setCateOrPercentLabelAutoFont(TextAttr textAttr){
    }

    //�������Ƶ�λ�úʹ�С
    protected Rectangle2D getCateOrPercentLabelBounds(int position, Dimension2D dim){
        double x = centerPoint.getX() - dim.getWidth()/2;
        double y = position == Constants.BOTTOM ? centerPoint.getY() + radius + CATEGORY_LABEL_GAP
                : centerPoint.getY() - radius - CATEGORY_LABEL_GAP;
        return new Rectangle2D.Double(x, y, dim.getWidth(), dim.getHeight());
    }

    // ֵ��ǩ
    protected void drawValueLabel(Graphics2D g2d, int resolution) {
        if(attrLabel != null && attrLabel.isEnable()){
            AttrLabelDetail valueLabelDetail = attrLabel.getGaugeValueLabelDetail();
            TextAttr valueLabelAttr = valueLabelDetail.getTextAttr();
            Dimension2D dim = GlyphUtils.calculateTextDimensionWithNoRotation(valueLabelString, valueLabelAttr, resolution);
            Rectangle2D labelBounds = getValueLabelBounds(valueLabelDetail.getPosition(), dim);
            GaugeGlyphHelper.drawStrings(g2d, valueLabelString, valueLabelAttr, labelBounds, resolution);
        }
    }

    //ֵ��ǩ��λ�úʹ�С
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
