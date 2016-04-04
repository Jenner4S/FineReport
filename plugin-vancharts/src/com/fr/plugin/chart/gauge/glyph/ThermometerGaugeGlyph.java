package com.fr.plugin.chart.gauge.glyph;

import com.fr.chart.base.GlyphUtils;
import com.fr.chart.base.TextAttr;
import com.fr.general.FRFont;
import com.fr.plugin.chart.base.AttrLabel;
import com.fr.plugin.chart.base.AttrLabelDetail;
import com.fr.plugin.chart.column.ColumnTopDownShadeStyle;
import com.fr.plugin.chart.gauge.GaugeDetailStyle;
import com.fr.plugin.chart.glyph.axis.VanChartGaugeAxisGlyph;
import com.fr.stable.Constants;

import java.awt.*;
import java.awt.geom.*;

/**
 * �Թ��Ǳ��̡�
 */
public class ThermometerGaugeGlyph extends GaugeGlyph {

    private static final long serialVersionUID = 2377125930562201704L;
    private static final int R = 5;//�Թܿ�10px��
    private static final int MAIN_TICK = 6;//��Ҫ�̶��߳��ȡ�
    private static final int SEC_TICK = 4;//��Ҫ�̶��߳��ȡ�
    private static final double SMALL_RADIUS = 0.6 * R;//ֵ����λ��СԲ�뾶��
    private static final double BIG_RADIUS = 1.2 * R;//ֵ����λ�ô�Բ�뾶��
    private static final int AXIS_GAP = 4;//������̶Ⱦ����Թܼ�ࡣ������̶Ⱦ����������ǩ��ࡣ
    private static final double LABEL_THERMOMETER_GAP = 0.8;//��ǩ�����Թ�ռ���ڱ�ǩ�߶ȵ�0.8.
    private static final double LABEL_LABEL_GAP = 0.5;//�ٷֱȱ�ǩ��ֵ��ǩ�м��ࡣ
    private static final double COLOR = 0.1;
    private static final double TEMP = 2;

    private Point2D valueLabelCenterPoint;
    private Point2D percentLabelCenterPoint;
    private RoundRectangle2D thermometerBounds;
    private RoundRectangle2D seriesBounds;
    private Point2D valuePoint;
    private Color valueColor;

    public ThermometerGaugeGlyph(Rectangle2D bounds, AttrLabel attrLabel, GaugeDetailStyle gaugeDetailStyle, VanChartGaugeAxisGlyph gaugeAxisGlyph) {
        super(bounds, attrLabel, gaugeDetailStyle, gaugeAxisGlyph);
    }

    //�Ƿ������ŵ��Թܡ����Ǳ������岼�ַ����෴��
    private boolean isVertical(){
        return gaugeDetailStyle.isHorizontalLayout();
    }

