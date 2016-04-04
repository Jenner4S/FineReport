package com.fr.plugin.chart.designer.component.label;

import com.fr.plugin.chart.designer.TableLayout4VanChartHelper;
import com.fr.plugin.chart.designer.component.VanChartTooltipContentPane;
import com.fr.plugin.chart.designer.style.VanChartStylePane;

import javax.swing.*;
import java.awt.*;

/**
 * ϵ�С�ֵ��ֵ��ռ���������Ǳ��̵�ֵ��ǩ��
 */
public class TooltipContentPaneWithOutCate  extends VanChartTooltipContentPane {

    private static final long serialVersionUID = 1198062632979554467L;

    public TooltipContentPaneWithOutCate(VanChartStylePane parent, JPanel showOnPane){
        super(parent, showOnPane);
    }

    protected JPanel createTableLayoutPaneWithTitle(String title, Component component) {
        return TableLayout4VanChartHelper.createTableLayoutPaneWithSmallTitle(title, component);
    }

    protected double[] getRowSize(double p){
        return new double[]{p,p,p};
    }

    protected Component[][] getPaneComponents(){
        return new Component[][]{
                new Component[]{isSeries,null},
                new Component[]{isValue,valueFormatButton},
                new Component[]{isValuePercent,valuePercentFormatButton},
        };
    }
}
