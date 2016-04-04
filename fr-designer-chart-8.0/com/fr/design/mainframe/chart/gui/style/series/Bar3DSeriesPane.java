// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.chart.gui.style.series;

import com.fr.chart.chartattr.Bar3DPlot;
import com.fr.chart.chartattr.Plot;
import com.fr.design.gui.frpane.UINumberDragPane;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.chart.gui.ChartStylePane;
import com.fr.general.Inter;
import java.awt.Component;
import javax.swing.JPanel;

// Referenced classes of package com.fr.design.mainframe.chart.gui.style.series:
//            AbstractPlotSeriesPane

public class Bar3DSeriesPane extends AbstractPlotSeriesPane
{

    private UINumberDragPane seriesGap;
    private UINumberDragPane categoryGap;
    private static final double HUNDRED = 100D;
    private static final double FIVE = 5D;

    public Bar3DSeriesPane(ChartStylePane chartstylepane, Plot plot)
    {
        super(chartstylepane, plot, false);
    }

    protected JPanel getContentInPlotType()
    {
        JPanel jpanel = null;
        if(plot instanceof Bar3DPlot)
        {
            Bar3DPlot bar3dplot = (Bar3DPlot)plot;
            if(bar3dplot.isHorizontalDrawBar())
            {
                seriesGap = new UINumberDragPane(-100D, 100D);
                categoryGap = new UINumberDragPane(0.0D, 500D);
                double d = -2D;
                double d1 = -1D;
                double ad[] = {
                    d, d1
                };
                double ad1[] = {
                    d, d
                };
                Component acomponent[][] = {
                    {
                        new UILabel(Inter.getLocText("FR-Chart-Gap_Series")), seriesGap
                    }, {
                        new UILabel(Inter.getLocText("FR-Chart-Gap_Category")), categoryGap
                    }
                };
                jpanel = TableLayoutHelper.createTableLayoutPane(acomponent, ad1, ad);
            }
        }
        return jpanel;
    }

    public void populateBean(Plot plot)
    {
        super.populateBean(plot);
        if(plot instanceof Bar3DPlot)
        {
            Bar3DPlot bar3dplot = (Bar3DPlot)plot;
            if(seriesGap != null)
                seriesGap.populateBean(Double.valueOf(bar3dplot.getSeriesOverlapPercent() * 100D));
            if(categoryGap != null)
                categoryGap.populateBean(Double.valueOf(bar3dplot.getCategoryIntervalPercent() * 100D));
        }
    }

    public void updateBean(Plot plot)
    {
        super.updateBean(plot);
        if(plot instanceof Bar3DPlot)
        {
            Bar3DPlot bar3dplot = (Bar3DPlot)plot;
            if(seriesGap != null)
                bar3dplot.setSeriesOverlapPercent(seriesGap.updateBean().doubleValue() / 100D);
            if(categoryGap != null)
                bar3dplot.setCategoryIntervalPercent(categoryGap.updateBean().doubleValue() / 100D);
        }
    }

    public volatile void updateBean(Object obj)
    {
        updateBean((Plot)obj);
    }

    public volatile void populateBean(Object obj)
    {
        populateBean((Plot)obj);
    }
}
