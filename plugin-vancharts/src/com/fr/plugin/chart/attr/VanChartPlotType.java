package com.fr.plugin.chart.attr;

import com.fr.general.ComparatorUtils;

/**
 * һ��ͼ��Ĳ�ͬ����
 */
public enum VanChartPlotType {
    NORMAL("normal"),
    STACK("stack"),
    STACK_BY_PERCENT("stackbypercent"),
    CUSTOM("custom")
    ;

    //���String��������ģ�����������ġ�
    private String type;
    
    private VanChartPlotType(String type){
        this.type = type;
    }
    
    public String getType() {
        return this.type;
    }
    
    private static VanChartPlotType[] types;
    
    public static VanChartPlotType parse(String type){
        if(types == null){
            types = VanChartPlotType.values();
        }
        for(VanChartPlotType vanChartPlotType : types){
            if(ComparatorUtils.equals(vanChartPlotType.getType(), type)){
                return vanChartPlotType;
            }
        }
        return NORMAL;
    }
}
