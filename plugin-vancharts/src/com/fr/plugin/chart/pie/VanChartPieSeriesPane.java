package com.fr.plugin.chart.pie;


import com.fr.chart.chartattr.Plot;
import com.fr.design.gui.ibutton.UIToggleButton;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.ispinner.UISpinner;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.chart.gui.ChartStylePane;
import com.fr.general.Inter;
import com.fr.plugin.chart.PiePlot4VanChart;
import com.fr.plugin.chart.designer.TableLayout4VanChartHelper;
import com.fr.plugin.chart.designer.style.series.VanChartAbstractPlotSeriesPane;

import javax.swing.*;
import java.awt.*;

//ͼ������-��ʽ ��ͼϵ�н���

public class VanChartPieSeriesPane extends VanChartAbstractPlotSeriesPane {

    private static final long serialVersionUID = 2800670681079289596L;
    private static final double MIN_ANGLE = PiePlot4VanChart.START_ANGLE;
    private static final double MAX_ANGLE = PiePlot4VanChart.END_ANGLE;

    private UISpinner startAngle;
    private UISpinner endAngle;
    private UISpinner innerRadius;
    private UIToggleButton supportRotation;

    public VanChartPieSeriesPane(ChartStylePane parent, Plot plot) {
        super(parent, plot);
    }

    protected JPanel getContentInPlotType() {
        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        double[] columnSize = {f};
        double[] rowSize = {p,p,p,p,p,p,p};
        Component[][] components = new Component[][]{
                new Component[]{createStylePane()},
                new Component[]{new JSeparator()},
                new Component[]{createSeriesStylePane(rowSize, new double[]{p,f})},
                new Component[]{new JSeparator()},
                new Component[]{createBorderPane()},
        };

        return TableLayoutHelper.createTableLayoutPane(components,rowSize,columnSize);
    }


    private JPanel createSeriesStylePane(double[] row, double[] col) {
        startAngle = new UISpinner(MIN_ANGLE, MAX_ANGLE, 1, 0);
        endAngle = new UISpinner(MIN_ANGLE, MAX_ANGLE, 1, 0);
        innerRadius = new UISpinner(0, 100, 1, 0);
        supportRotation = new UIToggleButton(Inter.getLocText("Plugin-ChartF_Rotation"));

        Component[][] components = new Component[][]{
                new Component[]{new UILabel(Inter.getLocText("Plugin-ChartF_StartAngle")),startAngle},
                new Component[]{new UILabel(Inter.getLocText("Plugin-ChartF_EndAngle")),endAngle},
                new Component[]{new UILabel(Inter.getLocText("Plugin-ChartF_InnerRadius")),innerRadius},
                new Component[]{supportRotation,null},
        };
        JPanel panel = TableLayoutHelper.createTableLayoutPane(components, row, col);
        return TableLayout4VanChartHelper.createTableLayoutPaneWithTitle(Inter.getLocText("FR-Designer-Widget_Style"), panel);
    }


    public void populateBean(Plot plot) {
        if(plot == null) {
            return;
        }

        super.populateBean(plot);

        if(plot.accept(PiePlot4VanChart.class)){
            PiePlot4VanChart piePlot4VanChart = (PiePlot4VanChart)plot;
            startAngle.setValue(piePlot4VanChart.getStartAngle());
            endAngle.setValue(piePlot4VanChart.getEndAngle());
            innerRadius.setValue(piePlot4VanChart.getInnerRadiusPercent());
            supportRotation.setSelected(piePlot4VanChart.isSupportRotation());
        }
    }

    public void updateBean(Plot plot) {
        if(plot == null) {
            return;
        }

        super.updateBean(plot);

        if(plot.accept(PiePlot4VanChart.class)){
            PiePlot4VanChart piePlot4VanChart = (PiePlot4VanChart)plot;
            piePlot4VanChart.setStartAngle(startAngle.getValue());
            piePlot4VanChart.setEndAngle(endAngle.getValue());
            piePlot4VanChart.setInnerRadiusPercent(innerRadius.getValue());
            piePlot4VanChart.setSupportRotation(supportRotation.isSelected());
        }
    }
}

