// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.chart.gui.type;

import com.fr.base.FRContext;
import com.fr.chart.base.TextAttr;
import com.fr.chart.chartattr.*;
import com.fr.chart.charttypes.XYScatterIndependentChart;
import com.fr.design.mainframe.ChartDesigner;
import com.fr.general.*;
import java.util.ArrayList;
import java.util.List;

// Referenced classes of package com.fr.design.mainframe.chart.gui.type:
//            PlotPane4ToolBar, ChartDesignerImagePane

public class XYSCatterPlotPane4ToolBar extends PlotPane4ToolBar
{

    private static final int XYSCATTER_CHART = 0;

    public XYSCatterPlotPane4ToolBar(ChartDesigner chartdesigner)
    {
        super(chartdesigner);
    }

    protected String getTypeIconPath()
    {
        return "com/fr/design/images/toolbar/xyscatter/";
    }

    protected List initDemoList()
    {
        ArrayList arraylist = new ArrayList();
        ChartDesignerImagePane chartdesignerimagepane = new ChartDesignerImagePane(getTypeIconPath(), 0, Inter.getLocText("FR-Chart-Type_XYScatter"), this);
        chartdesignerimagepane.setSelected(true);
        arraylist.add(chartdesignerimagepane);
        return arraylist;
    }

    protected Plot getSelectedClonedPlot()
    {
        Chart achart[] = XYScatterIndependentChart.XYScatterChartTypes;
        XYPlot xyplot = (XYPlot)achart[getSelectedIndex()].getPlot();
        setChartFontAttr(xyplot);
        Plot plot = null;
        try
        {
            plot = (Plot)xyplot.clone();
        }
        catch(CloneNotSupportedException clonenotsupportedexception)
        {
            FRLogger.getLogger().error("Error In XYScatterChart");
        }
        return plot;
    }

    public static void setChartFontAttr(XYPlot xyplot)
    {
        if(xyplot.getxAxis() != null)
        {
            TextAttr textattr = new TextAttr();
            textattr.setFRFont(FRContext.getDefaultValues().getFRFont());
            xyplot.getxAxis().setTextAttr(textattr);
        }
        if(xyplot.getyAxis() != null)
        {
            TextAttr textattr1 = new TextAttr();
            textattr1.setFRFont(FRContext.getDefaultValues().getFRFont());
            xyplot.getyAxis().setTextAttr(textattr1);
        }
    }
}
