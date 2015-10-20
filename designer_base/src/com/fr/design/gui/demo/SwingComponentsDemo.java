package com.fr.design.gui.demo;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.utils.DesignUtils;
import com.fr.design.utils.gui.GUICoreUtils;

/**
 * Created by IntelliJ IDEA.
 * User: Richer
 * Date: 11-6-27
 * Time: ����4:54
 */
public class SwingComponentsDemo extends JFrame {
    public SwingComponentsDemo() {
    	this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        init();
        JPanel contentPane = (JPanel)getContentPane();
        contentPane.setLayout(FRGUIPaneFactory.createBorderLayout());
        JTabbedPane tab = new JTabbedPane();
        contentPane.add(tab, BorderLayout.CENTER);
        tab.addTab("������", new ComboBoxDemo());
        tab.addTab("������ʾ", new MultiLineTooltipDemo());
        tab.addTab("�б�", new ListDemo());
        tab.addTab("��ǩ", new LabelDemo());
        tab.addTab("���غ�ʱ�϶�����", new LoadingPaneDemo());
    }

    private void init() {
        DesignUtils.initLookAndFeel();
        setTitle("����������ʾ");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JFrame f = new SwingComponentsDemo();
                f.setSize(500, 500);
                f.setVisible(true);
                GUICoreUtils.centerWindow(f);
                f.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            }
        });
    }
}
