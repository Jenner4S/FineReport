// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.chart.gui.type;

import com.fr.chart.chartattr.*;
import com.fr.chart.charttypes.CustomIndependentChart;
import com.fr.design.mainframe.ChartDesigner;
import com.fr.general.FRLogger;
import com.fr.general.Inter;
import java.util.ArrayList;
import java.util.List;

// Referenced classes of package com.fr.design.mainframe.chart.gui.type:
//            PlotPane4ToolBar, ChartDesignerImagePane

public class CustomPlotPane4ToolBar extends PlotPane4ToolBar
{

    private static final int CUSTOM_NO_SHEET = 0;

    public CustomPlotPane4ToolBar(ChartDesigner chartdesigner)
    {
        super(chartdesigner);
    }

    protected String getTypeIconPath()
    {
        return "com/fr/design/images/toolbar/custom/";
    }

    protected List initDemoList()
    {
        ArrayList arraylist = new ArrayList();
        ChartDesignerImagePane chartdesignerimagepane = new ChartDesignerImagePane(getTypeIconPath(), 0, Inter.getLocText("ChartF-Comb_Chart"), this);
        chartdesignerimagepane.setSelected(true);
        arraylist.add(chartdesignerimagepane);
        return arraylist;
    }

    protected Plot getSelectedClonedPlot()
    {
        Chart achart[] = CustomIndependentChart.combChartTypes;
        CustomPlot customplot = (CustomPlot)achart[getSelectedIndex()].getPlot();
        Plot plot = null;
        try
        {
            plot = (Plot)customplot.clone();
        }
        catch(CloneNotSupportedException clonenotsupportedexception)
        {
            FRLogger.getLogger().error("Error In CustomChart");
        }
        return plot;
    }
}
