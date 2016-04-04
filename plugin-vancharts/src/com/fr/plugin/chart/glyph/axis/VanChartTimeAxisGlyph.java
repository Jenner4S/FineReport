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
     * 主要刻度的日期类型
     */
    private TimeType mainType = TimeType.TIME_SECOND;//最大最小值和主要刻度单位就是以主要刻度类型（没有设置的时候默认为秒）为标准确认值的。主要刻度类型为天。月、年时，以天为标准。
    /**
     * 次要刻度的日期类型
     */
    private TimeType secondType = TimeType.TIME_SECOND;

    //画标签的时候用到，只计算一次
    private Format defaultFormat;//格式为常规时，根据最大最小值计算格式，为自动格式。

    /**
     * 设置主要日期类型
     */
    public void setMainType(TimeType mainType) {
        this.mainType = mainType;
    }

    /**
     * 返回主要日期类型
     */
    public TimeType getMainType() {
        return mainType;
    }

    /**
     * 设置次要日期类型
     */
    public void setSecondType(TimeType secondType) {
        this.secondType = secondType;
    }

    /**
     * 返回次要日期类型
     */
    public TimeType getSecondType() {
        return secondType;
    }

    /**
     * 计算最大值最小值
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
            //这里面都是以秒为基础算的.最大最小值和主要刻度单位。
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

        if (isCustomSecUnit()) {//次要刻度要和主要刻度是一个单位的，以主要刻度单位为准。
            setSecUnit(getSecUnit() * VanChartAttrHelper.getTimeTypeSecond(getSecondType())/timeTypeSecond);
        } else {
            setSecUnit(getMainUnit() / FIVE);
        }
    }

    public void drawAxisGrid(Graphics g) {
        // 主网格线.
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
     * kunsnat: 直接用日期对应的 刻度计算. 不要用之前的秒, 分对应的毫秒什么的. 日期不固定, 比如每个月的秒数就不一样.
     * @param startDate 起始日期
     * @param dateType 日期刻度类型
     * @param addValue 增加的值
     * @return 返回结果日期.
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
     * @return 刻度抽样次数，当坐标轴宽度大于15px且坐标刻度之间的间隔<15px的时候，刻度间隔显示
     */
    public int getTickSamplingTime() {
        int count = DEFAULT_SAMPLING_TIME;
        //最大最小值和主要刻度单位就是以主要刻度类型（没有设置的时候默认为秒）为标准确认值的。主要刻度类型为天。月、年时，以天为标准。
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
        double dateTypeNumber = 1;// 大概的天数目, 除以对应主单位类型大概的天数
        if(ComparatorUtils.equals(TimeType.TIME_MONTH, this.getMainType())) {
            dateTypeNumber = MONTHDAY;
        } else if(ComparatorUtils.equals(TimeType.TIME_YEAR, this.getMainType())) {
            dateTypeNumber = YEARDAY;
        }
        return dateTypeNumber;
    }

    /**
     * 计算坐标轴长度等 属性.
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
     * 得到坐标轴标签中 占用位置最大的标签的尺寸
     *
     * @param resolution 分辨率
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
     * 画出刻度相关. 刻度线 刻度标签 等
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
        //主要刻度
        while (isBeyondDateRange(minDate, maxDate, (int) this.getMainUnit())) {
            double tmpValue = ChartBaseUtils.date2Int(minDate, getMainType().getTimeType());
            Line2D line = getTickLine(tmpValue, MAIN_TICK_LENGTH, this.getMainTickLine());
            drawTickLine(g, line, mainStroke);
            minDate = nextDate(minDate, this.getMainType().getTimeType(), (int) this.getMainUnit() * labelCrement);
        }


        minDate = ChartBaseUtils.long2Date((long) minValue, this.getMainType().getTimeType());

        //次要刻度
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
     * 此坐标轴在zero点对应的值是多少，
     */
    public double getCrossValue(){
        return this.getMinValue();
    }


    /**
     * 获取坐标轴在箭头处，也即坐标尾部的值
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
     * 返回左轴可能的预留宽度 标签 + 标题 + 警戒值
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
        //最大最小值和主要刻度单位就是以主要刻度类型（没有设置的时候默认为秒）为标准确认值的。主要刻度类型为天。月、年时，以天为标准。
        //改正1：往前台传属性的时候，坐标轴这边并没有计算最大最小值，只是初始化（转化成以秒为单位的）。所以自定义的最大最小值都是以秒为单位的，只需要再转化成毫秒即可。
        //改正2：主次刻度单位初始的时候只有值，所以根据单位转化成毫秒。
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
