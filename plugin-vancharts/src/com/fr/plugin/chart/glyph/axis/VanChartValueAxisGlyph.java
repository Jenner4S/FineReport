package com.fr.plugin.chart.glyph.axis;

import com.fr.base.Formula;
import com.fr.base.GraphHelper;
import com.fr.base.Utils;
import com.fr.chart.base.ChartBaseUtils;
import com.fr.chart.base.ChartConstants;
import com.fr.chart.base.GlyphUtils;
import com.fr.chart.base.TextAttr;
import com.fr.chart.chartglyph.AxisHelper;
import com.fr.chart.chartglyph.TextGlyph;
import com.fr.general.FRLogger;
import com.fr.json.JSONException;
import com.fr.json.JSONObject;
import com.fr.plugin.chart.attr.VanChartAttrHelper;
import com.fr.stable.Constants;
import com.fr.stable.StringUtils;
import com.fr.stable.web.Repository;

import java.awt.*;
import java.awt.geom.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mitisky on 15/10/19.
 */
public class VanChartValueAxisGlyph extends VanChartBaseAxisGlyph {
    private static final double DIV_4_SECOND = 5.0;
    public static final int DEFAULT_MAX = 100;

    private static final int UNIT_GAP_LABEL = 3;
    private static final double LABEL_WIDTH = 8;
    protected static final double LOG_MIN = 1e-10;

    private static final double BASE_LOG = 10;

    private static final double MAX_TICK = 1000;
    private static final double TICK_NUMBER = 20;

    private boolean isPercentage = false;
    private boolean isLog = false;
    private double logBase = 10;

    public void setPercentage(boolean isPercentage){
        this.isPercentage = isPercentage;
    }

    /**
     * 返回是否百分比 (只有数字轴NumberAxis支持此功能)
     *
     * @return 是否百分比
     */
    public boolean isPercentage() {
        return this.isPercentage;
    }

    /**
     * 设置对数的底
     */
    public void setLogBase(double logBase) {
        this.logBase = logBase;
    }

    /**
     * 返回对数的底
     */
    public double getLogBase() {
        return logBase;
    }

    /**
     * 设置是否用对数坐标轴
     */
    public void setLog(boolean isLog) {
        this.isLog = isLog;
    }

    /**
     * 返回 是否为对数对数坐标轴
     * @return  是否为对数.
     */
    public boolean isLog() {
        return isLog;
    }

    public VanChartValueAxisGlyph(){
        if(format == null){
            format = VanChartAttrHelper.VALUE_FORMAT;
        }
    }

    /**
     * 返回对应的js类型.
     */
    public String getJSAxisType() {
        return "value";
    }

    public String getAxisName(){
        return "ValueAxis";
    }

    /**
     * 返回交叉点的值.
     */
    public double getCrossValueInPlot() {
        if (this.isLog()) {
            return 1;
        }
        return 0;
    }

    public double getObjectValue(Object ob) {
        Number number = Utils.objectToNumber(ob, true);
        if(number != null){
            return number.doubleValue();
        }
        return getCrossValue();
    }

    public Object getObjectFromFormula(Formula formula) {
        if(formula == null){
            return null;
        }

        if(formula.getResult() == null){
            return ChartBaseUtils.formula2Number(formula);
        } else {
            return formula.getResult();
        }
    }


    /**
     * 返回值 对应绘图区位置的坐标点
     *  处理了越界的情况. 比如 超过最大值的时候 取最大值
     */
    public Point2D getPointInBounds(double value) {
        if (!(this.isLog() && this.getCrossValue() > 0)) {
            if (value < this.getCrossValue()) {
                value = this.getCrossValue();
            }

            if (value > this.getArrowValue()) {
                value = this.getArrowValue();
            }
        }

        return this.getPoint2D(value);
    }

