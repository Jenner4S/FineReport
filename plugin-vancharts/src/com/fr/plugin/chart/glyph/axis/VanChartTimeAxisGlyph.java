package com.fr.plugin.chart.glyph.axis;

import com.fr.base.GraphHelper;
import com.fr.chart.base.ChartBaseUtils;
import com.fr.chart.base.ChartConstants;
import com.fr.general.ComparatorUtils;
import com.fr.general.DateUtils;
import com.fr.general.FRLogger;
import com.fr.json.JSONException;
import com.fr.json.JSONObject;
import com.fr.plugin.chart.attr.VanChartAttrHelper;
import com.fr.plugin.chart.attr.axis.TimeType;
import com.fr.stable.Constants;
import com.fr.stable.web.Repository;

import java.awt.*;
import java.awt.geom.Dimension2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.math.BigDecimal;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Mitisky on 15/10/19.
 */
public class VanChartTimeAxisGlyph extends VanChartBaseAxisGlyph {
    public static final int DEFAULT_MAX = 1000;
    private static final double FIVE = 5.0;
    private static final double LABEL_WIDTH = 8;
    private static final double MONTHDAY = 30;
    private static final double YEARDAY = 365;
    private static final double MAX_TICK = 1000;
    private static final double TICK_NUMBER = 20;
    /**
     * ��Ҫ�̶ȵ���������
     */
    private TimeType mainType = TimeType.TIME_SECOND;//�����Сֵ����Ҫ�̶ȵ�λ��������Ҫ�̶����ͣ�û�����õ�ʱ��Ĭ��Ϊ�룩Ϊ��׼ȷ��ֵ�ġ���Ҫ�̶�����Ϊ�졣�¡���ʱ������Ϊ��׼��
    /**
     * ��Ҫ�̶ȵ���������
     */
    private TimeType secondType = TimeType.TIME_SECOND;

    //����ǩ��ʱ���õ���ֻ����һ��
    private Format defaultFormat;//��ʽΪ����ʱ�����������Сֵ�����ʽ��Ϊ�Զ���ʽ��

    /**
     * ������Ҫ��������
     */
    public void setMainType(TimeType mainType) {
        this.mainType = mainType;
    }

    /**
     * ������Ҫ��������
     */
    public TimeType getMainType() {
        return mainType;
    }

    /**
     * ���ô�Ҫ��������
     */
    public void setSecondType(TimeType secondType) {
        this.secondType = secondType;
    }

    /**
     * ���ش�Ҫ��������
     */
    public TimeType getSecondType() {
        return secondType;
    }

