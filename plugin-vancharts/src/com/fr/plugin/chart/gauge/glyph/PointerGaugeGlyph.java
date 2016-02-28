package com.fr.plugin.chart.gauge.glyph;

import com.fr.chart.base.GlyphUtils;
import com.fr.chart.base.TextAttr;
import com.fr.chart.chartglyph.DataPoint;
import com.fr.general.ComparatorUtils;
import com.fr.plugin.chart.attr.VanChartAttrHelper;
import com.fr.plugin.chart.base.AttrLabel;
import com.fr.plugin.chart.base.AttrLabelDetail;
import com.fr.plugin.chart.gauge.GaugeDetailStyle;
import com.fr.plugin.chart.glyph.axis.VanChartGaugeAxisGlyph;
import com.fr.stable.Constants;

import java.awt.*;
import java.awt.geom.*;
import java.text.Format;

/**
 * ��ָ���Ǳ��̡�300�ȡ�
 */
public class PointerGaugeGlyph extends GaugeGlyph {
    private static final long serialVersionUID = 1317272737007992913L;
    private static final double OUT = 0.95;//�̶Ⱦ����Ǳ�����Χ�ļ��0.05R��

    private static final double HINGE_BACKGROUND = 0.16;//��Ŧ�����뾶ռ�뾶����.
    private static final double HINGE = 0.07;//��Ŧ�뾶ռ�뾶����
    private static final double ARROW_HEIGHT = 0.9;//ָ���ռ�뾶����
    private static final double ARROW_WIDTH = 0.04;//ָ���ռ�뾶����
    private static final double MAIN_TICK = 0.1;//��Ҫ�̶���ռ�뾶����
    private static final double SEC_TICK = 0.05;//��Ҫ�̶���ռ�뾶����
    private static final double AXIS_LABEL = 0.01 + MAIN_TICK;//�������ǩ���ھ��α߾�Բ�ܵľ���ռ�뾶������
    private static final double VALUE_LABEL_ROUND = 0.02;//ֵ��ǩ���ھ��ο�Բ�ǰ뾶ռ�뾶������
    protected static final double VALUE_GAP = 0.5;//ֵ��ǩ�������Ҽ��м��ռ�ֺ�0.5

    private static final int START_ANGLE = 240;//��ʼ�Ƕ�
    private static final int EXTENT = -300;//��Χ

    private static final double ANGLE1 = 225;
    private static final double ANGLE2 = 180;
    private static final double ANGLE3 = 0;
    private static final double ANGLE4 = -45;

    protected static final double PROPORTION = 5;

    protected Rectangle2D bigBounds = new Rectangle();//ֵ��ǩ���ھ��ο�����߽硣
    protected double oneLineHeight;//ֵ��ǩһ�б�ǩ�ĸߡ�

    public PointerGaugeGlyph(Rectangle2D bounds, AttrLabel attrLabel, GaugeDetailStyle gaugeDetailStyle, VanChartGaugeAxisGlyph gaugeAxisGlyph) {
        super(bounds, attrLabel, gaugeDetailStyle, gaugeAxisGlyph);
    }

    protected void initStartAngleAndExtent() {
        this.startAngle = START_ANGLE;
        this.extent = EXTENT;
    }

    protected void calculateRadiusAndCenterPoint(double width, double height, double cateLabelHeight) {
        double temp = height - cateLabelHeight - CATEGORY_LABEL_GAP;
        radius = Math.min(width, temp)/2;

        double x = width / 2;
        double y = height / 2;
        if(attrLabel != null && attrLabel.isEnable()){
            int position = attrLabel.getAttrLabelDetail().getPosition();
            if(position == Constants.BOTTOM){//�·�
                double gap = cateLabelHeight + CATEGORY_LABEL_GAP + radius;
                if(gap > (height - y)){//ֱ�ӷ��������߽�����ģ������ǩ�Ų��¡�
                    y = height - gap;
                }
            } else {//�Ϸ�
                double gap = cateLabelHeight + CATEGORY_LABEL_GAP + radius;
                if(gap > y) {//ֱ�ӷ��������߽�����ģ������ǩ�Ų��¡�
                    y = gap;
                }
            }
        }
        centerPoint = new Point2D.Double(x, y);
    }

