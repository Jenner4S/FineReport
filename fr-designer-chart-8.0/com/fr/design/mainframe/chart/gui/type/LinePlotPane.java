// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.chart.gui.type;

import com.fr.chart.chartattr.*;
import com.fr.chart.charttypes.LineIndependentChart;
import com.fr.general.FRLogger;
import com.fr.general.Inter;
import java.util.List;

// Referenced classes of package com.fr.design.mainframe.chart.gui.type:
//            AbstractChartTypePane, ChartImagePane

public class LinePlotPane extends AbstractChartTypePane
{

    private static final int LINE_CHART = 0;

    public LinePlotPane()
    {
    }

    protected String[] getTypeIconPath()
    {
        return (new String[] {
            "/com/fr/design/images/chart/LinePlot/type/0.png"
        });
    }

    protected String[] getTypeTipName()
    {
        return (new String[] {
            Inter.getLocText("I-LineStyle_Line")
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

    public void populateBean(Chart chart)
    {
        super.populateBean(chart);
        ((ChartImagePane)typeDemo.get(0)).isPressing = true;
        checkDemosBackground();
    }

    public void updateBean(Chart chart)
    {
        if(needsResetChart(chart))
            resetChart(chart);
        Chart achart[] = LineIndependentChart.lineChartTypes;
        LinePlot lineplot;
        if(achart.length > 0)
            try
            {
                lineplot = (LinePlot)achart[0].getPlot().clone();
            }
            catch(Exception exception)
            {
                lineplot = new LinePlot();
            }
        else
            lineplot = new LinePlot();
        try
        {
            chart.switchPlot((Plot)lineplot.clone());
        }
        catch(CloneNotSupportedException clonenotsupportedexception)
        {
            FRLogger.getLogger().error("Error In LineChart");
            chart.switchPlot(new LinePlot());
        }
    }

    protected String getPlotTypeID()
    {
        return "FineReportLineChart";
    }

    public String title4PopupWindow()
    {
        return Inter.getLocText("I-LineStyle_Line");
    }

    public Chart getDefaultChart()
    {
        return LineIndependentChart.lineChartTypes[0];
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
