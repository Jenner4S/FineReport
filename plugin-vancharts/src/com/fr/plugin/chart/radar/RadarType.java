package com.fr.plugin.chart.radar;

import com.fr.general.ComparatorUtils;

/**
 * Created by Mitisky on 15/12/29.
 */
public enum RadarType {
    CIRCLE("circle"),
    POLYGON("polygon")
    ;

    private String type;

    private RadarType(String type){
        this.type = type;
    }

    public String getType() {
        return this.type;
    }

    private static RadarType[] types;

    public static RadarType parse(String type){
        if(types == null){
            types = RadarType.values();
        }
        for(RadarType radarType : types){
            if(ComparatorUtils.equals(radarType.getType(), type)){
                return radarType;
            }
        }
        return CIRCLE;
    }
}
