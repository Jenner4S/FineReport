package com.fr.design.chart.series.SeriesCondition;

import com.fr.chart.base.ChartConstants;

/**
 * ��ͼ������ ��������.
 * @author kunsnat E-mail:kunsnat@gmail.com
 * @version ����ʱ�䣺2013-1-11 ����03:44:40
 */
public class MapPlotChartConditionPane extends ChartConditionPane {

	  public String[] columns2Populate() {
	        return new String[]{
	                ChartConstants.AREA_NAME,
	                ChartConstants.AREA_VALUE
	        };
	    }
}
