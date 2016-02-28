package com.fr.plugin.chart.designer.style.background.radar;

import com.fr.design.gui.ilable.UILabel;
import com.fr.general.Inter;
import com.fr.plugin.chart.designer.style.background.VanChartAlertValuePane;

import java.awt.*;

/**
 * �״�ͼ�ľ��������ý��棬û���������ѡ��ֻ��Y�����þ����ߡ�
 */
public class VanChartRadarAlertValuePane extends VanChartAlertValuePane {
    private static final long serialVersionUID = -4732783185768672053L;

    protected Component[][] getTopPaneComponents() {
        return new Component[][]{
                new Component[]{new UILabel(Inter.getLocText("Chart-Use_Value")),alertValue},
                new Component[]{new UILabel(Inter.getLocText("Plugin-ChartF_LineStyle")),alertLineStyle},
                new Component[]{new UILabel(Inter.getLocText("FR-Chart-Color_Color")),alertLineColor},
        };
    }

}