    /**
     * �������ֵ��Сֵ
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
            //�����涼������Ϊ�������.�����Сֵ����Ҫ�̶ȵ�λ��
            ValueAxisHelper.calculateValueNiceDomain(false, autoMin, autoMax, this, isCustomMinValue, isCustomMaxValue);
        } catch (Exception e) {
            FRLogger.getLogger().error(e.getMessage());
            setMinValue(autoMin);
            setMaxValue(autoMax);
            setMainUnit(new BigDecimal((autoMax - autoMin)/getDefaultTickCount()));
        }

        Date minDate = ChartBaseUtils.long2Date((long) getMinValue(), ChartConstants.SECOND_TYPE);
        double typeMin = ChartBaseUtils.date2Int(minDate, getMainType().getTimeType());
        setMinValue(typeMin);
        Date maxDate = ChartBaseUtils.long2Date((long) getMaxValue(), ChartConstants.SECOND_TYPE);
        double typeMax = ChartBaseUtils.date2Int(maxDate, getMainType().getTimeType());
        setMaxValue(typeMax);
        long timeTypeSecond = VanChartAttrHelper.getTimeTypeSecond(getMainType());
        double typeMainUnit = getMainUnit()/timeTypeSecond;
        setMainUnit(new BigDecimal(typeMainUnit));

        if (isCustomSecUnit()) {//��Ҫ�̶�Ҫ����Ҫ�̶���һ����λ�ģ�����Ҫ�̶ȵ�λΪ׼��
            setSecUnit(getSecUnit() * VanChartAttrHelper.getTimeTypeSecond(getSecondType())/timeTypeSecond);
        } else {
            setSecUnit(getMainUnit() / FIVE);
        }
    }

    public void drawAxisGrid(Graphics g) {
        // ��������.
        if (needDrawMainAxisGrid()) {
            drawGridLine(g, this.getMainGridStyle(), this.getMainGridColor(),
                    (int) this.getMainUnit(), this.getMainType().getTimeType());
        }
    }

    private boolean needDrawMainAxisGrid() {
        return getMainGridStyle() != Constants.LINE_NONE
                && getMainUnit() > 0
                && getMainGridColor() != null;
    }

    private void drawGridLine(Graphics g, int lineStyle, Color lineColor, int unitValue, int type) {
        Graphics2D g2d = (Graphics2D) g;

        Stroke oldStoke = g2d.getStroke();
        Paint oldPaint = g2d.getPaint();

        g2d.setPaint(lineColor);
        g2d.setStroke(GraphHelper.getStroke(lineStyle));

        Date minDate = ChartBaseUtils.long2Date((long) minValue, getMainType().getTimeType());
        Date maxDate = ChartBaseUtils.long2Date((long) maxValue, getMainType().getTimeType());
        int tickSamplingTime = this.getTickSamplingTime();

        DateUtils.object2Date(new Double(getMinValue()), false);
        while (isBeyondDateRange(minDate, maxDate, unitValue * tickSamplingTime)) {

            double tmpValue = ChartBaseUtils.date2Int(minDate, getMainType().getTimeType());

            Line2D[] lines = getGridLine(tmpValue);
            for (int i = 0; i < lines.length; i++) {
                g2d.draw(lines[i]);
            }

            minDate = nextDate(minDate, type, unitValue * tickSamplingTime);
        }
        g2d.setPaint(oldPaint);
        g2d.setStroke(oldStoke);
    }

    private boolean isBeyondDateRange(Date minDate, Date maxDate, int unitValue) {
        return minDate.getTime() <= maxDate.getTime() && unitValue > 0;
    }

    /**
     * kunsnat: ֱ�������ڶ�Ӧ�� �̶ȼ���. ��Ҫ��֮ǰ����, �ֶ�Ӧ�ĺ���ʲô��. ���ڲ��̶�, ����ÿ���µ������Ͳ�һ��.
     * @param startDate ��ʼ����
     * @param dateType ���ڿ̶�����
     * @param addValue ���ӵ�ֵ
     * @return ���ؽ������.
     */
    public Date nextDate(Date startDate, int dateType, int addValue) {
        if(dateType == ChartConstants.SECOND_TYPE) {
            startDate.setSeconds(startDate.getSeconds() + addValue);
        } else if(dateType == ChartConstants.MINUTE_TYPE) {
            startDate.setMinutes(startDate.getMinutes() + addValue);
        } else if(dateType == ChartConstants.HOUR_TYPE) {
            startDate.setHours(startDate.getHours() + addValue);
        } else if(dateType == ChartConstants.MONTH_TYPE) {
            startDate.setMonth(startDate.getMonth() + addValue);
        } else if(dateType == ChartConstants.YEAR_TYPE) {
            startDate.setYear(startDate.getYear() + addValue);
        } else if(dateType == ChartConstants.DAY_TYPE) {
            startDate.setDate(startDate.getDate() + addValue);
        }

        return startDate;
    }

    public double getObjectValue(Object ob) {
        Date date = DateUtils.object2Date(ob, true);
        if(date != null){
            return ChartBaseUtils.date2Int(date, getMainType().getTimeType());
        }
        return getCrossValue();
    }

    /**
     * @return �̶ȳ������������������ȴ���15px������̶�֮��ļ��<15px��ʱ�򣬿̶ȼ����ʾ
     */
    public int getTickSamplingTime() {
        int count = DEFAULT_SAMPLING_TIME;
        //�����Сֵ����Ҫ�̶ȵ�λ��������Ҫ�̶����ͣ�û�����õ�ʱ��Ĭ��Ϊ�룩Ϊ��׼ȷ��ֵ�ġ���Ҫ�̶�����Ϊ�졣�¡���ʱ������Ϊ��׼��
        double tickNumber = (this.getMaxValue() - this.getMinValue()) / (this.mainUnit.doubleValue() * getDateTypeNumber());
        while (this.axisLength > MIN_TICK_LENGTH && (this.axisLength / Math.ceil(tickNumber / count)) < MIN_TICK_LENGTH) {
            count++;
            if(count > MAX_TICK) {
                count = (int)(tickNumber/TICK_NUMBER);
                break;
            }
        }
        return count;
    }

