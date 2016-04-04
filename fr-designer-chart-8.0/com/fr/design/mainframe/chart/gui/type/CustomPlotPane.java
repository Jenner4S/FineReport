// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.chart.gui.type;

import com.fr.chart.chartattr.Chart;
import com.fr.chart.charttypes.CustomIndependentChart;
import com.fr.general.Inter;
import java.util.List;

// Referenced classes of package com.fr.design.mainframe.chart.gui.type:
//            AbstractChartTypePane, ChartImagePane

public class CustomPlotPane extends AbstractChartTypePane
{

    public CustomPlotPane()
    {
    }

    protected String getPlotTypeID()
    {
        return "FineReportCustomChart";
    }

    protected String[] getTypeIconPath()
    {
        return (new String[] {
            "/com/fr/design/images/chart/CustomPlot/type/0.png"
        });
    }

    protected String[] getTypeTipName()
    {
        return (new String[] {
            Inter.getLocText("ChartF-Comb_Chart")
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
        return Inter.getLocText("ChartF-Comb_Chart");
    }

    public void populateBean(Chart chart)
    {
        super.populateBean(chart);
        ((ChartImagePane)typeDemo.get(0)).isPressing = true;
        checkDemosBackground();
    }

    public void updateBean(Chart chart)
    {
        changePlotWithClone(chart, CustomIndependentChart.createCustomChart().getPlot());
    }

    public Chart getDefaultChart()
    {
        return CustomIndependentChart.combChartTypes[0];
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
