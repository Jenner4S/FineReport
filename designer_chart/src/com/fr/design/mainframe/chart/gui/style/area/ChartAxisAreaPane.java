package com.fr.design.mainframe.chart.gui.style.area;


import javax.swing.JPanel;

import com.fr.chart.chartattr.Plot;

/**
 * ���Ա�, ͼ����ʽ-����-��ͼ�� �����������.
* @author kunsnat E-mail:kunsnat@gmail.com
* @version ����ʱ�䣺2013-1-9 ����12:28:47
 */
public abstract class ChartAxisAreaPane extends JPanel {
	/**
	 * ���¼����������.
	 */
	public abstract void populateBean(Plot plot);

	/**
	 * �����������Ľ�������.
	 */
	public abstract void updateBean(Plot plot);
}
