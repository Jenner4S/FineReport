package com.fr.design.chart.gui.active.action;

import java.awt.event.ActionEvent;

import com.fr.design.chart.gui.ChartComponent;
import com.fr.design.mainframe.chart.ChartEditPane;
import com.fr.design.mainframe.chart.PaneTitleConstants;
import com.fr.general.Inter;
import com.fr.stable.StringUtils;

/**
 * Created by IntelliJ IDEA.
 * Author : Richer
 * Version: 6.5.6
 * Date   : 11-11-22
 * Time   : ����5:10
 */
public class SetAxisStyleAction extends ChartComponentAction {
    public SetAxisStyleAction(ChartComponent chartComponent) {
        super(chartComponent);
        this.setName(Inter.getLocText(new String[]{"Set", "ChartF-Axis", "Format"}));
    }

    public void actionPerformed(ActionEvent e) {
        showAxisStylePane();
    }

    public void showAxisStylePane() {
        String axisType = getActiveAxisGlyph() == null ? StringUtils.EMPTY : getActiveAxisGlyph().getAxisType();
        
    	ChartEditPane.getInstance().GoToPane(PaneTitleConstants.CHART_STYLE_TITLE, PaneTitleConstants.CHART_STYLE_AXIS_TITLE, axisType);
    }
}
