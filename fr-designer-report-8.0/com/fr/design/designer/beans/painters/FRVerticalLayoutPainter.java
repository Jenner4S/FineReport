// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.designer.beans.painters;

import com.fr.design.designer.creator.XLayoutContainer;
import com.fr.design.utils.ComponentUtils;
import java.awt.*;

// Referenced classes of package com.fr.design.designer.beans.painters:
//            FRBoxLayoutPainter

public class FRVerticalLayoutPainter extends FRBoxLayoutPainter
{

    public FRVerticalLayoutPainter(XLayoutContainer xlayoutcontainer)
    {
        super(xlayoutcontainer);
    }

    public void paint(Graphics g, int i, int j)
    {
        super.paint(g, i, j);
        int k = hotspot.y;
        Rectangle rectangle = ComponentUtils.getRelativeBounds(container);
        int l = rectangle.y;
        int i1 = rectangle.x;
        int ai[] = calculateAddPosition(0, (k + j) - l);
        if(ai.length != 0)
            drawHotLine(g, i, j, i1, ai[1] + l, i1 + container.getSize().width, ai[1] + l);
    }
}
