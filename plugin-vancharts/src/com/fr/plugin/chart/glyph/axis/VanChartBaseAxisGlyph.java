package com.fr.plugin.chart.glyph.axis;

import com.fr.base.Formula;
import com.fr.base.GraphHelper;
import com.fr.chart.base.ChartBaseUtils;
import com.fr.chart.base.ChartConstants;
import com.fr.chart.base.ChartUtils;
import com.fr.chart.base.GlyphUtils;
import com.fr.chart.chartglyph.AxisGlyph;
import com.fr.chart.chartglyph.ChartAlertValueGlyph;
import com.fr.chart.chartglyph.TitleGlyph;
import com.fr.general.ComparatorUtils;
import com.fr.json.JSONException;
import com.fr.json.JSONObject;
import com.fr.plugin.chart.attr.VanChartAttrHelper;
import com.fr.plugin.chart.attr.axis.AxisTickLineType;
import com.fr.plugin.chart.attr.axis.AxisType;
import com.fr.plugin.chart.attr.axis.VanChartCustomIntervalBackground;
import com.fr.plugin.chart.base.VanChartConstants;
import com.fr.stable.Constants;
import com.fr.stable.StableUtils;
import com.fr.stable.web.Repository;

import java.awt.*;
import java.awt.geom.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mitisky on 15/10/18.
 */
public abstract class VanChartBaseAxisGlyph extends AxisGlyph{

    private static final long serialVersionUID = 5027277827821496923L;
    private String vanAxisName;
    private AxisType vanAxisType;
    protected boolean isXAxis = true;
    private Rectangle2D chartBounds;//图表整体区域大小，用于计算坐标轴限制区域大小时坐标轴的区域大小。
    private Rectangle2D plotProcessBounds;////绘图区依次布局所有坐标轴过程中绘图区边界，和最终绘图区边界比较后得到最终坐标轴相对最终绘图区的位置

    private boolean titleUseHtml = false;

    private boolean autoLabelGap = true;

    private boolean limitSize = false;
    private double maxHeight = 15;

    private boolean commonValueFormat = true;
    private String customValueFormatText;
    private boolean customValueFormatUseHtml = false;

    private List<ChartAlertValueGlyph> alertValues = new ArrayList<ChartAlertValueGlyph>();

    private Color defaultIntervalBackgroundColor = null;
    private List<VanChartCustomIntervalBackground> customIntervalBackgroundArray = new ArrayList<VanChartCustomIntervalBackground>();

    private AxisTickLineType mainTickLine = AxisTickLineType.TICK_LINE_OUTSIDE;
    private AxisTickLineType secTickLine = AxisTickLineType.TICK_LINE_NONE;

    public void setMainTickLine(AxisTickLineType mainTickLine) {
        this.mainTickLine = mainTickLine;
    }

    public void setSecTickLine(AxisTickLineType secTickLine) {
        this.secTickLine = secTickLine;
    }

    public AxisTickLineType getSecTickLine() {
        return secTickLine;
    }

    public AxisTickLineType getMainTickLine() {
        return mainTickLine;
    }

    public void setVanAxisName(String vanAxisName) {
        this.vanAxisName = vanAxisName;
    }

    public String getVanAxisName() {
        return vanAxisName;
    }

    public void setISXAxis(boolean isXAxis) {
        this.isXAxis = isXAxis;
    }

    public void setChartBounds(Rectangle2D chartBounds){
        this.chartBounds = chartBounds;
    }

    public void setTitleUseHtml(boolean titleUseHtml) {
        this.titleUseHtml = titleUseHtml;
    }

    /**
     * 标题使用html
     * @return 标题使用html
     */
    public boolean isTitleUseHtml() {
        return titleUseHtml;
    }

    public void setAutoLabelGap(boolean autoLabelGap) {
        this.autoLabelGap = autoLabelGap;
    }

    /**
     * 自动间隔
     * @return 自动间隔
     */
    public boolean isAutoLabelGap() {
        return autoLabelGap;
    }

    public void setCommonValueFormat(boolean commonValueFormat) {
        this.commonValueFormat = commonValueFormat;
    }

    /**
     * 通用数据格式
     * @return 通用数据格式
     */
    public boolean isCommonValueFormat() {
        return commonValueFormat;
    }

    public void setCustomValueFormatText(String customValueFormatText) {
        this.customValueFormatText = customValueFormatText;
    }

    public String getCustomValueFormatText() {
        return customValueFormatText;
    }

    public void setCustomValueFormatUseHtml(boolean customValueFormatUseHtml) {
        this.customValueFormatUseHtml = customValueFormatUseHtml;
    }

