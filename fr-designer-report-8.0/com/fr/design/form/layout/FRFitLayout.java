// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.form.layout;

import com.fr.form.ui.container.WLayout;
import java.awt.*;

// Referenced classes of package com.fr.design.form.layout:
//            FRLayoutManager

public class FRFitLayout
    implements FRLayoutManager, LayoutManager
{

    private int interval;
    private Insets lastInset;

    public FRFitLayout()
    {
        this(0);
    }

    public FRFitLayout(int i)
    {
        interval = i;
    }

    public void setInterVal(int i)
    {
        interval = i;
    }

    public void addLayoutComponent(String s, Component component)
    {
    }

    public void removeLayoutComponent(Component component)
    {
    }

    public Dimension preferredLayoutSize(Container container)
    {
        return new Dimension(960, 540);
    }

    public Dimension minimumLayoutSize(Container container)
    {
        return new Dimension(960, WLayout.MIN_HEIGHT);
    }

    public void layoutContainer(Container container)
    {
        Insets insets;
        int i;
        int j;
label0:
        {
            synchronized(container.getTreeLock())
            {
                insets = container.getInsets();
                i = container.getWidth();
                j = container.getHeight();
                calculateLastInset(container, i, j);
                if(!insetNotChange(insets, lastInset))
                    break label0;
            }
            return;
        }
        int k = 0;
        for(int l = container.getComponentCount(); k < l; k++)
        {
            Component component = container.getComponent(k);
            Rectangle rectangle = component.getBounds();
            Rectangle rectangle1 = new Rectangle(rectangle);
            if(rectangle.x == lastInset.left)
            {
                rectangle1.x += insets.left - lastInset.left;
                rectangle1.width -= insets.left - lastInset.left;
            }
            if(rectangle.y == lastInset.top)
            {
                rectangle1.y += insets.top - lastInset.top;
                rectangle1.height -= insets.top - lastInset.top;
            }
            if(rectangle.x + rectangle.width == i - lastInset.right)
                rectangle1.width -= insets.right - lastInset.right;
            if(rectangle.y + rectangle.height == j - lastInset.bottom)
                rectangle1.height -= insets.bottom - lastInset.bottom;
            component.setBounds(rectangle1);
        }

        obj;
        JVM INSTR monitorexit ;
          goto _L1
        exception;
        throw exception;
_L1:
    }

    private void calculateLastInset(Container container, int i, int j)
    {
        int k = container.getComponentCount();
        if(k == 0)
        {
            lastInset = new Insets(0, 0, 0, 0);
            return;
        }
        Point point = container.getComponent(0).getLocation();
        lastInset = new Insets(point.y, point.x, container.getHeight(), container.getWidth());
        for(int l = 0; l < k; l++)
        {
            Component component = container.getComponent(l);
            Rectangle rectangle = component.getBounds();
            lastInset.left = Math.min(rectangle.x, lastInset.left);
            lastInset.right = Math.min(i - rectangle.x - rectangle.width, lastInset.right);
            lastInset.top = Math.min(rectangle.y, lastInset.top);
            lastInset.bottom = Math.min(j - rectangle.y - rectangle.height, lastInset.bottom);
        }

    }

    private boolean insetNotChange(Insets insets, Insets insets1)
    {
        return insets.left == insets1.left && insets.right == insets1.right && insets.top == insets1.top && insets.bottom == insets1.bottom;
    }

    public boolean isResizable()
    {
        return true;
    }
}
