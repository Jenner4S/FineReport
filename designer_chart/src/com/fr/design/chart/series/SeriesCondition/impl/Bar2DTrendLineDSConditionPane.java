package com.fr.design.chart.series.SeriesCondition.impl;

import com.fr.chart.base.AttrTrendLine;
import com.fr.design.chart.series.SeriesCondition.TrendLinePane;

/**
 * Created by IntelliJ IDEA.
 * Author : Richer
 * Version: 6.5.6
 * Date   : 11-11-30
 * Time   : ����11:19
 * ����ͼ �Ƕѻ� �������ߵ� �������Խ���
 */
public class Bar2DTrendLineDSConditionPane extends BarPlotDataSeriesConditionPane {
	private static final long serialVersionUID = -5888582645195218536L;

	protected void addTrendLineAction() {
        classPaneMap.put(AttrTrendLine.class, new TrendLinePane(this));
    }
}
