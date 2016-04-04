// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.chart.gui.type;

import com.fr.chart.chartattr.*;
import com.fr.chart.charttypes.BarIndependentChart;
import com.fr.general.FRLogger;
import com.fr.general.Inter;
import java.util.List;

// Referenced classes of package com.fr.design.mainframe.chart.gui.type:
//            AbstractBarPane, ChartImagePane

public class BarPlotPane extends AbstractBarPane
{

    private static final long serialVersionUID = 0x6fdc0f30907ab38L;

    public BarPlotPane()
    {
    }

    protected String[] getTypeIconPath()
    {
        return (new String[] {
            "/com/fr/design/images/chart/ColumnPlot/type/0.png", "/com/fr/design/images/chart/ColumnPlot/type/1.png", "/com/fr/design/images/chart/ColumnPlot/type/2.png", "/com/fr/design/images/chart/ColumnPlot/type/3.png", "/com/fr/design/images/chart/ColumnPlot/type/4.png", "/com/fr/design/images/chart/ColumnPlot/type/5.png", "/com/fr/design/images/chart/ColumnPlot/type/6.png"
        });
    }

    protected String[] getTypeTipName()
    {
        String s = Inter.getLocText("FR-Chart-Type_Bar");
        String s1 = Inter.getLocText("FR-Chart-Type_Stacked");
        String s2 = Inter.getLocText("FR-Chart-Use_Percent");
        String s3 = Inter.getLocText("FR-Chart-Chart_3D");
        return (new String[] {
            s, (new StringBuilder()).append(s1).append(s).toString(), (new StringBuilder()).append(s2).append(s1).append(s).toString(), (new StringBuilder()).append(s3).append(s).toString(), (new StringBuilder()).append(s3).append(s).append("(").append(Inter.getLocText("FR-Chart-Direction_Horizontal")).append(")").toString(), (new StringBuilder()).append(s3).append(s1).append(s).toString(), (new StringBuilder()).append(s3).append(s2).append(s1).append(s).toString()
        });
    }

    protected String[] getTypeLayoutPath()
    {
        return (new String[] {
            "/com/fr/design/images/chart/ColumnPlot/layout/0.png", "/com/fr/design/images/chart/ColumnPlot/layout/1.png", "/com/fr/design/images/chart/ColumnPlot/layout/2.png", "/com/fr/design/images/chart/ColumnPlot/layout/3.png"
        });
    }

    protected String[] getTypeLayoutTipName()
    {
        return getNormalLayoutTipName();
    }

    public String title4PopupWindow()
    {
        return Inter.getLocText("FR-Chart-Type_Bar");
    }

    protected Plot getSelectedClonedPlot()
    {
        Chart achart[] = BarIndependentChart.barChartTypes;
        BarPlot barplot = null;
        if(((ChartImagePane)typeDemo.get(0)).isPressing)
            barplot = (BarPlot)achart[0].getPlot();
        else
        if(((ChartImagePane)typeDemo.get(1)).isPressing)
            barplot = (BarPlot)achart[1].getPlot();
        else
        if(((ChartImagePane)typeDemo.get(2)).isPressing)
            barplot = (BarPlot)achart[2].getPlot();
        else
        if(((ChartImagePane)typeDemo.get(3)).isPressing)
            barplot = (BarPlot)achart[3].getPlot();
        else
        if(((ChartImagePane)typeDemo.get(4)).isPressing)
            barplot = (BarPlot)achart[4].getPlot();
        else
        if(((ChartImagePane)typeDemo.get(5)).isPressing)
            barplot = (BarPlot)achart[5].getPlot();
        else
        if(((ChartImagePane)typeDemo.get(6)).isPressing)
            barplot = (BarPlot)achart[6].getPlot();
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

    public void updateBean(Chart chart)
    {
        chart.switchPlot(getSelectedClonedPlot());
        super.updateBean(chart);
    }

    protected String getPlotTypeID()
    {
        return "FineReportBarChart";
    }

    public Chart getDefaultChart()
    {
        return BarIndependentChart.barChartTypes[0];
    }

    public volatile void updateBean(Object obj)
    {
        updateBean((Chart)obj);
    }
}
