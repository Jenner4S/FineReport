// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.chart.gui.active;

import com.fr.base.chart.Glyph;
import com.fr.chart.chartglyph.PlotGlyph;
import com.fr.design.chart.gui.ChartComponent;
import com.fr.design.chart.gui.active.action.SetPlotStyleAction;
import java.awt.Point;
import java.awt.geom.Point2D;

// Referenced classes of package com.fr.design.chart.gui.active:
//            ActiveGlyph

public class PlotActiveGlyph extends ActiveGlyph
{

    private PlotGlyph plotGlyph;

    public PlotActiveGlyph(ChartComponent chartcomponent, PlotGlyph plotglyph, Glyph glyph)
    {
        super(chartcomponent, glyph);
        plotGlyph = plotglyph;
    }

    public Glyph getGlyph()
    {
        return plotGlyph;
    }

    public void goRightPane()
    {
        (new SetPlotStyleAction(chartComponent)).showPlotPane();
    }

    public Point offset4Paint()
    {
        return new Point(0, 0);
    }

    public volatile Point2D offset4Paint()
    {
        return offset4Paint();
    }
}
