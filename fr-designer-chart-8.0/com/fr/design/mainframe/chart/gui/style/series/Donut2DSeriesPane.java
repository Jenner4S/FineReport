// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.chart.gui.style.series;

import com.fr.chart.chartattr.Donut2DPlot;
import com.fr.chart.chartattr.Plot;
import com.fr.design.gui.frpane.UINumberDragPane;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.chart.gui.ChartStylePane;
import com.fr.design.mainframe.chart.gui.style.ChartBeautyPane;
import java.awt.Component;
import javax.swing.JPanel;
import javax.swing.JSeparator;

// Referenced classes of package com.fr.design.mainframe.chart.gui.style.series:
//            AbstractPlotSeriesPane

public class Donut2DSeriesPane extends AbstractPlotSeriesPane
{

    private ChartBeautyPane stylePane;
    private UINumberDragPane innerRadiusPercent;
    private UINumberDragPane seriesGap;
    private UINumberDragPane categoryGap;
    private static final double MAXSERIESGAP = 50D;
    private static final double MAXCATEGORYGAP = 100D;
    private static final double MINGAP = 0D;
    private static final double MININNERPERCENT = 10D;
    private static final double MAXINNERPERCENT = 90D;
    private static final double HUNDRED = 100D;

    public Donut2DSeriesPane(ChartStylePane chartstylepane, Plot plot)
    {
        super(chartstylepane, plot, false);
    }

    public Donut2DSeriesPane(ChartStylePane chartstylepane, Plot plot, boolean flag)
    {
        super(chartstylepane, plot, flag);
    }

    protected JPanel getContentInPlotType()
    {
        stylePane = new ChartBeautyPane();
        seriesGap = new UINumberDragPane(0.0D, 50D);
        categoryGap = new UINumberDragPane(0.0D, 100D);
        innerRadiusPercent = new UINumberDragPane(10D, 90D);
        double d = -2D;
        double d1 = -1D;
        double ad[] = {
            d1
        };
        double ad1[] = {
            d
        };
        double ad2[] = {
            d, d1
        };
        double ad3[] = {
            d, d, d, d, d
        };
        Component acomponent[][] = {
            {
                stylePane, null
            }, {
                new JSeparator(), null
            }, {
                null, TableLayoutHelper.createTableLayoutPane4Chart(new String[] {
                    "InnerRadis"
                }, new Component[][] {
                    new Component[] {
                        innerRadiusPercent
                    }
                }, ad1, ad)
            }, {
                null, TableLayoutHelper.createTableLayoutPane4Chart(new String[] {
                    "FR-Chart-Gap_Series"
                }, new Component[][] {
                    new Component[] {
                        seriesGap
                    }
                }, ad1, ad)
            }, {
                null, TableLayoutHelper.createTableLayoutPane4Chart(new String[] {
                    "FR-Chart-Gap_Category"
                }, new Component[][] {
                    new Component[] {
                        categoryGap
                    }
                }, ad1, ad)
            }
        };
        return TableLayoutHelper.createTableLayoutPane(acomponent, ad3, ad2);
    }

    public void populateBean(Plot plot)
    {
        super.populateBean(plot);
        if(plot instanceof Donut2DPlot)
        {
            Donut2DPlot donut2dplot = (Donut2DPlot)plot;
            if(stylePane != null)
                stylePane.populateBean(Integer.valueOf(donut2dplot.getPlotStyle()));
            if(innerRadiusPercent != null)
                innerRadiusPercent.populateBean(Double.valueOf(donut2dplot.getInnerRadiusPercent() * 100D));
            if(seriesGap != null)
                seriesGap.populateBean(Double.valueOf(donut2dplot.getSeriesGap() * 100D));
            if(categoryGap != null)
                categoryGap.populateBean(Double.valueOf(donut2dplot.getCategoryGap() * 100D));
        }
    }

    public void updateBean(Plot plot)
    {
        super.updateBean(plot);
        if(plot instanceof Donut2DPlot)
        {
            Donut2DPlot donut2dplot = (Donut2DPlot)plot;
            if(stylePane != null)
                donut2dplot.setPlotStyle(stylePane.updateBean().intValue());
            if(innerRadiusPercent != null)
                donut2dplot.setInnerRadiusPercent(innerRadiusPercent.updateBean().doubleValue() / 100D);
            if(seriesGap != null)
                donut2dplot.setSeriesGap(seriesGap.updateBean().doubleValue() / 100D);
            if(categoryGap != null)
                donut2dplot.setCategoryGap(categoryGap.updateBean().doubleValue() / 100D);
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
