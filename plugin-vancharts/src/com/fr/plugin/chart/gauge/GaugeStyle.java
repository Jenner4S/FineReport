package com.fr.plugin.chart.gauge;

import com.fr.general.ComparatorUtils;

/**
 * Created by Mitisky on 15/11/27.
 */
public enum GaugeStyle {
    POINTER("pointer"),//多指针360
    POINTER_SEMI("pointer_semi"),//多指针180
    RING("ring"),//百分比圆环
    SLOT("slot"),//百分比刻度槽
    THERMOMETER("thermometer");//试管

    private String style;

    private GaugeStyle(String style){
        this.style = style;
    }

    public static GaugeStyle[] styles;

    public String getStyle() {
        return style;
    }

    public static GaugeStyle parse(String style){
        if(styles == null){
            styles = GaugeStyle.values();
        }
        for(GaugeStyle gaugeStyle : styles){
            if(ComparatorUtils.equals(gaugeStyle.getStyle(), style)){
                return gaugeStyle;
            }
        }
        return POINTER;
    }

}
