package com.fr.plugin.chart.designer.style.label;

import com.fr.chart.chartattr.Plot;
import com.fr.design.dialog.BasicPane;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.general.Inter;
import com.fr.plugin.chart.attr.plot.VanChartPlot;
import com.fr.plugin.chart.base.AttrLabel;
import com.fr.plugin.chart.designer.TableLayout4VanChartHelper;
import com.fr.plugin.chart.designer.style.VanChartStylePane;
import com.fr.plugin.chart.gauge.GaugeStyle;
import com.fr.plugin.chart.gauge.VanChartGaugePlot;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class VanChartPlotLabelPane extends BasicPane {
    private static final long serialVersionUID = -1701936672446232396L;
    protected UICheckBox isLabelShow;

    private VanChartPlotLabelDetailPane labelDetailPane;

    private VanChartPlotLabelDetailPane gaugeValueLabelPane;

    protected VanChartStylePane parent;
    private Plot plot;

    protected JPanel labelPane;

    public VanChartPlotLabelPane(Plot plot, VanChartStylePane parent) {
        this.parent = parent;
        this.plot = plot;
        isLabelShow = new UICheckBox(Inter.getLocText("Plugin-ChartF_UseLabel"));
        createLabelPane();
        addComponents();
    }

    private void createLabelPane() {
        labelPane = new JPanel(new BorderLayout(0, 4));
        if(plot.isMeterPlot()){
            labelDetailPane = new VanChartGaugeCateOrPercentLabelDetailPane(this.plot, this.parent);
            gaugeValueLabelPane = new VanChartGaugeValueLabelDetailPane(this.plot, this.parent);
            GaugeStyle gaugeStyle = ((VanChartGaugePlot)this.plot).getGaugeStyle();
            String cateTitle, valueTitle = Inter.getLocText("Plugin-ChartF_ValueLabel");
            switch (gaugeStyle){
                case POINTER:
                    cateTitle = Inter.getLocText("Plugin-ChartF_CategoryLabel");
                    break;
                case POINTER_SEMI:
                    cateTitle = Inter.getLocText("Plugin-ChartF_CategoryLabel");
                    break;
                default:
                    cateTitle = Inter.getLocText("Plugin-ChartF_PercentLabel");
                    break;
            }
            JPanel cateOrPercentPane = TableLayout4VanChartHelper.createTableLayoutPaneWithSmallTitle(cateTitle, labelDetailPane);
            JPanel valuePane = TableLayout4VanChartHelper.createTableLayoutPaneWithSmallTitle(valueTitle, gaugeValueLabelPane);
            labelPane.add(cateOrPercentPane, BorderLayout.NORTH);
            labelPane.add(new JSeparator(), BorderLayout.CENTER);
            labelPane.add(valuePane, BorderLayout.SOUTH);
        } else {
            labelDetailPane = new VanChartPlotLabelDetailPane(this.plot, this.parent);
            labelPane.add(labelDetailPane, BorderLayout.CENTER);
        }

    }

    protected void addComponents() {
        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        double[] columnSize = {f};
        double[] rowSize = {p,p,p};
        Component[][] components = new Component[][]{
                new Component[]{isLabelShow},
                new Component[]{new JSeparator()},
                new Component[]{labelPane}
        };

        JPanel panel = TableLayoutHelper.createTableLayoutPane(components, rowSize, columnSize);
        this.setLayout(new BorderLayout());
        this.add(panel,BorderLayout.CENTER);

        isLabelShow.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                checkBoxUse();
            }
        });
    }

    @Override
    protected String title4PopupWindow() {
        return null;
    }

    private void checkBoxUse() {
        labelPane.setVisible(isLabelShow.isSelected());
    }

    public void populate(AttrLabel attr) {
        if(attr == null) {
            attr = ((VanChartPlot)this.plot).getDefaultAttrLabel();
            attr.setEnable(false);
        }
        isLabelShow.setSelected(attr.isEnable());

        labelDetailPane.populate(attr.getAttrLabelDetail());

        if(gaugeValueLabelPane != null){
            gaugeValueLabelPane.populate(attr.getGaugeValueLabelDetail());
        }

        checkBoxUse();
    }

    public AttrLabel update() {
        if(!isLabelShow.isSelected()) {
            return null;
        }
        AttrLabel attrLabel = ((VanChartPlot)this.plot).getDefaultAttrLabel();
        attrLabel.setEnable(isLabelShow.isSelected());

        labelDetailPane.update(attrLabel.getAttrLabelDetail());

        if(gaugeValueLabelPane != null){
            gaugeValueLabelPane.update(attrLabel.getGaugeValueLabelDetail());
        }

        return attrLabel;
    }

}
