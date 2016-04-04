package com.fr.plugin.chart.attr.plot;

import com.fr.chart.chartglyph.ConditionCollection;
import com.fr.plugin.chart.attr.axis.VanChartAxis;

import java.util.List;

/**
 * Created by Mitisky on 15/11/30.
 */
public interface VanChartAxisPlot {
    /**
     * 创建x轴
     * @param axisName 轴名称
     * @param position 位置
     * @return 轴
     */
    public VanChartAxis createXAxis(String axisName, int position);

    /**
     * 创建y轴
     * @param axisName 轴名称
     * @param position 位置
     * @return 轴
     */
    public VanChartAxis createYAxis(String axisName, int position);

    public List<VanChartAxis> getYAxisList();

    public List<VanChartAxis> getXAxisList();

    public ConditionCollection getStackAndAxisCondition();

    /**
     * 自定义图表
     * @return 自定义图表
     */
    public boolean isCustomChart();
}
