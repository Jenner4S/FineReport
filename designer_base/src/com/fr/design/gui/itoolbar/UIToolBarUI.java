package com.fr.design.gui.itoolbar;


import java.awt.Component;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Window;
import java.awt.event.WindowListener;

import javax.swing.AbstractButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JRadioButton;
import javax.swing.JToolBar;
import javax.swing.RootPaneContainer;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.metal.MetalToolBarUI;

import com.fr.design.constants.UIConstants;
import com.fr.design.gui.ibutton.UIToolButtonBorder;
import com.fr.design.gui.icheckbox.UICheckBox;

public class UIToolBarUI extends MetalToolBarUI {
    public static final String IS_TOOL_BAR_BUTTON_KEY = "JToolBar.isToolbarButton";
    public static final int FLOATABLE_GRIP_SIZE = 8;

    /**
     * The Border used for buttons in a toolbar
     */
    private static Border toolButtonBorder = new UIToolButtonBorder();

    /**
     * 创建组件UI
     *
     * @param c 组件
     * @return 组件UI
     */
    public static ComponentUI createUI(JComponent c) {
        return new UIToolBarUI();
    }

    /**
     * 加载组件ui
     *
     * @param c 组件
     */
    public void installUI(JComponent c) {
        super.installUI(c);
        c.putClientProperty("JToolBar.isRollover", Boolean.TRUE);
    }

    /**
     * Overrides BasicToolBarUI.createFloatingWindow() to return a simple dialog
     * (which works with TinyLaF). Creates a window which contains the toolbar
     * after it has been dragged out from its container
     *
     * @return a <code>RootPaneContainer</code> object, containing the toolbar.
     */
    protected RootPaneContainer createFloatingWindow(JToolBar toolbar) {
        JDialog dialog;
        Window window = SwingUtilities.getWindowAncestor(toolbar);

        if (window instanceof Frame) {
            dialog = new JDialog((Frame) window, toolbar.getName(), false);
        } else if (window instanceof Dialog) {
            dialog = new JDialog((Dialog) window, toolbar.getName(), false);
        } else {
            dialog = new JDialog((Frame) null, toolbar.getName(), false);
        }

        dialog.setTitle(toolbar.getName());
        dialog.setResizable(false);
        WindowListener wl = createFrameListener();
        dialog.addWindowListener(wl);

        return dialog;
    }

    /**
     * Paints the given component.
     *
     * @param g The graphics context to use.
     * @param c The component to paint.
     */
    public void paint(Graphics g, JComponent c) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(UIConstants.NORMAL_BACKGROUND);
        g2.fillRect(0, 0, c.getWidth(), c.getHeight());
    }

    /**
     * Rewritten in 1.3. Now the border is defined through button margin.
     */
    protected void setBorderToRollover(Component c) {
        setBorderToNormal(c);
    }

    protected void setBorderToNormal(Component c) {
        if (!(c instanceof AbstractButton)) {
            return;
        }
        if (c instanceof UICheckBox) {
            return;
        }
        if (c instanceof JRadioButton) {
            return;
        }
        AbstractButton b = (AbstractButton) c;
        b.setRolloverEnabled(true);
        b.putClientProperty(IS_TOOL_BAR_BUTTON_KEY, Boolean.TRUE);

        if (!(b.getBorder() instanceof UIResource) && !(b.getBorder() instanceof UIToolButtonBorder)) {
            // user has installed her own border
            return;
        }

        b.setBorder(toolButtonBorder);
    }
}