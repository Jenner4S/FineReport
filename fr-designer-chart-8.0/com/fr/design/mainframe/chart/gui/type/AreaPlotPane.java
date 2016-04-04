// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.chart.gui.type;

import com.fr.chart.base.AttrAlpha;
import com.fr.chart.chartattr.*;
import com.fr.chart.chartglyph.ConditionAttr;
import com.fr.chart.chartglyph.ConditionCollection;
import com.fr.chart.charttypes.AreaIndependentChart;
import com.fr.general.Inter;
import java.util.List;

// Referenced classes of package com.fr.design.mainframe.chart.gui.type:
//            AbstractChartTypePane, ChartImagePane

public class AreaPlotPane extends AbstractChartTypePane
{

    private static final int STACK_AREA_CHART = 0;
    private static final int PERCENT_AREA_LINE_CHART = 1;
    private static final int STACK_3D_AREA_CHART = 2;
    private static final int PERCENT_3D_AREA_LINE_CHART = 3;

    public AreaPlotPane()
    {
    }

    protected String[] getTypeIconPath()
    {
        return (new String[] {
            "/com/fr/design/images/chart/AreaPlot/type/0.png", "/com/fr/design/images/chart/AreaPlot/type/1.png", "/com/fr/design/images/chart/AreaPlot/type/2.png", "/com/fr/design/images/chart/AreaPlot/type/3.png"
        });
    }

    protected String[] getTypeTipName()
    {
        String s = Inter.getLocText("FR-Chart-Type_Area");
        String s1 = Inter.getLocText("FR-Chart-Type_Stacked");
        String s2 = Inter.getLocText("FR-Chart-Use_Percent");
        String s3 = Inter.getLocText("FR-Chart-Chart_3D");
        return (new String[] {
            (new StringBuilder()).append(s1).append(s).toString(), (new StringBuilder()).append(s2).append(s1).append(s).toString(), (new StringBuilder()).append(s3).append(s1).append(s).toString(), (new StringBuilder()).append(s3).append(s2).append(s1).append(s).toString()
        });
    }

    protected String getPlotTypeID()
    {
        return "FineReportAreaChart";
    }

    protected String[] getTypeLayoutPath()
    {
        return (new String[] {
            "/com/fr/design/images/chart/AreaPlot/layout/0.png", "/com/fr/design/images/chart/AreaPlot/layout/1.png", "/com/fr/design/images/chart/AreaPlot/layout/2.png", "/com/fr/design/images/chart/AreaPlot/layout/3.png"
        });
    }

    protected String[] getTypeLayoutTipName()
    {
        return getNormalLayoutTipName();
    }

    public void populateBean(Chart chart)
    {
        super.populateBean(chart);
        Plot plot = chart.getPlot();
        if(plot instanceof AreaPlot)
        {
            AreaPlot areaplot = (AreaPlot)plot;
            if(areaplot.isStacked())
                if(areaplot.getyAxis().isPercentage())
                {
                    ((ChartImagePane)typeDemo.get(1)).isPressing = true;
                    lastTypeIndex = 1;
                } else
                {
                    ((ChartImagePane)typeDemo.get(0)).isPressing = true;
                    lastTypeIndex = 0;
                }
        } else
        if(plot instanceof Area3DPlot)
        {
            Area3DPlot area3dplot = (Area3DPlot)plot;
            if(area3dplot.isStacked())
                if(area3dplot.getyAxis().isPercentage())
                {
                    ((ChartImagePane)typeDemo.get(3)).isPressing = true;
                    lastTypeIndex = 3;
                } else
                {
                    ((ChartImagePane)typeDemo.get(2)).isPressing = true;
                    lastTypeIndex = 2;
                }
        }
        checkDemosBackground();
    }

    protected Plot getSelectedClonedPlot()
    {
        Object obj = null;
        if(((ChartImagePane)typeDemo.get(0)).isPressing)
        {
            obj = new AreaPlot();
            ((AreaPlot)obj).setStacked(true);
        } else
        if(((ChartImagePane)typeDemo.get(1)).isPressing)
        {
            obj = new AreaPlot();
            ((AreaPlot)obj).setStacked(true);
            ((AreaPlot)obj).getyAxis().setPercentage(true);
        } else
        if(((ChartImagePane)typeDemo.get(2)).isPressing)
        {
            obj = new Area3DPlot();
            ((Area3DPlot)obj).setStacked(true);
        } else
        if(((ChartImagePane)typeDemo.get(3)).isPressing)
        {
            obj = new Area3DPlot();
            ((Area3DPlot)obj).setStacked(true);
            ((Area3DPlot)obj).getyAxis().setPercentage(true);
        }
        createAreaCondition(((Plot) (obj)));
        return ((Plot) (obj));
    }

    public void updateBean(Chart chart)
    {
        chart.switchPlot(getSelectedClonedPlot());
        super.updateBean(chart);
    }

    private void createAreaCondition(Plot plot)
    {
        ConditionCollection conditioncollection = plot.getConditionCollection();
        AttrAlpha attralpha = (AttrAlpha)conditioncollection.getDefaultAttr().getExisted(com/fr/chart/base/AttrAlpha);
        if(attralpha == null)
        {
            attralpha = new AttrAlpha();
            conditioncollection.getDefaultAttr().addDataSeriesCondition(attralpha);
        }
        attralpha.setAlpha(0.7F);
    }

    public String title4PopupWindow()
    {
        return Inter.getLocText("FR-Chart-Type_Area");
    }

    public Chart getDefaultChart()
    {
        return AreaIndependentChart.areaChartTypes[0];
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
