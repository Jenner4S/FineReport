// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.chart.gui.style.series;

import com.fr.chart.chartattr.Area3DPlot;
import com.fr.chart.chartattr.Plot;
import com.fr.design.gui.frpane.UINumberDragPane;
import com.fr.design.gui.ilable.BoldFontTextLabel;
import com.fr.design.mainframe.chart.gui.ChartStylePane;
import com.fr.general.Inter;
import java.awt.BorderLayout;
import javax.swing.JPanel;

// Referenced classes of package com.fr.design.mainframe.chart.gui.style.series:
//            AbstractPlotSeriesPane

public class Area3DSeriesPane extends AbstractPlotSeriesPane
{

    private UINumberDragPane gapPane;
    private static final double HUNDRED = 100D;

    public Area3DSeriesPane(ChartStylePane chartstylepane, Plot plot)
    {
        super(chartstylepane, plot, false);
    }

    protected JPanel getContentInPlotType()
    {
        JPanel jpanel = new JPanel();
        jpanel.setLayout(new BorderLayout());
        jpanel.add(new BoldFontTextLabel((new StringBuilder()).append(Inter.getLocText("Pitch_Percentage")).append(":").toString()), "West");
        jpanel.add(gapPane = new UINumberDragPane(0.0D, 500D), "Center");
        gapPane.populateBean(Double.valueOf(100D));
        return jpanel;
    }

    public void populateBean(Plot plot)
    {
        if(plot instanceof Area3DPlot)
        {
            Area3DPlot area3dplot = (Area3DPlot)plot;
            super.populateBean(plot);
            gapPane.populateBean(Double.valueOf(area3dplot.getSeriesIntervalPercent() * 100D));
        }
    }

    public void updateBean(Plot plot)
    {
        if(plot instanceof Area3DPlot)
        {
            Area3DPlot area3dplot = (Area3DPlot)plot;
            super.updateBean(plot);
            area3dplot.setSeriesIntervalPercent(gapPane.updateBean().doubleValue() / 100D);
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