    //�����Թܡ������ᡢ�ٷֱȱ�ǩ��ֵ��ǩ��
    public void doLayout(double cateLabelHeight, int resolution){
        boolean hasPercent = false, hasValue= false;
        int percentPosition = -1, valuePosition = -1;
        Dimension2D percentDim = new Dimension(0,0), valueDim = new Dimension(0,0);//���ı���С�������߽�ȡ�
        double percentOneLineHeight = 0, valueOneLineHeight = 0;//��Ϊ�漰�������ı��������ʱ��ֻ��Ҫ�иߣ�0.8*�иߡ�

        if(attrLabel != null && attrLabel.isEnable()){
            AttrLabelDetail percentDetail = attrLabel.getAttrLabelDetail();
            AttrLabelDetail valueDetail = attrLabel.getGaugeValueLabelDetail();
            percentPosition = percentDetail.getPosition();
            valuePosition = valueDetail.getPosition();
            FRFont percentFont = percentDetail.getTextAttr().getFRFont();
            FRFont valueFont = valueDetail.getTextAttr().getFRFont();
            percentDim = GaugeGlyphHelper.calculateTextDimension(cateOrPercentLabelString, percentFont, resolution);
            valueDim = GaugeGlyphHelper.calculateTextDimension(valueLabelString, valueFont, resolution);
            percentOneLineHeight = GaugeGlyphHelper.calculateOneLineHeight(percentFont);
            valueOneLineHeight = GaugeGlyphHelper.calculateOneLineHeight(valueFont);
            hasPercent = percentDetail.getContent().hasLabelContent();
            hasValue = valueDetail.getContent().hasLabelContent();
        }

        String maxString = GaugeGlyphHelper.value2String(gaugeAxisGlyph.getMaxValue(), gaugeAxisGlyph.getFormat());
        String minString = GaugeGlyphHelper.value2String(gaugeAxisGlyph.getMinValue(), gaugeAxisGlyph.getFormat());
        Dimension2D axisLabelMaxDim = GlyphUtils.calculateTextDimensionWithNoRotation(maxString, gaugeAxisGlyph.getTextAttr(), resolution);
        Dimension2D axisLabelMinDim = GlyphUtils.calculateTextDimensionWithNoRotation(minString, gaugeAxisGlyph.getTextAttr(), resolution);
        Dimension2D axisLabelDim = new Dimension((int)Math.max(axisLabelMaxDim.getWidth(), axisLabelMinDim.getWidth()), (int)axisLabelMaxDim.getHeight());

        if(isVertical()){//�Թܴ�ֱ��
            doVerticalLayout(percentDim, valueDim, axisLabelDim, percentOneLineHeight, valueOneLineHeight,
                    hasPercent, percentPosition, hasValue, valuePosition);
            radius = bounds.getHeight();
        } else {//�Թ�ˮƽ��
            doHorizontalLayout(percentDim, valueDim, axisLabelDim, percentOneLineHeight, valueOneLineHeight,
                    hasPercent, percentPosition, hasValue, valuePosition);
            radius = bounds.getWidth();
        }
    }

    //�Թ�ˮƽ���ַ�����y������ϵ���һ��������������0�����ڸ߶ȣ����������ĸ߶Ⱥ͡���������߶Ⱥ����ʵ��y����ʼλ�ã�����������ƽ�ơ�
    private void doHorizontalLayout(Dimension2D percentDim, Dimension2D valueDim, Dimension2D axisLabelDim,
                                    double percentOneLineHeight, double valueOneLineHeight,
                                    boolean hasPercent, int percentPosition, boolean hasValue, int valuePosition) {
        double axisAndThermometerHeight = gaugeAxisGlyph.isShowAxisLabel() ? (axisLabelDim.getHeight() + AXIS_GAP) : 0;
        axisAndThermometerHeight += (MAIN_TICK + AXIS_GAP + R * 2);
        boolean percentTop = hasPercent && percentPosition == Constants.TOP;
        boolean percentBottom = hasPercent && percentPosition == Constants.BOTTOM;
        boolean valueTop = hasValue && valuePosition == Constants.TOP;
        boolean valueBottom = hasValue && valuePosition == Constants.BOTTOM;

        double percentY = 0,valueY = 0,thermometerAndAxisY;//��Щ��������������Ǳ��̵�ԭ�㡣
        double topGap = 0;
        //y������ϵ���һ��������������0�����ڸ߶ȣ����������ĸ߶Ⱥ�
        if(percentTop){
            percentY = topGap;
            topGap += percentDim.getHeight();
        }
        if(valueTop){
            topGap += (percentTop ? percentOneLineHeight * LABEL_LABEL_GAP : 0);
            valueY = topGap;
            topGap += valueDim.getHeight();
        }
        topGap += (valueTop ? valueOneLineHeight :(percentTop ? percentOneLineHeight : 0)) * LABEL_THERMOMETER_GAP;
        thermometerAndAxisY = topGap;
        topGap += axisAndThermometerHeight;
        if(percentBottom){
            topGap += percentOneLineHeight * LABEL_THERMOMETER_GAP;
            percentY = topGap;
            topGap += percentDim.getHeight();
        }
        if(valueBottom){
            topGap += (percentBottom ? percentOneLineHeight * LABEL_LABEL_GAP : valueOneLineHeight * LABEL_THERMOMETER_GAP);
            valueY = topGap;
            topGap += valueDim.getHeight();
        }

        //��������߶Ⱥ����ʵ��y����ʼλ��
        topGap = (bounds.getHeight() - topGap)/2;
        //����������ƽ��
        percentY += topGap;valueY+=topGap;thermometerAndAxisY+=topGap;
        initHorizontalBounds(percentDim, valueDim, axisLabelDim, percentY, valueY, thermometerAndAxisY, axisAndThermometerHeight);
    }