    /**
     * 返回值 所对应的坐标轴点
     */
    public Point2D getPoint2D(double value) {
        // 指数型坐标
        if (this.isLog() && this.getCrossValue() > 0) {
            if (value <= LOG_MIN) {
                value = this.getCrossValue();
            }
            value = (Math.log(value) - Math.log(this.getCrossValue())) / Math.log(this.getMainUnit());
        }
        // default
        else {
            value = value - this.getCrossValue();
        }

        double det = unitLength * value;
        double detY = this.hasAxisReversed() ? det : -det;
        double detX = this.hasAxisReversed() ? -det : det;

        if (this.getPosition() == Constants.LEFT || this.getPosition() == Constants.RIGHT) {
            return new Point2D.Double(originPoint.getX(), originPoint.getY() + detY);
        } else if(this.getPosition() == Constants.BOTTOM || this.getPosition() == Constants.TOP){
            return new Point2D.Double(originPoint.getX() + detX, originPoint.getY());
        } else {
            if(isXAxis){
                return new Point2D.Double(originPoint.getX() + detX, originPoint.getY());
            } else {
                return new Point2D.Double(originPoint.getX(), originPoint.getY() + detY);
            }
        }
    }

    /**
     * 返回坐标轴在左侧位置 可能占用的空间宽度 , 为了给数据表预留位置
     */
    public double getPreLeftWidth4DataSheet(int resolution) {
        calculateTitleDimesion(resolution);

        if(this.position == Constants.LEFT) {
            return getMaxLabelWidthAndInitStartEndLabelDim(resolution) + titleDim.getWidth() + getTickLengthShow();
        } else {
            return 0;
        }
    }

    private double getMaxLabelWidth(int resolution) {
        if (!isShowAxisLabel) {
            return 0;
        }
        double labelWidth = LABEL_WIDTH;
        java.util.List dimList = getAxisLabelDimList(resolution);
        if (dimList == null || dimList.isEmpty()) {
            return labelWidth;
        } else {
            for(int i = 0, size = dimList.size(); i < size; i++) {
                Dimension2D labelDim = (Dimension2D) dimList.get(i);
                double tmpWidth = shouldBeHeight() ? labelDim.getWidth() : labelDim.getHeight();
                labelWidth = (tmpWidth < labelWidth ? labelWidth : tmpWidth);
            }
        }
        return labelWidth;
    }

    /**
     * 获取值坐标轴占用位置最大的标签的尺寸, 横向坐标轴返回标签的最大高度，纵向坐标轴返回标签的最大宽度
     * 初始化最初和最后的标签Dim大小
     * @param resolution 分辨率
     */
    protected double getMaxLabelWidthAndInitStartEndLabelDim(int resolution) {
        if (!isShowAxisLabel) {
            return 0;
        }
        double labelWidth = LABEL_WIDTH;
        List dimList = getAxisLabelDimList(resolution);
        if (dimList == null || dimList.isEmpty()) {
            return labelWidth;
        } else {
            for(int i = 0, size = dimList.size(); i < size; i++) {
                Dimension2D labelDim = (Dimension2D) dimList.get(i);
                double tmpWidth = shouldBeHeight() ? labelDim.getHeight() : labelDim.getWidth();
                labelWidth = (tmpWidth < labelWidth ? labelWidth : tmpWidth);

                if (i == 0) {
                    startLabelDim = labelDim;
                }
                if (i == size - 1) {
                    endLabelDim = labelDim;
                }

            }
        }
        return labelWidth;
    }

    private List getAxisLabelDimList(int resolution) {
        BigDecimal increment = getBigDecimalMainUnit();
        if (increment.doubleValue() <= 0) {
            return null;
        }
        List dimList = new ArrayList();
        BigDecimal minBig = new BigDecimal(Double.toString(minValue));

        if(isLog()) {
            if(minValue == 0) {
                minValue = 1;
            }
        }

        increment = checkIncrementNotTooSmall(increment, maxValue - minValue);

        for (double min = minValue; min <= maxValue; min = isLog() ? min * increment.doubleValue() : min + increment.doubleValue()) {
            Dimension2D dim = getAxisLabelDim(value2String(minBig.doubleValue()), resolution);
            dimList.add(dim);
            minBig = isLog() ? minBig.multiply(increment) : minBig.add(increment);
        }
        return dimList;
    }

