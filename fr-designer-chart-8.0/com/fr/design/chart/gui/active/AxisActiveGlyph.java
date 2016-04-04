// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.chart.gui.active;

import com.fr.base.chart.Glyph;
import com.fr.chart.chartglyph.AxisGlyph;
import com.fr.design.chart.gui.ChartComponent;
import com.fr.design.chart.gui.active.action.SetAxisStyleAction;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;

// Referenced classes of package com.fr.design.chart.gui.active:
//            ActiveGlyph

public abstract class AxisActiveGlyph extends ActiveGlyph
{

    protected AxisGlyph axis;

    public AxisActiveGlyph(ChartComponent chartcomponent, AxisGlyph axisglyph, Glyph glyph)
    {
        super(chartcomponent, glyph);
        axis = axisglyph;
    }

    public void drawAllGlyph(Graphics2D graphics2d, int i)
    {
        Point2D point2d = offset4Paint();
        graphics2d.translate(point2d.getX(), point2d.getY());
        axis.drawWithOutAlert(graphics2d, i);
        graphics2d.translate(-point2d.getX(), -point2d.getY());
    }

    public void goRightPane()
    {
        (new SetAxisStyleAction(chartComponent)).showAxisStylePane();
    }

    public Glyph getGlyph()
    {
        return axis;
    }
}