    private void initHorizontalBounds(Dimension2D percentDim, Dimension2D valueDim, Dimension2D axisLabelDim,
                                      double percentY, double valueY, double thermometerAndAxisY, double axisAndThermometerHeight) {
        valueLabelCenterPoint = new Point2D.Double(bounds.getWidth()/2, valueY + valueDim.getHeight()/2);
        percentLabelCenterPoint = new Point2D.Double(bounds.getWidth()/2, percentY + percentDim.getHeight()/2);
        double leftGap = GAP + axisLabelDim.getWidth()/2;
        double height = 2 * R;
        double thermometerY = thermometerAndAxisY + (axisAndThermometerHeight - height);
        double length = bounds.getWidth() - 2 * leftGap;
        thermometerBounds = new RoundRectangle2D.Double(leftGap, thermometerY, length, height, height, height);
        double value_x = getValue(length, leftGap, false);
        valuePoint = new Point2D.Double(value_x, thermometerY + R);
        seriesBounds = new RoundRectangle2D.Double(leftGap, thermometerY, value_x - leftGap, height, height, height);
    }

    //�Թܴ�ֱ���ַ�����������������˳���ȼ��������0��λ�ú��ܺͣ�����������ƽ��
    private void doVerticalLayout(Dimension2D percentDim, Dimension2D valueDim, Dimension2D axisLabelDim,
                                  double percentOneLineHeight, double valueOneLineHeight,
                                  boolean hasPercent, int percentPosition, boolean hasValue, int valuePosition) {
        boolean percentLeft = hasPercent && percentPosition == Constants.LEFT;
        boolean percentRight = hasPercent && percentPosition == Constants.RIGHT;
        boolean valueLeft = hasValue && valuePosition == Constants.LEFT;
        boolean valueRight = hasValue && valuePosition == Constants.RIGHT;
        boolean eachSide = (percentLeft && valueRight) || (percentRight && valueLeft);//�ٷֱȱ�ǩ��ֵ��ǩ����ʾ����ʾ������
        double label_thermometer_gap = LABEL_THERMOMETER_GAP * Math.max(percentOneLineHeight, valueOneLineHeight);
        double axisAndThermometerWidth = R * 2 + AXIS_GAP + MAIN_TICK;//�Թܼ���������Ŀ��
        axisAndThermometerWidth += (gaugeAxisGlyph.isShowAxisLabel() ? AXIS_GAP + axisLabelDim.getWidth() : 0);

        double percentX,valueX,thermometerX;//��Щ��������������Ǳ��̵�ԭ�㡣
        double leftGap = 0;
        if(eachSide){//�ٷֱȱ�ǩ��ֵ��ǩ����ʾ����ʾ������
            if(percentRight){//ֵ���Թܺ������ᡢ�ٷֱ�
                valueX = leftGap;
                leftGap += (valueDim.getWidth() + label_thermometer_gap);
                thermometerX = leftGap;
                leftGap += (axisAndThermometerWidth + label_thermometer_gap);
                percentX = leftGap;
                leftGap += percentDim.getWidth();
            } else {//�ٷֱȡ��Թܺ������ᡢֵ
                percentX = leftGap;
                leftGap += (percentDim.getWidth() + label_thermometer_gap);
                thermometerX = leftGap;
                leftGap += (axisAndThermometerWidth + label_thermometer_gap);
                valueX = leftGap;
                leftGap += valueDim.getWidth();
            }
        } else {//����ʾ��һ�߻���ʾһ���򶼲���ʾ
            double maxLabelWidth = Math.max(percentDim.getWidth(), valueDim.getWidth());
            if(percentRight || valueRight){//�ٷֱ����ұ߻���ʾ��ֵ����߻���ʾ
                thermometerX = leftGap;
                leftGap += (axisAndThermometerWidth + label_thermometer_gap);
                percentX = leftGap;
                valueX = leftGap;
                leftGap += maxLabelWidth;
            } else {//�ٷֱ�����߻���ʾ��ֵ����߻���ʾ
                percentX = leftGap + maxLabelWidth - percentDim.getWidth();//�Ҷ���
                valueX = leftGap + maxLabelWidth - valueDim.getWidth();//�Ҷ���
                leftGap += (maxLabelWidth + label_thermometer_gap);
                thermometerX = leftGap;
                leftGap += axisAndThermometerWidth;
            }
        }

        leftGap = (bounds.getWidth() - leftGap)/2;
        valueX+=leftGap;percentX+=leftGap;thermometerX+=leftGap;
        initVerticalBounds(percentDim, valueDim, axisLabelDim, percentOneLineHeight, percentX, valueX, eachSide, hasPercent, thermometerX);
    }

