package com.fr.design.gui.isplitpanedivider;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.metal.MetalSplitPaneUI;

/**
 * Created by IntelliJ IDEA.
 * Author : daisy
 * Version: 6.5.6
 * Date: 14-2-12
 * Time: ����4:59
 */
public class UISplitPaneUI extends MetalSplitPaneUI {

    /**
     * ����UI
     * @param x ���
     * @return �������UI
     */
    public static ComponentUI createUI(JComponent x) {
   		return new UISplitPaneUI();
   	}

    /**
     * ����Divider
     * @return ����Ĭ�ϵ�divider
     */
   	public BasicSplitPaneDivider createDefaultDivider() {
   		return new UISplitPaneDivider(this);
   	}
}
