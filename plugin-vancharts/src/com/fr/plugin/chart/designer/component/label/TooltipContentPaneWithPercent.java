package com.fr.plugin.chart.designer.component.label;

import com.fr.plugin.chart.designer.TableLayout4VanChartHelper;
import com.fr.plugin.chart.designer.component.VanChartTooltipContentPane;
import com.fr.plugin.chart.designer.style.VanChartStylePane;

import javax.swing.*;
import java.awt.*;

/**
 * ֻ�аٷֱȣ��Ǳ��̵İٷֱȱ�ǩ��
 */
public class TooltipContentPaneWithPercent extends VanChartTooltipContentPane {
    private static final long serialVersionUID = -3739217668948747606L;

    public TooltipContentPaneWithPercent(VanChartStylePane parent, JPanel showOnPane){
        super(parent, showOnPane);
    }

    protected JPanel createTableLayoutPaneWithTitle(String title, Component component) {
        return TableLayout4VanChartHelper.createTableLayoutPaneWithSmallTitle(title, component);
    }

    protected double[] getRowSize(double p){
        return new double[]{p};
    }

    protected Component[][] getPaneComponents(){
        return new Component[][]{
                new Component[]{isValuePercent,valuePercentFormatButton},
        };
    }
}
