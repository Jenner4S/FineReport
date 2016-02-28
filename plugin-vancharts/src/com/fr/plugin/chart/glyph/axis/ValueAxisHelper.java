package com.fr.plugin.chart.glyph.axis;

import com.fr.plugin.chart.attr.VanChartAttrHelper;

import java.math.BigDecimal;

/**
 * Created by Mitisky on 15/12/24.
 */
public class ValueAxisHelper {
    public static final double TICK_COUNT = 5;
    public static final double RADAR_TICK_COUNT = 4;
    private static final double D3_TICK_COUNT = 10;
    private static final double TICK_INTERVAL = 0.25;

    private static final double STEP10 = 10;
    private static final double STEP5 = 5;
    private static final double STEP2 = 2;

    private static final double ERROR15 = .15;
    private static final double ERROR35 = .35;
    private static final double ERROR75 = .75;

    private static final double TEMP = .5;

    private static void setMinMaxTickInterval(VanChartBaseAxisGlyph axisGlyph, double min, double max, double tickInterval){
        axisGlyph.setMinValue(min);
        axisGlyph.setMaxValue(max);
        axisGlyph.setMainUnit(new BigDecimal(tickInterval));
    }

    /**
     * 根据数据配置的最大最小值计算坐标轴最大最小值
     * @param autoMin 数据配置得到的最小值
     * @param autoMax 数据配置得到的最大值
     * @param axisGlyph 坐标轴
     * @param isCustomMinValue 是否自定义最小值
     * @param isCustomMaxValue 是否自定义最大值
     */
    public static void calculateNiceDomain(double autoMin, double autoMax, VanChartValueAxisGlyph axisGlyph, boolean isCustomMinValue, boolean isCustomMaxValue){
        if(axisGlyph.isLog()){
            calculateLogNiceDomain(autoMin, autoMax, axisGlyph, isCustomMinValue, isCustomMaxValue);
        }else if(axisGlyph.isPercentage()){
            calculatePercentValueDomain(axisGlyph, isCustomMinValue, isCustomMaxValue);
        }else{
            calculateValueNiceDomain(autoMin, autoMax, axisGlyph, isCustomMinValue, isCustomMaxValue);
        }

    }

    //log
    private static void calculateLogNiceDomain(double minValue, double maxValue, VanChartValueAxisGlyph axisGlyph, boolean isCustomMinValue, boolean isCustomMaxValue){
        double logBase = axisGlyph.getLogBase();

        double tickInterval = logBase;
        if(axisGlyph.isCustomMainUnit()){
            tickInterval = (int)axisGlyph.getMainUnit();
        }

        minValue = minValue >= 1 ? 1 : minValue;
        minValue = minValue <= 0 ? 1 : minValue;
        if(isCustomMinValue){
            minValue = axisGlyph.getMinValue();
        }
        if(isCustomMaxValue){
            maxValue = axisGlyph.getMaxValue();
        }

        minValue = Math.pow(logBase, Math.floor(Math.log(minValue)/Math.log(logBase)));
        maxValue = Math.pow(logBase, Math.ceil(Math.log(maxValue)/Math.log(logBase)));

        setMinMaxTickInterval(axisGlyph, minValue, maxValue, tickInterval);
    }

    //百分比值轴
    private static void calculatePercentValueDomain(VanChartValueAxisGlyph axisGlyph, boolean isCustomMinValue, boolean isCustomMaxValue){
        double min = 0, max = 1, tickInterval = TICK_INTERVAL;

        if(isCustomMinValue){
            min = axisGlyph.getMinValue();
        }
        if(isCustomMaxValue) {
            max = axisGlyph.getMaxValue();
        }
        if(axisGlyph.isCustomMainUnit()){
            tickInterval = axisGlyph.getMainUnit();
        }
        setMinMaxTickInterval(axisGlyph, min, max, tickInterval);
    }

    //普通的值轴,最小值大于0，从0开始
    public static void calculateValueNiceDomain(double minValue, double maxValue, VanChartBaseAxisGlyph axisGlyph, boolean isCustomMinValue, boolean isCustomMaxValue){
        calculateValueNiceDomain(true, minValue, maxValue, axisGlyph, isCustomMinValue, isCustomMaxValue);
    }