    protected void drawInfo(Graphics2D g2d, int resolution){
        drawPaneBackground(g2d);//���̱���

        drawHingeBackground(g2d);//��Ŧ����

        initBigBounds();
        drawTicksAndAxisLabel(g2d, resolution);// �̶ȺͿ̶ȱ�ǩ

        drawValueLabel(g2d, resolution);//ֵ��ǩ

        drawArrow(g2d);//ָ��

        drawHinge(g2d); //��Ŧ

        drawCateOrPercentageLabel(g2d, resolution);//��������
    }

    //һ�߻��̶ȱ�ǩһ����СbigBounds
    private void initBigBounds() {
        if(attrLabel != null && attrLabel.isEnable()){
            AttrLabelDetail valueLabelDetail = attrLabel.getGaugeValueLabelDetail();
            TextAttr valueLabelAttr = valueLabelDetail.getTextAttr();
            oneLineHeight = GaugeGlyphHelper.calculateOneLineHeight(valueLabelAttr.getFRFont());
            setFirstBigBounds();
        }
    }

    protected void setFirstBigBounds() {
        //��Ȼ�����ü���������߻��Ǿ�ȷһ�㡣��Ϊ��������������ǩ�Ļ���bigBounds�õľ��ǳ�ʼ����ġ�
        double gap = HINGE_BACKGROUND * radius + oneLineHeight * VALUE_GAP;
        bigBounds.setFrame(centerPoint.getX() - radius, centerPoint.getY() + gap, radius * 2, Math.sqrt(2) * radius / 2 - gap);
    }

    //��Ŧ����
    private void drawHingeBackground(Graphics2D g2d){
        GaugeGlyphHelper.drawCircle(g2d, centerPoint, getHingeBackgroundSize() * radius, gaugeDetailStyle.getHingeBackgroundColor());
    }

    protected double getHingeBackgroundSize() {
        return HINGE_BACKGROUND;
    }

    // ָ��
    private void drawArrow(Graphics2D g2d) {
        for(Number value : valueList){
            g2d.setColor(GaugeGlyphHelper.getColor(gaugeDetailStyle.getNeedleColor()));

            double min = gaugeAxisGlyph.getMinValue();
            double max = gaugeAxisGlyph.getMaxValue();
            double valueAngle = startAngle + this.extent/(max - min) * (value.doubleValue() - min);
            valueAngle = Math.max(valueAngle, startAngle + this.extent);
            valueAngle = Math.min(valueAngle, startAngle);
            double valueX = centerPoint.getX() + radius * ARROW_HEIGHT * Math.cos(Math.PI * (valueAngle / PIE));
            double valueY = centerPoint.getY() - radius * ARROW_HEIGHT * Math.sin(Math.PI * (valueAngle / PIE));

            double leftX = centerPoint.getX() + radius * ARROW_WIDTH / 2 * Math.cos(Math.PI * (valueAngle - 90) / PIE);
            double leftY = centerPoint.getY() - radius * ARROW_WIDTH / 2 * Math.sin(Math.PI * (valueAngle - 90) / PIE);
            double rightX = centerPoint.getX() + radius * ARROW_WIDTH / 2 * Math.cos(Math.PI * (valueAngle + 90) / PIE);
            double rightY = centerPoint.getY() - radius * ARROW_WIDTH / 2 * Math.sin(Math.PI * (valueAngle + 90) / PIE);

            GeneralPath arrowPath = new GeneralPath();
            arrowPath.moveTo(valueX, valueY);
            arrowPath.lineTo(leftX, leftY);
            arrowPath.lineTo(rightX, rightY);
            arrowPath.lineTo(valueX, valueY);
            g2d.fill(arrowPath);
        }
    }

    //��Ŧ
    private void drawHinge(Graphics2D g2d){
        GaugeGlyphHelper.drawCircle(g2d, centerPoint, getHingeSize() * radius, gaugeDetailStyle.getHingeColor());
    }

    protected double getHingeSize() {
        return HINGE;
    }

