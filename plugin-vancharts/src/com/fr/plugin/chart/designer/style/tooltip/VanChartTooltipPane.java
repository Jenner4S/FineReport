package com.fr.plugin.chart.designer.style.tooltip;

import com.fr.chart.base.DataSeriesCondition;
import com.fr.chart.chartattr.Chart;
import com.fr.chart.chartattr.Plot;
import com.fr.chart.chartglyph.ConditionAttr;
import com.fr.design.dialog.BasicScrollPane;
import com.fr.general.Inter;
import com.fr.plugin.chart.base.AttrTooltip;
import com.fr.plugin.chart.designer.style.VanChartStylePane;

import javax.swing.*;
import java.awt.*;

public class VanChartTooltipPane extends BasicScrollPane<Chart> {
    private static final long serialVersionUID = -2974722365840564105L;
    private VanChartPlotTooltipPane tooltipPane;
    private Chart chart;
    private VanChartStylePane parent;


    public VanChartTooltipPane(VanChartStylePane parent){
        super();
        this.parent = parent;
    }

    @Override
    protected JPanel createContentPane() {
        JPanel contentPane = new JPanel(new BorderLayout());
        if(chart == null) {
            return contentPane;
        }
        tooltipPane = getTooltipPane(chart.getPlot());
        contentPane.add(tooltipPane, BorderLayout.NORTH);
        return contentPane;
    }

    @Override
    public void populateBean(Chart chart) {
        this.chart = chart;

        if(tooltipPane == null){
            this.remove(leftcontentPane);
            layoutContentPane();
            parent.initAllListeners();
        }

        Plot plot = this.chart.getPlot();
        ConditionAttr attrList = plot.getConditionCollection().getDefaultAttr();
        DataSeriesCondition attr = attrList.getExisted(AttrTooltip.class);
        if(tooltipPane != null) {
            tooltipPane.populate((AttrTooltip)attr);
        }
    }

    public void updateBean(Chart chart) {
        if(chart == null) {
            return;
        }
        ConditionAttr attrList = chart.getPlot().getConditionCollection().getDefaultAttr();
        DataSeriesCondition attr = attrList.getExisted(AttrTooltip.class);
        if(attr != null) {
            attrList.remove(attr);
        }
        AttrTooltip attrTooltip = tooltipPane.update();
        attrList.addDataSeriesCondition(attrTooltip);
    }

    @Override
    protected String title4PopupWindow() {
        return Inter.getLocText("Plugin-Chart_Tooltip");
    }

    protected VanChartPlotTooltipPane getTooltipPane(Plot plot) {
        return new VanChartPlotTooltipPane(plot, parent);
    }

}


