// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe;

import java.awt.*;
import java.io.Serializable;

public class RGridLayout
    implements LayoutManager, Serializable
{

    public static final String GridCorner = "GridCorner";
    public static final String GridColumn = "GridColumn";
    public static final String GridRow = "GridRow";
    public static final String Grid = "Grid";
    public static final String HorizontalBar = "HorizontalBar";
    public static final String VerticalBar = "VerticalBar";
    protected Component gridCorner;
    protected Component gridColumn;
    protected Component gridRow;
    protected Component grid;
    protected Component horizontalBar;
    protected Component verticalBar;

    public RGridLayout()
    {
    }

    public void addLayoutComponent(String s, Component component)
    {
        if("GridCorner".equals(s))
            gridCorner = component;
        else
        if("GridColumn".equals(s))
            gridColumn = component;
        else
        if("GridRow".equals(s))
            gridRow = component;
        else
        if("Grid".equals(s))
            grid = component;
        else
        if("HorizontalBar".equals(s))
            horizontalBar = component;
        else
        if("VerticalBar".equals(s))
            verticalBar = component;
    }

    public void removeLayoutComponent(Component component)
    {
    }

    public Dimension minimumLayoutSize(Container container)
    {
        Object obj = container.getTreeLock();
        JVM INSTR monitorenter ;
        Dimension dimension = new Dimension(0, 0);
        Dimension dimension1 = gridRow.getMaximumSize();
        Dimension dimension2 = gridColumn.getMaximumSize();
        Dimension dimension3 = grid.getMaximumSize();
        Dimension dimension4 = verticalBar.getMaximumSize();
        Dimension dimension5 = horizontalBar.getMaximumSize();
        dimension.width += dimension1.width + dimension3.width + dimension4.width;
        dimension.height = dimension2.height + dimension3.height + dimension5.height;
        Insets insets = container.getInsets();
        dimension.width += insets.left + insets.right;
        dimension.height += insets.top + insets.bottom;
        return dimension;
        Exception exception;
        exception;
        throw exception;
    }

    public Dimension preferredLayoutSize(Container container)
    {
        Object obj = container.getTreeLock();
        JVM INSTR monitorenter ;
        Dimension dimension = new Dimension(0, 0);
        Dimension dimension1 = gridRow.getPreferredSize();
        Dimension dimension2 = gridColumn.getPreferredSize();
        Dimension dimension3 = grid.getPreferredSize();
        Dimension dimension4 = verticalBar.getPreferredSize();
        Dimension dimension5 = horizontalBar.getPreferredSize();
        dimension.width += dimension1.width + dimension3.width + dimension4.width;
        dimension.height = dimension2.height + dimension3.height + dimension5.height;
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
            Dimension dimension = gridRow.getPreferredSize();
            Dimension dimension1 = gridColumn.getPreferredSize();
            Dimension dimension2 = verticalBar.getPreferredSize();
            gridCorner.setBounds(k, i, dimension.width, dimension1.height);
            verticalBar.setBounds(l - dimension2.width, 0, dimension2.width, j);
            gridColumn.setBounds(k + dimension.width, i, l - dimension.width - dimension2.width, dimension1.height);
            gridRow.setBounds(k, i + dimension1.height, dimension.width, j - dimension1.height);
            grid.setBounds(k + dimension.width, i + dimension1.height, l - dimension.width - dimension2.width, j - dimension1.height);
        }
    }

    public String toString()
    {
        return getClass().getName();
    }
}
