// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.chart.gui.type;

import com.fr.chart.base.AttrLineStyle;
import com.fr.chart.chartattr.Chart;
import com.fr.chart.chartattr.XYScatterPlot;
import com.fr.chart.chartglyph.ConditionAttr;
import com.fr.chart.chartglyph.ConditionCollection;
import com.fr.chart.charttypes.XYScatterIndependentChart;
import com.fr.general.Inter;
import java.util.List;

// Referenced classes of package com.fr.design.mainframe.chart.gui.type:
//            AbstractChartTypePane, ChartImagePane

public class XYScatterPlotPane extends AbstractChartTypePane
{

    private static final long serialVersionUID = 0xf7a6cebbcb81992dL;
    private static final int XYSCATTER_CHART = 0;

    public XYScatterPlotPane()
    {
    }

    protected String[] getTypeIconPath()
    {
        return (new String[] {
            "/com/fr/design/images/chart/XYScatterPlot/type/0.png"
        });
    }

    protected String[] getTypeTipName()
    {
        return (new String[] {
            Inter.getLocText("I-xyScatterStyle_Marker")
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
        return Inter.getLocText("I-xyScatterStyle_Marker");
    }

    public void updateBean(Chart chart)
    {
        if(needsResetChart(chart))
            resetChart(chart);
        XYScatterPlot xyscatterplot = new XYScatterPlot();
        chart.switchPlot(xyscatterplot);
        ConditionAttr conditionattr = xyscatterplot.getConditionCollection().getDefaultAttr();
        AttrLineStyle attrlinestyle = (AttrLineStyle)conditionattr.getConditionInType("AttrLineStyle");
        if(attrlinestyle != null)
            conditionattr.remove(attrlinestyle);
        conditionattr.addDataSeriesCondition(new AttrLineStyle(0));
    }

    protected String getPlotTypeID()
    {
        return "FineReportScatterChart";
    }

    public void populateBean(Chart chart)
    {
        ((ChartImagePane)typeDemo.get(0)).isPressing = true;
        checkDemosBackground();
    }

    public Chart getDefaultChart()
    {
        return XYScatterIndependentChart.XYScatterChartTypes[0];
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