    /**
     * 使用html
     * @return 使用html
     */
    public boolean isCustomValueFormatUseHtml() {
        return customValueFormatUseHtml;
    }

    public void setAlertValues(List<ChartAlertValueGlyph> alertValues) {
        this.alertValues = alertValues;
    }

    public List<ChartAlertValueGlyph> getAlertValues() {
        return alertValues;
    }

    public void setCustomIntervalBackgroundArray(List<VanChartCustomIntervalBackground> customIntervalBackgroundArray) {
        this.customIntervalBackgroundArray = customIntervalBackgroundArray;
    }

    public void setDefaultIntervalBackgroundColor(Color defaultIntervalBackgroundColor) {
        this.defaultIntervalBackgroundColor = defaultIntervalBackgroundColor;
    }

    public List<VanChartCustomIntervalBackground> getCustomIntervalBackgroundArray() {
        return customIntervalBackgroundArray;
    }

    public Color getDefaultIntervalBackgroundColor() {
        return defaultIntervalBackgroundColor;
    }

    /**
     * 设置是否限制区域大小
     * @param limitSize 是否限制区域大小
     */
    public void setLimitSize(boolean limitSize) {
        this.limitSize = limitSize;
    }

    /**
     * 返回是否限制区域大小
     * @return 返回是否限制区域大小
     */
    public boolean isLimitSize() {
        return limitSize;
    }

    /**
     * 设置区域最大占比
     * @param maxHeight 区域最大占比
     */
    public void setMaxHeight(double maxHeight) {
        this.maxHeight = maxHeight;
    }

    /**
     * 返回区域最大占比
     * @return 返回区域最大占比
     */
    public double getMaxHeight() {
        return maxHeight;
    }


    public void setVanAxisType(AxisType axisType) {
        this.vanAxisType = axisType;
    }

    public AxisType getVanAxisType() {
        return vanAxisType;
    }

    public double getDefaultTickCount() {
        return ValueAxisHelper.TICK_COUNT;
    }

    /**
     * 画出坐标轴的相关元素
     *
     * @param g          画布
     * @param resolution 分辨率
     */
    public void draw(Graphics g, int resolution) {
        if (getBounds() == null) {
            return;
        }
        Graphics2D g2d = (Graphics2D) g;

        g2d.translate(getBounds().getX(), getBounds().getY());

        drawAxisGrid(g2d);//网格线

        drawIntervalBackground(g2d);//间隔背景

        drawAlertValueGlyph(g2d, resolution);//警戒线

        drawAxisLine(g2d, resolution);//轴线、刻度、标签

        drawAxisTitle(g2d, resolution);//标题

        g2d.translate(-getBounds().getX(), -getBounds().getY());
    }

    /**
     * 初始化坐标轴的最大值和最小值
     * @param autoMin 自动计算后是否调整最大最小值（柱形图x轴为数值或者时间）
     * @param autoMax 最小值
     * @param adjustMinMax 最大值
     */
    public void initMinMaxValue(double autoMin, double autoMax, boolean adjustMinMax){
        initMinMaxValue(autoMin, autoMax);
    }

    /**
     * 得到坐标轴上值所对应的坐标轴点.
     * 分类轴的 刻度位置 调整半个 注意不要magic
     */
    public Point2D getPoint2D(double value) {
        Point2D point2D = null;
        value = value - this.getCrossValue();

        if (this.getPosition() == Constants.LEFT) {
            double starty = this.getBounds().getY();
            if (starty == originPoint.getY() || this.hasAxisReversed()) {
                point2D = new Point2D.Double(originPoint.getX(), originPoint.getY() + unitLength * value);
            } else {
                point2D = new Point2D.Double(originPoint.getX(), originPoint.getY() - unitLength * value);
            }
        } else if (this.getPosition() == Constants.RIGHT) {
            double detY = this.hasAxisReversed() ? unitLength * value : -unitLength * value;
            point2D = new Point2D.Double(originPoint.getX(), originPoint.getY() + detY);
        } else if (this.getPosition() == Constants.TOP || this.getPosition() == Constants.BOTTOM) {
            double detX = this.hasAxisReversed() ? -unitLength * value : unitLength * value;
            point2D = new Point2D.Double(originPoint.getX() + detX, originPoint.getY());
        } else {
            //垂直于0
            if(isXAxis){
                //bottom
                double detX = this.hasAxisReversed() ? -unitLength * value : unitLength * value;
                point2D = new Point2D.Double(originPoint.getX() + detX, originPoint.getY());
            } else {
                //left
                double starty = this.getBounds().getY();
                if (starty == originPoint.getY() || this.hasAxisReversed()) {
                    point2D = new Point2D.Double(originPoint.getX(), originPoint.getY() + unitLength * value);
                } else {
                    point2D = new Point2D.Double(originPoint.getX(), originPoint.getY() - unitLength * value);
                }
            }
        }

        return point2D;
    }

