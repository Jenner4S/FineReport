// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.chart.gui.type;

import com.fr.base.CoreDecimalFormat;
import com.fr.chart.base.AttrBorder;
import com.fr.chart.base.AttrContents;
import com.fr.chart.chartattr.Chart;
import com.fr.chart.chartattr.Pie3DPlot;
import com.fr.chart.chartattr.PiePlot;
import com.fr.chart.chartattr.Plot;
import com.fr.chart.chartglyph.ConditionAttr;
import com.fr.chart.chartglyph.ConditionCollection;
import com.fr.chart.charttypes.PieIndependentChart;
import com.fr.general.Inter;
import java.awt.Color;
import java.text.DecimalFormat;
import java.util.Iterator;

// Referenced classes of package com.fr.design.mainframe.chart.gui.type:
//            AbstractChartTypePane, ChartImagePane

public class PiePlotPane extends AbstractChartTypePane
{

    private static final long serialVersionUID = 0xf7a6cebbcb81992dL;
    private static final int PIE_CHART = 0;
    private static final int THREE_D_PIE_CHART = 1;

    public PiePlotPane()
    {
    }

    protected String[] getTypeIconPath()
    {
        return (new String[] {
            "/com/fr/design/images/chart/PiePlot/type/0.png", "/com/fr/design/images/chart/PiePlot/type/1.png"
        });
    }

    protected String[] getTypeTipName()
    {
        String s = Inter.getLocText("I-PieStyle_Normal");
        return (new String[] {
            s, (new StringBuilder()).append(Inter.getLocText("FR-Chart-Chart_3D")).append(s).toString()
        });
    }

    protected String[] getTypeLayoutPath()
    {
        return (new String[] {
            "/com/fr/design/images/chart/PiePlot/layout/0.png", "/com/fr/design/images/chart/PiePlot/layout/1.png", "/com/fr/design/images/chart/PiePlot/layout/2.png", "/com/fr/design/images/chart/PiePlot/layout/3.png"
        });
    }

    protected String[] getTypeLayoutTipName()
    {
        return getNormalLayoutTipName();
    }

    public String title4PopupWindow()
    {
        return Inter.getLocText("I-PieStyle_Normal");
    }

    private void createPieCondition(Plot plot)
    {
        ConditionCollection conditioncollection = plot.getConditionCollection();
        AttrBorder attrborder = (AttrBorder)conditioncollection.getDefaultAttr().getExisted(com/fr/chart/base/AttrBorder);
        if(attrborder == null)
        {
            attrborder = new AttrBorder();
            conditioncollection.getDefaultAttr().addDataSeriesCondition(attrborder);
        }
        attrborder.setBorderColor(Color.WHITE);
        attrborder.setBorderStyle(1);
        AttrContents attrcontents = new AttrContents("${VALUE}${PERCENT}");
        plot.setHotTooltipStyle(attrcontents);
        attrcontents.setFormat(new CoreDecimalFormat(new DecimalFormat(), "#.##"));
        attrcontents.setPercentFormat(new CoreDecimalFormat(new DecimalFormat(), "#.##%"));
    }

    public void populateBean(Chart chart)
    {
        super.populateBean(chart);
        for(Iterator iterator = typeDemo.iterator(); iterator.hasNext();)
        {
            ChartImagePane chartimagepane = (ChartImagePane)iterator.next();
            chartimagepane.isPressing = false;
        }

        Plot plot = chart.getPlot();
        if(plot instanceof Pie3DPlot)
        {
            ((ChartImagePane)typeDemo.get(1)).isPressing = true;
            lastTypeIndex = 1;
        } else
        {
            ((ChartImagePane)typeDemo.get(0)).isPressing = true;
            lastTypeIndex = 0;
        }
        checkDemosBackground();
    }

    protected Plot getSelectedClonedPlot()
    {
        Object obj;
        if(((ChartImagePane)typeDemo.get(0)).isPressing)
            obj = new PiePlot();
        else
            obj = new Pie3DPlot();
        createPieCondition(((Plot) (obj)));
        return ((Plot) (obj));
    }

    public void updateBean(Chart chart)
    {
        chart.switchPlot(getSelectedClonedPlot());
        super.updateBean(chart);
    }

    protected String getPlotTypeID()
    {
        return "FineReportPieChart";
    }

    public Chart getDefaultChart()
    {
        return PieIndependentChart.pieChartTypes[0];
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
