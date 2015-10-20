package com.fr.design.mainframe.chart;


import com.fr.chart.chartattr.ChartCollection;
import com.fr.design.gui.frpane.AbstractAttrNoScrollPane;

public abstract class AbstractChartAttrPane extends AbstractAttrNoScrollPane {
	public abstract void populate(ChartCollection collection);

	public abstract void update(ChartCollection collection);

	public void populateBean(ChartCollection collection) {
		if (collection == null) {
			return;
		}
		removeAttributeChangeListener();
		populate(collection);
	}
	
	/**
	 * ע��ͼ���л���ť �ı�� �¼�. 
	 * @param isFromChartHyper �༭���
	 */
	public void registerChartEditPane(ChartEditPane isFromChartHyper) {
		
	}

	/**
	 * ˢ��ͼ�����ݽ���
	 * @param collection ͼ���ռ���
	 */
	public void refreshChartDataPane(ChartCollection collection){

	}
}
