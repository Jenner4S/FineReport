package com.fr.design.mainframe.chart.gui.style;

import com.fr.design.dialog.BasicScrollPane;

/**
 * �����, ͼ���л�����.
 * @author kunsnat E-mail:kunsnat@gmail.com
 * @version ����ʱ�䣺2013-1-4 ����03:55:42
 */
public abstract class AbstractChartTabPane<T> extends BasicScrollPane<T> {

	public abstract String title4PopupWindow();
	public AbstractChartTabPane(){
		super();
	}
	
	//�ϲ�pane�Ѿ�����scroll����Ҫ���¼����ε�
	public AbstractChartTabPane(boolean noScroll){
		super(noScroll);
	}
}
