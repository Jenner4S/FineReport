package com.fr.design.chart.series.SeriesCondition;

import com.fr.chart.chartattr.CustomPlot;
import com.fr.chart.chartattr.Plot;

/**
 * ��˵�� ���ͼ �������Խ���.
* @author kunsnat E-mail:kunsnat@gmail.com
* @version ����ʱ�䣺2013-8-19 ����09:50:05
 */
public class DataSeriesCustomConditionPane extends DataSeriesConditionPane{
	private static final long serialVersionUID = -6568508006201353211L;

    /**
     * ����class����
     * @return class����
     */
    public Class<? extends Plot> class4Correspond() {
        return CustomPlot.class;
    }
}
