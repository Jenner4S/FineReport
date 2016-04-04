// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.form.layout;

import java.awt.*;
import java.io.Serializable;

// Referenced classes of package com.fr.design.form.layout:
//            FRLayoutManager

public class FRFormLayout
    implements FRLayoutManager, Serializable
{

    public FRFormLayout()
    {
    }

    public boolean isResizable()
    {
        return true;
    }

    public void addLayoutComponent(String s, Component component)
    {
    }

    public void removeLayoutComponent(Component component)
    {
    }

    public Dimension preferredLayoutSize(Container container)
    {
        return container.getPreferredSize();
    }

    public Dimension minimumLayoutSize(Container container)
    {
        return container.getMinimumSize();
    }

    public void layoutContainer(Container container)
    {
    }
}
