package com.fr.plugin.chart.designer.style.background.radar;

import com.fr.design.beans.BasicBeanPane;
import com.fr.design.gui.ilable.UILabel;
import com.fr.general.Inter;
import com.fr.plugin.chart.attr.axis.VanChartAlertValue;
import com.fr.plugin.chart.attr.axis.VanChartCustomIntervalBackground;
import com.fr.plugin.chart.designer.style.background.VanChartAxisAreaPane;

import java.awt.*;

/**
 * ��ʽ-����-��ͼ������-�״�ͼֻ��Y������ã���������������ߡ������ߣ�
 */
public class VanChartRadarAxisAreaPane extends VanChartAxisAreaPane {

    private static final long serialVersionUID = 2459614679918546393L;

    //�״�ͼֻ�к����y�������������
    protected Component[][] getGridLinePaneComponents() {
        return new Component[][]{
                new Component[]{new UILabel(Inter.getLocText("FR-Chart-Color_Color")),horizontalGridLine},
        };
    }

    protected Component[][] getIntervalPaneComponents() {
        return new Component[][]{
                new Component[]{new UILabel(Inter.getLocText("FR-Chart-Color_Color")),horizontalColorBackground},
        };
    }

    protected Class<? extends BasicBeanPane> getAlertPaneClass() {
        return VanChartRadarAlertValuePane.class;
    }

    protected void setAlertDemoAxisName(VanChartAlertValue demo, String[] axisNames) {
        demo.setAxisName(axisNames[axisNames.length - 1]);//Ĭ��y�ᣬ�������
    }

    protected Class<? extends BasicBeanPane> getIntervalPaneClass() {
        return VanChartRadarCustomIntervalBackgroundPane.class;
    }

    protected void setCustomIntervalBackgroundDemoAxisName(VanChartCustomIntervalBackground demo, String[] axisNames) {
        demo.setAxisName(axisNames[axisNames.length - 1]);
    }
}
