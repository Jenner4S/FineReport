// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.chart.gui.active;

import com.fr.base.ScreenResolution;
import com.fr.base.chart.BaseChartGlyph;
import com.fr.base.chart.Glyph;
import com.fr.design.chart.gui.ActiveGlyphFactory;
import com.fr.design.chart.gui.ChartComponent;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.util.Iterator;

public abstract class ActiveGlyph
{

    protected Glyph parentGlyph;
    protected ChartComponent chartComponent;

    public ActiveGlyph(ChartComponent chartcomponent, Glyph glyph)
    {
        chartComponent = chartcomponent;
        parentGlyph = glyph;
    }

    public abstract Glyph getGlyph();

    public void drawAllGlyph(Graphics2D graphics2d, int i)
    {
        Point2D point2d = offset4Paint();
        graphics2d.translate(point2d.getX(), point2d.getY());
        getGlyph().draw(graphics2d, i);
        graphics2d.translate(-point2d.getX(), -point2d.getY());
    }

    public abstract void goRightPane();

    public Point2D offset4Paint()
    {
        return new java.awt.geom.Point2D.Double(parentGlyph.getShape().getBounds().getX(), parentGlyph.getShape().getBounds().getY());
    }

    public void paint4ActiveGlyph(Graphics2D graphics2d, BaseChartGlyph basechartglyph)
    {
        if(parentGlyph == null)
        {
            return;
        } else
        {
            java.awt.Paint paint = graphics2d.getPaint();
            java.awt.Composite composite = graphics2d.getComposite();
            graphics2d.setPaint(Color.white);
            graphics2d.setComposite(AlphaComposite.getInstance(3, 0.5F));
            graphics2d.fill(basechartglyph.getShape());
            drawAllGlyph(graphics2d, ScreenResolution.getScreenResolution());
            graphics2d.setPaint(paint);
            graphics2d.setComposite(composite);
            return;
        }
    }

    protected void drawSelectedBounds4Active(Graphics2D graphics2d)
    {
        if(getGlyph() != null)
        {
            Shape shape = getGlyph().getShape();
            if(shape != null)
                graphics2d.draw(shape);
        }
    }

    public boolean contains(int i, int j)
    {
        if(getGlyph() == null || getGlyph().getShape() == null)
        {
            return false;
        } else
        {
            Point2D point2d = offset4Paint();
            return getGlyph().getShape().intersects((double)i - point2d.getX() - 2D, (double)j - point2d.getY() - 2D, 4D, 4D);
        }
    }

    public ActiveGlyph findActionGlyphFromChildren(int i, int j)
    {
        Glyph glyph = getGlyph();
        if(glyph == null)
            return null;
        Iterator iterator = glyph.selectableChildren();
        ActiveGlyph activeglyph = null;
        do
        {
            if(!iterator.hasNext() || activeglyph != null)
                break;
            ActiveGlyph activeglyph1 = ActiveGlyphFactory.createActiveGlyph(chartComponent, iterator.next(), glyph);
            if(activeglyph1 != null)
                activeglyph = activeglyph1.findActionGlyphFromChildren(i, j);
            if(activeglyph == null && activeglyph1 != null && activeglyph1.contains(i, j))
                activeglyph = activeglyph1;
        } while(true);
        if(activeglyph == null && contains(i, j))
            activeglyph = this;
        return activeglyph;
    }

    public void onMouseDragged(MouseEvent mouseevent)
    {
    }

    public void onMouseMove(MouseEvent mouseevent)
    {
    }
}
