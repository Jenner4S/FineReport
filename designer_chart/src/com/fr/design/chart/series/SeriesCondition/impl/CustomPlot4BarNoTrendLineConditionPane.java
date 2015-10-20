package com.fr.design.chart.series.SeriesCondition.impl;

import com.fr.chart.base.AttrAxisPosition;
import com.fr.design.chart.series.SeriesCondition.LabelAxisPositionPane;

/**
 * ��˵��: ���ͼ�� ���ε��������Խ���.
 * @author kunsnat E-mail:kunsnat@gmail.com
 * @version ����ʱ�䣺2012-4-20 ����05:14:44
 */
public class CustomPlot4BarNoTrendLineConditionPane extends BarPlotDataSeriesConditionPane {
	private static final long serialVersionUID = -6960758805042551364L;

	protected void addAxisPositionAction() {
		classPaneMap.put(AttrAxisPosition.class, new LabelAxisPositionPane(this));
	}
}
