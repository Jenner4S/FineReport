package com.fr.plugin.chart.designer.style.label;

import com.fr.chart.chartattr.Plot;
import com.fr.plugin.chart.designer.TableLayout4VanChartHelper;
import com.fr.plugin.chart.designer.component.label.TooltipContentPaneWithCateValue;
import com.fr.plugin.chart.designer.component.label.TooltipContentPaneWithOutCate;
import com.fr.plugin.chart.designer.style.VanChartStylePane;
import com.fr.plugin.chart.gauge.GaugeStyle;
import com.fr.plugin.chart.gauge.VanChartGaugePlot;

import javax.swing.*;
import java.awt.*;

/**
 * �Ǳ��̵�ֵ��ǩ
 */
public class VanChartGaugeValueLabelDetailPane extends VanChartPlotLabelDetailPane {
    private static final long serialVersionUID = 2601073419430634281L;

    private GaugeStyle gaugeStyle;

    public VanChartGaugeValueLabelDetailPane(Plot plot, VanChartStylePane parent) {
        super(plot, parent);
    }

    private void initGaugeStyle(Plot plot) {
        if(gaugeStyle == null){
            gaugeStyle = ((VanChartGaugePlot)plot).getGaugeStyle();
        }
    }

    protected void initToolTipContentPane(Plot plot) {
        initGaugeStyle(plot);
        switch (gaugeStyle){
            case POINTER:
                dataLabelContentPane = new TooltipContentPaneWithOutCate(parent, VanChartGaugeValueLabelDetailPane.this);
                break;
            case POINTER_SEMI:
                dataLabelContentPane = new TooltipContentPaneWithOutCate(parent, VanChartGaugeValueLabelDetailPane.this);
                break;
            default:
                dataLabelContentPane = new TooltipContentPaneWithCateValue(parent, VanChartGaugeValueLabelDetailPane.this);
                break;
        }
    }

    protected Component[][] getLabelPaneComponents(Plot plot, double p, double[] columnSize) {
        initGaugeStyle(plot);
        switch (gaugeStyle){
            case POINTER:
                return getLabelPaneComponentsWithBackground(plot, p, columnSize);
            case POINTER_SEMI:
                return getLabelPaneComponentsWithBackground(plot, p, columnSize);
            default:
                return super.getLabelPaneComponents(plot, p, columnSize);
        }
    }

    private Component[][] getLabelPaneComponentsWithBackground(Plot plot, double p, double[] columnSize) {
        return  new Component[][]{
                new Component[]{dataLabelContentPane,null},
                new Component[]{new JSeparator(),null},
                new Component[]{createLabelStylePane(new double[]{p,p}, columnSize, plot),null},
                new Component[]{new JSeparator(),null},
                new Component[]{createBackgroundColorPane(),null},
        };
    }

    protected double[] getLabelPaneRowSize(Plot plot, double p) {
        initGaugeStyle(plot);
        switch (gaugeStyle){
            case POINTER:
                return new double[]{p,p,p,p,p};
            case POINTER_SEMI:
                return new double[]{p,p,p,p,p};
            default:
                return super.getLabelPaneRowSize(plot, p);
        }
    }

    protected boolean hasLabelPosition(Plot plot) {
        initGaugeStyle(plot);
        switch (gaugeStyle){
            case THERMOMETER:
                return true;
            default:
                return false;
        }
    }

    protected JPanel createTableLayoutPaneWithTitle(String title, Component component) {
        return TableLayout4VanChartHelper.createTableLayoutPaneWithSmallTitle(title, component);
    }

    protected Component[][] getLabelStyleComponents(Plot plot) {
        return new Component[][]{
                new Component[]{textFontPane,null},
        };
    }

}