    // �̶ȺͿ̶ȱ�ǩ
    private void drawTicksAndAxisLabel(Graphics2D g2d, int resolution) {
        Object lastHint = g2d.getRenderingHint(RenderingHints.KEY_ANTIALIASING);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        double mainTick = gaugeAxisGlyph.getMainUnit();
        double secTick = gaugeAxisGlyph.getSecUnit();
        double mainTickLength = MAIN_TICK * radius;
        double secTickLength = SEC_TICK * radius;
        double minValue = gaugeAxisGlyph.getMinValue();
        double maxValue = gaugeAxisGlyph.getMaxValue();
        double mainTickAngleGap = this.extent/(maxValue - minValue) * mainTick;//��Ҫ�̶ȵ�λռ�ĽǶ�
        double secTickAngleGap = this.extent/(maxValue - minValue) * secTick;//��Ҫ�̶ȵ�λռ�ĽǶ�
        TextAttr labelAttr = gaugeAxisGlyph.getTextAttr();
        Format format = gaugeAxisGlyph.getFormat();

        double tempAngle = this.startAngle;
        for(double tickValue = minValue; tickValue <= maxValue + 1.0E-10; tickValue+= mainTick){
            Color tickColor = getValueColor(tickValue, minValue, maxValue);
            drawTickLine(g2d, mainTickLength, tickColor, tempAngle);
            drawTickLabel(g2d, tickValue, labelAttr, tempAngle, format, resolution);
            tempAngle += mainTickAngleGap;
        }

        tempAngle = this.startAngle;
        for(double tickValue = minValue; tickValue <= maxValue; tickValue += secTick){
            Color tickColor = getValueColor(tickValue, minValue, maxValue);
            drawTickLine(g2d, secTickLength, tickColor, tempAngle);
            tempAngle += secTickAngleGap;
        }

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, lastHint);
    }

    //�̶�
    private void drawTickLine(Graphics2D g2d, double tickLength, Color tickColor, double valueAngle){
        double startX = centerPoint.getX() + radius * OUT * Math.cos(Math.PI * (valueAngle / PIE));
        double startY = centerPoint.getY() - radius * OUT * Math.sin(Math.PI * (valueAngle / PIE));
        double endX = centerPoint.getX() + (radius * OUT - tickLength) * Math.cos(Math.PI * (valueAngle / PIE));
        double endY = centerPoint.getY() - (radius * OUT - tickLength) * Math.sin(Math.PI * (valueAngle / PIE));

        g2d.setStroke(new BasicStroke(1, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2d.setColor(tickColor);
        g2d.draw(new Line2D.Double(endX, endY, startX, startY));
    }

    //�̶ȱ�ǩ�����������ǩ
    private void drawTickLabel(Graphics2D g2d, double tickValue, TextAttr labelAttr, double valueAngle, Format format, int resolution){
        if(gaugeAxisGlyph.isShowAxisLabel()){
            String label = GaugeGlyphHelper.value2String(tickValue, format);
            Dimension2D dim = GlyphUtils.calculateTextDimensionWithNoRotation(label, labelAttr, resolution);
            double gap;//����������Բ�ĵ����߽��ھ��ε�һ�����������ĵؾ��롣
            if(dim.getHeight()/dim.getWidth() < Math.abs(Math.tan(Math.PI * (valueAngle / PIE)))){
                //�������±�
                gap = dim.getHeight()/(2 * Math.abs(Math.sin(Math.PI * (valueAngle / PIE))));
            } else {
                //�������ұ�
                gap = dim.getWidth()/(2 * Math.abs(Math.cos(Math.PI * (valueAngle / PIE))));
            }
            double temp = radius * OUT - radius * AXIS_LABEL - gap;
            double PX = centerPoint.getX() + temp * Math.cos(Math.PI * (valueAngle / PIE));
            double PY = centerPoint.getY() - temp * Math.sin(Math.PI * (valueAngle / PIE));
            Rectangle2D labelBounds = new Rectangle2D.Double(PX - dim.getWidth()/2, PY - dim.getHeight()/2, dim.getWidth(), dim.getHeight());
            GlyphUtils.drawStrings(g2d, label, labelAttr, labelBounds, resolution);
            if(attrLabel != null && attrLabel.isEnable()){
                calculateValueBackgroundBounds(labelBounds, valueAngle);
            }
        }
    }

    //240��-60��
    protected void calculateValueBackgroundBounds(Rectangle2D labelBounds, double valueAngle){
        double safe = labelBounds.getHeight()/PROPORTION;
        Rectangle2D safeBounds = new Rectangle2D.Double(labelBounds.getX() - safe, labelBounds.getY() - safe,
                labelBounds.getWidth() + safe * 2, labelBounds.getHeight() + safe * 2);
        if(valueAngle >= ANGLE1 || valueAngle <= ANGLE4){
            if(bigBounds.getMaxY() > safeBounds.getY()){
                bigBounds.setFrame(bigBounds.getX(), bigBounds.getY(), bigBounds.getWidth(), safeBounds.getY() - bigBounds.getY());
            }
        } else if(valueAngle >= ANGLE2){
            if(bigBounds.getX() < safeBounds.getMaxX()){
                bigBounds.setFrame(safeBounds.getMaxX(), bigBounds.getY(), bigBounds.getWidth(), bigBounds.getHeight());
            }
        } else if(valueAngle <= ANGLE3) {
            if(bigBounds.getMaxX() > safeBounds.getX()){
                bigBounds.setFrame(bigBounds.getX(), bigBounds.getY(), safeBounds.getX() - bigBounds.getX(), bigBounds.getHeight());
            }
        }
    }

    //�������Ƶ�λ�úʹ�С
    protected Rectangle2D getCateOrPercentLabelBounds(int position, Dimension2D dim){
        double x = centerPoint.getX() - dim.getWidth()/2;
        double y = position == Constants.BOTTOM ? centerPoint.getY() + radius + CATEGORY_LABEL_GAP - dim.getHeight()/PROPORTION
                : centerPoint.getY() - radius - CATEGORY_LABEL_GAP - dim.getHeight();
        return new Rectangle2D.Double(x, y, dim.getWidth(), dim.getHeight());
    }

    // ֵ��ǩ
    protected void drawValueLabel(Graphics2D g2d, int resolution) {
        if(attrLabel == null || !attrLabel.isEnable()){
            return;
        }
        if(valueLabelString.isEmpty()){
            return;
        }

        AttrLabelDetail valueLabelDetail = attrLabel.getGaugeValueLabelDetail();
        TextAttr valueLabelAttr = valueLabelDetail.getTextAttr();
        Dimension2D dim = GlyphUtils.calculateTextDimensionWithNoRotation(valueLabelString, valueLabelAttr, resolution);
        double backWidth = dim.getWidth() + oneLineHeight * VALUE_GAP * 2;
        double backHeight = dim.getHeight() + oneLineHeight * VALUE_GAP * 2;

        double gap1 = centerPoint.getX() - bigBounds.getX();
        double gap2 = bigBounds.getMaxX() - centerPoint.getX();
        double gap = Math.min(gap1, gap2);//��֤�Գ�
        backWidth = Math.min(gap * 2, backWidth);
        backHeight = Math.min(backHeight, bigBounds.getHeight());

        double backX = centerPoint.getX() - backWidth/2;
        double backY = bigBounds.getY() + (bigBounds.getHeight() - backHeight)/2;

        double verdanaGap = 0;
        if(System.getProperties().getProperty("os.name").toUpperCase().contains("WINDOWS")
                && ComparatorUtils.equals(valueLabelAttr.getFRFont().getFamily().toLowerCase(), "verdana")){
            //���Ӧ����windows��verdana�����е����⣬����һ�¡�
            verdanaGap = 8;
        }
        RoundRectangle2D backRect = new RoundRectangle2D.Double(backX, backY, backWidth + verdanaGap, backHeight,
                radius * VALUE_LABEL_ROUND, radius * VALUE_LABEL_ROUND);
        g2d.setColor(GaugeGlyphHelper.getColor(valueLabelDetail.getBackgroundColor()));
        g2d.fill(backRect);

        Rectangle2D labelRect = new Rectangle2D.Double(backX + oneLineHeight * VALUE_GAP, backY + oneLineHeight * VALUE_GAP,
                backWidth - oneLineHeight * VALUE_GAP * 2, backHeight - oneLineHeight * VALUE_GAP * 2);
        Shape oldClip = g2d.getClip();
        g2d.clipRect((int) backRect.getX(), (int) backRect.getY(), (int) backRect.getWidth(), (int) backRect.getHeight());
        GaugeGlyphHelper.drawMultiLineText(g2d, valueLabelString, valueLabelAttr, labelRect, resolution);
        g2d.setClip(oldClip);
    }

    public String getCateOrPercentLabelWithDataPoint(DataPoint dataPoint) {
        return VanChartAttrHelper.getCategoryName(attrLabel.getAttrLabelDetail().getContent(), dataPoint);
    }

    public String getValueLabelWithDataPoint(DataPoint dataPoint) {
        return VanChartAttrHelper.getLabelWithOutCategory(attrLabel.getGaugeValueLabelDetail().getContent(), dataPoint);
    }
}