    private BigDecimal checkIncrementNotTooSmall(BigDecimal increment, double allValue) {
        if(!isLog()) {
            BigDecimal inValue = increment.multiply(new BigDecimal(1000));
            if(inValue.doubleValue() < allValue) {
                return new BigDecimal(getTickSamplingTime());
            }
        }

        return increment;
    }

    /**
     * 计算坐标轴 单位长度
     */
    public void calculateAxisLengthUnit() {
        if (this.isLog()) {
            unitLength = axisLength / (Math.log(maxValue) / Math.log(this.getMainUnit()) - Math.log(minValue) / Math.log(this.getMainUnit()));
        } else if (maxValue - minValue > 0) {
            unitLength = axisLength / (maxValue - minValue);
        }
    }

    /**
     * 返回最小值
     */
    public double getCrossValue() {
        return minValue;
    }

    /**
     * 返回坐标轴箭头处的值, 最大值
     */
    public double getArrowValue() {
        return maxValue;
    }

    /**
     * 初始化最大值、最小值以及刻度大小
     *
     * @param autoMin 最小值
     * @param autoMax 最大值
     */
    public void initMinMaxValue(double autoMin, double autoMax) {
        initMinMaxValue(autoMin, autoMax, false);
    }

    /**
     * 初始化坐标轴的最大值和最小值
     * @param autoMin 自动计算后是否调整最大最小值（柱形图x轴为数值或者时间）
     * @param autoMax 最小值
     * @param adjustMinMax 最大值
     */
    public void initMinMaxValue(double autoMin, double autoMax, boolean adjustMinMax){
        if(autoMin > 0){
            autoMin = 0;
        }

        //防止死循环
        if(autoMax < autoMin){
            autoMin = 0;
            autoMax = DEFAULT_MAX;
        }else if(autoMax == autoMin){
            if(autoMin < 0){
                autoMax = 0;
            }else if(autoMin == 0){
                autoMax = DEFAULT_MAX;
            }else {
                autoMin = 0;
            }
        }

        try {
            ValueAxisHelper.calculateNiceDomain(autoMin, autoMax, this, isCustomMinValue, isCustomMaxValue);
        } catch (Exception e) {
            FRLogger.getLogger().error(e.getMessage());
            setMinValue(autoMin);
            setMaxValue(autoMax);
            setMainUnit(new BigDecimal((autoMax - autoMin)/getDefaultTickCount()));
        }

        if (!isCustomSecUnit()) {
            setSecUnit(getMainUnit() / DIV_4_SECOND);
        }
    }

    /**
     * @return 刻度抽样次数，当坐标轴宽度大于15px且坐标刻度之间的间隔<15px的时候，刻度间隔显示
     */
    public int getTickSamplingTime() {
        int count = DEFAULT_SAMPLING_TIME;
        double tickNumber = (this.getMaxValue() - this.getMinValue()) / this.mainUnit.doubleValue();

        if(this.isLog()) {// 对数时 另外计算
            double mainValue = Math.abs(this.getMaxValue() - this.getMinValue());
            int x = 0;
            double unitM = BASE_LOG;
            if(this.mainUnit.doubleValue() >= 0) {
                unitM = this.mainUnit.doubleValue();
            }

            while(Math.pow(unitM, x) < mainValue) {
                if(Math.pow(unitM, x + 1) > mainValue) {
                    tickNumber = x;
                    break;
                }
                x++;
            }
        }

        while(this.axisLength > MIN_TICK_LENGTH && (this.axisLength / Math.ceil(tickNumber / count)) < MIN_TICK_LENGTH) {
            count++;
            if(count > MAX_TICK) {
                count = (int)(tickNumber/TICK_NUMBER);
                break;
            }
        }
        return count;
    }

