/*
 * Copyright (c) 2001-2014,FineReport Inc, All Rights Reserved.
 */

package com.fr.design.chart.series.SeriesCondition;

import com.fr.chart.base.ChartConstants;

/**
 * ɢ��ͼ �������� ��ʾ���� ����.
 * Created by kunsnat on 14-3-10.
 * kunsnat@gmail.com
 */
public class XYPlotChartConditionPane extends ChartConditionPane {

    /**
     * ����������� �б�
     * @return ���������б�.
     */
    public String[] columns2Populate() {
        return new String[]{
                ChartConstants.SERIES_INDEX,
                ChartConstants.SERIES_NAME,
                ChartConstants.VALUE
        };
    }
}