    public static void calculateValueNiceDomain(boolean fromZero, double minValue, double maxValue, VanChartBaseAxisGlyph axisGlyph, boolean isCustomMinValue, boolean isCustomMaxValue){
        double tickInterval;
        double tickCount = axisGlyph.getDefaultTickCount();
        if(axisGlyph.isCustomMainUnit()){
            tickInterval = getTickInterval(fromZero, axisGlyph);
            double[] domain = calculateFixedTickInterval(fromZero, tickInterval, minValue, maxValue, axisGlyph, isCustomMinValue, isCustomMaxValue);
            minValue = domain[0];
            maxValue = domain[1];
        }else{//没有自定义tickInterval
            if(isCustomMinValue && isCustomMaxValue){
                minValue = axisGlyph.getMinValue();
                maxValue = axisGlyph.getMaxValue();
                maxValue = adjustMax(minValue, maxValue);//两个都是自定义，若最大值小于最小值，人为调整下
                tickInterval = (maxValue - minValue) / tickCount;
            }else if(isCustomMinValue){
                minValue = axisGlyph.getMinValue();
                maxValue = adjustMax(minValue, maxValue);
                tickInterval = linearTickDomain(minValue, maxValue, tickCount);
                maxValue = Math.ceil((maxValue - minValue) / tickInterval) * tickInterval + minValue;
            }else if(isCustomMaxValue){
                maxValue = axisGlyph.getMaxValue();
                if(minValue >= 0 && fromZero){
                    double temp = 0;
                    if(maxValue == 0){
                        temp = -VanChartValueAxisGlyph.DEFAULT_MAX;
                    } else if(maxValue < 0){
                        temp = maxValue - Math.abs(maxValue);
                    }
                    minValue = temp;
                    tickInterval = linearTickDomain(minValue, maxValue, tickCount);
                }else{
                    maxValue = adjustMax(minValue, maxValue);
                    tickInterval = linearTickDomain(minValue, maxValue, tickCount);
                    minValue = maxValue - Math.abs(Math.ceil((maxValue - minValue) / tickInterval) * tickInterval);
                }
            }else{
                minValue = minValue >= 0 && fromZero ? 0 : minValue;
                double[] domain = calculateNiceDomain(minValue, maxValue, tickCount);
                minValue = domain[0];
                maxValue = domain[1];
                tickInterval = (maxValue - minValue) / tickCount;
                if(axisGlyph.getDefaultTickCount() != RADAR_TICK_COUNT){
                    minValue = minValue >= 0 ? minValue : minValue - tickInterval;
                    maxValue += tickInterval;
                }
            }
        }
        setMinMaxTickInterval(axisGlyph, minValue, maxValue, tickInterval);
    }

    private static double getTickInterval(boolean fromZero, VanChartBaseAxisGlyph axisGlyph) {
        if(fromZero){
            return axisGlyph.getMainUnit();
        } else {//忽略刻度单位，以秒计算
            long timeTypeSecond = VanChartAttrHelper.getTimeTypeSecond(((VanChartTimeAxisGlyph)axisGlyph).getMainType());
            return axisGlyph.getMainUnit() * timeTypeSecond;
        }
    }

    private static double adjustMax(double minValue, double maxValue) {
        if(minValue >= maxValue){
            maxValue = minValue + (maxValue == 0 ? VanChartValueAxisGlyph.DEFAULT_MAX : Math.abs(maxValue));
        }
        return maxValue;
    }

    private static double linearTickDomain(double min, double max, double m){
        return d3_scale_linearTickRange(new double[] {min, max}, m)[2];
    }

    private static double[] calculateFixedTickInterval(boolean fromZero, double tickInterval, double minValue, double maxValue,
                                                       VanChartBaseAxisGlyph axisGlyph, boolean isCustomMinValue, boolean isCustomMaxValue){
        if(isCustomMinValue && isCustomMaxValue){
            minValue = axisGlyph.getMinValue();
            maxValue = axisGlyph.getMaxValue();
        }else if(isCustomMinValue){
            minValue = axisGlyph.getMinValue();
        }else if(isCustomMaxValue){
            maxValue = axisGlyph.getMaxValue();

            if(minValue >= 0 && fromZero){
                minValue = 0;
            }else{
                minValue = -1 * Math.ceil(Math.abs(minValue / tickInterval)) * tickInterval ;
            }
        }else{
            //用自动计算出来的最大最小值计算出tick的count
            minValue = minValue >= 0 && fromZero ? 0 : minValue;

            double tickCount = Math.ceil((maxValue - minValue) / tickInterval);

            if(fromZero){
                double[] domain = calculateNiceDomain(minValue, maxValue, tickCount);
                minValue = domain[0];
                maxValue = domain[1];
            }
        }

        return new double[]{minValue, maxValue};
    }

    private static double[] calculateNiceDomain(double minValue, double maxValue, double tickCount){
        return d3_scale_linearNice(new double[]{minValue, maxValue}, tickCount);
    }

    private static double[] d3_scale_linearNice(double[] domain, double m) {
        return d3_scale_nice(domain, d3_scale_linearTickRange(domain, m)[2]);
    }

    private static double[] d3_scale_nice(double [] domain, double step) {
        int i0 = 0, i1 = domain.length - 1, temp1;
        double x0 = domain[i0], x1 = domain[i1], temp2;
        if (x1 < x0) {
            temp1 = i0; i0 = i1; i1 = temp1;
            temp2 = x0; x0 = x1; x1 = temp2;
        }
        domain[i0] = Math.floor(x0 / step) * step;
        domain[i1] = Math.ceil(x1 / step) * step;
        return domain;
    }

    private static double[] d3_scale_linearTickRange(double[] domain, double m) {
        if (m == 0) {
            m = D3_TICK_COUNT;
        }
        domain = d3_scaleExtent(domain);
        double[] extent = new double[3];
        double span = domain[1] - domain[0], step = Math.pow(10, Math.floor(Math.log(span / m) / Math.log(10))), err = m / span * step;
        if (err <= ERROR15) {step *= STEP10;} else if (err <= ERROR35) {step *= STEP5;} else if (err <= ERROR75) {step *= STEP2;}
        extent[0] = Math.ceil(domain[0] / step) * step;
        extent[1] = Math.floor(domain[1] / step) * step + step * TEMP;
        extent[2] = step;
        return extent;
    }

    private static double[] d3_scaleExtent(double[] domain) {
        double start = domain[0], stop = domain[domain.length - 1];
        return start < stop ? domain : new double[]{stop, start};
    }

}
