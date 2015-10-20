package com.fr.design.chart.series.SeriesCondition.impl;

import com.fr.chart.base.AttrAxisPosition;
import com.fr.design.chart.series.SeriesCondition.DataSeriesConditionPane;
import com.fr.design.chart.series.SeriesCondition.LabelAxisPositionPane;

/**
 * ���ͼ �������Խ���.
 * 
 * Created by IntelliJ IDEA.
 * Author : Richer
 * Version: 6.5.6
 * Date   : 11-11-30
 * Time   : ����10:01
 */
public class CustomPlotDataSeriesConditionPane extends DataSeriesConditionPane {
	private static final long serialVersionUID = -6140023104743692628L;

	protected void addAxisPositionAction() {
		 classPaneMap.put(AttrAxisPosition.class, new LabelAxisPositionPane(this));
	}
}
