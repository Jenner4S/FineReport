// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.poly;

import com.fr.design.mainframe.RGridLayout;
import java.awt.*;

public class PolyEditorLayout extends RGridLayout
{

    public static final String BottomCorner = "BottomCorner";
    protected Component bottomCorner;

    public PolyEditorLayout()
    {
    }

    public void addLayoutComponent(String s, Component component)
    {
        super.addLayoutComponent(s, component);
        if("BottomCorner".equals(s))
            bottomCorner = component;
    }

    public void layoutContainer(Container container)
    {
        synchronized(container.getTreeLock())
        {
            Insets insets = container.getInsets();
            int i = insets.top;
            int j = container.getHeight() - insets.bottom;
            int k = insets.left;
            int l = container.getWidth() - insets.right;
            Dimension dimension = gridRow.getPreferredSize();
            Dimension dimension1 = gridColumn.getPreferredSize();
            int i1 = dimension.width;
            int j1 = dimension1.height;
            gridCorner.setBounds(k, i, i1, j1);
            byte byte0 = 15;
            byte byte1 = 15;
            verticalBar.setBounds(l - byte0, 0, byte0, j1);
            horizontalBar.setBounds(0, j - byte1, i1, byte1);
            gridColumn.setBounds(k + dimension.width, i, l - dimension.width - byte0, dimension1.height);
            gridRow.setBounds(k, i + dimension1.height, dimension.width, j - dimension1.height - byte1);
            byte byte2 = 30;
            byte byte3 = 30;
            if(bottomCorner != null)
                bottomCorner.setBounds(l - byte2, j - byte3, byte2, byte3);
            grid.setBounds(k + dimension.width, i + dimension1.height, l - dimension.width - byte0, j - dimension1.height - byte1);
        }
    }
}
