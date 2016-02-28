package com.fr.plugin.chart.line;

import com.fr.chart.chartattr.Chart;
import com.fr.chart.chartattr.Plot;
import com.fr.design.mainframe.chart.gui.type.ChartImagePane;
import com.fr.general.FRLogger;
import com.fr.general.Inter;
import com.fr.plugin.chart.attr.VanChartPlotType;
import com.fr.plugin.chart.designer.type.AbstractVanChartTypePane;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mitisky on 15/11/5.
 */
public class VanChartLinePlotPane extends AbstractVanChartTypePane {
    private static final long serialVersionUID = -8161581682558781651L;

    @Override
    protected List<ChartImagePane> initDemoList() {

        List <ChartImagePane> demoList = new ArrayList<ChartImagePane>();
        String[] iconPaths = getTypeIconPath();
        ChartImagePane pane = new ChartImagePane(iconPaths[0], Inter.getLocText("FR-Chart-Type_Line"));
        pane.isPressing = true;
        demoList.add(pane);
        demoList.add(new ChartImagePane(iconPaths[1], Inter.getLocText(new String[]{"FR-Chart-Type_Stacked","FR-Chart-Type_Line"})));
        demoList.add(new ChartImagePane(iconPaths[2], Inter.getLocText("FR-Chart-Mode_Custom")));
        return demoList;
    }

    @Override
    protected List<ChartImagePane> initStyleList() {
        return new ArrayList<ChartImagePane>();
    }


    @Override
    protected String[] getTypeIconPath() {
        return new String[]{"/com/fr/plugin/chart/line/images/line.png",
                "/com/fr/plugin/chart/line/images/stack.png",
                "/com/fr/plugin/chart/line/images/custom.png",
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
        return Inter.getLocText("Plugin-ChartF_NewLine");
    }

    /**
     * 更新界面内容
     */
    public void populateBean(Chart chart) {
        for(ChartImagePane imagePane : typeDemo) {
            imagePane.isPressing = false;
        }
        Plot plot = chart.getPlot();
        if(plot instanceof VanChartLinePlot) {
            VanChartPlotType plotType = ((VanChartLinePlot)plot).getVanChartPlotType();
            switch (plotType){
                case CUSTOM:
                    lastTypeIndex = 2;
                    break;
                default:
                    lastTypeIndex = plotType.ordinal();
                    break;
            }
            typeDemo.get(lastTypeIndex).isPressing = true;
        }
        checkDemosBackground();
    }

    protected Plot getSelectedClonedPlot(){
        VanChartLinePlot newPlot = null;
        Chart[] lineChart = LineIndependentVanChart.LineVanChartTypes;
        for(int i = 0, len = lineChart.length; i < len; i++){
            if(typeDemo.get(i).isPressing){
                newPlot = (VanChartLinePlot)lineChart[i].getPlot();
            }
        }
        Plot cloned = null;
        try {
            cloned = (Plot)newPlot.clone();
        } catch (CloneNotSupportedException e) {
            FRLogger.getLogger().error("Error In LineChart");
        }
        return cloned;
    }

    /**
     * 界面是否接受
     * @param ob 传入的对象
     * @return 是否是chart对象
     */
    public boolean accept(Object ob) {
        Plot plot = ((Chart) ob).getPlot();
        return super.accept(ob) && plot.accept(VanChartLinePlot.class);
    }

    public Chart getDefaultChart() {
        return LineIndependentVanChart.LineVanChartTypes[0];
    }
}
