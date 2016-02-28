package com.fr.solution.plugin.chart.echarts.ui.map;

import javax.swing.JPanel;

import com.fr.design.dialog.BasicScrollPane;
import com.fr.solution.plugin.chart.echarts.base.NewChart;

public class MapEChartsLegendPane extends BasicScrollPane<NewChart> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4025921494119826136L;

	public MapEChartsLegendPane(MapEChartsStylePane parent) {

    }
    @Override
    protected JPanel createContentPane() {
        return new JPanel();
    }

    @Override
    public void populateBean(NewChart ob) {

    }

    @Override
    protected String title4PopupWindow() {
        return "Lengend";
    }

}
