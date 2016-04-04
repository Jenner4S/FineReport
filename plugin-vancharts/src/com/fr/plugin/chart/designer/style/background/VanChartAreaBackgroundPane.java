package com.fr.plugin.chart.designer.style.background;

import com.fr.chart.chartattr.Chart;
import com.fr.chart.chartattr.Plot;
import com.fr.design.gui.frpane.AbstractAttrNoScrollPane;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.chart.PaneTitleConstants;
import com.fr.design.mainframe.chart.gui.style.AbstractChartTabPane;
import com.fr.plugin.chart.designer.component.background.VanChartBackgroundPane;
import com.fr.plugin.chart.designer.component.border.VanChartBorderWithRadiusPane;
import com.fr.plugin.chart.designer.style.background.radar.VanChartRadarAxisAreaPane;

import javax.swing.*;
import java.awt.*;

//ͼ����|��ͼ�� �߿�ͱ���
public class VanChartAreaBackgroundPane extends AbstractChartTabPane<Chart> {

    private static final long serialVersionUID = 104381641640066015L;
    private VanChartBorderWithRadiusPane chartBorderPane;
    private VanChartBackgroundPane chartBackgroundPane;
    private VanChartAxisAreaPane chartAxisAreaPane;

    private JPanel contentPane;

    private boolean isPlot;//��ͼ��

    private AbstractAttrNoScrollPane parent;

    public VanChartAreaBackgroundPane(boolean isPlot, AbstractAttrNoScrollPane parent){
        super();
        this.isPlot = isPlot;
        this.parent = parent;
    }

    @Override
    protected JPanel createContentPane() {
        contentPane = new JPanel(new BorderLayout());
        chartBorderPane = new VanChartBorderWithRadiusPane();
        chartBackgroundPane = new VanChartBackgroundPane();

        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        double[] columnSize = { f };
        double[] rowSize = {p,p,p};
        Component[][] components = new Component[][]{
                new Component[]{chartBorderPane},
                new Component[]{chartBackgroundPane}
        };
        JPanel panel = TableLayoutHelper.createTableLayoutPane(components, rowSize, columnSize);
        contentPane.add(panel, BorderLayout.CENTER);
        return contentPane;
    }

    private void refreshContentPane(Plot plot) {
        contentPane.removeAll();
        chartBorderPane = new VanChartBorderWithRadiusPane();
        chartBackgroundPane = new VanChartBackgroundPane();

        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        double[] columnSize = { f };
        double[] rowSize = {p,p,p};
        Component[][] components;

        if(plot.isSupportBorder()){//�б߿�ͱ���
            chartAxisAreaPane = new VanChartAxisAreaPane();
            components = new Component[][]{
                    new Component[]{chartBorderPane},
                    new Component[]{chartBackgroundPane},
                    new Component[]{chartAxisAreaPane},
            };
        } else {
            chartAxisAreaPane = new VanChartRadarAxisAreaPane();
            components = new Component[][]{
                    new Component[]{chartAxisAreaPane},
            };
        }

        JPanel panel = TableLayoutHelper.createTableLayoutPane(components, rowSize, columnSize);
        contentPane.add(panel,BorderLayout.CENTER);

        parent.initAllListeners();
    }

    /**
     *       ����
     *    @return ����
     */
    public String title4PopupWindow() {
        if(isPlot){
            return PaneTitleConstants.CHART_STYLE_AREA_PLOT_TITLE;
        }
        return PaneTitleConstants.CHART_STYLE_AREA_AREA_TITLE;
    }

    @Override
    public void updateBean(Chart chart) {
        if (chart == null) {
            chart = new Chart();
        }
        if(isPlot){
            Plot plot = chart.getPlot();
            chartBorderPane.update(plot);
            chartBackgroundPane.update(plot);
            if(plot.isSupportIntervalBackground() && chartAxisAreaPane != null){
                chartAxisAreaPane.updateBean(plot);
            }
        } else {
            chartBorderPane.update(chart);
            chartBackgroundPane.update(chart);
        }
    }

    @Override
    public void populateBean(Chart chart) {
        if(chart == null) {
            return;
        }
        if(isPlot){
            Plot plot = chart.getPlot();
            if(plot.isSupportIntervalBackground() && chartAxisAreaPane == null){
                //����������������ã����羯���ߡ������ߡ��������
                refreshContentPane(plot);
                chartAxisAreaPane.populateBean(plot);
            }
            chartBorderPane.populate(plot);
            chartBackgroundPane.populate(plot);
        } else {
            chartBorderPane.populate(chart);
            chartBackgroundPane.populate(chart);
        }
    }

    @Override
    public Chart updateBean() {
        return null;
    }
}

