package com.fr.design.mainframe.chart.gui.style;

import com.fr.design.gui.style.GradientPane;
import com.fr.design.mainframe.backgroundpane.ColorBackgroundPane;
import com.fr.design.mainframe.backgroundpane.NullBackgroundPane;

/**
 * ��������, ��ͼƬ������ѡ��.
 * @author kunsnat E-mail:kunsnat@gmail.com
 * @version ����ʱ�䣺2013-1-21 ����04:00:58
 */
public class ChartBackgroundNoImagePane extends ChartBackgroundPane {

	public ChartBackgroundNoImagePane() {
		super();
	}
	
	protected void initList() {
		paneList.add(new NullBackgroundPane());
		paneList.add(new ColorBackgroundPane());
		paneList.add(new GradientPane(CHART_GRADIENT_WIDTH));
	}
}
