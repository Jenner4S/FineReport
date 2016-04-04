// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.form.layout;

import com.fr.form.ui.container.WLayout;
import com.fr.general.ComparatorUtils;
import java.awt.*;

// Referenced classes of package com.fr.design.form.layout:
//            FRLayoutManager

public class FRTitleLayout
    implements FRLayoutManager, LayoutManager
{

    public static final String TITLE = "Title";
    public static final String BODY = "Body";
    private Component title;
    private Component body;
    private int gap;

    public FRTitleLayout()
    {
        this(0);
    }

    public FRTitleLayout(int i)
    {
        gap = i;
    }

    public int getGap()
    {
        return gap;
    }

    public void setGap(int i)
    {
        gap = i;
    }

    public void addLayoutComponent(String s, Component component)
    {
        synchronized(component.getTreeLock())
        {
            if(ComparatorUtils.equals(s, null))
                s = "Body";
            if(ComparatorUtils.equals(s, "Body"))
                body = component;
            else
            if(ComparatorUtils.equals(s, "Title"))
                title = component;
        }
    }

    public void removeLayoutComponent(Component component)
    {
        synchronized(component.getTreeLock())
        {
            if(component == title)
                title = null;
        }
    }

    public Dimension preferredLayoutSize(Container container)
    {
        return new Dimension(WLayout.MIN_WIDTH, WLayout.MIN_HEIGHT * 2);
    }

    public Dimension minimumLayoutSize(Container container)
    {
        return new Dimension(WLayout.MIN_WIDTH, title != null ? WLayout.MIN_HEIGHT + 36 : WLayout.MIN_HEIGHT);
    }

    public void layoutContainer(Container container)
    {
        synchronized(container.getTreeLock())
        {
            int i = container.getWidth();
            int j = container.getHeight();
            byte byte0 = ((byte)(title != null ? 36 : 0));
            for(int k = 0; k < container.getComponentCount(); k++)
            {
                Component component = container.getComponent(k);
                if(component == title)
                {
                    component.setBounds(0, 0, i, 36);
                    continue;
                }
                if(component == body)
                {
                    int l = byte0 + gap;
                    component.setBounds(0, l, i, j - l);
                }
            }

        }
    }

    public Object getConstraints(Component component)
    {
        if(component == null)
            return null;
        if(component == title)
            return "Title";
        if(component == body)
            return "Body";
        else
            return null;
    }

    public boolean isResizable()
    {
        return false;
    }
}
