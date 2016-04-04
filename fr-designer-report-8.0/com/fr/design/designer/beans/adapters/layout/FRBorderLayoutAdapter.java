// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.designer.beans.adapters.layout;

import com.fr.design.designer.beans.ConstraintsGroupModel;
import com.fr.design.designer.beans.HoverPainter;
import com.fr.design.designer.beans.painters.FRBorderLayoutPainter;
import com.fr.design.designer.creator.*;
import com.fr.design.designer.properties.FRBorderLayoutConstraints;
import com.fr.design.form.layout.FRBorderLayout;
import com.fr.design.utils.gui.LayoutUtils;
import com.fr.form.ui.container.WBorderLayout;
import com.fr.general.ComparatorUtils;
import java.awt.Component;
import java.awt.Dimension;

// Referenced classes of package com.fr.design.designer.beans.adapters.layout:
//            AbstractLayoutAdapter

public class FRBorderLayoutAdapter extends AbstractLayoutAdapter
{

    private HoverPainter painter;

    public FRBorderLayoutAdapter(XLayoutContainer xlayoutcontainer)
    {
        super(xlayoutcontainer);
        painter = new FRBorderLayoutPainter(xlayoutcontainer);
    }

    public HoverPainter getPainter()
    {
        return painter;
    }

    public void fix(XCreator xcreator)
    {
        FRBorderLayout frborderlayout = (FRBorderLayout)container.getFRLayout();
        Object obj = frborderlayout.getConstraints(xcreator);
        if(ComparatorUtils.equals(obj, "North"))
            ((XWBorderLayout)container).toData().setNorthSize(xcreator.getHeight());
        else
        if(ComparatorUtils.equals(obj, "South"))
            ((XWBorderLayout)container).toData().setSouthSize(xcreator.getHeight());
        else
        if(ComparatorUtils.equals(obj, "East"))
            ((XWBorderLayout)container).toData().setEastSize(xcreator.getWidth());
        else
        if(ComparatorUtils.equals(obj, "West"))
            ((XWBorderLayout)container).toData().setWestSize(xcreator.getWidth());
        else
            return;
        container.recalculateChildrenPreferredSize();
    }

    public void addComp(XCreator xcreator, int i, int j)
    {
        String s = getPlacement(xcreator, i, j);
        container.add(xcreator, s);
        LayoutUtils.layoutRootContainer(container);
    }

    public boolean accept(XCreator xcreator, int i, int j)
    {
        String s = getPlacement(xcreator, i, j);
        FRBorderLayout frborderlayout = (FRBorderLayout)container.getLayout();
        Component component = frborderlayout.getLayoutComponent(s);
        return component == null;
    }

    public Dimension getPreferredSize(XCreator xcreator)
    {
        int i = container.getWidth();
        int j = container.getHeight();
        Dimension dimension = xcreator.getSize();
        if(dimension.width > i / 3)
            dimension.width = i / 3;
        if(dimension.height > j / 3)
            dimension.height = j / 3;
        return dimension;
    }

    private String getPlacement(XCreator xcreator, int i, int j)
    {
        int k = container.getWidth();
        int l = container.getHeight();
        WBorderLayout wborderlayout = ((XWBorderLayout)container).toData();
        int i1 = wborderlayout.getNorthSize();
        int j1 = wborderlayout.getSouthSize();
        int k1 = wborderlayout.getEastSize();
        int l1 = wborderlayout.getWestSize();
        if(j < i1)
            return "North";
        if(j >= i1 && j < l - j1)
        {
            if(i < l1)
                return "West";
            if(i >= l1 && i < k - k1)
                return "Center";
            else
                return "East";
        } else
        {
            return "South";
        }
    }

    public void addNextComponent(XCreator xcreator)
    {
        FRBorderLayout frborderlayout = (FRBorderLayout)container.getLayout();
        Component component = frborderlayout.getLayoutComponent("North");
        Component component1 = frborderlayout.getLayoutComponent("South");
        Component component2 = frborderlayout.getLayoutComponent("West");
        Component component3 = frborderlayout.getLayoutComponent("East");
        Component component4 = frborderlayout.getLayoutComponent("Center");
        if(component == null)
            container.add(xcreator, "North");
        else
        if(component1 == null)
            container.add(xcreator, "South");
        else
        if(component2 == null)
            container.add(xcreator, "West");
        else
        if(component3 == null)
            container.add(xcreator, "East");
        else
        if(component4 == null)
            container.add(xcreator, "Center");
        LayoutUtils.layoutRootContainer(container);
    }

    public void addBefore(XCreator xcreator, XCreator xcreator1)
    {
        addNextComponent(xcreator1);
    }

    public void addAfter(XCreator xcreator, XCreator xcreator1)
    {
        addNextComponent(xcreator1);
    }

    public boolean canAcceptMoreComponent()
    {
        FRBorderLayout frborderlayout = (FRBorderLayout)container.getLayout();
        Component component = frborderlayout.getLayoutComponent("North");
        Component component1 = frborderlayout.getLayoutComponent("South");
        Component component2 = frborderlayout.getLayoutComponent("West");
        Component component3 = frborderlayout.getLayoutComponent("East");
        Component component4 = frborderlayout.getLayoutComponent("Center");
        return component == null || component1 == null || component2 == null || component3 == null || component4 == null;
    }

    public ConstraintsGroupModel getLayoutConstraints(XCreator xcreator)
    {
        return new FRBorderLayoutConstraints(container, xcreator);
    }
}
