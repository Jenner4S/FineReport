// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.form.layout;

import java.awt.*;

// Referenced classes of package com.fr.design.form.layout:
//            FRLayoutManager

public class FRAbsoluteLayout
    implements FRLayoutManager
{

    public FRAbsoluteLayout()
    {
    }

    public void addLayoutComponent(String s, Component component)
    {
    }

    public void removeLayoutComponent(Component component)
    {
    }

    public Dimension preferredLayoutSize(Container container)
    {
        return new Dimension(10, 10);
    }

    public Dimension minimumLayoutSize(Container container)
    {
        return new Dimension(10, 10);
    }

    public void layoutContainer(Container container)
    {
    }

    public boolean isResizable()
    {
        return true;
    }
}
