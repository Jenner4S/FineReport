// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.form.layout;

import java.awt.*;

// Referenced classes of package com.fr.design.form.layout:
//            FRFlowLayout

public class FRHorizontalLayout extends FRFlowLayout
{

    public static final int LEFT = 0;
    public static final int CENTER = 1;
    public static final int RIGHT = 2;

    public FRHorizontalLayout()
    {
        this(1);
    }

    public FRHorizontalLayout(int i)
    {
        super(0, 0);
        setAlignment(i);
    }

    public FRHorizontalLayout(int i, int j, int k)
    {
        super(i, j, k);
    }

    protected void moveComponents(Container container, Insets insets, int i)
    {
        int j = container.getWidth() - i - insets.left - insets.right;
        int k = insets.left;
        switch(alignment)
        {
        case 0: // '\0'
            k += 0;
            break;

        case 1: // '\001'
            k += j / 2;
            break;

        case 2: // '\002'
            k += j;
            break;
        }
        int l = insets.top + vgap;
        for(int i1 = 0; i1 < container.getComponentCount(); i1++)
        {
            Component component = container.getComponent(i1);
            if(component.isVisible())
            {
                component.setLocation(k, l);
                k += component.getPreferredSize().width + hgap;
            }
        }

    }

    protected int reSizeComponents(Container container, Insets insets)
    {
        int i = container.getHeight() - insets.top - insets.bottom - 2 * vgap;
        int j = 0;
        for(int k = 0; k < container.getComponentCount(); k++)
        {
            Component component = container.getComponent(k);
            if(component.isVisible())
            {
                component.setSize(component.getPreferredSize().width, i);
                j += component.getPreferredSize().width + hgap;
            }
        }

        return j;
    }
}
