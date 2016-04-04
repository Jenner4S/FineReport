package com.fr.plugin.chart.radar;

import com.fr.plugin.chart.line.VanChartLineDataPoint;

/**
 * Created by Mitisky on 16/1/7.
 */
public class VanChartRadarDataPoint extends VanChartLineDataPoint {
    private static final long serialVersionUID = 5757737246599950108L;

    private boolean columnType;

    public VanChartRadarDataPoint(boolean columnType){
        super();
        this.columnType = columnType;
    }

    /**
     * �Ƿ���������(����ͼ�������⣬�Զ�����ʽ����ϵ��ɫһ�������������Զ���ɫ)
     * @return �Ƿ���������
     */
    public boolean isOutside() {
        return !columnType;
    }

}