    protected Rectangle2D getLabelBounds(double value, double offset,String labelString,int resolution) {
        if(this.getCubic() == null){
            return super.getLabelBounds(value, offset, labelString, resolution);
        }else{
            if (plotLastBounds == null) {
                return null;
            }
            Rectangle2D labelBounds = null;
            Point2D centerP = getPoint2D(value + offset);
            TextAttr textAttr = this.getTextAttr();
            if (textAttr == null) {
                textAttr = new TextAttr();
            }
            Dimension2D dim = GlyphUtils.calculateTextDimensionWithRotation(labelString, textAttr, resolution);
            int position = this.getPosition();
            double height = dim.getHeight();
            double width = dim.getWidth();

            if(position == Constants.LEFT || position == Constants.RIGHT){
                labelBounds = new Rectangle2D.Double(centerP.getX() - this.getBounds().getWidth() / 2.0 - width / 2.0,
                        centerP.getY() - height / 2.0, width, height);
            }else if(position == Constants.BOTTOM){
                labelBounds = new Rectangle2D.Double(centerP.getX() - width / 2.0,
                        centerP.getY() + this.bounds.getHeight() / 2.0 - height / 2.0,
                        width, height);
            }

            return labelBounds;
        }
    }


    /**
     * 画刻度  y轴
     *
     * @param g
     */
    protected void drawTicks(Graphics g, int resolution) {
        if (this.axisLength <= MIN_TICK_LENGTH) {
            return;
        }
        Graphics2D g2d = (Graphics2D) g;
        BasicStroke mainStroke = new BasicStroke(TICK_WIDTH, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER);
        int tickSamplingTime = getTickSamplingTime();

        if (this.isLog()) {
            drawTicks4Log(g2d, mainStroke);
        } else if (maxValue > minValue) {
            if(this.getMainUnit() > 0 && this.getSecUnit() > 0) {
                drawTicks4General(g2d, mainStroke,tickSamplingTime);
            }
        }

        this.drawTickLable(g, resolution);

    }

    protected void drawTickLable(Graphics g, int resolution){
        if (this.axisLength <= MIN_TICK_LENGTH) {
            return;
        }

        Graphics2D g2d = (Graphics2D)g;
        double divide_unit = 1.0;

        // showUnit处理
        String showUnit = this.getShowUnit();
        if (StringUtils.isNotEmpty(showUnit) && isShowAxisLabel()) {
            divide_unit = AxisHelper.getDivideUnit(showUnit);
            drawUnitGlyph(g2d, ChartConstants.getUnitKey2Value(showUnit), resolution);
        }
        int labelCrement = ((this.getLabelNumber() > 0) ? this.getLabelNumber() : getTickSamplingTime());
        // new BigDecimal(tickLength).. 0.4 转化有问题
        BigDecimal tickLengthBig = this.getBigDecimalMainUnit();
        BigDecimal labelCrementBig = new BigDecimal(Double.toString(labelCrement));

        if (this.isLog()) {
            drawLabel4Log(g, divide_unit, tickLengthBig, labelCrementBig, resolution);
        } else if (maxValue > minValue) {
            if(this.getMainUnit() > 0 && this.getSecUnit() > 0) {
                drawLabel4General(g, divide_unit, tickLengthBig, labelCrementBig, resolution);
            }
        }
    }

    private void drawTicks4Log(Graphics g, BasicStroke mainStroke) {
        if (this.getMainUnit() > 1.0) {
            for (double value = this.getCrossValue(), i = Math.log(value) / Math.log(this.getMainUnit()); value <= maxValue;
                 value = Math.exp((++i) * Math.log(this.getMainUnit()))) {
                // 主要刻度线
                Line2D line = getTickLine(value, MAIN_TICK_LENGTH, this.getMainTickLine());
                drawTickLine(g, line, mainStroke);
            }
        }
        // 对数 : 如果刻度小于1, 将会是负数的话  有可能会溢出导致死机.
        if (this.getSecUnit() > 1.0) {
            double value = this.getCrossValue();
            for (int k = 0; value <= maxValue; value += k, k++) {
                Line2D line = getTickLine(value, SEC_TICK_LENGTH, this.getSecTickLine());
                drawTickLine(g, line, mainStroke);
            }
        }
    }