    private void initVerticalBounds(Dimension2D percentDim, Dimension2D valueDim, Dimension2D axisLabelDim,
                                    double percentOneLineHeight,
                                    double percentX, double valueX, boolean eachSide, boolean hasPercent, double thermometerX){
        double width = 2 * R;
        double topGap = GAP + axisLabelDim.getHeight()/2;
        double length = bounds.getHeight() - 2 * topGap;
        double y = getValue(length, topGap, true);
        thermometerBounds = new RoundRectangle2D.Double(thermometerX, topGap, width, length, width, width);
        valuePoint = new Point2D.Double(thermometerX + R, y);
        seriesBounds = new RoundRectangle2D.Double(thermometerX, y, width, bounds.getHeight() - topGap - y, width, width);
        double bottom = bounds.getHeight() - topGap - R;
        if(eachSide){
            if(y + percentDim.getHeight()/2 > bottom){
                percentLabelCenterPoint = new Point2D.Double(percentX + percentDim.getWidth()/2, bottom - percentDim.getHeight()/2);
            } else {
                percentLabelCenterPoint = new Point2D.Double(percentX + percentDim.getWidth()/2, y);
            }
            if(y + valueDim.getHeight()/2 > bottom){
                valueLabelCenterPoint = new Point2D.Double(valueX + valueDim.getWidth()/2, bottom - valueDim.getHeight()/2);
            } else {
                valueLabelCenterPoint = new Point2D.Double(valueX + valueDim.getWidth()/2, y);
            }
        } else{
            double sumGap = hasPercent ? percentDim.getHeight()/2 + percentOneLineHeight * LABEL_LABEL_GAP : 0;
            sumGap += valueDim.getHeight();
            if(y + sumGap > bottom){
                y = bottom - sumGap;
            }
            percentLabelCenterPoint = new Point2D.Double(percentX + percentDim.getWidth()/2, y);
            valueLabelCenterPoint = new Point2D.Double(valueX + valueDim.getWidth()/2, y + sumGap - valueDim.getHeight()/2);
        }
    }

    private double getValue(double length, double gap, boolean isVertical) {
        length = length - 2 * R;//������ĳ��ȱ��Թܳ���������R
        if(valueList.isEmpty()){
            return -1;
        }
        double value = valueList.get(0).doubleValue();
        double min = gaugeAxisGlyph.getMinValue();
        double max = gaugeAxisGlyph.getMaxValue();
        value = Math.max(value, min);
        value = Math.min(value, max);
        valueColor = getValueColor(value, min, max);
        return isVertical ? getValueY(length, gap, min, max, value) : getValueX(length, gap, min, max, value);
    }

