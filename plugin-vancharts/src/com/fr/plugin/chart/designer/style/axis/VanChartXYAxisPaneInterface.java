package com.fr.plugin.chart.designer.style.axis;

import com.fr.plugin.chart.attr.axis.VanChartAxis;
import com.fr.plugin.chart.designer.style.VanChartStylePane;

/**
 * ���������ӿڡ�����������ѡ��ĺ���ֵ��
 */
public interface VanChartXYAxisPaneInterface {

    //parent����ȥ�����²��֣�����ͼ����null����
    public void populate(VanChartAxis axis, VanChartStylePane parent);

    public VanChartAxis update(VanChartAxis axis) ;

}
