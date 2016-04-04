// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.designer.beans.painters;

import com.fr.design.designer.creator.XLayoutContainer;
import com.fr.design.utils.ComponentUtils;
import java.awt.*;

// Referenced classes of package com.fr.design.designer.beans.painters:
//            FRBoxLayoutPainter

public class FRHorizontalLayoutPainter extends FRBoxLayoutPainter
{

    public FRHorizontalLayoutPainter(XLayoutContainer xlayoutcontainer)
    {
        super(xlayoutcontainer);
    }

    public void paint(Graphics g, int i, int j)
    {
        super.paint(g, i, j);
        int k = hotspot.x;
        Rectangle rectangle = ComponentUtils.getRelativeBounds(container);
        int l = rectangle.y;
        int i1 = rectangle.x;
        int ai[] = calculateAddPosition((k - i1) + i, 0);
        if(ai.length != 0)
            drawHotLine(g, i, j, ai[0] + i1, l, ai[0] + i1, l + container.getSize().height);
    }
}
