package com.fr.plugin.chart.line;

import com.fr.design.gui.ilable.UILabel;
import com.fr.general.Inter;
import com.fr.plugin.chart.column.VanChartCustomStackAndAxisConditionPane;

import java.awt.*;

/**
 * �ѻ��������ᣨû�аٷֱȣ�
 */
public class VanChartLineCustomStackAndAxisConditionPane extends VanChartCustomStackAndAxisConditionPane {
    protected Component[][] getDeployComponents() {
        Component[][] components = new Component[][]{
                new Component[]{new UILabel(Inter.getLocText("ChartF-X_Axis")),XAxis},
                new Component[]{new UILabel(Inter.getLocText("ChartF-Y_Axis")),YAxis},
                new Component[]{new UILabel(Inter.getLocText("FR-Chart-Type_Stacked")),isStacked},
        };

        return components;
    }
}
