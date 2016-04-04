package com.fr.plugin.chart.radar;

import com.fr.chart.chartattr.Chart;
import com.fr.chart.chartattr.Plot;
import com.fr.design.mainframe.chart.gui.type.ChartImagePane;
import com.fr.general.FRLogger;
import com.fr.general.Inter;
import com.fr.plugin.chart.designer.type.AbstractVanChartTypePane;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mitisky on 15/12/28.
 */
public class VanChartRadarPlotPane  extends AbstractVanChartTypePane {

    private static final long serialVersionUID = -4599483879031804911L;

    protected List<ChartImagePane> initDemoList() {
        List <ChartImagePane> demoList = new ArrayList<ChartImagePane>();
        String[] iconPaths = getTypeIconPath();
        String[] tipNames = new String[]{Inter.getLocText("Plugin-ChartF_Radar"),
                Inter.getLocText("Plugin-ChartF_StackColumnTypeRadar")
        };

        for(int i = 0, len = iconPaths.length; i < len; i++){
            ChartImagePane pane = new ChartImagePane(iconPaths[i], tipNames[i]);
            demoList.add(pane);
            if(i == 0){
                pane.isPressing = true;
            }
        }

        return demoList;
    }

    @Override
    protected List<ChartImagePane> initStyleList() {
        return new ArrayList<ChartImagePane>();
    }


    @Override
    protected String[] getTypeIconPath() {
        return new String[]{"/com/fr/plugin/chart/radar/images/radar.png",
                "/com/fr/plugin/chart/radar/images/stack.png"
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
        return Inter.getLocText("Plugin-ChartF_NewRadar");
    }

    /**
     * ���½�������
     */
    public void populateBean(Chart chart) {
        for(ChartImagePane imagePane : typeDemo) {
            imagePane.isPressing = false;
        }
        Plot plot = chart.getPlot();
        if(plot instanceof VanChartRadarPlot) {
            lastTypeIndex = ((VanChartRadarPlot)plot).getVanChartPlotType().ordinal();
            typeDemo.get(lastTypeIndex).isPressing = true;
        }
        checkDemosBackground();
    }

    protected Plot getSelectedClonedPlot(){
        VanChartRadarPlot newPlot = null;
        Chart[] RadarChart = RadarIndependentVanChart.RadarVanChartTypes;
        for(int i = 0, len = RadarChart.length; i < len; i++){
            if(typeDemo.get(i).isPressing){
                newPlot = (VanChartRadarPlot)RadarChart[i].getPlot();
            }
        }

        Plot cloned = null;
        try {
            cloned = (Plot)newPlot.clone();
        } catch (CloneNotSupportedException e) {
            FRLogger.getLogger().error("Error In RadarChart");
        }
        return cloned;
    }

    protected void cloneOldConditionCollection(Plot oldPlot, Plot newPlot) throws CloneNotSupportedException{
    }

    /**
     * �����Ƿ����
     * @param ob ����Ķ���
     * @return �Ƿ���chart����
     */
    public boolean accept(Object ob) {
        Plot plot = ((Chart) ob).getPlot();
        return super.accept(ob) && plot.accept(VanChartRadarPlot.class);
    }

    public Chart getDefaultChart() {
        return RadarIndependentVanChart.RadarVanChartTypes[0];
    }

}
