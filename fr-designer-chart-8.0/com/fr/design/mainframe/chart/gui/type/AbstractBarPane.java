// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.chart.gui.type;

import com.fr.chart.chartattr.*;
import java.util.List;

// Referenced classes of package com.fr.design.mainframe.chart.gui.type:
//            AbstractChartTypePane, ChartImagePane

public abstract class AbstractBarPane extends AbstractChartTypePane
{

    protected static final int COLOMN_CHART = 0;
    protected static final int STACK_COLOMN_CHART = 1;
    protected static final int PERCENT_STACK_COLOMN_CHART = 2;
    protected static final int THREE_D_COLOMN_CHART = 3;
    protected static final int THREE_D_COLOMN_HORIZON_DRAW_CHART = 4;
    protected static final int THREE_D_STACK_COLOMN_CHART = 5;
    protected static final int THREE_D_PERCENT_STACK_COLOMN_CHART = 6;

    public AbstractBarPane()
    {
    }

    public void populateBean(Chart chart)
    {
        super.populateBean(chart);
        BarPlot barplot = (BarPlot)chart.getPlot();
        if(barplot.isStacked())
        {
            if(barplot.getyAxis().isPercentage())
            {
                if(barplot instanceof Bar3DPlot)
                {
                    ((ChartImagePane)typeDemo.get(6)).isPressing = true;
                    lastTypeIndex = 6;
                } else
                {
                    ((ChartImagePane)typeDemo.get(2)).isPressing = true;
                    lastTypeIndex = 2;
                }
            } else
            if(barplot instanceof Bar3DPlot)
            {
                ((ChartImagePane)typeDemo.get(5)).isPressing = true;
                lastTypeIndex = 5;
            } else
            {
                ((ChartImagePane)typeDemo.get(1)).isPressing = true;
                lastTypeIndex = 1;
            }
        } else
        if(barplot instanceof Bar3DPlot)
        {
            if(((Bar3DPlot)barplot).isHorizontalDrawBar())
            {
                ((ChartImagePane)typeDemo.get(4)).isPressing = true;
                lastTypeIndex = 4;
            } else
            {
                ((ChartImagePane)typeDemo.get(3)).isPressing = true;
                lastTypeIndex = 3;
            }
        } else
        {
            ((ChartImagePane)typeDemo.get(0)).isPressing = true;
            lastTypeIndex = 0;
        }
        checkDemosBackground();
    }

    public volatile void populateBean(Object obj)
    {
        populateBean((Chart)obj);
    }
}
