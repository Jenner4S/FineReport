package com.fr.design.mainframe.chart.gui.style.series;

import javax.swing.JPanel;

import com.fr.chart.chartattr.Plot;
import com.fr.design.mainframe.chart.gui.ChartStylePane;

/**
 * ��ά��ͼ��ϵ�����Խ���, û�з��, û�еڶ���ͼ��.
 * @author kunsnat E-mail:kunsnat@gmail.com
 * @version ����ʱ�䣺2013-10-30 ����09:59:31
 */
public class Pie3DSeriesPane extends Pie2DSeriesPane {

	public Pie3DSeriesPane(ChartStylePane parent, Plot plot) {
		super(parent, plot);
	}
	
	@Override
	protected JPanel getContentInPlotType() {
		initCom();

		return null;
	}

}
