package com.fr.design.mainframe.chart.gui;

import java.awt.BorderLayout;

import javax.swing.JPanel;

import com.fr.design.chart.report.GisMapDataPane;
import com.fr.design.chart.report.MapDataPane;
import com.fr.chart.chartattr.ChartCollection;
import com.fr.chart.chartattr.GisMapPlot;
import com.fr.chart.chartattr.MapPlot;
import com.fr.design.gui.frpane.AttributeChangeListener;
import com.fr.design.mainframe.chart.AbstractChartAttrPane;
import com.fr.design.mainframe.chart.PaneTitleConstants;
import com.fr.design.mainframe.chart.gui.data.DataContentsPane;
import com.fr.design.mainframe.chart.gui.data.NormalChartDataPane;

public class ChartDataPane extends AbstractChartAttrPane {
	
	protected DataContentsPane contentsPane;
	protected AttributeChangeListener listener;
	
	private boolean supportCellData = true;
	
	public ChartDataPane(AttributeChangeListener listener) {
		super();
		this.listener = listener;
	}
	
	@Override
	protected JPanel createContentPane() {
		contentsPane = new NormalChartDataPane(listener, ChartDataPane.this);
		return contentsPane;
	}

	/**
	 * 界面标题
	 */
	public String getIconPath() {
		return "com/fr/design/images/chart/ChartData.png";
	}

    /**
     * 界面标题
     * @return 界面标题
     */
	public String title4PopupWindow() {
		return PaneTitleConstants.CHART_DATA_TITLE;
	}

	protected void repeatLayout(ChartCollection collection) {
		if(contentsPane != null) {
			this.remove(contentsPane);
		}
		
		this.setLayout(new BorderLayout(0, 0));
		if (collection != null && collection.getChartCount() <= 0) {
			contentsPane = new NormalChartDataPane(listener, ChartDataPane.this);
		} else if (collection.getSelectedChart().getPlot() instanceof MapPlot) {
			contentsPane = new MapDataPane(listener);
		} else if(collection.getSelectedChart().getPlot() instanceof GisMapPlot){
			contentsPane = new GisMapDataPane(listener);
		}else {
			contentsPane = new NormalChartDataPane(listener, ChartDataPane.this);
		}
		
		if(contentsPane != null) {
			contentsPane.setSupportCellData(supportCellData);
		}
	}
	
	/**
	 * 设置数据界面是否支持单元格
	 */
	public void setSupportCellData(boolean supportCellData) {
		this.supportCellData = supportCellData;
	}
	
	/**
	 * 更新界面 数据内容
	 */
	public void populate(ChartCollection collection) {
		repeatLayout(collection);

		contentsPane.populate(collection);

		this.add(contentsPane, BorderLayout.CENTER);

        this.validate();
	}

	/**
	 * 保存 数据界面内容
	 */
	public void update(ChartCollection collection) {
		if(contentsPane != null) {
			contentsPane.update(collection);
		}
	}

	/**
	 * 刷新图表数据界面
	 * @param collection 图表收集器
	 */
	public void refreshChartDataPane(ChartCollection collection){
		this.populate(collection);
	}
}