    //�Թܴ�ֱʱ
    private double getValueY(double length, double topGap, double min, double max, double value) {
        double valueLength = Math.min(length, length / (max - min) * (value - min));
        return bounds.getHeight() - topGap - valueLength - R;
    }

    //�Թ�ˮƽʱ
    private double getValueX(double length, double leftGap, double min, double max, double value) {
        return leftGap + length/(max - min) * (value - min) + R;
    }

    protected void drawInfo(Graphics2D g2d, int resolution){
        drawThermometer(g2d);//�Թ���ص�ͼ��

        drawTicksAndAxisLabel(g2d, resolution);//������

        drawCateOrPercentageLabel(g2d, resolution);//�ٷֱȱ�ǩ
        drawValueLabel(g2d, resolution);//ֵ��ǩ
    }

    //�Թ���ص�ͼ��
    private void drawThermometer(Graphics2D g2d) {
        g2d.setColor(GaugeGlyphHelper.getColor(gaugeDetailStyle.getSlotBackgroundColor()));
        g2d.fill(thermometerBounds);

        ThermometerTopDownShadeStyle series = new ThermometerTopDownShadeStyle(valueColor, seriesBounds, !isVertical());
        series.paintStyle(g2d);

        GaugeGlyphHelper.drawCircle(g2d, valuePoint, BIG_RADIUS, getDarkerValueColor(valueColor));
        GaugeGlyphHelper.drawCircle(g2d, valuePoint, SMALL_RADIUS, gaugeDetailStyle.getNeedleColor());

    }

