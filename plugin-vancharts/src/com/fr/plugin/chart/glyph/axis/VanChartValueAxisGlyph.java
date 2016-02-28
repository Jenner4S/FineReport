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
     * �����Ƿ�ٷֱ� (ֻ��������NumberAxis֧�ִ˹���)
     *
     * @return �Ƿ�ٷֱ�
     */
    public boolean isPercentage() {
        return this.isPercentage;
    }

    /**
     * ���ö����ĵ�
     */
    public void setLogBase(double logBase) {
        this.logBase = logBase;
    }

    /**
     * ���ض����ĵ�
     */
    public double getLogBase() {
        return logBase;
    }

    /**
     * �����Ƿ��ö���������
     */
    public void setLog(boolean isLog) {
        this.isLog = isLog;
    }

    /**
     * ���� �Ƿ�Ϊ��������������
     * @return  �Ƿ�Ϊ����.
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
     * ���ض�Ӧ��js����.
     */
    public String getJSAxisType() {
        return "value";
    }

    public String getAxisName(){
        return "ValueAxis";
    }

    /**
     * ���ؽ�����ֵ.
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
     * ����ֵ ��Ӧ��ͼ��λ�õ������
     *  ������Խ������. ���� �������ֵ��ʱ�� ȡ���ֵ
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
     * ����ֵ ����Ӧ���������
     */
    public Point2D getPoint2D(double value) {
        // ָ��������
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
     * ���������������λ�� ����ռ�õĿռ��� , Ϊ�˸����ݱ�Ԥ��λ��
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
     * ��ȡֵ������ռ��λ�����ı�ǩ�ĳߴ�, ���������᷵�ر�ǩ�����߶ȣ����������᷵�ر�ǩ�������
     * ��ʼ����������ı�ǩDim��С
     * @param resolution �ֱ���
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
     * ���������� ��λ����
     */
    public void calculateAxisLengthUnit() {
        if (this.isLog()) {
            unitLength = axisLength / (Math.log(maxValue) / Math.log(this.getMainUnit()) - Math.log(minValue) / Math.log(this.getMainUnit()));
        } else if (maxValue - minValue > 0) {
            unitLength = axisLength / (maxValue - minValue);
        }
    }

    /**
     * ������Сֵ
     */
    public double getCrossValue() {
        return minValue;
    }

    /**
     * �����������ͷ����ֵ, ���ֵ
     */
    public double getArrowValue() {
        return maxValue;
    }

    /**
     * ��ʼ�����ֵ����Сֵ�Լ��̶ȴ�С
     *
     * @param autoMin ��Сֵ
     * @param autoMax ���ֵ
     */
    public void initMinMaxValue(double autoMin, double autoMax) {
        initMinMaxValue(autoMin, autoMax, false);
    }

    /**
     * ��ʼ������������ֵ����Сֵ
     * @param autoMin �Զ�������Ƿ���������Сֵ������ͼx��Ϊ��ֵ����ʱ�䣩
     * @param autoMax ��Сֵ
     * @param adjustMinMax ���ֵ
     */
    public void initMinMaxValue(double autoMin, double autoMax, boolean adjustMinMax){
        if(autoMin > 0){
            autoMin = 0;
        }

        //��ֹ��ѭ��
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
     * @return �̶ȳ������������������ȴ���15px������̶�֮��ļ��<15px��ʱ�򣬿̶ȼ����ʾ
     */
    public int getTickSamplingTime() {
        int count = DEFAULT_SAMPLING_TIME;
        double tickNumber = (this.getMaxValue() - this.getMinValue()) / this.mainUnit.doubleValue();

        if(this.isLog()) {// ����ʱ �������
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
     * ���̶�  y��
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

        // showUnit����
        String showUnit = this.getShowUnit();
        if (StringUtils.isNotEmpty(showUnit) && isShowAxisLabel()) {
            divide_unit = AxisHelper.getDivideUnit(showUnit);
            drawUnitGlyph(g2d, ChartConstants.getUnitKey2Value(showUnit), resolution);
        }
        int labelCrement = ((this.getLabelNumber() > 0) ? this.getLabelNumber() : getTickSamplingTime());
        // new BigDecimal(tickLength).. 0.4 ת��������
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
                // ��Ҫ�̶���
                Line2D line = getTickLine(value, MAIN_TICK_LENGTH, this.getMainTickLine());
                drawTickLine(g, line, mainStroke);
            }
        }
        // ���� : ����̶�С��1, �����Ǹ����Ļ�  �п��ܻ������������.
        if (this.getSecUnit() > 1.0) {
            double value = this.getCrossValue();
            for (int k = 0; value <= maxValue; value += k, k++) {
                Line2D line = getTickLine(value, SEC_TICK_LENGTH, this.getSecTickLine());
                drawTickLine(g, line, mainStroke);
            }
        }
    }

    private void drawTicks4General(Graphics g, BasicStroke mainStroke, int tickSamplingTime) {
        // kunsnat: ��С��minValue�̶Ȳ���
        double tmp = getMainUnit() * tickSamplingTime;
        for (double value = minValue + tmp; value <= maxValue; value += tmp) {
            // ��Ҫ�̶���
            Line2D line = getTickLine(value, MAIN_TICK_LENGTH, this.getMainTickLine());
            drawTickLine(g, line, mainStroke);
        }
        if (tickSamplingTime <= 1) {
            // kunsnat: ��С��minValue�̶Ȳ��� ������ʼֵΪminValue + smallTickLength
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
        // kunsnat: ������ֵ. && ������int, ���С��0 ��-1֮��ĵĶ�ʡ����.. �ر��ǰٷֵ�ʱ��
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
     * �����������Լ�������
     */
    public void drawAxisGrid(Graphics g) {
        if (this.axisLength <= MIN_TICK_LENGTH) {
            return;
        }
        Graphics2D g2d = (Graphics2D) g;

        // ��������.
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

    //������
    protected void drawMainGridLineWithValue(Graphics2D g2d, double value) {
        Line2D[] lines = getGridLine(value);
        for(Line2D line2D : lines) {
            g2d.draw(line2D);
        }
    }

    /**
     * תΪjson����
     *
     * @param repo ����
     * @return ����json
     * @throws com.fr.json.JSONException �״�
     */
    public JSONObject toJSONObject(Repository repo) throws JSONException {
        JSONObject js = super.toJSONObject(repo);
        if(isLog()){
            js.put("log", getLogBase());
        }
        return js;
    }
}
