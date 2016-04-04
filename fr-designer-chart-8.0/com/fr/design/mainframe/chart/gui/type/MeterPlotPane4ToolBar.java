// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.chart.gui.type;

import com.fr.base.FRContext;
import com.fr.chart.base.TextAttr;
import com.fr.chart.chartattr.*;
import com.fr.chart.chartglyph.MeterStyle;
import com.fr.chart.charttypes.MeterIndependentChart;
import com.fr.design.mainframe.ChartDesigner;
import com.fr.general.*;
import java.util.ArrayList;
import java.util.List;

// Referenced classes of package com.fr.design.mainframe.chart.gui.type:
//            PlotPane4ToolBar, ChartDesignerImagePane

public class MeterPlotPane4ToolBar extends PlotPane4ToolBar
{

    private static final int METER = 0;
    private static final int BLUE_METER = 1;
    private static final int SIMPLE_METER = 2;

    public MeterPlotPane4ToolBar(ChartDesigner chartdesigner)
    {
        super(chartdesigner);
    }

    protected String getTypeIconPath()
    {
        return "com/fr/design/images/toolbar/meter/";
    }

    protected List initDemoList()
    {
        ArrayList arraylist = new ArrayList();
        ChartDesignerImagePane chartdesignerimagepane = new ChartDesignerImagePane(getTypeIconPath(), 0, Inter.getLocText("FR-Chart-Type_Meter"), this);
        chartdesignerimagepane.setSelected(true);
        arraylist.add(chartdesignerimagepane);
        arraylist.add(new ChartDesignerImagePane(getTypeIconPath(), 1, (new StringBuilder()).append(Inter.getLocText("FR-Chart-Type_Meter")).append(1).toString(), this));
        arraylist.add(new ChartDesignerImagePane(getTypeIconPath(), 2, (new StringBuilder()).append(Inter.getLocText("FR-Chart-Type_Meter")).append(2).toString(), this));
        return arraylist;
    }

    protected Plot getSelectedClonedPlot()
    {
        Chart achart[] = MeterIndependentChart.meterChartTypes;
        MeterPlot meterplot = (MeterPlot)achart[getSelectedIndex()].getPlot();
        setChartFontAttr4MeterStyle(meterplot);
        Plot plot = null;
        try
        {
            plot = (Plot)meterplot.clone();
        }
        catch(CloneNotSupportedException clonenotsupportedexception)
        {
            FRLogger.getLogger().error("Error In MeterChart");
        }
        return plot;
    }

    public static void setChartFontAttr4MeterStyle(MeterPlot meterplot)
    {
        if(meterplot.getMeterStyle() != null)
            meterplot.getMeterStyle().setTitleTextAttr(new TextAttr(FRContext.getDefaultValues().getFRFont()));
    }
}
