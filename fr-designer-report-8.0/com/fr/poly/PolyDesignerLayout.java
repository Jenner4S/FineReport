// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.poly;

import com.fr.general.ComparatorUtils;
import java.awt.*;
import java.io.Serializable;

public class PolyDesignerLayout
    implements LayoutManager, Serializable
{

    public static final String Center = "Center";
    public static final String Vertical = "Vertical";
    public static final String HRuler = "HRuler";
    public static final String VRuler = "VRuler";
    private Component designer;
    private Component verScrollBar;
    private Component hRuler;
    private Component vRuler;

    public PolyDesignerLayout()
    {
    }

    public void addLayoutComponent(String s, Component component)
    {
        if("Center".equals(s))
            designer = component;
        else
        if("Vertical".equals(s))
            verScrollBar = component;
        else
        if("HRuler".equals(s))
            hRuler = component;
        else
        if("VRuler".equals(s))
            vRuler = component;
    }

    public void removeLayoutComponent(Component component)
    {
        if(ComparatorUtils.equals(component, designer))
            designer = null;
        else
        if(ComparatorUtils.equals(component, verScrollBar))
            verScrollBar = null;
        else
        if(ComparatorUtils.equals(component, hRuler))
            hRuler = null;
        else
        if(ComparatorUtils.equals(component, vRuler))
            vRuler = null;
    }

    public Dimension preferredLayoutSize(Container container)
    {
        Object obj = container.getTreeLock();
        JVM INSTR monitorenter ;
        Dimension dimension = new Dimension();
        Dimension dimension1 = designer.getPreferredSize();
        dimension.width += dimension1.width;
        dimension.height += dimension1.height;
        Dimension dimension2 = verScrollBar.getPreferredSize();
        dimension.width += dimension2.width;
        Insets insets = container.getInsets();
        dimension.width += insets.left + insets.right;
        dimension.height += insets.top + insets.bottom;
        return dimension;
        Exception exception;
        exception;
        throw exception;
    }

    public Dimension minimumLayoutSize(Container container)
    {
        Object obj = container.getTreeLock();
        JVM INSTR monitorenter ;
        Dimension dimension = new Dimension();
        Dimension dimension1 = designer.getMinimumSize();
        dimension.width += dimension1.width;
        dimension.height += dimension1.height;
        Dimension dimension2 = verScrollBar.getMinimumSize();
        dimension.width += dimension2.width;
        Insets insets = container.getInsets();
        dimension.width += insets.left + insets.right;
        dimension.height += insets.top + insets.bottom;
        return dimension;
        Exception exception;
        exception;
        throw exception;
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
            Dimension dimension = hRuler != null ? hRuler.getPreferredSize() : new Dimension();
            Dimension dimension1 = vRuler != null ? vRuler.getPreferredSize() : new Dimension();
            Dimension dimension2 = verScrollBar.getPreferredSize();
            if(hRuler != null)
                hRuler.setBounds((k + dimension1.width) - 1, i, l - dimension1.width, dimension.height);
            if(vRuler != null)
                vRuler.setBounds(k, (i + dimension1.height) - 1, dimension1.width, j - dimension.height);
            verScrollBar.setBounds(l - dimension1.width, i + dimension1.height, dimension2.width, j - dimension.height - 16);
            designer.setBounds(k + dimension1.width, i + dimension1.height, l - dimension2.width - dimension1.width, j);
        }
    }
}
