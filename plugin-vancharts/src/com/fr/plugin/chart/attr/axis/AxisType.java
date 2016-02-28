package com.fr.plugin.chart.attr.axis;

import com.fr.general.ComparatorUtils;

/**
 * 坐标轴类型
 */
public enum AxisType {
    AXIS_CATEGORY("category"),
    AXIS_TIME("datetime"),
    AXIS_VALUE("value");
    private String axisType;

    private AxisType(String axisType){
        this.axisType = axisType;
    }

    public String getAxisType() {
        return this.axisType;
    }

    private static AxisType[] types;

    /**
     * 将字符串转换成枚举类型
     * @param p 待转换的整数
     * @return 枚举
     */
    public static AxisType parse(String p) {
        if (types == null) {
            types = AxisType.values();
        }
        for (AxisType type : types) {
            if (ComparatorUtils.equals(p, type.axisType)) {
                return type;
            }
        }
        return AXIS_CATEGORY;
    }
}
