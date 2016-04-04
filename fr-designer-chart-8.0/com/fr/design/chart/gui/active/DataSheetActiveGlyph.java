// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.chart.gui.active;

import com.fr.base.chart.Glyph;
import com.fr.chart.chartglyph.DataSheetGlyph;
import com.fr.design.chart.gui.ChartComponent;
import com.fr.design.chart.gui.active.action.SetDataSheetAction;

// Referenced classes of package com.fr.design.chart.gui.active:
//            ActiveGlyph

public class DataSheetActiveGlyph extends ActiveGlyph
{

    private DataSheetGlyph dataSheetGlyph;

    public DataSheetActiveGlyph(ChartComponent chartcomponent, DataSheetGlyph datasheetglyph, Glyph glyph)
    {
        super(chartcomponent, glyph);
        dataSheetGlyph = datasheetglyph;
    }

    public Glyph getGlyph()
    {
        return dataSheetGlyph;
    }

    public void goRightPane()
    {
        (new SetDataSheetAction(chartComponent)).showDataSheetStylePane();
    }
}
