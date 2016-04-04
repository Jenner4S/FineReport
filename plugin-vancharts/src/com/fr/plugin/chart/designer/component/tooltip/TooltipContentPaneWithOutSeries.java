package com.fr.plugin.chart.designer.component.tooltip;

import com.fr.plugin.chart.designer.TableLayout4VanChartHelper;
import com.fr.plugin.chart.designer.component.VanChartTooltipContentPane;
import com.fr.plugin.chart.designer.style.VanChartStylePane;

import javax.swing.*;
import java.awt.*;

/**
 * �Ǳ��̷Ƕ�ָ�����ݵ���ʾ��û��ϵ������
 */
public class TooltipContentPaneWithOutSeries extends VanChartTooltipContentPane{

    private static final long serialVersionUID = -1973565663365672717L;

    public TooltipContentPaneWithOutSeries(VanChartStylePane parent, JPanel showOnPane){
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
                new Component[]{isCategory,null},
                new Component[]{isValue,valueFormatButton},
                new Component[]{isValuePercent,valuePercentFormatButton},
        };
    }
}
