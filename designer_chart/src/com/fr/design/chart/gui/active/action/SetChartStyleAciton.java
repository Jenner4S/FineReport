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
 * Time   : ����5:00
 */
public class SetChartStyleAciton extends ChartComponentAction {

    public SetChartStyleAciton(ChartComponent chartComponent) {
        super(chartComponent);
        this.setName(Inter.getLocText(new String[]{"DashBoard-ChartFloat", "Style"}));
    }

    public void actionPerformed(ActionEvent e) {
         showChartStylePane();
    }

    public void showChartStylePane() {
    	ChartEditPane.getInstance().GoToPane(PaneTitleConstants.CHART_STYLE_TITLE, PaneTitleConstants.CHART_STYLE_AREA_TITLE, PaneTitleConstants.CHART_STYLE_AREA_AREA_TITLE);
    }
}
