// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.chart.gui.type;

import com.fr.chart.chartattr.*;
import com.fr.chart.charttypes.PieIndependentChart;
import com.fr.design.mainframe.ChartDesigner;
import com.fr.general.FRLogger;
import com.fr.general.Inter;
import java.util.ArrayList;
import java.util.List;

// Referenced classes of package com.fr.design.mainframe.chart.gui.type:
//            PlotPane4ToolBar, ChartDesignerImagePane

public class PiePlotPane4ToolBar extends PlotPane4ToolBar
{

    private static final int PIE_CHART = 0;
    private static final int THREE_D_PIE_CHART = 1;

    public PiePlotPane4ToolBar(ChartDesigner chartdesigner)
    {
        super(chartdesigner);
    }

    protected String getTypeIconPath()
    {
        return "com/fr/design/images/toolbar/pie/";
    }

    protected List initDemoList()
    {
        ArrayList arraylist = new ArrayList();
        ChartDesignerImagePane chartdesignerimagepane = new ChartDesignerImagePane(getTypeIconPath(), 0, Inter.getLocText("I-PieStyle_Normal"), this);
        chartdesignerimagepane.setSelected(true);
        arraylist.add(chartdesignerimagepane);
        arraylist.add(new ChartDesignerImagePane(getTypeIconPath(), 1, Inter.getLocText(new String[] {
            "FR-Chart-Chart_3D", "I-PieStyle_Normal"
        }), this));
        return arraylist;
    }

    protected Plot getSelectedClonedPlot()
    {
        Chart achart[] = PieIndependentChart.pieChartTypes;
        PiePlot pieplot = (PiePlot)achart[getSelectedIndex()].getPlot();
        Plot plot = null;
        try
        {
            plot = (Plot)pieplot.clone();
        }
        catch(CloneNotSupportedException clonenotsupportedexception)
        {
            FRLogger.getLogger().error("Error In PieChart");
        }
        return plot;
    }
}