    protected Line2D getOtherPositionGridLine(Point2D centerP){
        if(this.getPosition() == Constants.BOTTOM){
            return super.getOtherPositionGridLine(centerP);
        } else {
            if(isXAxis){
                return new Line2D.Double(centerP.getX(), getPlotLastBounds().getY() - getBounds().getY(), centerP.getX(), getPlotLastBounds().getY() - getBounds().getY() + getPlotLastBounds().getHeight());
            } else {
                return new Line2D.Double(getPlotLastBounds().getX() - getBounds().getX(), centerP.getY(), getPlotLastBounds().getX() - getBounds().getX() + getPlotLastBounds().getWidth(), centerP.getY());
            }
        }
    }

    protected Rectangle2D getOtherPositionTitleBounds(Dimension2D dim, int titlePosition) {
        if(isXAxis){
            return getBottomTitleBounds(dim, titlePosition);
        } else {
            return getLeftTitleBounds(dim, titlePosition);
        }
    }

    protected Line2D getOtherPositionTickLine(Point2D centerP, Point2D bottomP, Point2D leftP) {
        if(this.getPosition() == Constants.BOTTOM){
            return super.getOtherPositionTickLine(centerP, bottomP, leftP);
        } else {
            if(isXAxis){
                return super.getOtherPositionTickLine(centerP, bottomP, leftP);
            } else {
                return new Line2D.Double(centerP, leftP);
            }
        }
    }

    protected Rectangle2D getOtherPositionLabelBounds(Point2D centerP, double width, double height, double margin) {
        if(this.getPosition() == Constants.BOTTOM){
            return super.getOtherPositionLabelBounds(centerP, width, height, margin);
        } else {
            if(isXAxis){
                return  super.getOtherPositionLabelBounds(centerP, width, height, margin);
            } else {
                return new Rectangle2D.Double(centerP.getX() - width - margin,
                        centerP.getY() - height / 2, width, height);
            }
        }
    }

    protected double getOtherPoint2ValueLength(Point2D startPoint, Point2D endPoint) {
        if(this.getPosition() == Constants.BOTTOM || this.getPosition() == Constants.TOP){
            return super.getOtherPoint2ValueLength(startPoint, endPoint);
        } else {
            if(isXAxis){
                return super.getOtherPoint2ValueLength(startPoint, endPoint);
            } else {
                return Math.abs(startPoint.getY() - endPoint.getY());
            }
        }
    }

    protected double getLeftRightY(int titlePosition) {
        double startY = (axisLength - titleDim.getHeight()) / 2;
        if (titlePosition == Constants.TOP) {
            startY = -2 * TitleGlyph.GAP;
        } else if (titlePosition == Constants.BOTTOM) {
            startY = axisLength - titleDim.getHeight() + 2 * TitleGlyph.GAP;
        }
        return startY;
    }

    /**
     * 是否是横轴
     *
     * @return true则是横着的坐标轴，false表示竖着的坐标轴
     */
    protected boolean shouldBeHeight() {
        return isXAxis;
    }

    //获取标签的宽度，分类轴标签会重载，分类轴标签的宽度与绘图区的宽度、高度有关
    protected double getCateLabelWidth(Rectangle2D plotBounds, int resolution) {
        return getMaxLabelWidthAndInitStartEndLabelDim(resolution);
    }

