// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.chart.gui.style.series;

import com.fr.chart.chartattr.Plot;
import com.fr.design.mainframe.chart.gui.ChartStylePane;

// Referenced classes of package com.fr.design.mainframe.chart.gui.style.series:
//            ChartSeriesPane, MeterSeriesPane4ChartDesigner, MapSeriesPane4ChartDesigner, MeterSeriesPane, 
//            MapSeriesPane

public class SeriesPane4ChartDesigner extends ChartSeriesPane
{

    public SeriesPane4ChartDesigner(ChartStylePane chartstylepane)
    {
        super(chartstylepane);
    }

    protected MeterSeriesPane createMeterSeriesPane(ChartStylePane chartstylepane, Plot plot)
    {
        return new MeterSeriesPane4ChartDesigner(chartstylepane, plot);
    }

    protected MapSeriesPane createMapSeriesPane(ChartStylePane chartstylepane, Plot plot)
    {
        return new MapSeriesPane4ChartDesigner(chartstylepane, plot);
    }
}
