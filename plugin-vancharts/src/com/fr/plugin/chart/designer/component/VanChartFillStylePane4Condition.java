package com.fr.plugin.chart.designer.component;

import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.chart.gui.style.ChartFillStylePane;

import javax.swing.*;
import java.awt.*;

/**
 * ��ɫ ֻ��������ؼ�����ɫtitle�� �������� ��ɫ���õ�
 */
public class VanChartFillStylePane4Condition extends ChartFillStylePane {
    private static final long serialVersionUID = 2470094484790112401L;

    @Override
    protected void initLayout() {
        customPane.setPreferredSize(new Dimension(200, 130));
        colorGradient.setPreferredSize(new Dimension(120, 30));

        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        double[] columnSize = { f };
        double[] rowSize = { p, p};
        Component[][] components = new Component[][]{
                new Component[]{styleSelectBox},
                new Component[]{customPane}
        } ;
        JPanel panel = TableLayoutHelper.createTableLayoutPane(components,rowSize,columnSize);
        this.setLayout(new BorderLayout());
        this.add(panel,BorderLayout.CENTER);
    }

    @Override
    public Dimension getPreferredSize() {
        if(styleSelectBox.getSelectedIndex() != styleSelectBox.getItemCount() - 1) {
            return new Dimension(styleSelectBox.getPreferredSize().width, 20);
        }
        return super.getPreferredSize();
    }
}
