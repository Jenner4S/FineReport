package com.fr.plugin.chart.data;

import com.fr.chart.chartdata.NormalChartData;

/**
 * Created by Mitisky on 16/1/19.
 * ���������ݹ���
 */
public class VanChartNormalChartData extends NormalChartData {

    public VanChartNormalChartData () {

    }

    public VanChartNormalChartData (Object[] category_array, Object[] series_array, Object[][] series_value_2D) {
        super(category_array, series_array, series_value_2D);
    }

    /**
     * ���������.1000����.
     * �����κδ���
     */
    public void dealHugeData() {
        //�µ�ͼ�����ݲ������ﴦ�������
        //��ͼ��ʱ��������ͼ��ͬ���ص�ֻ��һ��������ͼ���������Ƿ���ô�����ģʽ��
    }
}
