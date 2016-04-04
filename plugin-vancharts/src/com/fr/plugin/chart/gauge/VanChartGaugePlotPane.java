package com.fr.plugin.chart.gauge;

import com.fr.chart.chartattr.Chart;
import com.fr.chart.chartattr.Plot;
import com.fr.design.mainframe.chart.gui.type.ChartImagePane;
import com.fr.general.FRLogger;
import com.fr.general.Inter;
import com.fr.plugin.chart.designer.type.AbstractVanChartTypePane;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mitisky on 15/11/27.
 */
public class VanChartGaugePlotPane extends AbstractVanChartTypePane {

    private static final long serialVersionUID = -4599483879031804911L;

    protected List<ChartImagePane> initDemoList() {
        List <ChartImagePane> demoList = new ArrayList<ChartImagePane>();
        String[] iconPaths = getTypeIconPath();
        String[] tipNames = new String[]{Inter.getLocText("Plugin-ChartF_Gauge_Pointer"),
                Inter.getLocText("Plugin-ChartF_Gauge_Pointer180"),
                Inter.getLocText("Plugin-ChartF_Gauge_Ring"),
                Inter.getLocText("Plugin-ChartF_Gauge_Slot"),
                Inter.getLocText("Plugin-ChartF_Gauge_Cuvette")
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
        return new String[]{"/com/fr/plugin/chart/gauge/images/pointer.png",
                "/com/fr/plugin/chart/gauge/images/pointer_180.png",
                "/com/fr/plugin/chart/gauge/images/ring.png",
                "/com/fr/plugin/chart/gauge/images/slot.png",
                "/com/fr/plugin/chart/gauge/images/cuvette.png",
        };
    }

    @Override
    protected String[] getTypeLayoutPath() {
        return new String[]{
        };
    }


    /**
     * 返回界面标题
     * @return 界面标题
     */
    public String title4PopupWindow() {
        return Inter.getLocText("Plugin-ChartF_NewGauge");
    }

    /**
     * 更新界面内容
     */
    public void populateBean(Chart chart) {
        for(ChartImagePane imagePane : typeDemo) {
            imagePane.isPressing = false;
        }
        Plot plot = chart.getPlot();
        if(plot instanceof VanChartGaugePlot) {
            lastTypeIndex = ((VanChartGaugePlot)plot).getGaugeStyle().ordinal();
            typeDemo.get(lastTypeIndex).isPressing = true;
        }
        checkDemosBackground();
    }

    protected Plot getSelectedClonedPlot(){
        VanChartGaugePlot newPlot = null;
        Chart[] GaugeChart = GaugeIndependentVanChart.GaugeVanChartTypes;
        for(int i = 0, len = GaugeChart.length; i < len; i++){
            if(typeDemo.get(i).isPressing){
                newPlot = (VanChartGaugePlot)GaugeChart[i].getPlot();
            }
        }

        Plot cloned = null;
        try {
            cloned = (Plot)newPlot.clone();
        } catch (CloneNotSupportedException e) {
            FRLogger.getLogger().error("Error In GaugeChart");
        }
        return cloned;
    }

    /**
     * 保存界面属性
     */
    public void updateBean(Chart chart) {
        boolean oldISMulti = chart.getPlot() instanceof VanChartGaugePlot && ((VanChartGaugePlot)chart.getPlot()).isMultiPointer();
        super.updateBean(chart);
        boolean newISMulti = chart.getPlot() instanceof VanChartGaugePlot && ((VanChartGaugePlot)chart.getPlot()).isMultiPointer();
        if(oldISMulti != newISMulti){
            chart.setFilterDefinition(null);
        }
    }

    protected void cloneOldConditionCollection(Plot oldPlot, Plot newPlot) throws CloneNotSupportedException{
    }

    /**
     * 界面是否接受
     * @param ob 传入的对象
     * @return 是否是chart对象
     */
    public boolean accept(Object ob) {
        Plot plot = ((Chart) ob).getPlot();
        return super.accept(ob) && plot.accept(VanChartGaugePlot.class);
    }

    public Chart getDefaultChart() {
        return GaugeIndependentVanChart.GaugeVanChartTypes[0];
    }

}
