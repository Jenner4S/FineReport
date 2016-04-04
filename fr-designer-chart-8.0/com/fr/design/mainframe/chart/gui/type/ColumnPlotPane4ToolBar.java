// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.chart.gui.type;

import com.fr.chart.chartattr.*;
import com.fr.chart.charttypes.ColumnIndependentChart;
import com.fr.design.mainframe.ChartDesigner;
import com.fr.general.FRLogger;
import com.fr.general.Inter;
import java.util.ArrayList;
import java.util.List;

// Referenced classes of package com.fr.design.mainframe.chart.gui.type:
//            PlotPane4ToolBar, ChartDesignerImagePane

public class ColumnPlotPane4ToolBar extends PlotPane4ToolBar
{

    private static final int COLOMN_CHART = 0;
    private static final int STACK_COLOMN_CHART = 1;
    private static final int PERCENT_STACK_COLOMN_CHART = 2;
    private static final int THREE_D_COLOMN_CHART = 3;
    private static final int THREE_D_COLOMN_HORIZON_DRAW_CHART = 4;
    private static final int THREE_D_STACK_COLOMN_CHART = 5;
    private static final int THREE_D_PERCENT_STACK_COLOMN_CHART = 6;

    public ColumnPlotPane4ToolBar(ChartDesigner chartdesigner)
    {
        super(chartdesigner);
    }

    protected String getTypeIconPath()
    {
        return "com/fr/design/images/toolbar/column/";
    }

    protected List initDemoList()
    {
        ArrayList arraylist = new ArrayList();
        ChartDesignerImagePane chartdesignerimagepane = new ChartDesignerImagePane(getTypeIconPath(), 0, Inter.getLocText("FR-Chart-Type_Column"), this);
        chartdesignerimagepane.setSelected(true);
        arraylist.add(chartdesignerimagepane);
        arraylist.add(new ChartDesignerImagePane(getTypeIconPath(), 1, Inter.getLocText(new String[] {
            "FR-Chart-Type_Stacked", "FR-Chart-Type_Column"
        }), this));
        arraylist.add(new ChartDesignerImagePane(getTypeIconPath(), 2, Inter.getLocText(new String[] {
            "FR-Chart-Use_Percent", "FR-Chart-Type_Stacked", "FR-Chart-Type_Column"
        }), this));
        arraylist.add(new ChartDesignerImagePane(getTypeIconPath(), 3, Inter.getLocText(new String[] {
            "FR-Chart-Chart_3D", "FR-Chart-Type_Column"
        }), this));
        arraylist.add(new ChartDesignerImagePane(getTypeIconPath(), 4, Inter.getLocText(new String[] {
            "FR-Chart-Chart_3D", "FR-Chart-Type_Column", "FR-Chart-Direction_Horizontal"
        }, new String[] {
            "", "(", ")"
        }), this));
        arraylist.add(new ChartDesignerImagePane(getTypeIconPath(), 5, Inter.getLocText(new String[] {
            "FR-Chart-Chart_3D", "FR-Chart-Type_Stacked", "FR-Chart-Type_Column"
        }), this));
        arraylist.add(new ChartDesignerImagePane(getTypeIconPath(), 6, Inter.getLocText(new String[] {
            "FR-Chart-Chart_3D", "FR-Chart-Use_Percent", "FR-Chart-Type_Stacked", "FR-Chart-Type_Column"
        }), this));
        return arraylist;
    }

    protected Plot getSelectedClonedPlot()
    {
        Chart achart[] = ColumnIndependentChart.columnChartTypes;
        BarPlot barplot = (BarPlot)achart[getSelectedIndex()].getPlot();
        Plot plot = null;
        try
        {
            plot = (Plot)barplot.clone();
        }
        catch(CloneNotSupportedException clonenotsupportedexception)
        {
            FRLogger.getLogger().error("Error In ColumnChart");
        }
        return plot;
    }
}
