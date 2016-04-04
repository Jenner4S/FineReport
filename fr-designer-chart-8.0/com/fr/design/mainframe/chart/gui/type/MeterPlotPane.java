// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.chart.gui.type;

import com.fr.chart.chartattr.*;
import com.fr.chart.chartglyph.MeterStyle;
import com.fr.chart.charttypes.MeterIndependentChart;
import com.fr.general.FRLogger;
import com.fr.general.Inter;
import java.util.List;

// Referenced classes of package com.fr.design.mainframe.chart.gui.type:
//            AbstractChartTypePane, ChartImagePane

public class MeterPlotPane extends AbstractChartTypePane
{

    private static final int METER = 0;
    private static final int BLUE_METER = 1;
    private static final int SIMPLE_METER = 2;

    public MeterPlotPane()
    {
    }

    protected String[] getTypeIconPath()
    {
        return (new String[] {
            "/com/fr/design/images/chart/MeterPlot/type/0.png", "/com/fr/design/images/chart/MeterPlot/type/1.png", "/com/fr/design/images/chart/MeterPlot/type/2.png"
        });
    }

    protected String[] getTypeTipName()
    {
        String s = Inter.getLocText("FR-Chart-Type_Meter");
        return (new String[] {
            (new StringBuilder()).append(Inter.getLocText("FR-Chart-Mode_Custom")).append(s).toString(), (new StringBuilder()).append(s).append("1").toString(), (new StringBuilder()).append(s).append("2").toString()
        });
    }

    protected String[] getTypeLayoutPath()
    {
        return new String[0];
    }

    protected String[] getTypeLayoutTipName()
    {
        return new String[0];
    }

    protected Plot getSelectedClonedPlot()
    {
        Chart achart[] = MeterIndependentChart.meterChartTypes;
        MeterPlot meterplot = null;
        if(((ChartImagePane)typeDemo.get(0)).isPressing)
            meterplot = (MeterPlot)achart[0].getPlot();
        else
        if(((ChartImagePane)typeDemo.get(2)).isPressing)
            meterplot = (MeterPlot)achart[2].getPlot();
        else
        if(((ChartImagePane)typeDemo.get(1)).isPressing)
            meterplot = (MeterPlot)achart[1].getPlot();
        Plot plot = null;
        try
        {
            plot = (Plot)meterplot.clone();
        }
        catch(CloneNotSupportedException clonenotsupportedexception)
        {
            FRLogger.getLogger().error("Error In ColumnChart");
        }
        return plot;
    }

    public String title4PopupWindow()
    {
        return Inter.getLocText("FR-Chart-Type_Meter");
    }

    public void updateBean(Chart chart)
    {
        if(needsResetChart(chart))
            resetChart(chart);
        chart.switchPlot(getSelectedClonedPlot());
    }

    protected String getPlotTypeID()
    {
        return "FineReportMeterChart";
    }

    public void populateBean(Chart chart)
    {
        MeterPlot meterplot = (MeterPlot)chart.getPlot();
        MeterStyle meterstyle = meterplot.getMeterStyle();
        if(meterstyle.getMeterType() == 0)
        {
            ((ChartImagePane)typeDemo.get(0)).isPressing = true;
            ((ChartImagePane)typeDemo.get(1)).isPressing = false;
            ((ChartImagePane)typeDemo.get(2)).isPressing = false;
        } else
        if(meterstyle.getMeterType() == 1)
        {
            ((ChartImagePane)typeDemo.get(0)).isPressing = false;
            ((ChartImagePane)typeDemo.get(1)).isPressing = true;
            ((ChartImagePane)typeDemo.get(2)).isPressing = false;
        } else
        {
            ((ChartImagePane)typeDemo.get(0)).isPressing = false;
            ((ChartImagePane)typeDemo.get(1)).isPressing = false;
            ((ChartImagePane)typeDemo.get(2)).isPressing = true;
        }
        checkDemosBackground();
    }

    public Chart getDefaultChart()
    {
        return MeterIndependentChart.meterChartTypes[0];
    }

    public volatile void updateBean(Object obj)
    {
        updateBean((Chart)obj);
    }

    public volatile void populateBean(Object obj)
    {
        populateBean((Chart)obj);
    }
}
