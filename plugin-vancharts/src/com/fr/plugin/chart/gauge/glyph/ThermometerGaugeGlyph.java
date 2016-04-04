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
 * 试管仪表盘。
 */
public class ThermometerGaugeGlyph extends GaugeGlyph {

    private static final long serialVersionUID = 2377125930562201704L;
    private static final int R = 5;//试管宽10px。
    private static final int MAIN_TICK = 6;//主要刻度线长度。
    private static final int SEC_TICK = 4;//次要刻度线长度。
    private static final double SMALL_RADIUS = 0.6 * R;//值所在位置小圆半径。
    private static final double BIG_RADIUS = 1.2 * R;//值所在位置大圆半径。
    private static final int AXIS_GAP = 4;//坐标轴刻度距离试管间距。坐标轴刻度距离坐标轴标签间距。
    private static final double LABEL_THERMOMETER_GAP = 0.8;//标签距离试管占相邻标签高度的0.8.
    private static final double LABEL_LABEL_GAP = 0.5;//百分比标签和值标签中间间距。
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

    //是否是竖着的试管。与仪表盘整体布局方向相反。
    private boolean isVertical(){
        return gaugeDetailStyle.isHorizontalLayout();
    }

    //布局试管、坐标轴、百分比标签、值标签。
    public void doLayout(double cateLabelHeight, int resolution){
        boolean hasPercent = false, hasValue= false;
        int percentPosition = -1, valuePosition = -1;
        Dimension2D percentDim = new Dimension(0,0), valueDim = new Dimension(0,0);//纯文本大小，不含边界等。
        double percentOneLineHeight = 0, valueOneLineHeight = 0;//因为涉及到多行文本，计算的时候只需要行高，0.8*行高。

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

        if(isVertical()){//试管垂直画
            doVerticalLayout(percentDim, valueDim, axisLabelDim, percentOneLineHeight, valueOneLineHeight,
                    hasPercent, percentPosition, hasValue, valuePosition);
            radius = bounds.getHeight();
        } else {//试管水平画
            doHorizontalLayout(percentDim, valueDim, axisLabelDim, percentOneLineHeight, valueOneLineHeight,
                    hasPercent, percentPosition, hasValue, valuePosition);
            radius = bounds.getWidth();
        }
    }

