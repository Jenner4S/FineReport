package com.fr.design.chart.series.SeriesCondition.impl;

import com.fr.chart.base.AttrAxisPosition;
import com.fr.design.chart.series.SeriesCondition.LabelAxisPositionPane;

/**
 * ��˵��: ���ͼ�� �������� �Ƕѻ� �������ߵ��������Խ���.
 * @author kunsnat E-mail:kunsnat@gmail.com
 * @version ����ʱ�䣺2012-4-20 ����05:27:35
 */
public class CustomPlot4LineConditionPane extends LinePlotDataSeriesConditionPane {
	private static final long serialVersionUID = -5614266090158187836L;

	protected void addAxisPositionAction() {
		classPaneMap.put(AttrAxisPosition.class, new LabelAxisPositionPane(this));
	}
}
