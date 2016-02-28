package com.fr.plugin.chart.designer.style.axis;

import com.fr.plugin.chart.attr.axis.VanChartAxis;
import com.fr.plugin.chart.designer.style.VanChartStylePane;

/**
 * 坐标轴界面接口。包括有类型选择的和数值轴
 */
public interface VanChartXYAxisPaneInterface {

    //parent传过去就重新布局（条形图），null则不用
    public void populate(VanChartAxis axis, VanChartStylePane parent);

    public VanChartAxis update(VanChartAxis axis) ;

}
