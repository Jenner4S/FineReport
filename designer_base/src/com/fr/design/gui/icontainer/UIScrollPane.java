package com.fr.design.gui.icontainer;

import com.fr.design.constants.UIConstants;
import com.fr.design.gui.iscrollbar.UIScrollBar;

import javax.swing.*;
import java.awt.*;

/**
 * @author zhou
 * @since 2012-5-9����4:39:33
 */
public class UIScrollPane extends JScrollPane {

    private static final long serialVersionUID = 1L;

    public UIScrollPane(Component c) {
        super(c, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        this.setHorizontalScrollBar(createHorizontalScrollBar());
        this.getVerticalScrollBar().setUnitIncrement(30);
        this.getVerticalScrollBar().setBlockIncrement(30);
    }

    @Override
    /**
     * ����ˮƽ������
     */
    public JScrollBar createHorizontalScrollBar() {
        UIScrollBar sbr = new UIScrollBar(JScrollBar.HORIZONTAL);
        sbr.setBackground(UIConstants.NORMAL_BACKGROUND);
        return sbr;
    }

    @Override
    /**
     * ���ɴ�ֱ������
     */
    public JScrollBar createVerticalScrollBar() {
        UIScrollBar sbr = new UIScrollBar(JScrollBar.VERTICAL);
        sbr.setBackground(UIConstants.NORMAL_BACKGROUND);
        return sbr;
    }

}
