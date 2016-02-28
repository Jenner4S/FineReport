package com.fr.solution.plugin.chart.echarts.ui.map;

import com.fr.design.constants.LayoutConstants;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;

import javax.swing.*;
import java.awt.*;

public class TableLayout4ChartHelper {

    private static final int SMALL_GAP = 20;
    public static JPanel createTableLayoutPaneWithTitle(String title, Component component){
        return TableLayout4ChartHelper.createTitlePane(title, component, LayoutConstants.CHART_ATTR_TOMARGIN);
    }

    public static JPanel createTableLayoutPaneWithSmallTitle(String title, Component component){
        return TableLayout4ChartHelper.createTitlePane(title, component, TableLayout4ChartHelper.SMALL_GAP);
    }

    public static JPanel createTitlePane(String title, Component component, int gap){
        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        double[] columnSize = {gap, f};
        double[] rowSize = {p, p};
        Component[][] components = new Component[][]{
                new Component[]{new UILabel(title),null},
                new Component[]{null,component},
        };
        return TableLayoutHelper.createTableLayoutPane(components, rowSize, columnSize);
    }

}