    protected double getDateTypeNumber() {
        double dateTypeNumber = 1;// ��ŵ�����Ŀ, ���Զ�Ӧ����λ���ʹ�ŵ�����
        if(ComparatorUtils.equals(TimeType.TIME_MONTH, this.getMainType())) {
            dateTypeNumber = MONTHDAY;
        } else if(ComparatorUtils.equals(TimeType.TIME_YEAR, this.getMainType())) {
            dateTypeNumber = YEARDAY;
        }
        return dateTypeNumber;
    }

    /**
     * ���������᳤�ȵ� ����.
     */
    protected void calculateAxisLengthUnit(){
        double tickNumber = this.getMaxValue() - this.getMinValue();
        if (tickNumber <= 0) {
            unitLength = 1;
            return;
        }

        unitLength = axisLength / Math.max(1, tickNumber);
    }

    /**
     * �õ��������ǩ�� ռ��λ�����ı�ǩ�ĳߴ�
     *
     * @param resolution �ֱ���
     */
    protected double getMaxLabelWidthAndInitStartEndLabelDim(int resolution){
        if (!isShowAxisLabel) {
            return 0;
        }
        double labelWidth = LABEL_WIDTH;

        Date minDate = ChartBaseUtils.long2Date((long) minValue, getMainType().getTimeType());
        Date maxDate = ChartBaseUtils.long2Date((long) maxValue, getMainType().getTimeType());
        int first = 0;
        int tickSamplingTime = getTickSamplingTime();
        int labelCrement = ((this.getLabelNumber() > 0) ? this.getLabelNumber() : tickSamplingTime);

        while (isBeyondDateRange(minDate, maxDate,(int) this.getMainUnit() * labelCrement)) {
            String valueString = "";
            if (format != null) {
                valueString = format.format(minDate);
            } else {
                if(defaultFormat == null){
                    calculateDefaultFormat();
                }
                valueString = defaultFormat.format(minDate);
            }

            Dimension2D dim = getAxisLabelDim(valueString, resolution);
            if(first == 0) {
                startLabelDim = dim;
                first += 1;
            } else {
                endLabelDim = dim;
            }

            double tmpWidth = getAxisLabelWidth(valueString, resolution);
            labelWidth = (tmpWidth < labelWidth ? labelWidth : tmpWidth);
            minDate = nextDate(minDate, getMainType().getTimeType(), (int) this.getMainUnit() * labelCrement);
        }
        return labelWidth;
    }

    private void calculateDefaultFormat() {
        if(format == null){
            double milliSeconds = getMainUnit() * VanChartAttrHelper.getTimeTypeMilliSecond4MinMax(getMainType());
            if(milliSeconds < ChartConstants.DAY_2_MILLISECOND){
                defaultFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            } else if(milliSeconds < ChartConstants.MONTH_2_MILLISECOND){
                defaultFormat = new SimpleDateFormat("yyyy-MM-dd");
            } else {
                defaultFormat = new SimpleDateFormat("yyyy-MM");
            }
        }
    }

