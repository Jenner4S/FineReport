package com.fr.plugin.chart;

import com.fr.data.core.FormatField;
import com.fr.general.DateUtils;
import com.fr.general.FRLogger;
import com.fr.stable.StringUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * ʱ�������� ��ʼ�������С����
 */
public class VanChartDateUtils {

    public static List<DateFormat> dateFormatArray = new ArrayList<DateFormat>();

    /**
     * ת������������
     * @param str �ַ���
     * @param returnNull ���ת��ʧ�ܣ��Ƿ񷵻ؿ�
     * @return ��������
     */
    public static Date object2Date(String str, boolean returnNull) {
        Date date = DateUtils.object2Date(str, returnNull);
        if(date == null){
            date = VanChartDateUtils.transDate(str, returnNull);
        }
        return date;
    }

    /**
     * ת������������
     * @param str �ַ���
     * @param returnNull ���ת��ʧ�ܣ��Ƿ񷵻ؿ�
     * @return ��������
     */
    public static Date transDate(String str, boolean returnNull) {
        if(dateFormatArray.isEmpty()){
            String[] stringFormats = FormatField.getInstance().getDateFormatArray();
            for(String string : stringFormats){
                dateFormatArray.add(new SimpleDateFormat(string));
            }
        }

        for (DateFormat format : dateFormatArray) {
            try {
                String lock = StringUtils.EMPTY;
                synchronized (lock) {
                    return format.parse(str);
                }
            } catch (ParseException e) {

            }
        }

        if (returnNull) {
            return null;
        } else {
            FRLogger.getLogger().error(str + " can't be parsed to Date");
        }

        // ���ص�ǰ����
        return new java.util.Date();
    }
}
