package com.fr.design.gui.imenu;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

/**
 * Created by IntelliJ IDEA.
 * Author : daisy
 * Version: 6.5.6
 * Date: 13-12-30
 * Time: ÉÏÎç10:30
 */
public class UIMenuBar extends JMenuBar {

    private static final int LEFT_GAP = 5;

    public UIMenuBar() {
        super();
        this.setBorder(new EmptyBorder(0, LEFT_GAP, 0, 0));

    }

    /**
     * ¸üÐÂUI
     */
    public void updateUI() {
        setUI(new UIMenuBarUI());
    }
}
