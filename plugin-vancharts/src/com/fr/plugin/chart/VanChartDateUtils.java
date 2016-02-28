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
 * 时间坐标轴 初始化最大最小秒数
 */
public class VanChartDateUtils {

    public static List<DateFormat> dateFormatArray = new ArrayList<DateFormat>();

    /**
     * 转换成日期类型
     * @param str 字符串
     * @param returnNull 如果转换失败，是否返回空
     * @return 日期类型
     */
    public static Date object2Date(String str, boolean returnNull) {
        Date date = DateUtils.object2Date(str, returnNull);
        if(date == null){
            date = VanChartDateUtils.transDate(str, returnNull);
        }
        return date;
    }

    /**
     * 转换成日期类型
     * @param str 字符串
     * @param returnNull 如果转换失败，是否返回空
     * @return 日期类型
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

        // 返回当前日期
        return new java.util.Date();
    }
}
