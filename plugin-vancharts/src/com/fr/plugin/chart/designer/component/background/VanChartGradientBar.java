package com.fr.plugin.chart.designer.component.background;

import com.fr.design.style.background.gradient.GradientBar;

/**
 * ���bar������ק����
 */
public class VanChartGradientBar extends GradientBar {
    private static final long serialVersionUID = 2787525421995954889L;

    public VanChartGradientBar(int minvalue, int maxvalue) {
        super(minvalue, maxvalue);
    }

    @Override
    protected void addMouseDragListener() {
        //�������ק�¼�
    }
}
