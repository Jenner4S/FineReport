// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.chart.gui.type;

import com.fr.chart.chartattr.Chart;
import com.fr.chart.chartattr.RangePlot;
import com.fr.chart.charttypes.RangeIndependentChart;
import com.fr.general.Inter;
import java.util.List;

// Referenced classes of package com.fr.design.mainframe.chart.gui.type:
//            AbstractChartTypePane, ChartImagePane

public class RangePlotPane extends AbstractChartTypePane
{

    private static final long serialVersionUID = 0xf7a6cebbcb81992dL;
    private static final int RANGE = 0;

    public RangePlotPane()
    {
    }

    protected String[] getTypeIconPath()
    {
        return (new String[] {
            "/com/fr/design/images/chart/RangePlot/type/0.png"
        });
    }

    protected String[] getTypeTipName()
    {
        return (new String[] {
            Inter.getLocText("ChartF-Range_Chart")
        });
    }

    protected String getPlotTypeID()
    {
        return "FineReportRangeChart";
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
        return Inter.getLocText("ChartF-Range_Chart");
    }

    public void updateBean(Chart chart)
    {
        if(needsResetChart(chart))
            resetChart(chart);
        RangePlot rangeplot = new RangePlot();
        chart.switchPlot(rangeplot);
    }

    public void populateBean(Chart chart)
    {
        ((ChartImagePane)typeDemo.get(0)).isPressing = true;
        checkDemosBackground();
    }

    public Chart getDefaultChart()
    {
        return RangeIndependentChart.rangeChartTypes[0];
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