    private void drawTicks4General(Graphics g, BasicStroke mainStroke, int tickSamplingTime) {
        // kunsnat: 最小的minValue刻度不画
        double tmp = getMainUnit() * tickSamplingTime;
        for (double value = minValue + tmp; value <= maxValue; value += tmp) {
            // 主要刻度线
            Line2D line = getTickLine(value, MAIN_TICK_LENGTH, this.getMainTickLine());
            drawTickLine(g, line, mainStroke);
        }
        if (tickSamplingTime <= 1) {
            // kunsnat: 最小的minValue刻度不画 所以起始值为minValue + smallTickLength
            for (double value = minValue + getSecUnit(); value <= maxValue; value += getSecUnit()) {
                Line2D line = getTickLine(value, SEC_TICK_LENGTH, this.getSecTickLine());
                drawTickLine(g, line, mainStroke);
            }
        }
    }

    private void drawUnitGlyph(Graphics2D g2d, String showUnit, int resolution) {
        TextGlyph unitGlyph = new TextGlyph(showUnit, new TextAttr());
        Dimension2D dim = unitGlyph.preferredDimension(resolution);
        double maxLabelWidth = this.getMaxLabelWidth(resolution);

        TextAttr labelAttr = getTextAttr();
        double labelSize = labelAttr.getFRFont().getSize();

        Rectangle2D chartBounds = null;
        if (this.getPosition() == Constants.TOP) {
            if(this.axisReversed){
                chartBounds = new Rectangle2D.Double(-dim.getWidth()-UNIT_GAP_LABEL - maxLabelWidth/2, dim.getHeight() / 2, dim.getWidth(), dim.getHeight());
            }else{
                chartBounds = new Rectangle2D.Double(getBounds().getWidth() + maxLabelWidth/2 + UNIT_GAP_LABEL,
                        +dim.getHeight() / 2,
                        dim.getWidth(), dim.getHeight());
            }
        } else if (this.getPosition() == Constants.LEFT) {
            if(this.axisReversed){
                chartBounds = new Rectangle2D.Double(0, getBounds().getHeight() + UNIT_GAP_LABEL + labelSize/2,
                        dim.getWidth(), dim.getHeight());
            }else{
                chartBounds = new Rectangle2D.Double(0, -dim.getHeight() - UNIT_GAP_LABEL - labelSize/2,
                        dim.getWidth(), dim.getHeight());
            }

        } else if (this.getPosition() == Constants.BOTTOM) {
            if(this.axisReversed){
                chartBounds = new Rectangle2D.Double(-dim.getWidth()-UNIT_GAP_LABEL - maxLabelWidth/2, -dim.getHeight() / 2, dim.getWidth(), dim.getHeight());
            }else{
                chartBounds = new Rectangle2D.Double(getBounds().getWidth() + maxLabelWidth/2 + UNIT_GAP_LABEL,
                        -dim.getHeight() / 2,
                        dim.getWidth(), dim.getHeight());
            }
        } else if (this.getPosition() == Constants.RIGHT) {
            if(this.axisReversed){
                chartBounds = new Rectangle2D.Double(maxLabelWidth - dim.getWidth(),  getBounds().getHeight() + UNIT_GAP_LABEL + labelSize/2,
                        dim.getWidth(), dim.getHeight());
            }else{
                chartBounds = new Rectangle2D.Double(maxLabelWidth - dim.getWidth(),  -dim.getHeight() - UNIT_GAP_LABEL - labelSize/2,
                        dim.getWidth(), dim.getHeight());
            }
        }
        unitGlyph.setBounds(chartBounds);
        unitGlyph.draw(g2d, resolution);
    }

