// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.poly.hanlder;

import com.fr.base.BaseUtils;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.general.Inter;
import javax.swing.JWindow;

public class BlockForbiddenWindow extends JWindow
{

    private static final int WIDTH = 150;
    private static final int HEIGHT = 20;
    private UIButton promptButton;

    public BlockForbiddenWindow()
    {
        promptButton = new UIButton(Inter.getLocText("FR-Designer_Block-intersect"), BaseUtils.readIcon("/com/fr/web/images/form/forbid.png"));
        add(promptButton);
        setSize(150, 20);
    }

    public void showWindow(int i, int j)
    {
        setLocation(i - 75, j - 10);
        setVisible(true);
    }

    public void hideWindow()
    {
        setVisible(false);
    }
}
