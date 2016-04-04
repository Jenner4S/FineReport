// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.chart.gui.type;

import com.fr.chart.chartattr.Chart;
import com.fr.chart.chartattr.Plot;
import com.fr.chart.charttypes.AreaIndependentChart;
import com.fr.design.mainframe.ChartDesigner;
import com.fr.general.FRLogger;
import com.fr.general.Inter;
import java.util.ArrayList;
import java.util.List;

// Referenced classes of package com.fr.design.mainframe.chart.gui.type:
//            PlotPane4ToolBar, ChartDesignerImagePane

public class AreaPlotPane4ToolBar extends PlotPane4ToolBar
{

    private static final int STACK_AREA_CHART = 0;
    private static final int PERCENT_AREA_LINE_CHART = 1;
    private static final int STACK_3D_AREA_CHART = 2;
    private static final int PERCENT_3D_AREA_LINE_CHART = 3;

    public AreaPlotPane4ToolBar(ChartDesigner chartdesigner)
    {
        super(chartdesigner);
    }

    protected String getTypeIconPath()
    {
        return "com/fr/design/images/toolbar/area/";
    }

    protected List initDemoList()
    {
        ArrayList arraylist = new ArrayList();
        String s = Inter.getLocText("FR-Chart-Type_Area");
        String s1 = Inter.getLocText("FR-Chart-Type_Stacked");
        String s2 = Inter.getLocText("FR-Chart-Use_Percent");
        ChartDesignerImagePane chartdesignerimagepane = new ChartDesignerImagePane(getTypeIconPath(), 0, (new StringBuilder()).append(s1).append(s).toString(), this);
        chartdesignerimagepane.setSelected(true);
        arraylist.add(chartdesignerimagepane);
        arraylist.add(new ChartDesignerImagePane(getTypeIconPath(), 1, (new StringBuilder()).append(s2).append(s1).append(s).toString(), this));
        String s3 = Inter.getLocText("FR-Chart-Chart_3D");
        arraylist.add(new ChartDesignerImagePane(getTypeIconPath(), 2, (new StringBuilder()).append(s3).append(s1).append(s).toString(), this));
        arraylist.add(new ChartDesignerImagePane(getTypeIconPath(), 3, (new StringBuilder()).append(s3).append(s2).append(s1).append(s).toString(), this));
        return arraylist;
    }

    protected Plot getSelectedClonedPlot()
    {
        Chart achart[] = AreaIndependentChart.areaChartTypes;
        Plot plot = achart[getSelectedIndex()].getPlot();
        Plot plot1 = null;
        try
        {
            plot1 = (Plot)plot.clone();
        }
        catch(CloneNotSupportedException clonenotsupportedexception)
        {
            FRLogger.getLogger().error("Error In AreaChart");
        }
        return plot1;
    }
}
