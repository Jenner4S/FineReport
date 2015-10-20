package com.fr.design.mainframe.chart.gui.data;

import com.fr.chart.chartattr.Chart;
import com.fr.chart.chartattr.ChartCollection;
import com.fr.chart.chartattr.Plot;
import com.fr.chart.chartdata.ReportDataDefinition;
import com.fr.design.ChartTypeInterfaceManager;
import com.fr.design.beans.FurtherBasicBeanPane;
import com.fr.design.mainframe.chart.gui.ChartDataPane;
import com.fr.design.mainframe.chart.gui.data.report.AbstractReportDataContentPane;
import com.fr.general.Inter;

import java.awt.*;

public class ReportDataPane extends FurtherBasicBeanPane<ChartCollection>{

	private AbstractReportDataContentPane contentPane;
	private ChartDataPane parent;
	
	public ReportDataPane(ChartDataPane parent) {
		this.parent = parent;
	}
	
	private AbstractReportDataContentPane getContentPane(Chart chart) {
		if(chart == null) {
			return null;
		}
		
		Plot plot = chart.getPlot();
		
		return ChartTypeInterfaceManager.getInstance().getReportDataSourcePane(plot, parent);
	}

    /**
     * ��Ԫ������
     * @return ����ı���
     */
	public String title4PopupWindow() {
		return Inter.getLocText("FR-Chart_Cell_Data");
	}

    /**
     * �����Ƿ�����������
     * @param ob ��Ҫ�жϵĶ���
     * @return �����Ƿ���ܶ���
     */
	public boolean accept(Object ob) {
		return ob instanceof ChartCollection && ((ChartCollection)ob).getSelectedChart().getFilterDefinition() instanceof ReportDataDefinition;
	}

    /**
     * ��������
     */
	public void reset() {
		
	}

    /**
     * ˢ��contentPane
     * @param collection ͼ�����Եļ���
     */
	public void refreshContentPane(ChartCollection collection) {
		this.removeAll();
		
		this.setLayout(new BorderLayout());
		contentPane = getContentPane(collection.getSelectedChart());
		if(contentPane != null) {
			this.add(contentPane, BorderLayout.CENTER);
		}
	}
	
	/**
	 * �������Ƿ����.
	 */
	public void checkBoxUse() {
		if(contentPane != null) {
			contentPane.checkBoxUse();
		}
	}

	@Override
	public void populateBean(ChartCollection collection) {
		if(collection == null) {
			return;
		}
		if(contentPane != null) {
			contentPane.populateBean(collection);
		}
	}
	
	public void updateBean(ChartCollection collection) {
		if(contentPane != null) {
			contentPane.updateBean(collection);
		}
	}

	@Override
	public ChartCollection updateBean() {
		return null;
	}
}
