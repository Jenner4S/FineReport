package com.fr.plugin.chart.designer.style.series;

import com.fr.chart.base.ChartConstants;
import com.fr.design.chart.series.SeriesCondition.ChartConditionPane;

/**
 * ֻ��ϵ�н�������
 */
public class VanChartSeriesConditionPane extends ChartConditionPane {

    /**
     * ֻ��ϵ�н�������
     * @return ϵ��ֵ��ϵ����
     */
    public String[] columns2Populate() {
        return new String[]{
                ChartConstants.SERIES_INDEX,
                ChartConstants.SERIES_NAME
        };
    }
}
