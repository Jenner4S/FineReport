package com.fr.design.gui.icontainer;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.metal.MetalScrollBarUI;
import javax.swing.plaf.metal.MetalScrollPaneUI;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Created by IntelliJ IDEA.
 * Author : daisy
 * Version: 6.5.6
 * Date: 14-2-13
 * Time: ����10:07
 */
public class UIScrollPaneUI extends MetalScrollPaneUI implements PropertyChangeListener {

    /**
     * ����UI
     * @param c ���
     * @return  ���UI
     */
    public static ComponentUI createUI(JComponent c) {
        return new UIScrollPaneUI();
    }

    /**
     * Ϊ�����ʼ��UI
     * @param c ���
     */
    public void installUI(JComponent c) {
        super.installUI(c);

        // Note: It never happened before Java 1.5 that scrollbar is null
        JScrollBar sb = scrollpane.getHorizontalScrollBar();
        if (sb != null) {
            sb.putClientProperty(MetalScrollBarUI.FREE_STANDING_PROP, Boolean.FALSE);
        }

        sb = scrollpane.getVerticalScrollBar();
        if (sb != null) {
            sb.putClientProperty(MetalScrollBarUI.FREE_STANDING_PROP, Boolean.FALSE);
        }
    }

    protected PropertyChangeListener createScrollBarSwapListener() {
        return this;
    }

    /**
     * ���Ըı�ķ���
     * @param e �¼�
     */
    public void propertyChange(PropertyChangeEvent e) {
    }
}