    /**
     *预计算纵轴 标签高度 和 横轴标签宽度 对Plot高度 和宽度的 影响
     * @param plotBounds 边界
     * @param cateLabelWidth 宽度
     * @param titleDim 大小
     */
    public void calculateBoundsWidthOrientationAndTitle(Rectangle2D plotBounds, double cateLabelWidth, Dimension2D titleDim) {
        double plotX = plotBounds.getX(), plotY = plotBounds.getY(),
                plotW = plotBounds.getWidth(), plotH = plotBounds.getHeight();

        double preTitleWidthGap = (titleDim.getWidth() > 0) ? titleDim.getWidth() + ChartConstants.AXIS_LINE_LABEL_GAP : 0;
        double preTitleHeightGap = (titleDim.getHeight() > 0) ? titleDim.getHeight() + ChartConstants.AXIS_LINE_LABEL_GAP : 0;

        if(limitSize){
            //限制区域大小，不考虑label和title，直接根据图表区计算
            cateLabelWidth = shouldBeHeight() ? chartBounds.getHeight() * maxHeight/VanChartAttrHelper.PERCENT : chartBounds.getWidth() * maxHeight/VanChartAttrHelper.PERCENT;
            preTitleHeightGap = 0;
            preTitleWidthGap = 0;
        }

        double tickLength = getTickLengthShow();

        switch (this.getPosition()) {
            case VanChartConstants.AXIS_LEFT:
                plotX += cateLabelWidth + tickLength + preTitleWidthGap;
                plotW -= cateLabelWidth + tickLength + preTitleWidthGap;
                break;
            case VanChartConstants.AXIS_BOTTOM:
                plotH -= cateLabelWidth + tickLength + preTitleHeightGap;
                break;
            case VanChartConstants.AXIS_RIGHT:
                plotW -= cateLabelWidth + tickLength + preTitleWidthGap;
                break;
            case VanChartConstants.AXIS_TOP:
                plotY += cateLabelWidth + tickLength + preTitleHeightGap;
                plotH -= cateLabelWidth + tickLength + preTitleHeightGap;
                break;
            case VanChartConstants.AXIS_VERTICAL_ZERO:
                //这个先不考虑，要去看它垂直的坐标轴数组的第一个坐标轴，如果是非数值型，忽略，如果是数值型，看0值以下的高度和坐标轴标签高度的最大值
                break;
        }
        plotBounds.setRect(plotX, plotY, plotW, plotH);
    }

    /**
     * 绘图区依次布局所有坐标轴过程中绘图区边界，和最终绘图区边界比较后得到最终坐标轴相对最终绘图区的位置
     * @param bounds 过程中绘图区边界
     */
    public void initAxisGlyphStartPoint(Rectangle2D bounds){
        this.plotProcessBounds = new Rectangle2D.Double(bounds.getX(), bounds.getY(), bounds.getWidth(), bounds.getHeight());
    }

    /**
     * 处理0值对齐
     * @param verticalZero 它所垂直的坐标轴的0值得位置
     */
    public void dealOnZeroAxisGlyphLocation(double verticalZero){
        if(this.getPosition() != VanChartConstants.AXIS_VERTICAL_ZERO){
            return;
        }
        if(isXAxis){
            this.setBounds(new Rectangle2D.Double(this.getBounds().getX(), verticalZero, this.getBounds().getWidth(), this.getBounds().getHeight()));
        } else {
            this.setBounds(new Rectangle2D.Double(verticalZero - this.getBounds().getWidth(), this.getBounds().getY(), this.getBounds().getWidth(), this.getBounds().getHeight()));
        }
    }

    //数据表时，先调整左侧，再调整下面。导致x坐标轴的axisGridLength是调整左侧之后，下面之前，不对。这里更新一下。
    public void finallyUpdateAxisGridLength(double length) {
        setAxisGridLength(length);
    }

    /**
     * 初始化坐标轴的边界
     */
    protected void init(Rectangle2D plotBounds) {
        switch (this.getPosition()) {
            case VanChartConstants.AXIS_LEFT:
                leftCase(plotBounds);
                break;
            case VanChartConstants.AXIS_BOTTOM:
                bottomCase(plotBounds);
                break;
            case VanChartConstants.AXIS_RIGHT:
                rightCase(plotBounds);
                break;
            case VanChartConstants.AXIS_TOP:
                topCase(plotBounds);
                break;
            case VanChartConstants.AXIS_VERTICAL_ZERO:
                zeroCase(plotBounds);
                break;
            default:
                break;
        }
    }

    //中间过程处理，确定axisZero， unitLength等
    private void zeroCase(Rectangle2D plotBounds) {
        if(isXAxis){
            bottomCase(plotBounds);
        } else {
            leftCase(plotBounds);
        }
    }

    private void topCase(Rectangle2D plotBounds) {
        Point2D axisZero; //相对于axis的bounds的坐标
        double axisLength, axisGridLength;
        Rectangle2D bounds;

        axisLength = plotBounds.getWidth();
        axisGridLength = plotBounds.getHeight();
        double gap = plotBounds.getY() - plotProcessBounds.getY();
        bounds = new Rectangle2D.Double(0, -axisLabelWidth - gap, plotBounds.getWidth(), axisLabelWidth);
        if (!this.hasAxisReversed()) {
            axisZero = new Point2D.Double(0, axisLabelWidth);
        } else {
            axisZero = new Point2D.Double(plotBounds.getWidth(), axisLabelWidth);
        }

        setAttr(bounds, axisZero, axisLength, axisGridLength);
    }

