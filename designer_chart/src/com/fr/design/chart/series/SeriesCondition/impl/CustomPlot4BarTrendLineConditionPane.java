package com.fr.design.chart.series.SeriesCondition.impl;

import com.fr.chart.base.AttrAxisPosition;
import com.fr.design.chart.series.SeriesCondition.LabelAxisPositionPane;

/**
 * ��˵��: ���ͼ�� �Ƕѻ� �������ߵ��������Խ���.
 * @author kunsnat E-mail:kunsnat@gmail.com
 * @version ����ʱ�䣺2012-4-20 ����05:23:38
 */
public class CustomPlot4BarTrendLineConditionPane extends Bar2DTrendLineDSConditionPane {
	private static final long serialVersionUID = 5216036756293601852L;

	protected void addAxisPositionAction() {
		classPaneMap.put(AttrAxisPosition.class, new LabelAxisPositionPane(this));
	}
}
