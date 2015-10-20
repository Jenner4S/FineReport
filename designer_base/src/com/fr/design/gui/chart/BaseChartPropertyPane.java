package com.fr.design.gui.chart;

import com.fr.base.chart.BaseChartCollection;
import com.fr.design.designer.TargetComponent;
import com.fr.design.mainframe.BaseWidgetPropertyPane;
import com.fr.design.mainframe.DockingView;

/**
 * ͼ�����Խ��� ����, ���ڶ๤��Э��..
 * @author kunsnat E-mail:kunsnat@gmail.com
 * @version ����ʱ�䣺2013-7-10 ����09:12:11
 */
public abstract class BaseChartPropertyPane extends DockingView {
	
	public abstract void setSureProperty();

	/**
	 * �����Ƿ�֧�ֵ�Ԫ������.
	 */
	public abstract void setSupportCellData(boolean supportCellData);
	
	public abstract void populateChartPropertyPane(BaseChartCollection collection, TargetComponent<?> ePane);
	
	public abstract void setWidgetPropertyPane(BaseWidgetPropertyPane pane);
}
