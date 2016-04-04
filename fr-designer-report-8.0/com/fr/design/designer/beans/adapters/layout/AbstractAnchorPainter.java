// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.designer.beans.adapters.layout;

import com.fr.design.designer.beans.Painter;
import com.fr.design.form.util.XCreatorConstants;
import java.awt.*;

public abstract class AbstractAnchorPainter
    implements Painter
{

    protected Container container;
    protected Rectangle hotspot;

    public AbstractAnchorPainter(Container container1)
    {
        container = container1;
    }

    public void setRenderingBounds(Rectangle rectangle)
    {
        hotspot = rectangle;
    }

    protected void drawHotspot(Graphics g, Rectangle rectangle, Color color)
    {
        drawHotspot(g, rectangle.x, rectangle.y, rectangle.width, rectangle.height, color);
    }

    protected void drawHotspot(Graphics g, int i, int j, int k, int l, Color color)
    {
        Graphics2D graphics2d = (Graphics2D)g;
        java.awt.Stroke stroke = graphics2d.getStroke();
        graphics2d.setStroke(XCreatorConstants.STROKE);
        Color color1 = graphics2d.getColor();
        graphics2d.setColor(color);
        graphics2d.drawRect(i, j, k, l);
        graphics2d.setColor(color1);
        graphics2d.setStroke(stroke);
    }
}
