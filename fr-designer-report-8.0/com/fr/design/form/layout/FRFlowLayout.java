// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.form.layout;

import java.awt.*;
import java.io.Serializable;

// Referenced classes of package com.fr.design.form.layout:
//            FRLayoutManager

public abstract class FRFlowLayout
    implements LayoutManager, FRLayoutManager, Serializable
{

    protected int hgap;
    protected int vgap;
    protected int alignment;

    public FRFlowLayout()
    {
        this(0, 0);
    }

    public FRFlowLayout(int i, int j)
    {
        hgap = i;
        vgap = j;
    }

    public FRFlowLayout(int i, int j, int k)
    {
        hgap = j;
        vgap = k;
        setAlignment(i);
    }

    public void setHgap(int i)
    {
        hgap = i;
    }

    public void setVgap(int i)
    {
        vgap = i;
    }

    public void setAlignment(int i)
    {
        alignment = i;
    }

    public void layoutContainer(Container container)
    {
        synchronized(container.getTreeLock())
        {
            Insets insets = container.getInsets();
            moveComponents(container, insets, reSizeComponents(container, insets));
        }
    }

    protected abstract int reSizeComponents(Container container, Insets insets);

    protected abstract void moveComponents(Container container, Insets insets, int i);

    public void addLayoutComponent(String s, Component component)
    {
    }

    public Dimension minimumLayoutSize(Container container)
    {
        return null;
    }

    public Dimension preferredLayoutSize(Container container)
    {
        return container.getPreferredSize();
    }

    public void removeLayoutComponent(Component component)
    {
    }

    public boolean isResizable()
    {
        return false;
    }
}