    //试管水平布局方法：y方向从上到下一次算出各部分相对0的所在高度，并算出整体的高度和。根据整体高度和算出实际y方向开始位置，各部分向下平移。
    private void doHorizontalLayout(Dimension2D percentDim, Dimension2D valueDim, Dimension2D axisLabelDim,
                                    double percentOneLineHeight, double valueOneLineHeight,
                                    boolean hasPercent, int percentPosition, boolean hasValue, int valuePosition) {
        double axisAndThermometerHeight = gaugeAxisGlyph.isShowAxisLabel() ? (axisLabelDim.getHeight() + AXIS_GAP) : 0;
        axisAndThermometerHeight += (MAIN_TICK + AXIS_GAP + R * 2);
        boolean percentTop = hasPercent && percentPosition == Constants.TOP;
        boolean percentBottom = hasPercent && percentPosition == Constants.BOTTOM;
        boolean valueTop = hasValue && valuePosition == Constants.TOP;
        boolean valueBottom = hasValue && valuePosition == Constants.BOTTOM;

        double percentY = 0,valueY = 0,thermometerAndAxisY;//这些都是相对于整个仪表盘的原点。
        double topGap = 0;
        //y方向从上到下一次算出各部分相对0的所在高度，并算出整体的高度和
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

        //根据整体高度和算出实际y方向开始位置
        topGap = (bounds.getHeight() - topGap)/2;
        //各部分向下平移
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

    //试管垂直布局方法：根据三个部分顺序先计算相对于0的位置和总和，再整体向左平移
    private void doVerticalLayout(Dimension2D percentDim, Dimension2D valueDim, Dimension2D axisLabelDim,
                                  double percentOneLineHeight, double valueOneLineHeight,
                                  boolean hasPercent, int percentPosition, boolean hasValue, int valuePosition) {
        boolean percentLeft = hasPercent && percentPosition == Constants.LEFT;
        boolean percentRight = hasPercent && percentPosition == Constants.RIGHT;
        boolean valueLeft = hasValue && valuePosition == Constants.LEFT;
        boolean valueRight = hasValue && valuePosition == Constants.RIGHT;
        boolean eachSide = (percentLeft && valueRight) || (percentRight && valueLeft);//百分比标签和值标签都显示且显示在两边
        double label_thermometer_gap = LABEL_THERMOMETER_GAP * Math.max(percentOneLineHeight, valueOneLineHeight);
        double axisAndThermometerWidth = R * 2 + AXIS_GAP + MAIN_TICK;//试管加上坐标轴的宽度
        axisAndThermometerWidth += (gaugeAxisGlyph.isShowAxisLabel() ? AXIS_GAP + axisLabelDim.getWidth() : 0);

        double percentX,valueX,thermometerX;//这些都是相对于整个仪表盘的原点。
        double leftGap = 0;
        if(eachSide){//百分比标签和值标签都显示且显示在两边
            if(percentRight){//值、试管和坐标轴、百分比
                valueX = leftGap;
                leftGap += (valueDim.getWidth() + label_thermometer_gap);
                thermometerX = leftGap;
                leftGap += (axisAndThermometerWidth + label_thermometer_gap);
                percentX = leftGap;
                leftGap += percentDim.getWidth();
            } else {//百分比、试管和坐标轴、值
                percentX = leftGap;
                leftGap += (percentDim.getWidth() + label_thermometer_gap);
                thermometerX = leftGap;
                leftGap += (axisAndThermometerWidth + label_thermometer_gap);
                valueX = leftGap;
                leftGap += valueDim.getWidth();
            }
        } else {//都显示在一边或显示一个或都不显示
            double maxLabelWidth = Math.max(percentDim.getWidth(), valueDim.getWidth());
            if(percentRight || valueRight){//百分比在右边或不显示，值在左边或不显示
                thermometerX = leftGap;
                leftGap += (axisAndThermometerWidth + label_thermometer_gap);
                percentX = leftGap;
                valueX = leftGap;
                leftGap += maxLabelWidth;
            } else {//百分比在左边或不显示，值在左边或不显示
                percentX = leftGap + maxLabelWidth - percentDim.getWidth();//右对齐
                valueX = leftGap + maxLabelWidth - valueDim.getWidth();//右对齐
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
        length = length - 2 * R;//坐标轴的长度比试管长度左右少R
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

    //试管垂直时
    private double getValueY(double length, double topGap, double min, double max, double value) {
        double valueLength = Math.min(length, length / (max - min) * (value - min));
        return bounds.getHeight() - topGap - valueLength - R;
    }

    //试管水平时
    private double getValueX(double length, double leftGap, double min, double max, double value) {
        return leftGap + length/(max - min) * (value - min) + R;
    }

    protected void drawInfo(Graphics2D g2d, int resolution){
        drawThermometer(g2d);//试管相关的图形

        drawTicksAndAxisLabel(g2d, resolution);//坐标轴

        drawCateOrPercentageLabel(g2d, resolution);//百分比标签
        drawValueLabel(g2d, resolution);//值标签
    }

    //试管相关的图形
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

    //坐标轴
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
            //从上到下，从大到小
            double startX = thermometerBounds.getMaxX() + AXIS_GAP;//始终不变
            double y = thermometerBounds.getY() + R;//向下移动
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
            //从左到右，从小到大
            double endY = thermometerBounds.getY() - AXIS_GAP;//始终不变
            double x = thermometerBounds.getX() + R;//向右移动
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

    //分类名称的位置和大小
    protected Rectangle2D getCateOrPercentLabelBounds(int position, Dimension2D dim){
        double x = percentLabelCenterPoint.getX() - dim.getWidth()/2;
        double y = percentLabelCenterPoint.getY() - dim.getHeight()/2;
        return new Rectangle2D.Double(x, y, dim.getWidth(), dim.getHeight());
    }

    //值标签的位置和大小
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
         * 渐变结束的颜色
         * @return 渐变结束的颜色
         */
        protected Color getStartColor(){
            return this.baseColor;
        }

        /**
         * 渐变开始的颜色
         * @return 渐变开始的颜色
         */
        protected Color getEndColor(){
            baseColor = baseColor == null ? new Color(255, 255, 255, 0) : baseColor;

            float[] hsb = new float[3];
            Color.RGBtoHSB(baseColor.getRed(), baseColor.getGreen(), baseColor.getBlue(), hsb);

            return Color.getHSBColor(hsb[0], Math.max(0, (float)(hsb[1] * (1 - COLOR))), Math.min(1, (float)(hsb[2] * (1 + COLOR))));
        }
    }
}
