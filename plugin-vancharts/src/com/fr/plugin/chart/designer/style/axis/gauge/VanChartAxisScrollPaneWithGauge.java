package com.fr.plugin.chart.designer.style.axis.gauge;

import com.fr.plugin.chart.designer.style.axis.VanChartAxisScrollPaneWithOutTypeSelect;
import com.fr.plugin.chart.gauge.GaugeStyle;

/**
 * ���ֻ�Ǽ�һ��scroll.�Ǳ��̵������ᣬ��ֵ�ᡣ
 */
public class VanChartAxisScrollPaneWithGauge extends VanChartAxisScrollPaneWithOutTypeSelect{
    private static final long serialVersionUID = 7069253678757456911L;

    protected void initAxisPane() {
        axisPane = new VanChartGaugeAxisPane();
    }

    public void setGaugeStyle(GaugeStyle gaugeStyle) {
        if(axisPane instanceof VanChartGaugeAxisPane){
            ((VanChartGaugeAxisPane)axisPane).setGaugeStyle(gaugeStyle);
        }
    }

}
