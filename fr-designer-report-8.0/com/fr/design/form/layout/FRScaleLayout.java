// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.form.layout;

import com.fr.form.ui.container.WFitLayout;
import com.fr.form.ui.container.WLayout;
import java.awt.*;

// Referenced classes of package com.fr.design.form.layout:
//            FRLayoutManager

public class FRScaleLayout
    implements FRLayoutManager
{

    public FRScaleLayout()
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
        return new Dimension(WLayout.MIN_WIDTH, WFitLayout.MIN_HEIGHT);
    }

    public Dimension minimumLayoutSize(Container container)
    {
        return new Dimension(WLayout.MIN_WIDTH, WFitLayout.MIN_HEIGHT);
    }

    public void layoutContainer(Container container)
    {
label0:
        {
            synchronized(container.getTreeLock())
            {
                if(container.getComponentCount() >= 1)
                    break label0;
            }
            return;
        }
        Component component = container.getComponent(0);
        if(component != null)
        {
            Rectangle rectangle = container.getBounds();
            component.setBounds(0, 0, rectangle.width, component.getHeight());
        }
        obj;
        JVM INSTR monitorexit ;
          goto _L1
        exception;
        throw exception;
_L1:
    }

    public boolean isResizable()
    {
        return true;
    }
}
