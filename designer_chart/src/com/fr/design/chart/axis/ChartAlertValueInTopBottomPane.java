package com.fr.design.chart.axis;

import com.fr.general.Inter;


/**
 * ������ ���� ���½���. 
 * @author kunsnat E-mail:kunsnat@gmail.com
 * @version ����ʱ�䣺2013-5-22 ����09:53:28
 */
public class ChartAlertValueInTopBottomPane extends ChartAlertValuePane {
	
	protected String getLeftName() {
		return Inter.getLocText("Chart_Alert_Bottom");
	}
	
	protected String getRightName() {
		return Inter.getLocText("Chart_Alert_Top");
	}
}
