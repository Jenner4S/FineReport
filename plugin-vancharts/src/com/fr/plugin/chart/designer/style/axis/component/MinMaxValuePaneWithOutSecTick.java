package com.fr.plugin.chart.designer.style.axis.component;

import com.fr.design.chart.axis.MinMaxValuePane;

import java.awt.*;

/**
 * ���ֵ����Сֵ����Ҫ�̶ȵ�λ��û�д�Ҫ�̶ȵ�λ
 */
public class MinMaxValuePaneWithOutSecTick extends MinMaxValuePane {

    private static final long serialVersionUID = -887359523503645758L;

    protected Component[][] getPanelComponents() {
        return 	new Component[][]{
                new Component[]{minCheckBox, minValueField},
                new Component[]{maxCheckBox, maxValueField},
                new Component[]{isCustomMainUnitBox, mainUnitField},
        };
    }

}
