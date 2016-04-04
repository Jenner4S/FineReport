package com.fr.plugin.chart.attr;

import com.fr.general.ComparatorUtils;

/**
 * 一种图表的不同类型
 */
public enum VanChartPlotType {
    NORMAL("normal"),
    STACK("stack"),
    STACK_BY_PERCENT("stackbypercent"),
    CUSTOM("custom")
    ;

    //这个String会存起来的，不能随意更改。
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
