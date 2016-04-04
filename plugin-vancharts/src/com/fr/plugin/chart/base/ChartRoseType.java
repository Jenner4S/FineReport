package com.fr.plugin.chart.base;

import com.fr.general.ComparatorUtils;

/**
 * Created by Mitisky on 15/8/21.
 */
public enum ChartRoseType {
    PIE("normal"),
    PIE_SAME_ARC("sameArc"),
    PIE_DIFFERENT_ARC("differentArc");

    private String roseType;

    private ChartRoseType(String roseType){
        this.roseType = roseType;
    }

    public String getRoseType() {
        return roseType;
    }

    private static ChartRoseType[] positions;

    /**
     * ���ַ���ת����ö������
     * @param p ��ת��������
     * @return ö��
     */
    public static ChartRoseType parse(String p) {
        if (positions == null) {
            positions = ChartRoseType.values();
        }
        for (ChartRoseType ip : positions) {
            if (ComparatorUtils.equals(p, ip.roseType)) {
                return ip;
            }
        }
        return PIE;
    }
}