    private void rightCase(Rectangle2D plotBounds) {
        Point2D axisZero; //相对于axis的bounds的坐标
        double axisLength, axisGridLength;
        Rectangle2D bounds;

        axisLength = plotBounds.getHeight();
        axisGridLength = plotBounds.getWidth();
        double gap = plotProcessBounds.getWidth() - (plotBounds.getX() - plotProcessBounds.getX());
        bounds = new Rectangle2D.Double(gap, 0, axisLabelWidth, plotBounds.getHeight());
        if (!this.hasAxisReversed()) {
            axisZero = new Point2D.Double(0, plotBounds.getHeight());
        } else {
            axisZero = new Point2D.Double(0, 0);
        }

        setAttr(bounds, axisZero, axisLength, axisGridLength);
    }

    private void bottomCase(Rectangle2D plotBounds) {
        Point2D axisZero; //相对于axis的bounds的坐标
        double axisLength, axisGridLength;
        Rectangle2D bounds;

        axisLength = plotBounds.getWidth();
        axisGridLength = plotBounds.getHeight();
        double gap = plotProcessBounds.getHeight() + plotProcessBounds.getY() - (plotBounds.getY() + plotBounds.getHeight());
        bounds = new Rectangle2D.Double(0, gap + plotBounds.getHeight(), plotBounds.getWidth(), axisLabelWidth);
        if (!this.hasAxisReversed()) {
            axisZero = new Point2D.Double(0, 0);
        } else {
            axisZero = new Point2D.Double(plotBounds.getWidth(), 0);
        }

        setAttr(bounds, axisZero, axisLength, axisGridLength);
    }

    private void leftCase(Rectangle2D plotBounds) {
        Point2D axisZero; //相对于axis的bounds的坐标
        double axisLength, axisGridLength;
        Rectangle2D bounds;

        axisLength = plotBounds.getHeight();
        axisGridLength = plotBounds.getWidth();
        double gap = plotBounds.getX() - plotProcessBounds.getX();
        bounds = new Rectangle2D.Double(-axisLabelWidth - gap, 0, axisLabelWidth, plotBounds.getHeight());
        if (!this.hasAxisReversed()) {
            axisZero = new Point2D.Double(axisLabelWidth, plotBounds.getHeight());
        } else {
            axisZero = new Point2D.Double(axisLabelWidth, 0);
        }

        setAttr(bounds, axisZero, axisLength, axisGridLength);
    }

    public void drawAlertValueGlyph(Graphics2D g2d, int resolution) {
        for(ChartAlertValueGlyph alertValueGlyph : alertValues){
            alertValueGlyph.dealWithAlertLine();
            //画警戒线，包括警戒值标签
            alertValueGlyph.draw(g2d, resolution);
        }
    }

    protected void drawIntervalBackground(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        if(getDefaultIntervalBackgroundColor() != null){
            Paint oldPaint = g2d.getPaint();
            g2d.setColor(getDefaultIntervalBackgroundColor());
            if(isLog()){
                drawDefaultIntervalBackgroundWithLog(g2d);
            } else {
                drawDefaultIntervalBackgroundWithGeneral(g2d);
            }
            g2d.setPaint(oldPaint);
        } else {
            drawCustomBackground(g2d);
        }
    }

    private void drawDefaultIntervalBackgroundWithLog(Graphics2D g2d) {
        if (this.getMainUnit() > 1.0) {
            double value = this.getCrossValue();
            double start = Math.log(value) / Math.log(getMainUnit());
            double number = Math.log(maxValue)/Math.log(getMainUnit()) - Math.log(minValue)/Math.log(getMainUnit());
            if(number / 2 == Math.ceil(number / 2)){//偶数从第二个开始画才能保证最外面一层是有的
                value = Math.pow(getMainUnit(), ++start);
            }
            for (; value <= maxValue; value = Math.exp((++start) * Math.log(getMainUnit()))) {
                double startValue = value;
                value = Math.exp((++start) * Math.log(getMainUnit()));
                if(value > maxValue + 1.0E-10){
                    value = maxValue;
                }
                drawDefaultIntervalBackgroundWithValue(g2d, startValue, value);
            }
        }
    }

