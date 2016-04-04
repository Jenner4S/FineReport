// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.chart.gui.type;

import com.fr.chart.chartattr.BubblePlot;
import com.fr.chart.chartattr.Chart;
import com.fr.chart.charttypes.BubbleIndependentChart;
import com.fr.general.Inter;
import java.util.List;

// Referenced classes of package com.fr.design.mainframe.chart.gui.type:
//            AbstractChartTypePane, ChartImagePane

public class BubblePlotPane extends AbstractChartTypePane
{

    private static final long serialVersionUID = 0xf7a6cebbcb81992dL;
    private static final int BUBBLE_CHART = 0;

    public BubblePlotPane()
    {
    }

    protected String[] getTypeIconPath()
    {
        return (new String[] {
            "/com/fr/design/images/chart/BubblePlot/type/0.png"
        });
    }

    protected String[] getTypeTipName()
    {
        return (new String[] {
            Inter.getLocText("FR-Chart-Chart_BubbleChart")
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

    public String title4PopupWindow()
    {
        return Inter.getLocText("FR-Chart-Chart_BubbleChart");
    }

    public void updateBean(Chart chart)
    {
        if(needsResetChart(chart))
            resetChart(chart);
        BubblePlot bubbleplot = new BubblePlot();
        chart.switchPlot(bubbleplot);
    }

    protected String getPlotTypeID()
    {
        return "FineReportBubbleChart";
    }

    public void populateBean(Chart chart)
    {
        ((ChartImagePane)typeDemo.get(0)).isPressing = true;
        checkDemosBackground();
    }

    public Chart getDefaultChart()
    {
        return BubbleIndependentChart.bubbleChartTypes[0];
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
