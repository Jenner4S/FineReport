// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.designer.beans.painters;

import com.fr.design.designer.beans.HoverPainter;
import com.fr.design.designer.creator.XCreator;
import com.fr.design.form.layout.FRGridLayout;
import com.fr.design.form.util.XCreatorConstants;
import java.awt.*;

// Referenced classes of package com.fr.design.designer.beans.painters:
//            FRGridLayoutAnchorPainter

public class FRGridLayoutPainter extends FRGridLayoutAnchorPainter
    implements HoverPainter
{

    private Point point;
    private XCreator current;

    public FRGridLayoutPainter(Container container)
    {
        super(container);
    }

    public void setHotspot(Point point1)
    {
        point = point1;
    }

    public void setCreator(XCreator xcreator)
    {
        current = xcreator;
    }

    public void paint(Graphics g, int i, int j)
    {
        super.paint(g, i, j);
        int k = point.x;
        int l = point.y;
        int i1 = grid_layout.getColumns();
        int j1 = grid_layout.getRows();
        if(i1 == 0)
            i1 = 1;
        if(j1 == 0)
            j1 = 1;
        double d = (double)hotspot.width / (double)i1;
        double d1 = (double)hotspot.height / (double)j1;
        int k1 = (int)((double)k / d);
        int l1 = (int)((double)l / d1);
        k = (int)((double)k1 * d + (double)hotspot.x);
        l = (int)((double)l1 * d1 + (double)hotspot.y);
        drawHotspot(g, k, l, (int)d, (int)d1, XCreatorConstants.SELECTION_COLOR);
    }
}
