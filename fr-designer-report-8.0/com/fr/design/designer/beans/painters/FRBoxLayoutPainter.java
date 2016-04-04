// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.designer.beans.painters;

import com.fr.design.designer.creator.XLayoutContainer;
import com.fr.design.form.util.XCreatorConstants;
import java.awt.*;

// Referenced classes of package com.fr.design.designer.beans.painters:
//            AbstractPainter

public abstract class FRBoxLayoutPainter extends AbstractPainter
{

    public FRBoxLayoutPainter(XLayoutContainer xlayoutcontainer)
    {
        super(xlayoutcontainer);
    }

    protected int[] calculateAddPosition(int i, int j)
    {
        int ai[] = new int[2];
        int k = container.getComponentCount();
        for(int l = 0; l < k; l++)
        {
            Component component = container.getComponent(l);
            if(l == 0)
            {
                ai[0] = component.getBounds().x;
                ai[1] = component.getBounds().y;
            }
            Component component1 = null;
            if(l < k - 1)
                component1 = container.getComponent(l + 1);
            if(component1 != null)
            {
                if(i > component.getBounds().x && i < component1.getBounds().x)
                    ai[0] = component.getBounds().x + component.getBounds().width;
                else
                if(i <= component.getBounds().x && ai[0] > component.getBounds().x)
                    ai[0] = component.getBounds().x;
                if(j > component.getBounds().y && j < component1.getBounds().y)
                {
                    ai[1] = component.getBounds().y + component.getSize().height;
                    continue;
                }
                if(j <= component.getBounds().y && ai[1] > component.getBounds().y)
                    ai[1] = component.getBounds().y;
                continue;
            }
            if(i > component.getBounds().x)
                ai[0] = component.getBounds().x + component.getBounds().width;
            if(j > component.getBounds().y)
                ai[1] = component.getBounds().y + component.getSize().height;
        }

        return ai;
    }

    protected void drawHotLine(Graphics g, int i, int j, int k, int l, int i1, int j1)
    {
        Graphics2D graphics2d = (Graphics2D)g;
        graphics2d.setPaint(XCreatorConstants.LAYOUT_HOTSPOT_COLOR);
        graphics2d.setStroke(XCreatorConstants.STROKE);
        graphics2d.drawLine(k - i, l - j, i1 - j, j1 - j);
    }
}