    private void drawDefaultIntervalBackgroundWithGeneral(Graphics2D g2d) {
        BigDecimal valueMin = new BigDecimal(Double.toString(minValue));
        BigDecimal unitBig = new BigDecimal(Double.toString(getMainUnit() * getTickSamplingTime()));
        BigDecimal valueMax = new BigDecimal(Double.toString(maxValue));
        //最外面一层始终是有的
        double number = Math.ceil(valueMax.subtract(valueMin).divide(unitBig).doubleValue());
        if(number / 2 == Math.ceil(number / 2)){//偶数从第二个开始画才能保证最外面一层是有的
            valueMin = valueMin.add(unitBig);
        }
        for ( ; valueMin.compareTo(valueMax) <= 0; valueMin = valueMin.add(unitBig)) {
            double startValue = valueMin.doubleValue();
            valueMin = valueMin.add(unitBig);
            if(valueMin.compareTo(valueMax) > 0){
                valueMin = valueMax;
            }
            drawDefaultIntervalBackgroundWithValue(g2d, startValue, valueMin.doubleValue());
        }
    }

    private void drawDefaultIntervalBackgroundWithValue(Graphics2D g2d, double startValue, double endValue) {
        Shape shape = getCustomBackgroundShape(startValue, endValue, false);
        g2d.fill(shape);
    }

