// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.designer.beans.adapters.layout;

import com.fr.design.designer.beans.ConstraintsGroupModel;
import com.fr.design.designer.creator.*;
import com.fr.design.designer.properties.BoundsGroupModel;
import com.fr.design.utils.ComponentUtils;
import com.fr.design.utils.gui.LayoutUtils;
import com.fr.form.ui.container.WAbsoluteLayout;
import java.awt.Rectangle;

// Referenced classes of package com.fr.design.designer.beans.adapters.layout:
//            AbstractLayoutAdapter

public class FRAbsoluteLayoutAdapter extends AbstractLayoutAdapter
{

    public FRAbsoluteLayoutAdapter(XLayoutContainer xlayoutcontainer)
    {
        super(xlayoutcontainer);
    }

    public boolean accept(XCreator xcreator, int i, int j)
    {
        return i >= 0 && j >= 0 && xcreator.getHeight() <= container.getHeight() && xcreator.getWidth() <= container.getWidth();
    }

    protected void addComp(XCreator xcreator, int i, int j)
    {
        if(XCreatorUtils.getParentXLayoutContainer(xcreator) != null)
        {
            Rectangle rectangle = ComponentUtils.getRelativeBounds(container);
            Rectangle rectangle1 = ComponentUtils.getRelativeBounds(xcreator);
            i = rectangle1.x - rectangle.x;
            j = rectangle1.y - rectangle.y;
        } else
        {
            int k = xcreator.getWidth() / 2;
            int l = xcreator.getHeight() / 2;
            i -= k;
            j -= l;
        }
        fix(xcreator, i, j);
        container.add(xcreator);
        LayoutUtils.layoutRootContainer(container);
    }

    public void fix(XCreator xcreator)
    {
        WAbsoluteLayout wabsolutelayout = (WAbsoluteLayout)container.toData();
        fix(xcreator, xcreator.getX(), xcreator.getY());
        wabsolutelayout.setBounds(xcreator.toData(), xcreator.getBounds());
    }

    public void fix(XCreator xcreator, int i, int j)
    {
        if(i < 0)
            i = 0;
        else
        if(i + xcreator.getWidth() > container.getWidth())
            i = container.getWidth() - xcreator.getWidth();
        if(j < 0)
            j = 0;
        else
        if(j + xcreator.getHeight() > container.getHeight())
            j = container.getHeight() - xcreator.getHeight();
        xcreator.setLocation(i, j);
    }

    public ConstraintsGroupModel getLayoutConstraints(XCreator xcreator)
    {
        return new BoundsGroupModel((XWAbsoluteLayout)container, xcreator);
    }
}
