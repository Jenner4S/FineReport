// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.designer.beans.painters;

import com.fr.design.designer.beans.adapters.layout.AbstractAnchorPainter;
import com.fr.design.form.layout.FRGridLayout;
import com.fr.design.form.util.XCreatorConstants;
import java.awt.*;

public class FRGridLayoutAnchorPainter extends AbstractAnchorPainter
{

    protected FRGridLayout grid_layout;

    public FRGridLayoutAnchorPainter(Container container)
    {
        super(container);
        grid_layout = (FRGridLayout)container.getLayout();
    }

    public void paint(Graphics g, int i, int j)
    {
        int k = grid_layout.getColumns();
        int l = grid_layout.getRows();
        int i1 = hotspot.width;
        int j1 = hotspot.height;
        Graphics2D graphics2d = (Graphics2D)g;
        Stroke stroke = graphics2d.getStroke();
        graphics2d.setStroke(new BasicStroke(1.0F));
        g.setColor(XCreatorConstants.LAYOUT_SEP_COLOR);
        g.drawRect(hotspot.x, hotspot.y, hotspot.width, hotspot.height);
        if(k != 0)
        {
            for(int k1 = 1; k1 < k; k1++)
            {
                int i2 = (k1 * i1) / k;
                g.drawLine(hotspot.x + i2, hotspot.y, hotspot.x + i2, hotspot.y + j1);
            }

        }
        if(l != 0)
        {
            for(int l1 = 1; l1 < l; l1++)
            {
                int j2 = (l1 * j1) / l;
                g.drawLine(hotspot.x, hotspot.y + j2, hotspot.x + i1, hotspot.y + j2);
            }

        }
        graphics2d.setStroke(stroke);
    }
}
