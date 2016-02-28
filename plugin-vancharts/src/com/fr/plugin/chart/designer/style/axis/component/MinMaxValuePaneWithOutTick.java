package com.fr.plugin.chart.designer.style.axis.component;

import com.fr.design.chart.axis.MinMaxValuePane;

import java.awt.*;

/**
 * 最大最小值设置，没有主次刻度单位设置
 */
public class MinMaxValuePaneWithOutTick extends MinMaxValuePane {
    private static final long serialVersionUID = -957414093602086034L;

    protected Component[][] getPanelComponents() {
        return 	new Component[][]{
                new Component[]{minCheckBox, minValueField},
                new Component[]{maxCheckBox, maxValueField},
        };
    }
}
