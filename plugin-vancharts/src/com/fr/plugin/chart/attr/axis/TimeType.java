package com.fr.plugin.chart.attr.axis;

import com.fr.general.ComparatorUtils;
import com.fr.general.Inter;

/**
 * Created by Mitisky on 15/10/28.
 */
public enum TimeType {
    TIME_YEAR("FR-Base-Time_Year", 3),
    TIME_MONTH("FR-Designer_Month", 2),
    TIME_DAY("FR-Base_Sun", 1),
    TIME_HOUR("FR-Base_Sche_Hour", 4),
    TIME_MINUTE("FR-Base_Sche_Minute", 5),
    TIME_SECOND("FR-Base_Sche_Second", 6);

    private String locText;
    private int timeType;

    private TimeType(String locText, int timeType){
        this.locText = locText;
        this.timeType = timeType;
    }

    public int getTimeType() {
        return timeType;
    }

    public String getLocText() {
        return Inter.getLocText(locText);
    }

    private static TimeType[] types;

    /**
     * ���ַ���ת����ö������
     * @param p ��ת��������
     * @return ö��
     */
    public static TimeType parseString(String p) {
        if (types == null) {
            types = TimeType.values();
        }
        for (TimeType type : types) {
            if (ComparatorUtils.equals(p, type.getLocText())) {
                return type;
            }
        }
        return TIME_YEAR;
    }

    /**
     * ���ַ���ת����ö������
     * @param p ��ת��������
     * @return ö��
     */
    public static TimeType parseInt(int p) {
        if (types == null) {
            types = TimeType.values();
        }
        for (TimeType type : types) {
            if (ComparatorUtils.equals(p, type.getTimeType())) {
                return type;
            }
        }
        return TIME_YEAR;
    }
}