    private void drawCustomBackground(Graphics2D g2d) {
        if(customIntervalBackgroundArray == null || customIntervalBackgroundArray.isEmpty()){
            return;
        }

        Paint oldPaint = g2d.getPaint();
        Composite oldComposite = g2d.getComposite();
        for(VanChartCustomIntervalBackground background : customIntervalBackgroundArray){
            double startValue = 0, endValue = 0;
            if(background.getFromFormula() != null && background.getToFormula() != null){
                //设计器那边解析不了公式，给个默认值
                //改正：解析不了公式，就不画。设计器这边可以解析出直接赋予的值。如=200、=Apple等
                startValue = getObjectValue(getObjectFromFormula(background.getFromFormula()));
                endValue = getObjectValue(getObjectFromFormula(background.getToFormula()));
            }
            //若是drawBetween，start==end是有区域的
            Shape shape = getCustomBackgroundShape(startValue, endValue);
            g2d.setColor(background.getBackgroundColor());
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float)background.getAlpha()));
            g2d.fill(shape);
        }
        g2d.setPaint(oldPaint);
        g2d.setComposite(oldComposite);
    }

    public Object getObjectFromFormula(Formula formula) {
        if(formula == null){
            return null;
        }

        if(formula.getResult() == null){
            String content = formula.getContent();
            if(content.startsWith("=")) {
                content = content.substring(1);
            }
            return content;
        } else {
            return formula.getResult();
        }
    }

    private Shape getCustomBackgroundShape(double startValue, double endValue) {
        return getCustomBackgroundShape(startValue, endValue, true);
    }

    //普通间隔背景不考虑是否drawBetweenTick
    protected Shape getCustomBackgroundShape(double startValue, double endValue, boolean isCustom) {
        Point2D start = getPointInBounds(startValue);
        Point2D end = getPointInBounds(endValue);
        if(isXAxis){
            double x = Math.min(end.getX(), start.getX()), y, width = Math.abs(end.getX() - start.getX());
            boolean top = getPosition() == VanChartConstants.AXIS_TOP;
            if(isDrawBetweenTick() && isCustom){
                width += getUnitLen();
            }
            if(top){
                y = start.getY();
            } else {
                y = start.getY() - getAxisGridLength();
            }
            return new Rectangle2D.Double(x, y, width, getAxisGridLength());
        } else {
            double x, y = Math.min(start.getY(), end.getY()), height = Math.abs(end.getY() - start.getY());
            boolean right = getPosition() == VanChartConstants.AXIS_RIGHT;
            if(isDrawBetweenTick() && isCustom){
                y -= getUnitLen();
                height += getUnitLen();
            }
            if(right){
                x = start.getX() - getAxisGridLength();
            } else {
                x = start.getX();
            }
            return new Rectangle2D.Double(x, y, getAxisGridLength(), height);
        }
    }

    /**
     * 对象在坐标轴的位置，category、time、value。如果drawBetween 则返回中间位置
     * @param ob 该对象
     * @return 位置
     */
    public Point2D getAlertValuePoint(Object ob) {
        return getPointInBounds(getObjectValue(ob));
    }

    /**
     * 对象在坐标轴的位置，category、time、value
     * @param ob 该对象
     * @return 位置
     */
    public Point2D getValuePoint(Object ob) {
        return getPointInBounds(getObjectValue(ob));
    }

    public double getObjectValue(Object ob) {
        return 0;
    }

    protected void drawAfterPlot4AlertValues(Graphics2D g2d, int resolution) {
    }

    protected boolean isRevertAndArrow() {
        return false;
    }

    /**
     * 获取刻度线
     * @param value 刻度线所在值
     * @param tickLength 刻度线长度
     * @param tickLineType 刻度线类型
     * @return 刻度线
     */
    protected Line2D getTickLine(double value, int tickLength, AxisTickLineType tickLineType) {
        Point2D centerP = getPoint2D(value);
        return getTickLineByCenterPoint(centerP, tickLength, tickLineType);
    }

    protected Line2D getTickLineByCenterPoint(Point2D centerP, int tickLength, AxisTickLineType tickLineType) {
        Point2D leftP = new Point2D.Double(centerP.getX() - tickLength, centerP.getY());
        Point2D rightP = new Point2D.Double(centerP.getX() + tickLength, centerP.getY());
        Point2D topP = new Point2D.Double(centerP.getX(), centerP.getY() - tickLength);
        Point2D bottomP = new Point2D.Double(centerP.getX(), centerP.getY() + tickLength);

        if (ComparatorUtils.equals(tickLineType, AxisTickLineType.TICK_LINE_OUTSIDE)) {
            if (this.getPosition() == Constants.LEFT) {
                return new Line2D.Double(centerP, leftP);
            } else if (this.getPosition() == Constants.TOP) {
                return new Line2D.Double(centerP, topP);
            } else if (this.getPosition() == Constants.RIGHT) {
                return new Line2D.Double(centerP, rightP);
            } else {
                return getOtherPositionTickLine(centerP, bottomP, leftP);
            }
        } else {
            return null;
        }
    }

    /**
     * 返回刻度 展示的长度
     */
    protected double getTickLengthShow() {
        return Math.max(getTickLength4Type(getMainTickLine(), MAIN_TICK_LENGTH), getTickLength4Type(getSecTickLine(), SEC_TICK_LENGTH));
    }

    private double getTickLength4Type(AxisTickLineType tickType, int tickLength) {
        switch (tickType) {
            case TICK_LINE_OUTSIDE:
                return tickLength;
            default:
                return 0;
        }
    }

    /**
     * 画坐标轴的线
     */
    protected void drawAxisLine(Graphics g, int resolution) {
        Graphics2D g2d = (Graphics2D) g;

        Paint oldPaint = g2d.getPaint();
        Stroke oldStroke = g2d.getStroke();

        int lineStyle = this.getLineStyle();
        if (this.getLineColor() != null && lineStyle != Constants.LINE_NONE) {
            g2d.setStroke(GraphHelper.getStroke(lineStyle));
            g2d.setPaint(lineColor);

            Line2D.Double line = new Line2D.Double(getPoint2D(getMinValue()), getPoint2D(getMaxTickValue()));

            g2d.draw(line);

            drawArrow(g2d, axisReversed ? line.getP2() : line.getP1(), axisReversed ? line.getP1() : line.getP2());
        }

        // 画刻度
        drawTicks(g2d, resolution);

        g2d.setStroke(oldStroke);
        g2d.setPaint(oldPaint);
    }

    protected double getMaxTickValue() {
        return getMaxValue();
    }

    protected boolean shouldDrawTickLine(Line2D line) {
        return line != null && lineColor != null;
    }

    /**
     * 画坐标轴标题
     */
    protected void drawAxisTitle(Graphics g, int resolution) {
        if (titleGlyph != null) {
            Rectangle2D chartBounds = calculatorTitleBounds(resolution);

            titleGlyph.getTextAttr().setRotation(-titleGlyph.getTextAttr().getRotation());
            GlyphUtils.drawStrings(g, titleGlyph.getText(), titleGlyph.getTextAttr(), chartBounds, resolution);
            titleGlyph.getTextAttr().setRotation(-titleGlyph.getTextAttr().getRotation());
        }
    }

    /**
     * 最终的坐标轴标签画法
     */
    protected void drawLabel(Graphics g, double value, double offset, String labelString, int resolution) {
        textAttr.setRotation(-textAttr.getRotation());
        super.drawLabel(g, value, offset, labelString, resolution);
        textAttr.setRotation(-textAttr.getRotation());
    }

    protected void drawLabel(Graphics g, double value, double offset, String labelString, GeneralPath labelPath, int resolution) {
        textAttr.setRotation(-textAttr.getRotation());
        super.drawLabel(g, value, offset, labelString, labelPath, resolution);
        textAttr.setRotation(-textAttr.getRotation());
    }

    /**
     * 转为json数据
     *
     * @param repo 请求
     * @return 返回json
     * @throws com.fr.json.JSONException 抛错
     */
    public JSONObject toJSONObject(Repository repo) throws JSONException {
        JSONObject js = new JSONObject();
        js.put("type", vanAxisType.getAxisType());
        if(position == VanChartConstants.AXIS_VERTICAL_ZERO){
            js.put("onZero", true);
        } else {
            js.put("position", ChartUtils.getPositionString(position));
        }
        if(isLimitSize()){
            js.put(isXAxis ? "maxHeight" : "maxWidth", getMaxHeight() + VanChartAttrHelper.STRING_PERCENT);
        }

        String lineAndTickColor = StableUtils.javaColorToCSSColor(lineColor);
        //轴线样式
        js.put("lineColor", lineAndTickColor);
        js.put("lineWidth", VanChartAttrHelper.getAxisLineStyle(this.lineStyle));
        js.put("showArrow", this.isArrowShow);

        //刻度线
        js.put("enableTick", ComparatorUtils.equals(getMainTickLine(), AxisTickLineType.TICK_LINE_OUTSIDE));
        js.put("enableMinorTick", ComparatorUtils.equals(getSecTickLine(), AxisTickLineType.TICK_LINE_OUTSIDE));
        js.put("tickColor", lineAndTickColor);
        js.put("minorTickColor", lineAndTickColor);

        //网格线
        if(getMainGridColor() == null){
            js.put("gridLineWidth", 0);//界面上无法配置，传默认1过去
        } else {
            js.put("gridLineColor", StableUtils.javaColorToCSSColor(getMainGridColor()));
            js.put("gridLineWidth", 1);//界面上无法配置，传默认1过去
        }

        js.put("reversed", this.axisReversed);

        addValueFormat(js, repo);
        addAxisLabelJSON(js, repo);
        addAxisTitleJSON(js, repo);
        addAlertJSON(js,repo);
        addIntervalBackgroundJSON(js, repo);

        addMinMaxValue(js, repo);

        return js;
    }

    protected void addMinMaxValue(JSONObject js, Repository repo) throws JSONException{
        if(isCustomMaxValue){
            js.put("max", getMaxValue());
        }
        if(isCustomMinValue){
            js.put("min", getMinValue());
        }
        if(isCustomMainUnit()){
            js.put("tickInterval", getMainUnit());
        }
        if(isCustomSecUnit()){
            js.put("minorTickInterval", getSecUnit());
        }

    }

    protected void addValueFormat(JSONObject js, Repository repo) throws JSONException{
        if(isCommonValueFormat()){
            JSONObject formatJS = new JSONObject();
            formatJS.put("format", ChartBaseUtils.format2JS(getFormat()));
            js.put("formatter", formatJS);
        } else {
            js.put("formatter", getCustomValueFormatText());
            js.put("useHtml", isCustomValueFormatUseHtml());
        }
    }

    protected void addAxisLabelJSON(JSONObject js, Repository repo) throws JSONException{
        if(isShowAxisLabel){
            js.put("showLabel", true);
            js.put("labelStyle", VanChartAttrHelper.getCSSFontJSONWithFont(textAttr.getFRFont()));
            js.put("labelRotation", textAttr.getRotation());
            if(!isAutoLabelGap()){
                js.put("step", getLabelNumber());
            }
        } else {
            js.put("showLabel", false);
            js.put("labelStyle", VanChartAttrHelper.getCSSFontJSONWithFont(textAttr.getFRFont()));
        }
    }

    private void addAxisTitleJSON(JSONObject js, Repository repo) throws JSONException{
        if (titleGlyph != null && !titleGlyph.getText().isEmpty()) {
            JSONObject titleJS = new JSONObject();
            titleJS.put("useHtml", titleUseHtml);
            titleJS.put("text", titleGlyph.getText());
            titleJS.put("style", VanChartAttrHelper.getCSSFontJSONWithFont(titleGlyph.getTextAttr().getFRFont()));
            titleJS.put("align", ChartUtils.getPositionString(titleGlyph.getPosition()));
            titleJS.put("rotation", titleGlyph.getTextAttr().getRotation());
            js.put("title", titleJS);
        }
    }

    private void addAlertJSON(JSONObject js, Repository repo) throws JSONException{
        //警戒线
        List list = new ArrayList();
        for(ChartAlertValueGlyph alert : alertValues){
            list.add(alert.toJSONObject(repo));
        }
        js.put("plotLines", list);
    }

    private void addIntervalBackgroundJSON(JSONObject js, Repository repo) throws JSONException{
        //间隔背景
        if(defaultIntervalBackgroundColor != null){
            js.put("plotBands", StableUtils.javaColor2JSColorWithAlpha(defaultIntervalBackgroundColor));
        } else {
            List backgroundList = new ArrayList();
            for(VanChartCustomIntervalBackground background : customIntervalBackgroundArray){
                backgroundList.add(background.toJSONObject(repo));
            }
            js.put("plotBands", backgroundList);
        }
    }

}
