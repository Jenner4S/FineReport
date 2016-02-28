package com.fr.plugin.chart.pie;


import com.fr.chart.chartattr.Chart;
import com.fr.chart.chartattr.Plot;
import com.fr.design.mainframe.chart.gui.type.ChartImagePane;
import com.fr.general.FRLogger;
import com.fr.general.Inter;
import com.fr.plugin.chart.PiePlot4VanChart;
import com.fr.plugin.chart.designer.type.AbstractVanChartTypePane;

import java.util.ArrayList;
import java.util.List;

/**
 * ��ͼ(������) ���Ա� ѡ������ ���ֽ���.
 */
public class VanChartPiePlotPane extends AbstractVanChartTypePane {

    private static final long serialVersionUID = 6163246902689597259L;

    @Override
    protected List<ChartImagePane> initDemoList() {
        List <ChartImagePane> demoList = new ArrayList<ChartImagePane>();
        String[] iconPaths = getTypeIconPath();
        ChartImagePane pane = new ChartImagePane(iconPaths[0], Inter.getLocText("I-PieStyle_Normal"));
        pane.isPressing = true;
        demoList.add(pane);
        demoList.add(new ChartImagePane(iconPaths[1], Inter.getLocText("Plugin-ChartF_SameArcPie"), true));
        demoList.add(new ChartImagePane(iconPaths[2], Inter.getLocText("Plugin-ChartF_DifferentArcPie"), true));
        return demoList;
    }

    @Override
    protected List<ChartImagePane> initStyleList() {
        return new ArrayList<ChartImagePane>();
    }


    @Override
    protected String[] getTypeIconPath() {
        return new String[]{"/com/fr/plugin/chart/pie/images/pie.png",
                "/com/fr/plugin/chart/pie/images/same.png",
                "/com/fr/plugin/chart/pie/images/different.png"
        };
    }

    @Override
    protected String[] getTypeLayoutPath() {
        return new String[]{
        };
    }


    /**
     * ���ؽ������
     * @return �������
     */
    public String title4PopupWindow() {
        return Inter.getLocText("Plugin-ChartF_NewPie");
    }


    /**
     * ���½�������
     */
    public void populateBean(Chart chart) {
        for(ChartImagePane imagePane : typeDemo) {
            imagePane.isPressing = false;
        }
        Plot plot = chart.getPlot();
        if(plot instanceof PiePlot4VanChart) {
            lastTypeIndex = ((PiePlot4VanChart)plot).getRoseType().ordinal();
            typeDemo.get(lastTypeIndex).isPressing = true;
        }
        checkDemosBackground();
    }

    protected Plot getSelectedClonedPlot(){
        PiePlot4VanChart newPlot = null;
        Chart[] pieChart = PieIndependentVanChart.newPieChartTypes;
        for(int i = 0, len = pieChart.length; i < len; i++){
            if(typeDemo.get(i).isPressing){
                newPlot = (PiePlot4VanChart)pieChart[i].getPlot();
            }
        }

        Plot cloned = null;
        try {
            cloned = (Plot)newPlot.clone();
        } catch (CloneNotSupportedException e) {
            FRLogger.getLogger().error("Error In ColumnChart");
        }
        return cloned;
    }



    /**
     * �����Ƿ����
     * @param ob ����Ķ���
     * @return �Ƿ���chart����
     */
    public boolean accept(Object ob) {
        return super.accept(ob) && ((Chart) ob).getPlot().accept(PiePlot4VanChart.class);
    }

    public Chart getDefaultChart() {
        return PieIndependentVanChart.newPieChartTypes[0];
    }
}