    private void drawLabel4Log(Graphics g, double divide_unit,
                               BigDecimal tickLengthBig, BigDecimal labelCrementBig,
                               int resolution) {

        if (this.getMainUnit() > 1) {
            BigDecimal valueBig = new BigDecimal(Double.toString(this.getCrossValue()));
            BigDecimal creBig = new BigDecimal(Math.pow(tickLengthBig.doubleValue(), labelCrementBig.doubleValue()));
            for (double value = this.getCrossValue(); value <= maxValue; value = valueBig.doubleValue()) {

                BigDecimal unitValue = valueBig.multiply(new BigDecimal(Double.toString(1 / divide_unit)));
                drawLabel(g, value, 0, this.value2String(unitValue.doubleValue()), resolution);

                valueBig = creBig.multiply(valueBig);
            }
        }
    }

    private void drawLabel4General(Graphics g, double divide_unit,
                                   BigDecimal tickLengthBig, BigDecimal labelCrementBig,
                                   int resolution) {
        // kunsnat: 处理精度值. && 不能用int, 会把小于0 到-1之间的的都省略了.. 特别是百分的时候
        BigDecimal bigValue = new BigDecimal(Double.toString(minValue));
        GeneralPath labelPath = new GeneralPath();
        for (double value = minValue; value - maxValue < 1E-6; value = bigValue.doubleValue()) {
            BigDecimal unitValue = bigValue.multiply(new BigDecimal(Double.toString(1 / divide_unit)));
            String labelString = this.value2String(unitValue.doubleValue());
            drawLabel(g, value, 0, labelString, labelPath, resolution);
            bigValue = bigValue.add(tickLengthBig.multiply(labelCrementBig));
        }
    }

    /**
     * 主次网格线以及警戒线
     */
    public void drawAxisGrid(Graphics g) {
        if (this.axisLength <= MIN_TICK_LENGTH) {
            return;
        }
        Graphics2D g2d = (Graphics2D) g;

        // 主网格线.
        if (this.getMainGridStyle() != Constants.LINE_NONE && this.getMainUnit() > 0
                && this.getMainGridColor() != null) {
            int tickSamplingTime = this.getTickSamplingTime();
            drawMainGridLine(g2d, tickSamplingTime);
        }
    }

    private void drawMainGridLine(Graphics2D g2d, int tickSamplingTime) {
        Stroke oldStoke = g2d.getStroke();
        Paint oldPaint = g2d.getPaint();

        g2d.setPaint(this.getMainGridColor());
        g2d.setStroke(GraphHelper.getStroke(this.getMainGridStyle()));

        if (this.isLog()) {
            drawMainGridLineWithLog(g2d);
        } else {
            drawMainGridLineWithGeneral(g2d, tickSamplingTime);
        }

        g2d.setPaint(oldPaint);
        g2d.setStroke(oldStoke);
    }

    private void drawMainGridLineWithLog(Graphics2D g2d) {
        if (this.getMainUnit() > 1.0) {
            for (double value = this.getCrossValue(), i = Math.log(value) / Math.log(this.getMainUnit()); value <= maxValue; value = Math.exp((++i) * Math.log(this.getMainUnit()))) {
               drawMainGridLineWithValue(g2d, value);
            }
        }
    }

    private void drawMainGridLineWithGeneral(Graphics2D g2d,int tickSamplingTime) {
        BigDecimal valueBig = new BigDecimal(Double.toString(minValue));
        BigDecimal unitBig = new BigDecimal(Double.toString(getMainUnit() * tickSamplingTime));
        BigDecimal valueMax = new BigDecimal(Double.toString(maxValue));
        for ( ; valueBig.compareTo(valueMax) <= 0; valueBig = valueBig.add(unitBig)) {
            drawMainGridLineWithValue(g2d, valueBig.doubleValue());
        }
    }

    //网格线
    protected void drawMainGridLineWithValue(Graphics2D g2d, double value) {
        Line2D[] lines = getGridLine(value);
        for(Line2D line2D : lines) {
            g2d.draw(line2D);
        }
    }

    /**
     * 转为json数据
     *
     * @param repo 请求
     * @return 返回json
     * @throws com.fr.json.JSONException 抛错
     */
    public JSONObject toJSONObject(Repository repo) throws JSONException {
        JSONObject js = super.toJSONObject(repo);
        if(isLog()){
            js.put("log", getLogBase());
        }
        return js;
    }
}
