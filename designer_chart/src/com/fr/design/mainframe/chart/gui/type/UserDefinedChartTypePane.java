package com.fr.design.mainframe.chart.gui.type;


import com.fr.chart.chartattr.Chart;
import com.fr.stable.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by eason on 15/4/23.
 */
public abstract class UserDefinedChartTypePane extends AbstractChartTypePane{

    protected List<ChartImagePane> initDemoList() {
        List <ChartImagePane> demoList = new ArrayList<ChartImagePane>();
        ChartImagePane pane = new ChartImagePane(getTypeIconPath()[0], title4PopupWindow(), true);
        pane.isPressing = true;
        demoList.add(pane);
        return demoList;
    }

    /**
     * 默认没有没有样式布局
     */
    protected List<ChartImagePane> initStyleList() {
        return new ArrayList<ChartImagePane>();
    }

    protected String[] getTypeLayoutPath() {
        return new String[]{StringUtils.EMPTY};
    }

    protected String[] getTypeIconPath(){
        return new String[]{"/com/fr/design/images/chart/default.png"};
    }

    public void updateBean(Chart chart) {

    }

    public void populateBean(Chart chart){
        typeDemo.get(0).isPressing = true;
        checkDemosBackground();
    }

    /**
     * 弹出界面的标题
     * @return 标题
     */
    public String title4PopupWindow(){
        return "";
    }


}