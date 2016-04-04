// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.chart.gui.active;

import com.fr.base.chart.Glyph;
import com.fr.chart.chartglyph.TitleGlyph;
import com.fr.design.chart.gui.ChartComponent;
import com.fr.design.chart.gui.active.action.SetTitleStyleAction;
import java.awt.Point;
import java.awt.geom.Point2D;

// Referenced classes of package com.fr.design.chart.gui.active:
//            ActiveGlyph

public class TextActiveGlyph extends ActiveGlyph
{

    private TitleGlyph titleGlyph;

    public TextActiveGlyph(ChartComponent chartcomponent, TitleGlyph titleglyph, Glyph glyph)
    {
        super(chartcomponent, glyph);
        titleGlyph = titleglyph;
    }

    public Glyph getGlyph()
    {
        return titleGlyph;
    }

    public Point offset4Paint()
    {
        return new Point(0, 0);
    }

    public void goRightPane()
    {
        (new SetTitleStyleAction(chartComponent)).showTitlePane();
    }

    public volatile Point2D offset4Paint()
    {
        return offset4Paint();
    }
}