    /**
     * �����̶����. �̶��� �̶ȱ�ǩ ��
     */
    protected void drawTicks(Graphics g, int resolution){
        if (this.axisLength < MIN_TICK_LENGTH) {
            return;
        }
        BasicStroke mainStroke = new BasicStroke(TICK_WIDTH, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER);

        Date minDate = ChartBaseUtils.long2Date((long) minValue, this.getMainType().getTimeType());
        Date maxDate = ChartBaseUtils.long2Date((long) maxValue, this.getMainType().getTimeType());

        Format format = ( this.getFormat() == null ? defaultFormat : this.getFormat() );
        int tickSamplingTime = getTickSamplingTime();
        int labelCrement = ((this.getLabelNumber() > 0) ? this.getLabelNumber() : tickSamplingTime);
        //��Ҫ�̶�
        while (isBeyondDateRange(minDate, maxDate, (int) this.getMainUnit())) {
            double tmpValue = ChartBaseUtils.date2Int(minDate, getMainType().getTimeType());
            Line2D line = getTickLine(tmpValue, MAIN_TICK_LENGTH, this.getMainTickLine());
            drawTickLine(g, line, mainStroke);
            minDate = nextDate(minDate, this.getMainType().getTimeType(), (int) this.getMainUnit() * labelCrement);
        }


        minDate = ChartBaseUtils.long2Date((long) minValue, this.getMainType().getTimeType());

        //��Ҫ�̶�
        if(tickSamplingTime <= 1){
            while (isBeyondDateRange(minDate, maxDate, (int) this.getSecUnit())) {
                double tmpValue = ChartBaseUtils.date2Int(minDate, getMainType().getTimeType());
                Line2D line = getTickLine(tmpValue, SEC_TICK_LENGTH, this.getSecTickLine());
                drawTickLine(g, line, mainStroke);
                minDate = nextDate(minDate, this.getMainType().getTimeType(), (int) this.getSecUnit() * labelCrement);
            }
        }


        minDate = ChartBaseUtils.long2Date((long) minValue, getMainType().getTimeType());
        GeneralPath boundsPath = new GeneralPath();
        while (isBeyondDateRange(minDate, maxDate, (int) this.getMainUnit())) {
            double tmpValue = ChartBaseUtils.date2Int(minDate,getMainType().getTimeType());
            String valueString = format.format(minDate);
            Rectangle2D labelBounds = getLabelBounds(tmpValue, 0, valueString, resolution);
            if (!boundsPath.intersects(labelBounds)) {
                drawLabel(g, tmpValue, 0, valueString, resolution);
                boundsPath.append(labelBounds, false);
            }

            minDate = nextDate(minDate, this.getMainType().getTimeType(), (int) this.getMainUnit() * labelCrement);
        }
    }

    /**
     * ����������zero���Ӧ��ֵ�Ƕ��٣�
     */
    public double getCrossValue(){
        return this.getMinValue();
    }


    /**
     * ��ȡ�������ڼ�ͷ����Ҳ������β����ֵ
     */
    public double getArrowValue(){
        return this.getMaxValue();
    }

    public String getJSAxisType(){
        return "time";
    }

    public String getAxisName(){
        return "timeAxis";
    }

    /**
     * ����������ܵ�Ԥ����� ��ǩ + ���� + ����ֵ
     */
    public double getPreLeftWidth4DataSheet(int resolution){
        calculateTitleDimesion(resolution);
        double titlePreWidth = 0;
        if (titleDim.getWidth() > 0) {
            titlePreWidth = titleDim.getWidth() + ChartConstants.AXIS_LINE_LABEL_GAP;
        }
        double labelWidth = getMaxLabelWidthAndInitStartEndLabelDim(resolution);
        return (this.position == Constants.LEFT) ? (labelWidth + titlePreWidth) : 0;
    }


    protected void addMinMaxValue(JSONObject js, Repository repo) throws JSONException{
        //�����Сֵ����Ҫ�̶ȵ�λ��������Ҫ�̶����ͣ�û�����õ�ʱ��Ĭ��Ϊ�룩Ϊ��׼ȷ��ֵ�ġ���Ҫ�̶�����Ϊ�졣�¡���ʱ������Ϊ��׼��
        //����1����ǰ̨�����Ե�ʱ����������߲�û�м��������Сֵ��ֻ�ǳ�ʼ����ת��������Ϊ��λ�ģ��������Զ���������Сֵ��������Ϊ��λ�ģ�ֻ��Ҫ��ת���ɺ��뼴�ɡ�
        //����2�����ο̶ȵ�λ��ʼ��ʱ��ֻ��ֵ�����Ը��ݵ�λת���ɺ��롣
        if(isCustomMaxValue){
            js.put("max", getMaxValue() * ChartConstants.SECOND_2_MILLISECOND);
        }
        if(isCustomMinValue){
            js.put("min", getMinValue() * ChartConstants.SECOND_2_MILLISECOND);
        }
        if(isCustomMainUnit()){
            js.put("tickInterval", getMainUnit() * VanChartAttrHelper.getTimeTypeMilliSecond(getMainType()));
        }
        if(isCustomSecUnit()){
            js.put("minorTickInterval", getSecUnit() * VanChartAttrHelper.getTimeTypeMilliSecond(getSecondType()));
        }

    }
}
