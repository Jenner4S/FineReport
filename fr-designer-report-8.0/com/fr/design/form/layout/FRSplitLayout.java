// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.form.layout;

import java.awt.*;
import java.io.Serializable;

// Referenced classes of package com.fr.design.form.layout:
//            FRLayoutManager

public abstract class FRSplitLayout
    implements FRLayoutManager, LayoutManager2, Serializable
{

    public static final String CENTER = "center";
    public static final String ASIDE = "aside";
    protected double ratio;
    protected int hgap;
    protected int vgap;
    protected Component center;
    protected Component aside;

    public FRSplitLayout()
    {
        this(0.5D);
    }

    public FRSplitLayout(double d)
    {
        this(d, 0, 0);
    }

    public FRSplitLayout(double d, int i, int j)
    {
        ratio = d;
        hgap = i;
        vgap = j;
    }

    public double getRatio()
    {
        return ratio;
    }

    public void setRatio(double d)
    {
        if(d < 0.0D || d > 1.0D)
        {
            throw new IllegalArgumentException("Ratio must be in [0, 1]");
        } else
        {
            ratio = d;
            return;
        }
    }

    public int getHgap()
    {
        return hgap;
    }

    public void setHgap(int i)
    {
        hgap = i;
    }

    public int getVgap()
    {
        return vgap;
    }

    public void setVgap(int i)
    {
        vgap = i;
    }

    public Object getConstraints(Component component)
    {
        if(component == center)
            return "center";
        if(component == aside)
            return "aside";
        else
            return null;
    }

    public void addLayoutComponent(Component component, Object obj)
    {
        synchronized(component.getTreeLock())
        {
            if(obj == null || (obj instanceof String))
                addLayoutComponent((String)obj, component);
        }
    }

    public void addLayoutComponent(String s, Component component)
    {
        synchronized(component.getTreeLock())
        {
            if(s == null)
                s = "center";
            if("center".equals(s))
                center = component;
            else
            if("aside".equals(s))
                aside = component;
        }
    }

    public void removeLayoutComponent(Component component)
    {
        synchronized(component.getTreeLock())
        {
            if(component == center)
                center = null;
            else
            if(component == aside)
                aside = null;
        }
    }

    public Component getLayoutComponent(Object obj)
    {
        if("center".equals(obj))
            return center;
        if("aside".equals(obj))
            return aside;
        else
            throw new IllegalArgumentException((new StringBuilder()).append("error ").append(obj).append("!").toString());
    }

    public boolean isResizable()
    {
        return false;
    }

    public Dimension preferredLayoutSize(Container container)
    {
        Object obj = container.getTreeLock();
        JVM INSTR monitorenter ;
        Dimension dimension = null;
        if(aside != null)
            dimension = aside.getPreferredSize();
        Dimension dimension1 = null;
        if(center != null)
            dimension1 = center.getPreferredSize();
        return calculateAppropriateSize(container, dimension, dimension1);
        Exception exception;
        exception;
        throw exception;
    }

    public Dimension minimumLayoutSize(Container container)
    {
        Object obj = container.getTreeLock();
        JVM INSTR monitorenter ;
        Dimension dimension = null;
        if(aside != null)
            dimension = aside.getMinimumSize();
        Dimension dimension1 = null;
        if(center != null)
            dimension1 = center.getMinimumSize();
        return calculateAppropriateSize(container, dimension, dimension1);
        Exception exception;
        exception;
        throw exception;
    }

    public Dimension maximumLayoutSize(Container container)
    {
        Object obj = container.getTreeLock();
        JVM INSTR monitorenter ;
        Dimension dimension = null;
        if(aside != null)
            dimension = aside.getMaximumSize();
        Dimension dimension1 = null;
        if(center != null)
            dimension1 = center.getMaximumSize();
        return calculateAppropriateSize(container, dimension, dimension1);
        Exception exception;
        exception;
        throw exception;
    }

    protected abstract Dimension calculateAppropriateSize(Container container, Dimension dimension, Dimension dimension1);

    public float getLayoutAlignmentX(Container container)
    {
        return 0.5F;
    }

    public float getLayoutAlignmentY(Container container)
    {
        return 0.5F;
    }

    public void invalidateLayout(Container container)
    {
    }
}
