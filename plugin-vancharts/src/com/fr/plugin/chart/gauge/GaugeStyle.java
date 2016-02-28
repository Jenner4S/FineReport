package com.fr.plugin.chart.gauge;

import com.fr.general.ComparatorUtils;

/**
 * Created by Mitisky on 15/11/27.
 */
public enum GaugeStyle {
    POINTER("pointer"),//��ָ��360
    POINTER_SEMI("pointer_semi"),//��ָ��180
    RING("ring"),//�ٷֱ�Բ��
    SLOT("slot"),//�ٷֱȿ̶Ȳ�
    THERMOMETER("thermometer");//�Թ�

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
