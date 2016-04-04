// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.chart.gui.style.series;

import com.fr.chart.chartattr.Bar2DPlot;
import com.fr.chart.chartattr.Plot;
import com.fr.design.gui.frpane.UINumberDragPane;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.chart.gui.ChartStylePane;
import com.fr.design.mainframe.chart.gui.style.ChartBeautyPane;
import com.fr.general.Inter;
import java.awt.Component;
import javax.swing.JPanel;
import javax.swing.JSeparator;

// Referenced classes of package com.fr.design.mainframe.chart.gui.style.series:
//            AbstractPlotSeriesPane

public class Bar2DSeriesPane extends AbstractPlotSeriesPane
{

    protected ChartBeautyPane stylePane;
    private UINumberDragPane seriesGap;
    private UINumberDragPane categoryGap;
    private static final double HUNDRED = 100D;
    private static final double FIVE = 5D;

    public Bar2DSeriesPane(ChartStylePane chartstylepane, Plot plot)
    {
        super(chartstylepane, plot, false);
    }

    public Bar2DSeriesPane(ChartStylePane chartstylepane, Plot plot, boolean flag)
    {
        super(chartstylepane, plot, true);
    }

    protected JPanel getContentInPlotType()
    {
        seriesGap = new UINumberDragPane(-100D, 100D);
        categoryGap = new UINumberDragPane(0.0D, 500D);
        stylePane = new ChartBeautyPane();
        double d = -2D;
        double d1 = -1D;
        double ad[] = {
            d, d1
        };
        double ad1[] = {
            d, d, d, d
        };
        Component acomponent[][] = {
            {
                stylePane, null
            }, {
                new JSeparator(), null
            }, {
                new UILabel(Inter.getLocText("FR-Chart-Gap_Series")), seriesGap
            }, {
                new UILabel(Inter.getLocText("FR-Chart-Gap_Category")), categoryGap
            }
        };
        return TableLayoutHelper.createTableLayoutPane(acomponent, ad1, ad);
    }

    public void populateBean(Plot plot)
    {
        super.populateBean(plot);
        if(plot instanceof Bar2DPlot)
        {
            Bar2DPlot bar2dplot = (Bar2DPlot)plot;
            if(stylePane != null)
                stylePane.populateBean(Integer.valueOf(bar2dplot.getPlotStyle()));
            if(seriesGap != null)
                seriesGap.populateBean(Double.valueOf(bar2dplot.getSeriesOverlapPercent() * 100D));
            if(categoryGap != null)
                categoryGap.populateBean(Double.valueOf(bar2dplot.getCategoryIntervalPercent() * 100D));
        }
    }

    public void updateBean(Plot plot)
    {
        super.updateBean(plot);
        if(plot instanceof Bar2DPlot)
        {
            Bar2DPlot bar2dplot = (Bar2DPlot)plot;
            if(stylePane != null)
                bar2dplot.setPlotStyle(stylePane.updateBean().intValue());
            if(seriesGap != null && !bar2dplot.isStacked())
                bar2dplot.setSeriesOverlapPercent(seriesGap.updateBean().doubleValue() / 100D);
            if(categoryGap != null)
                bar2dplot.setCategoryIntervalPercent(categoryGap.updateBean().doubleValue() / 100D);
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
