// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.form.layout;

import java.awt.*;

// Referenced classes of package com.fr.design.form.layout:
//            FRFlowLayout

public class FRVerticalLayout extends FRFlowLayout
{

    public static final int TOP = 0;
    public static final int CENTER = 1;
    public static final int BOTTOM = 2;

    public FRVerticalLayout()
    {
        this(0, 0);
    }

    public FRVerticalLayout(int i)
    {
        this(i, 0, 0);
    }

    public FRVerticalLayout(int i, int j)
    {
        this(0, i, j);
    }

    public FRVerticalLayout(int i, int j, int k)
    {
        super(i, j, k);
    }

    protected void moveComponents(Container container, Insets insets, int i)
    {
        int j = container.getHeight() - i - insets.top - insets.bottom;
        int k = insets.top;
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
        int l = insets.left + hgap;
        for(int i1 = 0; i1 < container.getComponentCount(); i1++)
        {
            Component component = container.getComponent(i1);
            if(component.isVisible())
            {
                component.setLocation(l, k);
                k += component.getPreferredSize().height + vgap;
            }
        }

    }

    protected int reSizeComponents(Container container, Insets insets)
    {
        int i = container.getWidth() - insets.left - insets.right - 2 * hgap;
        int j = 0;
        for(int k = 0; k < container.getComponentCount(); k++)
        {
            Component component = container.getComponent(k);
            if(component.isVisible())
            {
                component.setSize(i, component.getPreferredSize().height);
                j += component.getPreferredSize().height + vgap;
            }
        }

        return j;
    }
}
