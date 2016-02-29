package com.fr.design.mainframe.chart.gui.type;

import com.fr.base.CoreDecimalFormat;
import com.fr.chart.base.AttrContents;
import com.fr.chart.chartattr.*;
import com.fr.chart.charttypes.FunnelIndependentChart;
import com.fr.general.Inter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Randost
 * Date: 14-12-2
 * Time: 下午2:50
 * To change this template use File | Settings | File Templates.
 */
public class FunnelPlotPane extends AbstractChartTypePane{

    private static final int FUNNEL_CHART = 0;

    @Override
    protected List<ChartImagePane> initDemoList() {
        List <ChartImagePane> demoList = new ArrayList<ChartImagePane>();
        ChartImagePane pane = new ChartImagePane(getTypeIconPath()[0], Inter.getLocText("FR-Chart-Type_Funnel"));
        pane.isPressing = true;
        demoList.add(pane);
        return demoList;
    }

    @Override
    protected List<ChartImagePane> initStyleList() {
        return new ArrayList<ChartImagePane>();
    }

    @Override
    protected String[] getTypeIconPath() {
        return new String[]{"/com/fr/design/images/chart/FunnelPlot/type/0.png",
                "/com/fr/design/images/chart/FunnelPlot/type/1.png",
        };
    }

    @Override
    protected String[] getTypeLayoutPath() {
        return new String[]{"/com/fr/design/images/chart/FunnelPlot/layout/0.png",
                "/com/fr/design/images/chart/FunnelPlot/layout/1.png",
                "/com/fr/design/images/chart/FunnelPlot/layout/2.png",
                "/com/fr/design/images/chart/FunnelPlot/layout/3.png",
        };
    }



    /**
     * 返回界面标题
     * @return 界面标题
     */
    public String title4PopupWindow() {
        return Inter.getLocText("FR-Chart-Type_Funnel");
    }

    /**
     * 更新界面内容
     */
    public void populateBean(Chart chart) {
        super.populateBean(chart);
        typeDemo.get(FUNNEL_CHART).isPressing = true;

        checkDemosBackground();
    }

    /**
     * 保存界面属性
     */
    public void updateBean(Chart chart) {
        chart.switchPlot(getSelectedClonedPlot());
        super.updateBean(chart);
    }

    protected Plot getSelectedClonedPlot(){
        Plot newPlot = new FunnelPlot();
        createFunnelCondition(newPlot);
        return newPlot;
    }

    private void createFunnelCondition(Plot plot) {
        AttrContents attrContents = new AttrContents("${VALUE}${PERCENT}");
        plot.setHotTooltipStyle(attrContents);
        attrContents.setFormat(new CoreDecimalFormat(new DecimalFormat(), "#.##"));
        attrContents.setPercentFormat(new CoreDecimalFormat(new DecimalFormat(), "#.##%"));
    }

    /**
     * 界面是否接受
     * @param ob 传入的对象
     * @return 是否是chart对象
     */
    public boolean accept(Object ob) {
        return super.accept(ob) && ((Chart) ob).getPlot() instanceof FunnelPlot;
    }


    public Chart getDefaultChart() {
        return FunnelIndependentChart.funnelChartTypes[0];
    }
}