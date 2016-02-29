package com.fr.design.chart.gui.active.action;

import java.awt.event.ActionEvent;

import com.fr.design.chart.gui.ChartComponent;
import com.fr.design.mainframe.chart.ChartEditPane;
import com.fr.design.mainframe.chart.PaneTitleConstants;
import com.fr.general.Inter;

/**
 * Created by IntelliJ IDEA.
 * Author : Richer
 * Version: 6.5.6
 * Date   : 11-11-22
 * Time   : 下午4:58
 */
public class SetPlotStyleAction extends ChartComponentAction {
    private static final long serialVersionUID = 2894127568015714372L;

    public SetPlotStyleAction(ChartComponent chartComponent) {
        super(chartComponent);
        this.setName(Inter.getLocText(new String[]{"Set", "ChartF-Plot"}));
    }

    public void actionPerformed(ActionEvent e) {
        showPlotPane();
    }

    public void showPlotPane() {
    	ChartEditPane.getInstance().GoToPane(PaneTitleConstants.CHART_STYLE_TITLE, PaneTitleConstants.CHART_STYLE_AREA_TITLE, PaneTitleConstants.CHART_STYLE_AREA_PLOT_TITLE);
    }
}