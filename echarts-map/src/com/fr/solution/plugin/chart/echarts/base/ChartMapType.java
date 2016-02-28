package com.fr.solution.plugin.chart.echarts.base;

import com.fr.general.ComparatorUtils;

public enum ChartMapType {
//	CHINESE_MAP,WORLD_MAP;
	CHINESE_MAP("china"),
	WORLD_MAP("world"),
	GUIDEMAP("guide");
    private String type;

    private ChartMapType(String type){
        this.type = type;
    }

    public String getType() {
        return type;
    }

    private static ChartMapType[] positions;

    /**
     * ���ַ���ת����ö������
     * @param p ��ת��������
     * @return ö��
     */
    public static ChartMapType parse(String p) {
        if (positions == null) {
            positions = ChartMapType.values();
        }
        for (ChartMapType ip : positions) {
            if (ComparatorUtils.equals(p, ip.type)) {
                return ip;
            }
        }
        return CHINESE_MAP;
    }
}
