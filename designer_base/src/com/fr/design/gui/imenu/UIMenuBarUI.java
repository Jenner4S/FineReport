package com.fr.design.gui.imenu;

import com.fr.design.constants.UIConstants;

import javax.swing.plaf.basic.BasicMenuBarUI;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * Author : daisy
 * Version: 6.5.6
 * Date: 13-12-30
 * Time: ����10:36
 */
public class UIMenuBarUI extends BasicMenuBarUI {

    public UIMenuBarUI() {
    }

    public void paint(java.awt.Graphics g, javax.swing.JComponent c) {
        //�Ƿ���ȫ��͸��
        super.paint(g, c);
        if (!c.isOpaque()) {
            return;
        }
        Color oldColor = g.getColor();
        g.setColor(UIConstants.NORMAL_BACKGROUND);
        g.fillRect(0, 0, c.getWidth(), c.getHeight());
        g.setColor(oldColor);
    }


}
