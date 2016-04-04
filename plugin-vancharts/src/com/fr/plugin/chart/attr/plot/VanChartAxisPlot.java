package com.fr.plugin.chart.attr.plot;

import com.fr.chart.chartglyph.ConditionCollection;
import com.fr.plugin.chart.attr.axis.VanChartAxis;

import java.util.List;

/**
 * Created by Mitisky on 15/11/30.
 */
public interface VanChartAxisPlot {
    /**
     * ����x��
     * @param axisName ������
     * @param position λ��
     * @return ��
     */
    public VanChartAxis createXAxis(String axisName, int position);

    /**
     * ����y��
     * @param axisName ������
     * @param position λ��
     * @return ��
     */
    public VanChartAxis createYAxis(String axisName, int position);

    public List<VanChartAxis> getYAxisList();

    public List<VanChartAxis> getXAxisList();

    public ConditionCollection getStackAndAxisCondition();

    /**
     * �Զ���ͼ��
     * @return �Զ���ͼ��
     */
    public boolean isCustomChart();
}
