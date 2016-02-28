package com.fr.plugin.chart.attr.axis;

/**
 * Created by Mitisky on 15/11/23.
 */
public enum AxisTickLineType {
    TICK_LINE_NONE(0),
    TICK_LINE_OUTSIDE(2);

    private int type;

    private AxisTickLineType(int type){
        this.type = type;
    }

    private static AxisTickLineType[] types;

    public int getType(){
        return this.type;
    }

    public static AxisTickLineType parse(int type){
        if(types == null){
            types = AxisTickLineType.values();
        }
        for(AxisTickLineType tickLineType : types){
            if(tickLineType.getType() == type){
                return tickLineType;
            }
        }
        return TICK_LINE_NONE;
    }
}
