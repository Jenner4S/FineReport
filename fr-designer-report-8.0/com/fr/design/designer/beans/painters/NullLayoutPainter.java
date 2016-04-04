// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.designer.beans.painters;

import com.fr.design.designer.beans.HoverPainter;
import com.fr.design.designer.beans.LayoutAdapter;
import com.fr.design.designer.creator.XLayoutContainer;
import java.awt.*;

// Referenced classes of package com.fr.design.designer.beans.painters:
//            AbstractPainter

public class NullLayoutPainter extends AbstractPainter
{

    public NullLayoutPainter(XLayoutContainer xlayoutcontainer)
    {
        super(xlayoutcontainer);
    }

    public void paint(Graphics g, int i, int j)
    {
        HoverPainter hoverpainter = container.getLayoutAdapter().getPainter();
        if(hoverpainter != null)
        {
            hoverpainter.setCreator(creator);
            hoverpainter.setHotspot(hotspot);
            hoverpainter.setRenderingBounds(hotspot_bounds);
            hoverpainter.paint(g, i, j);
        } else
        {
            g.setColor(Color.lightGray);
            g.drawRect(hotspot_bounds.x, hotspot_bounds.y, hotspot_bounds.width, hotspot_bounds.height);
        }
    }
}
