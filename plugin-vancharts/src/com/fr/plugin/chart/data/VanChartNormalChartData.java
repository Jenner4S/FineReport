package com.fr.plugin.chart.data;

import com.fr.chart.chartdata.NormalChartData;

/**
 * Created by Mitisky on 16/1/19.
 * 不做大数据过滤
 */
public class VanChartNormalChartData extends NormalChartData {

    public VanChartNormalChartData () {

    }

    public VanChartNormalChartData (Object[] category_array, Object[] series_array, Object[][] series_value_2D) {
        super(category_array, series_array, series_value_2D);
    }

    /**
     * 处理大数据.1000分类.
     * 不做任何处理
     */
    public void dealHugeData() {
        //新的图表数据不在这里处理大数据
        //画图的时候处理：折线图相同像素点只画一个，其他图表有配置是否采用大数据模式。
    }
}
