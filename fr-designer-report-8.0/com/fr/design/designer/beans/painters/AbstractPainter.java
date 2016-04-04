// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.designer.beans.painters;

import com.fr.design.designer.beans.HoverPainter;
import com.fr.design.designer.creator.XCreator;
import com.fr.design.designer.creator.XLayoutContainer;
import com.fr.design.form.util.XCreatorConstants;
import com.fr.general.Inter;
import java.awt.*;

public abstract class AbstractPainter
    implements HoverPainter
{

    protected Point hotspot;
    protected Rectangle hotspot_bounds;
    protected XLayoutContainer container;
    protected XCreator creator;

    public AbstractPainter(XLayoutContainer xlayoutcontainer)
    {
        container = xlayoutcontainer;
    }

    public void setHotspot(Point point)
    {
        hotspot = point;
    }

    public void paint(Graphics g, int i, int j)
    {
        if(hotspot_bounds != null)
            drawHotspot(g, hotspot_bounds.x, hotspot_bounds.y, hotspot_bounds.width, hotspot_bounds.height, Color.lightGray, true, false);
    }

    public void setRenderingBounds(Rectangle rectangle)
    {
        hotspot_bounds = rectangle;
    }

    public void setCreator(XCreator xcreator)
    {
        creator = xcreator;
    }

    protected void drawHotspot(Graphics g, int i, int j, int k, int l, boolean flag)
    {
        Color color = flag ? XCreatorConstants.LAYOUT_HOTSPOT_COLOR : XCreatorConstants.LAYOUT_FORBIDDEN_COLOR;
        drawHotspot(g, i, j, k, l, color, flag, false);
    }

    protected void drawRegionBackground(Graphics g, int i, int j, int k, int l, Color color, boolean flag)
    {
        drawHotspot(g, i, j, k, l, color, flag, true);
    }

    protected void drawHotspot(Graphics g, int i, int j, int k, int l, Color color, boolean flag, 
            boolean flag1)
    {
        Graphics2D graphics2d = (Graphics2D)g;
        Color color1 = graphics2d.getColor();
        Stroke stroke = graphics2d.getStroke();
        graphics2d.setStroke(XCreatorConstants.STROKE);
        graphics2d.setColor(color);
        if(!flag)
            graphics2d.drawString((new StringBuilder()).append(Inter.getLocText("Cannot-Add_To_This_Area")).append("!").toString(), i + k / 3, j + l / 2);
        else
        if(flag1)
            graphics2d.fillRect(i, j, k, l);
        else
            graphics2d.drawRect(i, j, k, l);
        graphics2d.setStroke(stroke);
        graphics2d.setColor(color1);
    }
}
