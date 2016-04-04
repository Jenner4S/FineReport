package com.fr.plugin.chart.designer;

import com.fr.design.constants.LayoutConstants;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;

import javax.swing.*;
import java.awt.*;

/**
 * ���� ����+���
 */
public class TableLayout4VanChartHelper {

    private static final int SMALL_GAP = 20;
    /**
     * ���Ⲽ��(�����˵�����߿�46)
     * @param title  ����
     * @param component ���
     * @return ���ֺõ����
     */
    public static JPanel createTableLayoutPaneWithTitle(String title, Component component){
        return TableLayout4VanChartHelper.createTitlePane(title, component, LayoutConstants.CHART_ATTR_TOMARGIN);
    }

    /**
     * ���Ⲽ��(�����˵���������20)
     * @param title  ����
     * @param component ���
     * @return ���ֺõ����
     */
    public static JPanel createTableLayoutPaneWithSmallTitle(String title, Component component){
        return TableLayout4VanChartHelper.createTitlePane(title, component, TableLayout4VanChartHelper.SMALL_GAP);
    }

    /**
     * ���Ⲽ�֣�ָ��gap��
     * @param title ����
     * @param component ���
     * @param gap ��������
     * @return ���ֺõ����
     */
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
