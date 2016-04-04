package com.fr.plugin.chart.designer.style.label;

import com.fr.chart.base.DataSeriesCondition;
import com.fr.chart.chartattr.Chart;
import com.fr.chart.chartattr.Plot;
import com.fr.chart.chartglyph.ConditionAttr;
import com.fr.design.dialog.BasicScrollPane;
import com.fr.design.mainframe.chart.PaneTitleConstants;
import com.fr.plugin.chart.base.AttrLabel;
import com.fr.plugin.chart.designer.style.VanChartStylePane;

import javax.swing.*;
import java.awt.*;

public class VanChartLabelPane extends BasicScrollPane<Chart> {
    private VanChartPlotLabelPane labelPane;
    private Chart chart;
    private VanChartStylePane parent;
    private static final long serialVersionUID = -5449293740965811991L;


    public VanChartLabelPane(VanChartStylePane parent){
        super();
        this.parent = parent;
    }

    @Override
    protected JPanel createContentPane() {
        JPanel contentPane = new JPanel(new BorderLayout());
        if(chart == null) {
            return contentPane;
        }
        labelPane = getLabelPane(chart.getPlot());
        contentPane.add(labelPane, BorderLayout.NORTH);
        return contentPane;
    }

    @Override
    public void populateBean(Chart chart) {
        this.chart = chart;

        if(labelPane == null){
            this.remove(leftcontentPane);
            layoutContentPane();
            parent.initAllListeners();
        }

        Plot plot = this.chart.getPlot();
        ConditionAttr attrList = plot.getConditionCollection().getDefaultAttr();
        DataSeriesCondition attr = attrList.getExisted(AttrLabel.class);
        if(labelPane != null) {
            labelPane.populate((AttrLabel)attr);
        }
    }

    public void updateBean(Chart chart) {
        if(chart == null) {
            return;
        }
        ConditionAttr attrList = chart.getPlot().getConditionCollection().getDefaultAttr();
        DataSeriesCondition attr = attrList.getExisted(AttrLabel.class);
        if(attr != null) {
            attrList.remove(attr);
        }
        AttrLabel attrLabel = labelPane.update();
        attrList.addDataSeriesCondition(attrLabel);
    }

    @Override
    protected String title4PopupWindow() {
        return PaneTitleConstants.CHART_STYLE_LABEL_TITLE;
    }

    protected VanChartPlotLabelPane getLabelPane(Plot plot) {
        return new VanChartPlotLabelPane(plot,parent);
    }

}

