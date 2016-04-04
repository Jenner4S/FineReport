package com.fr.plugin.chart.designer.style.axis.gauge;

import com.fr.design.chart.axis.MinMaxValuePane;
import com.fr.design.gui.ibutton.UIButtonGroup;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.chart.PaneTitleConstants;
import com.fr.design.mainframe.chart.gui.style.ChartTextAttrPane;
import com.fr.design.style.color.ColorSelectBox;
import com.fr.general.ComparatorUtils;
import com.fr.general.Inter;
import com.fr.plugin.chart.attr.axis.VanChartAxis;
import com.fr.plugin.chart.attr.axis.VanChartGaugeAxis;
import com.fr.plugin.chart.designer.TableLayout4VanChartHelper;
import com.fr.plugin.chart.designer.style.axis.VanChartValueAxisPane;
import com.fr.plugin.chart.designer.style.axis.component.MinMaxValuePaneWithOutTick;
import com.fr.plugin.chart.gauge.GaugeStyle;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * 仪表盘的坐标轴界面
 */
public class VanChartGaugeAxisPane extends VanChartValueAxisPane{

    private static final long serialVersionUID = -9213466625457882224L;

    private ColorSelectBox mainTickColor;
    private ColorSelectBox secTickColor;

    private GaugeStyle gaugeStyle;

    public void setGaugeStyle(GaugeStyle gaugeStyle) {
        this.gaugeStyle = gaugeStyle;
    }

    protected JPanel createContentPane(boolean isXAxis){
        if(gaugeStyle == null){
            gaugeStyle = GaugeStyle.POINTER;
        }
        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        double[] columnSize = {f, p};
        double[] rowSize = {p,p,p,p,p,p,p,p};
        return TableLayoutHelper.createTableLayoutPane(getPanelComponents(p, f, columnSize), rowSize, columnSize);
    }

    private Component[][] getPanelComponents(double p, double f, double[] columnSize) {
        switch (gaugeStyle){
            case THERMOMETER:
                return new Component[][]{
                        new Component[]{createLabelPane(new double[]{p, p, p}, columnSize), null},
                        new Component[]{new JSeparator(), null},
                        new Component[]{createValueDefinition(), null},
                        new Component[]{new JSeparator(), null},
                        new Component[]{createTickColorPane(new double[]{p, p, p}, new double[]{p, f}), null},
                        new Component[]{new JSeparator(), null},
                        new Component[]{createValueStylePane()},
                };
            case RING:
                return new Component[][]{
                        new Component[]{createValueDefinition(), null},
                };
            case SLOT:
                return new Component[][]{
                        new Component[]{createValueDefinition(), null}
                };
            default:
                return new Component[][]{
                        new Component[]{createLabelPane(new double[]{p, p, p}, columnSize), null},
                        new Component[]{new JSeparator(), null},
                        new Component[]{createValueDefinition(), null},
                        new Component[]{new JSeparator(), null},
                        new Component[]{createValueStylePane()},
                };
        }
    }

    protected JPanel createLabelPane(double[] row, double[] col){
        showLabel = new UIButtonGroup(new String[]{Inter.getLocText("Chart-Use_Show"), Inter.getLocText("Plugin-ChartF_Hidden")});
        labelTextAttrPane = new ChartTextAttrPane();
        JPanel panel = new JPanel(new BorderLayout(0, 6));
        panel.add(showLabel, BorderLayout.NORTH);
        panel.add(labelTextAttrPane, BorderLayout.CENTER);
        showLabel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkLabelPane();
            }
        });
        return TableLayout4VanChartHelper.createTableLayoutPaneWithTitle(PaneTitleConstants.CHART_STYLE_LABEL_TITLE, panel);
    }

    private JPanel createValueDefinition(){
        switch (gaugeStyle){
            case RING:
                minMaxValuePane = new MinMaxValuePaneWithOutTick();
                break;
            case SLOT:
                minMaxValuePane = new MinMaxValuePaneWithOutTick();
                break;
            default:
                minMaxValuePane = new MinMaxValuePane();
                break;
        }
        return TableLayout4VanChartHelper.createTableLayoutPaneWithTitle(Inter.getLocText("Plugin-ChartF_ValueDefinition"), minMaxValuePane);
    }

    private JPanel createTickColorPane(double[] row, double[] col){
        mainTickColor = new ColorSelectBox(100);
        secTickColor = new ColorSelectBox(100);
        Component[][] components = new Component[][]{
                new Component[]{new UILabel(Inter.getLocText("Plugin-ChartF_MainGraduationLine")), mainTickColor},
                new Component[]{new UILabel(Inter.getLocText("Plugin-ChartF_SecondGraduationLine")), secTickColor},
        };
        JPanel panel = TableLayoutHelper.createTableLayoutPane(components, row, col);
        return TableLayout4VanChartHelper.createTableLayoutPaneWithTitle(Inter.getLocText("Plugin-ChartF_TickColor"), panel);
    }

    public void populateBean(VanChartAxis axis){
        boolean defaultPanel = ComparatorUtils.equals(gaugeStyle, GaugeStyle.POINTER) || ComparatorUtils.equals(gaugeStyle, GaugeStyle.POINTER_SEMI);
        if(!defaultPanel){
            reLayoutPane(false);
        }
        VanChartGaugeAxis gaugeAxis = (VanChartGaugeAxis)axis;
        if(mainTickColor != null){
            mainTickColor.setSelectObject(gaugeAxis.getMainTickColor());
        }
        if(secTickColor != null){
            secTickColor.setSelectObject(gaugeAxis.getSecTickColor());
        }
        super.populateBean(gaugeAxis);
    }

    public void updateBean(VanChartAxis axis) {
        VanChartGaugeAxis gaugeAxis = (VanChartGaugeAxis)axis;
        if(mainTickColor != null){
            gaugeAxis.setMainTickColor(mainTickColor.getSelectObject());
        }
        if(secTickColor != null){
            gaugeAxis.setSecTickColor(secTickColor.getSelectObject());
        }
        super.updateBean(gaugeAxis);
    }
}
