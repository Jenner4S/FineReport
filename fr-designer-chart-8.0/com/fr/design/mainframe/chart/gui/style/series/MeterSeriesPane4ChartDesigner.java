// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.chart.gui.style.series;

import com.fr.chart.chartattr.Plot;
import com.fr.design.mainframe.chart.gui.ChartStylePane;

// Referenced classes of package com.fr.design.mainframe.chart.gui.style.series:
//            MeterSeriesPane, UIColorPickerPane

public class MeterSeriesPane4ChartDesigner extends MeterSeriesPane
{

    public MeterSeriesPane4ChartDesigner(ChartStylePane chartstylepane, Plot plot)
    {
        super(chartstylepane, plot);
    }

    protected UIColorPickerPane createColorPickerPane()
    {
        return new UIColorPickerPane("meterString");
    }
}
