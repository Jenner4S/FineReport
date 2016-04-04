package com.fr.plugin.chart.attr;

/**
 * �ߵ���̬����ͨ����ֱ���⻬����
 */
public enum  LineStyle {
    NORMAL(),
    STEP(),
    CURVE();

    private static LineStyle[] types;

    /**
     * ���±�ת����ö������
     * @param p ��ת��������
     * @return ö��
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
