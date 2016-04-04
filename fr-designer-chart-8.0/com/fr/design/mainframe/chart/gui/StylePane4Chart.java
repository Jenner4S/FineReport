// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.chart.gui;

import com.fr.chart.chartattr.*;
import com.fr.design.gui.frpane.AttributeChangeListener;
import com.fr.design.mainframe.chart.gui.style.series.ChartSeriesPane;
import com.fr.design.mainframe.chart.gui.style.series.SeriesPane4ChartDesigner;

// Referenced classes of package com.fr.design.mainframe.chart.gui:
//            ChartStylePane

public class StylePane4Chart extends ChartStylePane
{

    public StylePane4Chart(AttributeChangeListener attributechangelistener, boolean flag)
    {
        super(attributechangelistener, flag);
    }

    public void update(ChartCollection chartcollection)
    {
        int i = chartcollection.getSelectedIndex();
        super.update(chartcollection);
        chartcollection.getSelectedChart().setStyleGlobal(false);
        chartcollection.setChartName(i, chartcollection.getSelectedChart().getTitle().getTextObject().toString());
    }

    protected ChartSeriesPane createChartSeriesPane()
    {
        return new SeriesPane4ChartDesigner(this);
    }
}
