// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.chart.gui.type;

import com.fr.base.FRContext;
import com.fr.chart.base.TextAttr;
import com.fr.chart.chartattr.*;
import com.fr.chart.charttypes.BubbleIndependentChart;
import com.fr.design.mainframe.ChartDesigner;
import com.fr.general.*;
import java.util.ArrayList;
import java.util.List;

// Referenced classes of package com.fr.design.mainframe.chart.gui.type:
//            PlotPane4ToolBar, ChartDesignerImagePane

public class BubblePlotPane4ToolBar extends PlotPane4ToolBar
{

    private static final int BUBBLE_CHART = 0;

    public BubblePlotPane4ToolBar(ChartDesigner chartdesigner)
    {
        super(chartdesigner);
    }

    protected String getTypeIconPath()
    {
        return "com/fr/design/images/toolbar/bubble/";
    }

    protected List initDemoList()
    {
        ArrayList arraylist = new ArrayList();
        ChartDesignerImagePane chartdesignerimagepane = new ChartDesignerImagePane(getTypeIconPath(), 0, Inter.getLocText("FR-Chart-Chart_BubbleChart"), this);
        chartdesignerimagepane.setSelected(true);
        arraylist.add(chartdesignerimagepane);
        return arraylist;
    }

    protected Plot getSelectedClonedPlot()
    {
        Chart achart[] = BubbleIndependentChart.bubbleChartTypes;
        BubblePlot bubbleplot = (BubblePlot)achart[getSelectedIndex()].getPlot();
        setChartFontAttr(bubbleplot);
        Plot plot = null;
        try
        {
            plot = (Plot)bubbleplot.clone();
        }
        catch(CloneNotSupportedException clonenotsupportedexception)
        {
            FRLogger.getLogger().error("Error In BubbleChart");
        }
        return plot;
    }

    public static void setChartFontAttr(BubblePlot bubbleplot)
    {
        if(bubbleplot.getxAxis() != null)
        {
            TextAttr textattr = new TextAttr();
            textattr.setFRFont(FRContext.getDefaultValues().getFRFont());
            bubbleplot.getxAxis().setTextAttr(textattr);
        }
        if(bubbleplot.getyAxis() != null)
        {
            TextAttr textattr1 = new TextAttr();
            textattr1.setFRFont(FRContext.getDefaultValues().getFRFont());
            bubbleplot.getyAxis().setTextAttr(textattr1);
        }
    }
}
