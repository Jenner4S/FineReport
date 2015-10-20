package com.fr.design.gui.demo;

import java.awt.BorderLayout;
import java.awt.Component;

import com.fr.design.gui.ilable.UILabel;
import javax.swing.JPanel;

import com.fr.design.gui.ilist.CheckBoxList;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;

/**
 * Created by IntelliJ IDEA.
 * User: Richer
 * Date: 11-6-30
 * Time: ����9:17
 */
public class ListDemo extends JPanel {
    public ListDemo() {
        setLayout(FRGUIPaneFactory.createBorderLayout());
         double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;

        Component[][] coms = new Component[][]{
                {new UILabel("��ѡ���б�:"), createCheckBoxList()},
        };
        double[] rowSize = new double[coms.length];
        double[] columnSize = {p, f};
        for (int i = 0; i < rowSize.length; i++) {
            rowSize[i] = p;
        }
        JPanel centerPane = TableLayoutHelper.createTableLayoutPane(coms, rowSize, columnSize);
        add(centerPane, BorderLayout.CENTER);
    }

    private CheckBoxList createCheckBoxList() {
        CheckBoxList cbl = new CheckBoxList(new Object[] {"����", "����", "����", "����"});
        return cbl;
    }
}
