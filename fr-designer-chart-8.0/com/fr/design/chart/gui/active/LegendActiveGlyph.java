// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.chart.gui.active;

import com.fr.base.chart.Glyph;
import com.fr.chart.chartglyph.LegendGlyph;
import com.fr.design.chart.gui.ChartComponent;
import com.fr.design.chart.gui.active.action.SetLegendStyleAction;

// Referenced classes of package com.fr.design.chart.gui.active:
//            ActiveGlyph

public class LegendActiveGlyph extends ActiveGlyph
{

    private LegendGlyph legendGlyph;

    public LegendActiveGlyph(ChartComponent chartcomponent, LegendGlyph legendglyph, Glyph glyph)
    {
        super(chartcomponent, glyph);
        legendGlyph = legendglyph;
    }

    public Glyph getGlyph()
    {
        return legendGlyph;
    }

    public void goRightPane()
    {
        (new SetLegendStyleAction(chartComponent)).showLegendStylePane();
    }
}
