// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.chart.gui.type;

import com.fr.base.CoreDecimalFormat;
import com.fr.chart.base.AttrContents;
import com.fr.chart.chartattr.*;
import com.fr.chart.charttypes.FunnelIndependentChart;
import com.fr.general.Inter;
import java.text.DecimalFormat;
import java.util.List;

// Referenced classes of package com.fr.design.mainframe.chart.gui.type:
//            AbstractChartTypePane, ChartImagePane

public class FunnelPlotPane extends AbstractChartTypePane
{

    private static final int FUNNEL_CHART = 0;

    public FunnelPlotPane()
    {
    }

    protected String[] getTypeIconPath()
    {
        return (new String[] {
            "/com/fr/design/images/chart/FunnelPlot/type/0.png"
        });
    }

    protected String[] getTypeTipName()
    {
        return (new String[] {
            Inter.getLocText("FR-Chart-Type_Funnel")
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
        return Inter.getLocText("FR-Chart-Type_Funnel");
    }

    public void populateBean(Chart chart)
    {
        super.populateBean(chart);
        ((ChartImagePane)typeDemo.get(0)).isPressing = true;
        checkDemosBackground();
    }

    public void updateBean(Chart chart)
    {
        chart.switchPlot(getSelectedClonedPlot());
        super.updateBean(chart);
    }

    protected String getPlotTypeID()
    {
        return "FineReportFunnelChart";
    }

    protected Plot getSelectedClonedPlot()
    {
        FunnelPlot funnelplot = new FunnelPlot();
        createFunnelCondition(funnelplot);
        return funnelplot;
    }

    private void createFunnelCondition(Plot plot)
    {
        AttrContents attrcontents = new AttrContents("${VALUE}${PERCENT}");
        plot.setHotTooltipStyle(attrcontents);
        attrcontents.setFormat(new CoreDecimalFormat(new DecimalFormat(), "#.##"));
        attrcontents.setPercentFormat(new CoreDecimalFormat(new DecimalFormat(), "#.##%"));
    }

    public Chart getDefaultChart()
    {
        return FunnelIndependentChart.funnelChartTypes[0];
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
