package com.fr.design.chart.series.SeriesCondition;

import com.fr.chart.base.ChartConstants;


/**
 * ��ͼ ������ʾ ����.
 * @author kunsnat E-mail:kunsnat@gmail.com
 * @version ����ʱ�䣺2013-1-11 ����03:48:32
 */
public class PiePlotChartConditionPane extends ChartConditionPane {

	  public String[] columns2Populate() {
	        return new String[]{
	                ChartConstants.SERIES_INDEX,
	                ChartConstants.SERIES_NAME,
	                ChartConstants.VALUE
	        };
	    }
}
