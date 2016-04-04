// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.designer.beans.painters;

import com.fr.design.designer.beans.adapters.layout.FRBorderLayoutAdapter;
import com.fr.design.designer.creator.XLayoutContainer;
import com.fr.design.designer.creator.XWBorderLayout;
import com.fr.design.form.layout.FRBorderLayout;
import com.fr.form.ui.container.WBorderLayout;
import java.awt.*;

// Referenced classes of package com.fr.design.designer.beans.painters:
//            AbstractPainter

public class FRBorderLayoutPainter extends AbstractPainter
{

    public FRBorderLayoutPainter(XLayoutContainer xlayoutcontainer)
    {
        super(xlayoutcontainer);
    }

    public void paint(Graphics g, int i, int j)
    {
        super.paint(g, i, j);
        int k = hotspot.x;
        int l = hotspot.y;
        int i1 = hotspot_bounds.x;
        int j1 = hotspot_bounds.y;
        int k1 = hotspot_bounds.width;
        int l1 = hotspot_bounds.height;
        FRBorderLayoutAdapter frborderlayoutadapter = (FRBorderLayoutAdapter)container.getLayoutAdapter();
        FRBorderLayout frborderlayout = (FRBorderLayout)container.getLayout();
        boolean flag = frborderlayoutadapter.accept(creator, k - i1, l - j1);
        WBorderLayout wborderlayout = ((XWBorderLayout)container).toData();
        int i2 = wborderlayout.getNorthSize();
        int j2 = wborderlayout.getSouthSize();
        int k2 = wborderlayout.getEastSize();
        int l2 = wborderlayout.getWestSize();
        int i3 = i1;
        int j3 = j1;
        int k3 = k1;
        int l3 = l1;
        if(l < j1 + i2)
        {
            i3 = i1;
            j3 = j1;
            k3 = k1;
            l3 = i2;
        } else
        if(l >= j1 + i2 && l < (j1 + l1) - j2)
        {
            if(k < i1 + l2)
            {
                Component component = frborderlayout.getLayoutComponent("North");
                Component component3 = frborderlayout.getLayoutComponent("South");
                i3 = i1;
                j3 = j1;
                if(component != null)
                    j3 += i2;
                k3 = l2;
                l3 = l1;
                if(component != null)
                    l3 -= i2;
                if(component3 != null)
                    l3 -= j2;
            } else
            if(k >= i1 + l2 && k < (i1 + k1) - k2)
            {
                Component component1 = frborderlayout.getLayoutComponent("North");
                Component component4 = frborderlayout.getLayoutComponent("South");
                Component component6 = frborderlayout.getLayoutComponent("East");
                Component component7 = frborderlayout.getLayoutComponent("West");
                i3 = i1;
                if(component7 != null)
                    i3 += l2;
                j3 = j1;
                if(component1 != null)
                    j3 += i2;
                k3 = k1;
                if(component7 != null)
                    k3 -= l2;
                if(component6 != null)
                    k3 -= k2;
                l3 = l1;
                if(component1 != null)
                    l3 -= i2;
                if(component4 != null)
                    l3 -= j2;
            } else
            {
                Component component2 = frborderlayout.getLayoutComponent("North");
                Component component5 = frborderlayout.getLayoutComponent("South");
                i3 = (i1 + k1) - k2;
                j3 = j1;
                if(component2 != null)
                    j3 += i2;
                k3 = k2;
                l3 = l1;
                if(component2 != null)
                    l3 -= i2;
                if(component5 != null)
                    l3 -= j2;
            }
        } else
        {
            i3 = i1;
            j3 = (j1 + l1) - j2;
            k3 = k1;
            l3 = j2;
        }
        drawHotspot(g, i3, j3, k3, l3, flag);
    }
}
