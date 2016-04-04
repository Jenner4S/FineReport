// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.chart.gui.active;

import com.fr.base.chart.Glyph;
import com.fr.chart.chartglyph.PlotGlyph;
import com.fr.chart.chartglyph.TrendLineGlyph;
import com.fr.design.chart.gui.ChartComponent;
import com.fr.design.chart.gui.active.action.SetAnalysisLineStyleAction;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;

// Referenced classes of package com.fr.design.chart.gui.active:
//            ActiveGlyph

public class TrendLineActiveGlyph extends ActiveGlyph
{

    private TrendLineGlyph trendLine;

    public TrendLineActiveGlyph(ChartComponent chartcomponent, TrendLineGlyph trendlineglyph, Glyph glyph)
    {
        super(chartcomponent, glyph);
        trendLine = trendlineglyph;
    }

    public Glyph getGlyph()
    {
        return trendLine;
    }

    public void drawAllGlyph(Graphics2D graphics2d, int i)
    {
        Point2D point2d = offset4Paint();
        graphics2d.translate(point2d.getX(), point2d.getY());
        PlotGlyph plotglyph = (PlotGlyph)parentGlyph;
        ArrayList arraylist = new ArrayList();
        plotglyph.getAllTrendLineGlyph(arraylist);
        for(int j = 0; j < arraylist.size(); j++)
            ((TrendLineGlyph)arraylist.get(j)).draw(graphics2d, i);

        graphics2d.translate(-point2d.getX(), -point2d.getY());
    }

    public void goRightPane()
    {
        (new SetAnalysisLineStyleAction(chartComponent)).showAnalysisLineStylePane();
    }
}
