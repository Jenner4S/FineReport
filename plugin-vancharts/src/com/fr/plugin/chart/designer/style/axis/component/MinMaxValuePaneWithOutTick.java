package com.fr.plugin.chart.designer.style.axis.component;

import com.fr.design.chart.axis.MinMaxValuePane;

import java.awt.*;

/**
 * �����Сֵ���ã�û�����ο̶ȵ�λ����
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
