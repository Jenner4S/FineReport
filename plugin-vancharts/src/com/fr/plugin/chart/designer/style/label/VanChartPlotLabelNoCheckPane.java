package com.fr.plugin.chart.designer.style.label;

import com.fr.chart.chartattr.Plot;
import com.fr.plugin.chart.base.AttrLabel;
import com.fr.plugin.chart.designer.style.VanChartStylePane;

import java.awt.*;

/**
 * �����������õ�
 */
public class VanChartPlotLabelNoCheckPane extends VanChartPlotLabelPane {

    private static final long serialVersionUID = 8124894034484334810L;

    public VanChartPlotLabelNoCheckPane(Plot plot,VanChartStylePane parent) {
        super(plot, parent);
    }

    protected void addComponents() {
        this.setLayout(new BorderLayout());
        this.add(labelPane,BorderLayout.CENTER);
    }

    public void populate(AttrLabel attr) {
        super.populate(attr);
        isLabelShow.setSelected(true);
        labelPane.setVisible(true);
    }
}
