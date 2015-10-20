package com.fr.design.mainframe.chart.gui.data;

import com.fr.design.mainframe.chart.AbstractChartAttrPane;
import com.fr.design.mainframe.chart.PaneTitleConstants;

/**
 * ͼ���������ݽ���, ���Ա� ��ʽ����.
 * @author kunsnat E-mail:kunsnat@gmail.com
 * @version ����ʱ�䣺2013-1-18 ����03:42:20
 */
public abstract class DataContentsPane extends AbstractChartAttrPane {


	/**
	 * �������
	 */
	public String getIconPath() {
		return "com/fr/design/images/chart/ChartData.png";
	}

	/**
	 * �������
	 */
	public String title4PopupWindow() {
		return PaneTitleConstants.CHART_DATA_TITLE;
	}
	
	/**
	 * �����Ƿ������Ԫ������.
	 */
	public abstract void setSupportCellData(boolean surpportCellData);
	
	/**
	 * �������Ӧ�¼�.
	 */
//	public void removeAttributeListener() {
//		super.removeAttributeChangeListener();
//	}
}
