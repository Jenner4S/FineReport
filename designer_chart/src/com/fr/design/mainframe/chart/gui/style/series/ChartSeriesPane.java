package com.fr.design.mainframe.chart.gui.style.series;


import com.fr.chart.chartattr.Chart;
import com.fr.design.ChartTypeInterfaceManager;
import com.fr.design.dialog.BasicScrollPane;
import com.fr.design.mainframe.chart.PaneTitleConstants;
import com.fr.design.mainframe.chart.gui.ChartStylePane;

import javax.swing.*;
import java.awt.*;

/**
 * ���Ա�, ͼ����ʽ -- ϵ�� ����. ͨ��initDiffer ���ز�ͬPlot ��ͬ��Pane.
* @author kunsnat E-mail:kunsnat@gmail.com
* @version ����ʱ�䣺2013-1-5 ����10:31:50
 */
public class ChartSeriesPane extends BasicScrollPane<Chart>{

	protected AbstractPlotSeriesPane seriesStyleContentPane;
	protected Chart chart;

	protected ChartStylePane parent;


	public ChartSeriesPane(ChartStylePane parent) {
		super();
		this.parent = parent;
	}
	/**
	 * �������
	 * @return ϵ��
	 */
	@Override
	public String title4PopupWindow() {
		return PaneTitleConstants.CHART_STYLE_SERIES_TITLE;
	}

	@Override
	protected JPanel createContentPane() {
		JPanel contentPane = new JPanel(new BorderLayout());
		if(chart == null) {
			return contentPane;
		} 
		initDifferentPlotPane();
		if(seriesStyleContentPane != null) {
			contentPane.add(seriesStyleContentPane, BorderLayout.CENTER);
		}
		return contentPane;
	}

	/**
	 * �����������
	 */
	@Override
	public void updateBean(Chart chart) {
		if(chart == null) {
			return;
		}
		if(seriesStyleContentPane != null) {
			seriesStyleContentPane.setCurrentChart(chart);
			seriesStyleContentPane.updateBean(chart.getPlot());
		}
	}

	/**
	 * ���½���
	 */
	@Override
	public void populateBean(Chart chart) {
		this.chart = chart;
		if(seriesStyleContentPane == null) {
			this.remove(leftcontentPane);
			layoutContentPane();
			parent.initAllListeners();
		}
		if(seriesStyleContentPane != null) {
			seriesStyleContentPane.setCurrentChart(chart);
			seriesStyleContentPane.populateBean(chart.getPlot());
		}
	}
	
	/**
	 * ��ʼ����ͬ��Plotϵ�н���.
	 */
	public void initDifferentPlotPane() {
        seriesStyleContentPane =  (AbstractPlotSeriesPane)ChartTypeInterfaceManager.getInstance().getPlotSeriesPane(parent, this.chart.getPlot());
	}

}

