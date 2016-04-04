package com.fr.plugin.chart.attr;

/**
 * 线的形态，普通、垂直、光滑曲线
 */
public enum  LineStyle {
    NORMAL(),
    STEP(),
    CURVE();

    private static LineStyle[] types;

    /**
     * 将下标转换成枚举类型
     * @param p 待转换的整数
     * @return 枚举
     */
    public static LineStyle parse(int p) {
        if (types == null) {
            types = LineStyle.values();
        }
        for (LineStyle type : types) {
            if (p == type.ordinal()) {
                return type;
            }
        }
        return NORMAL;
    }
}
