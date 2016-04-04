// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.chart.gui.type;

import com.fr.chart.chartattr.*;
import com.fr.chart.charttypes.RangeIndependentChart;
import com.fr.design.mainframe.ChartDesigner;
import com.fr.general.FRLogger;
import com.fr.general.Inter;
import java.util.ArrayList;
import java.util.List;

// Referenced classes of package com.fr.design.mainframe.chart.gui.type:
//            PlotPane4ToolBar, ChartDesignerImagePane

public class RangePlotPane4ToolBar extends PlotPane4ToolBar
{

    private static final int RANGE = 0;

    public RangePlotPane4ToolBar(ChartDesigner chartdesigner)
    {
        super(chartdesigner);
    }

    protected String getTypeIconPath()
    {
        return "com/fr/design/images/toolbar/range/";
    }

    protected List initDemoList()
    {
        ArrayList arraylist = new ArrayList();
        ChartDesignerImagePane chartdesignerimagepane = new ChartDesignerImagePane(getTypeIconPath(), 0, Inter.getLocText("ChartF-Range_Chart"), this);
        chartdesignerimagepane.setSelected(true);
        arraylist.add(chartdesignerimagepane);
        return arraylist;
    }

    protected Plot getSelectedClonedPlot()
    {
        Chart achart[] = RangeIndependentChart.rangeChartTypes;
        RangePlot rangeplot = (RangePlot)achart[getSelectedIndex()].getPlot();
        Plot plot = null;
        try
        {
            plot = (Plot)rangeplot.clone();
        }
        catch(CloneNotSupportedException clonenotsupportedexception)
        {
            FRLogger.getLogger().error("Error In RangeChart");
        }
        return plot;
    }
}
