package com.fr.design.gui.demo;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.fr.design.gui.ilable.UILabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.fr.design.gui.ilable.ActionLabel;
import com.fr.design.gui.ilable.MultilineLabel;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.general.Inter;

/**
 * Created by IntelliJ IDEA.
 * User: Richer
 * Date: 11-7-1
 * Time: ����8:54
 */
public class LabelDemo extends JPanel {
    public LabelDemo() {
        setLayout(FRGUIPaneFactory.createBorderLayout());
         double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;

        Component[][] coms = new Component[][]{
                {new UILabel(Inter.getLocText(new String[]{"Hyperlink", "Label"})+":"), createActionLabel()},
                {new UILabel("�����ֵı�ǩ:"), createMultilineLabel()}
        };
        double[] rowSize = new double[coms.length];
        double[] columnSize = {p, f};
        for (int i = 0; i < rowSize.length; i++) {
            rowSize[i] = p;
        }



        JPanel centerPane = TableLayoutHelper.createTableLayoutPane(coms, rowSize, columnSize);
        add(centerPane, BorderLayout.CENTER);
    }

    private ActionLabel createActionLabel() {
        ActionLabel al = new ActionLabel("���ң��ҿ��ķ�Ӧ");
        al.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(LabelDemo.this, "���ܶԵ��������Ӧ!");
            }
        });
        return al;
    }

    private MultilineLabel createMultilineLabel() {
        MultilineLabel mll = new MultilineLabel("abc\n����");
        return mll;
    }
}
