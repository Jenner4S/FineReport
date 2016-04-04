// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.chart.gui.style.series;

import com.fr.chart.chartattr.Plot;
import com.fr.design.mainframe.chart.gui.ChartStylePane;
import javax.swing.JPanel;

// Referenced classes of package com.fr.design.mainframe.chart.gui.style.series:
//            Pie2DSeriesPane

public class Pie3DSeriesPane extends Pie2DSeriesPane
{

    public Pie3DSeriesPane(ChartStylePane chartstylepane, Plot plot)
    {
        super(chartstylepane, plot);
    }

    protected JPanel getContentInPlotType()
    {
        initCom();
        return null;
    }
}
