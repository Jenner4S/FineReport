package com.fr.design.chart.series.SeriesCondition.impl;

import com.fr.chart.base.AttrAxisPosition;
import com.fr.design.chart.series.SeriesCondition.LabelAxisPositionPane;

/**
 * ��˵��: ���ͼ�� �ѻ����ͼ(��������)�������Խ���.
 * @author kunsnat E-mail:kunsnat@gmail.com
 * @version ����ʱ�䣺2012-4-20 ����05:28:52
 */
public class CustomPlot4AreaConditionPane extends AreaPlotDataSeriesCondtionPane {
	private static final long serialVersionUID = 6938149100125099651L;

	protected void addAxisPositionAction() {
		classPaneMap.put(AttrAxisPosition.class, new LabelAxisPositionPane(this));
	}
}
