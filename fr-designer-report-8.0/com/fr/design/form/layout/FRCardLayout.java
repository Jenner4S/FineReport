// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.form.layout;

import com.fr.design.designer.creator.XLayoutContainer;
import com.fr.form.ui.container.WLayout;
import com.fr.general.ComparatorUtils;
import java.awt.CardLayout;
import java.awt.Container;

// Referenced classes of package com.fr.design.form.layout:
//            FRLayoutManager

public class FRCardLayout extends CardLayout
    implements FRLayoutManager
{

    public FRCardLayout()
    {
    }

    public FRCardLayout(int i, int j)
    {
        super(i, j);
    }

    public boolean isResizable()
    {
        return false;
    }

    public void show(Container container, String s)
    {
        synchronized(container.getTreeLock())
        {
            int i = container.getComponentCount();
            for(int j = 0; j < i; j++)
            {
                XLayoutContainer xlayoutcontainer = (XLayoutContainer)container.getComponent(j);
                WLayout wlayout = xlayoutcontainer.toData();
                if(ComparatorUtils.equals(wlayout.getWidgetName(), s))
                    xlayoutcontainer.setVisible(true);
                else
                    xlayoutcontainer.setVisible(false);
            }

        }
    }
}
