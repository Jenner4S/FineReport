package com.fr.design.mainframe.chart.gui.style.series;


import com.fr.chart.chartattr.*;
import com.fr.design.dialog.BasicScrollPane;
import com.fr.design.mainframe.chart.PaneTitleConstants;
import com.fr.design.mainframe.chart.gui.ChartStylePane;

import javax.swing.*;
import java.awt.*;

/**
 * 属性表, 图表样式 -- 系列 界面. 通过initDiffer 加载不同Plot 不同的Pane.
* @author kunsnat E-mail:kunsnat@gmail.com
* @version 创建时间：2013-1-5 上午10:31:50
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
	 * 界面标题
	 * @return 系列
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
	 * 保存界面属性
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
	 * 更新界面
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
	 * 初始化不同的Plot系列界面.
	 */
	public void initDifferentPlotPane() {
		Plot plot = chart.getPlot();
		if(plot instanceof Bar2DPlot) {
			seriesStyleContentPane = new Bar2DSeriesPane(parent, plot);
		} else if(plot instanceof Bar3DPlot) {
			seriesStyleContentPane = new Bar3DSeriesPane(parent, plot);
		} else if(plot instanceof Pie3DPlot) {
			seriesStyleContentPane = new Pie3DSeriesPane(parent, plot);
		} else if(plot instanceof PiePlot) {
			seriesStyleContentPane = new Pie2DSeriesPane(parent, plot);
		} else if(plot instanceof LinePlot) {
			seriesStyleContentPane = new LineSeriesPane(parent, plot);
		} else if(plot instanceof AreaPlot) {
			seriesStyleContentPane = new AreaSeriesPane(parent, plot);
		} else if(plot instanceof Area3DPlot) {
			seriesStyleContentPane = new Area3DSeriesPane(parent, plot);
		} else if(plot instanceof XYScatterPlot) {
			seriesStyleContentPane = new XYScatterSeriesPane(parent, plot);
		} else if(plot instanceof BubblePlot) {
			seriesStyleContentPane = new BubbleSeriesPane(parent, plot);
		} else if(plot instanceof RadarPlot) {
			seriesStyleContentPane = new RadarSeriesPane(parent, plot);
		} else if(plot instanceof RangePlot) {
			seriesStyleContentPane = new RangeSeriesPane(parent, plot);
		} else if(plot instanceof MapPlot) {
			seriesStyleContentPane = createMapSeriesPane(parent,plot);
		} else if(plot instanceof MeterPlot) {
			seriesStyleContentPane = createMeterSeriesPane(parent, plot);
		} else if(plot instanceof StockPlot) {
			seriesStyleContentPane = new StockSeriesPane(parent, plot);
		} else if(plot instanceof CustomPlot) {
			seriesStyleContentPane = new CustomSeriesPane(parent, plot);
		} else if(plot instanceof Donut2DPlot){
			seriesStyleContentPane = new Donut2DSeriesPane(parent, plot);
		} else if(plot instanceof FunnelPlot) {
            seriesStyleContentPane = new FunnelSeriesPane(parent, plot);
        }

	}

	protected MeterSeriesPane createMeterSeriesPane(ChartStylePane parent,Plot plot){
		return new MeterSeriesPane(parent, plot);
	}

	protected MapSeriesPane createMapSeriesPane(ChartStylePane parent,Plot plot){
		return new MapSeriesPane(parent, plot);
	}

}

