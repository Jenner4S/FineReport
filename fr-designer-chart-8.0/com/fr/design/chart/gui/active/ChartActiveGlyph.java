// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.chart.gui.active;

import com.fr.base.chart.BaseChartGlyph;
import com.fr.base.chart.Glyph;
import com.fr.design.chart.gui.ChartComponent;
import com.fr.design.chart.gui.active.action.SetChartStyleAciton;
import java.awt.Point;
import java.awt.geom.Point2D;

// Referenced classes of package com.fr.design.chart.gui.active:
//            ActiveGlyph

public class ChartActiveGlyph extends ActiveGlyph
{

    private BaseChartGlyph glyphChart;

    public ChartActiveGlyph(ChartComponent chartcomponent, BaseChartGlyph basechartglyph)
    {
        this(chartcomponent, basechartglyph, null);
    }

    public ChartActiveGlyph(ChartComponent chartcomponent, BaseChartGlyph basechartglyph, Glyph glyph)
    {
        super(chartcomponent, glyph);
        glyphChart = basechartglyph;
    }

    public Glyph getGlyph()
    {
        return glyphChart;
    }

    public Point2D offset4Paint()
    {
        return new Point(0, 0);
    }

    public void goRightPane()
    {
        (new SetChartStyleAciton(chartComponent)).showChartStylePane();
    }
}
