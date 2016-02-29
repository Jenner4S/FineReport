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
 * Time   : 下午4:56
 */
public class SetLegendStyleAction extends ChartComponentAction {
    private static final long serialVersionUID = 3253190503195130478L;

    public SetLegendStyleAction(ChartComponent chartComponent) {
        super(chartComponent);
        this.setName(Inter.getLocText("Set_Legend_Sytle"));
    }

    public void actionPerformed(ActionEvent e) {
        showLegendStylePane();
    }

    public void showLegendStylePane() {
    	ChartEditPane.getInstance().GoToPane(PaneTitleConstants.CHART_STYLE_TITLE, PaneTitleConstants.CHART_STYLE_LEGNED_TITLE);
    }
}