    private Color getDarkerValueColor(Color color) {
        color = color == null ? new Color(255, 255, 255, 0) : color;

        float[] hsb = new float[3];
        Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), hsb);

        return Color.getHSBColor(hsb[0], Math.min(1, (float)(hsb[1] * (1 + COLOR))), Math.max(0, (float)(hsb[2] * (1 - COLOR))));
    }

    //������
    private void drawTicksAndAxisLabel(Graphics2D g2d, int resolution) {
        double max = gaugeAxisGlyph.getMaxValue();
        double min = gaugeAxisGlyph.getMinValue();
        double mainTick = gaugeAxisGlyph.getMainUnit();
        double secTick = gaugeAxisGlyph.getSecUnit();
        Color mainTickColor = gaugeAxisGlyph.getMainTickColor();
        Color secTickColor = gaugeAxisGlyph.getSecTickColor();
        TextAttr textAttr = gaugeAxisGlyph.getTextAttr();
        double length = (isVertical() ? thermometerBounds.getHeight() : thermometerBounds.getWidth()) - R * 2;
        double mainTickGap = length/(max - min) * mainTick;
        double secTickGap = length/(max - min) * secTick;

        if(isVertical()){
            //���ϵ��£��Ӵ�С
            double startX = thermometerBounds.getMaxX() + AXIS_GAP;//ʼ�ղ���
            double y = thermometerBounds.getY() + R;//�����ƶ�
            double endX = startX + SEC_TICK;
            double labelStartX = endX + AXIS_GAP;
            for(double tickValue = max; tickValue >= min; tickValue -= secTick){
                drawTickLine(g2d, secTickColor, startX, y, endX, y);
                y += secTickGap;
            }
            endX = startX + MAIN_TICK;
            y = thermometerBounds.getY() + R;
            for(double tickValue = max; tickValue >= min - 1.0E-10; tickValue -= mainTick){
                drawTickLine(g2d, mainTickColor, startX, y, endX, y);
                drawTickLabel4Vertical(g2d, resolution, textAttr, tickValue, y, labelStartX);
                y += mainTickGap;
            }


        } else {
            //�����ң���С����
            double endY = thermometerBounds.getY() - AXIS_GAP;//ʼ�ղ���
            double x = thermometerBounds.getX() + R;//�����ƶ�
            double startY = endY - SEC_TICK;
            double labelEndY = startY - AXIS_GAP;
            for(double tickValue = min; tickValue <= max; tickValue += secTick){
                drawTickLine(g2d, secTickColor, x, startY, x, endY);
                x += secTickGap;
            }
            x = thermometerBounds.getX() + R;
            startY = endY - MAIN_TICK;
            for(double tickValue = min; tickValue <= max + 1.0E-10; tickValue += mainTick){
                drawTickLine(g2d, mainTickColor, x, startY, x, endY);
                drawTickLabel4Horizontal(g2d, resolution, textAttr, tickValue, x, labelEndY);
                x += mainTickGap;
            }
        }
    }

    private void drawTickLine(Graphics2D g2d, Color tickColor, double startX, double startY, double endX, double endY) {
        g2d.setStroke(new BasicStroke(1, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2d.setColor(GaugeGlyphHelper.getColor(tickColor));
        g2d.draw(new Line2D.Double(endX, endY, startX, startY));
    }

    private void drawTickLabel4Horizontal(Graphics2D g2d, int resolution, TextAttr textAttr, double tickValue, double centerX, double endY) {
        if(gaugeAxisGlyph.isShowAxisLabel()){
            String label = GaugeGlyphHelper.value2String(tickValue, gaugeAxisGlyph.getFormat());
            Dimension2D dim = GlyphUtils.calculateTextDimensionWithNoRotation(label, textAttr, resolution);
            Rectangle2D bound = new Rectangle2D.Double(centerX - dim.getWidth()/2, endY - dim.getHeight(), dim.getWidth(), dim.getHeight());
            GlyphUtils.drawStrings(g2d, label, textAttr, bound, resolution);
        }
    }

    private void drawTickLabel4Vertical(Graphics2D g2d, int resolution, TextAttr textAttr, double tickValue, double centerY, double startX) {
        if(gaugeAxisGlyph.isShowAxisLabel()){
            String label = GaugeGlyphHelper.value2String(tickValue, gaugeAxisGlyph.getFormat());
            Dimension2D dim = GlyphUtils.calculateTextDimensionWithNoRotation(label, textAttr, resolution);
            Rectangle2D bound = new Rectangle2D.Double(startX, centerY - dim.getHeight()/2 - TEMP, dim.getWidth(), dim.getHeight());
            GlyphUtils.drawStrings(g2d, label, textAttr, bound, resolution);
        }
    }

    //�������Ƶ�λ�úʹ�С
    protected Rectangle2D getCateOrPercentLabelBounds(int position, Dimension2D dim){
        double x = percentLabelCenterPoint.getX() - dim.getWidth()/2;
        double y = percentLabelCenterPoint.getY() - dim.getHeight()/2;
        return new Rectangle2D.Double(x, y, dim.getWidth(), dim.getHeight());
    }

    //ֵ��ǩ��λ�úʹ�С
    protected Rectangle2D getValueLabelBounds(int position, Dimension2D dim) {
        double x = valueLabelCenterPoint.getX() - dim.getWidth()/2;
        double y = valueLabelCenterPoint.getY() - dim.getHeight()/2;
        return new Rectangle2D.Double(x, y, dim.getWidth(), dim.getHeight());
    }


    private class ThermometerTopDownShadeStyle extends ColumnTopDownShadeStyle {
        private ThermometerTopDownShadeStyle(Color baseColor, RectangularShape rect, boolean isHorizontal) {
            super(baseColor, rect, isHorizontal);
        }

        /**
         * �����������ɫ
         * @return �����������ɫ
         */
        protected Color getStartColor(){
            return this.baseColor;
        }

        /**
         * ���俪ʼ����ɫ
         * @return ���俪ʼ����ɫ
         */
        protected Color getEndColor(){
            baseColor = baseColor == null ? new Color(255, 255, 255, 0) : baseColor;

            float[] hsb = new float[3];
            Color.RGBtoHSB(baseColor.getRed(), baseColor.getGreen(), baseColor.getBlue(), hsb);

            return Color.getHSBColor(hsb[0], Math.max(0, (float)(hsb[1] * (1 - COLOR))), Math.min(1, (float)(hsb[2] * (1 + COLOR))));
        }
    }
}
