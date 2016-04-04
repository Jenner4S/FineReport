// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.chart.gui.style.area;

import com.fr.chart.chartattr.Chart;
import com.fr.chart.chartattr.Plot;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.chart.PaneTitleConstants;
import com.fr.design.mainframe.chart.gui.ChartStylePane;
import com.fr.design.mainframe.chart.gui.style.*;
import java.awt.BorderLayout;
import java.awt.Component;
import javax.swing.JPanel;

// Referenced classes of package com.fr.design.mainframe.chart.gui.style.area:
//            RadarAxisAreaPane, Plot3DAxisAreaPane, DefaultAxisAreaPane, ChartAxisAreaPane

public class ChartPlotAreaPane extends AbstractChartTabPane
{

    private static final long serialVersionUID = 0xdc1190abf3cf1536L;
    private static final int WIDTH = 220;
    private ChartBackgroundPane plotBackgroundPane;
    private ChartBackgroundNoImagePane plotNoImagePane;
    private ChartAxisAreaPane axisAreaPane;
    private ChartBorderPane plotBorderPane;
    private JPanel contentPane;
    private ChartStylePane parent;

    public ChartPlotAreaPane()
    {
    }

    public String title4PopupWindow()
    {
        return PaneTitleConstants.CHART_STYLE_AREA_PLOT_TITLE;
    }

    public void setParentPane(ChartStylePane chartstylepane)
    {
        parent = chartstylepane;
    }

    protected JPanel createContentPane()
    {
        contentPane = new JPanel();
        plotBackgroundPane = new ChartBackgroundPane();
        plotNoImagePane = new ChartBackgroundNoImagePane();
        plotBorderPane = new ChartBorderPane();
        contentPane.setLayout(new BorderLayout());
        contentPane.add(plotBackgroundPane, "Center");
        return contentPane;
    }

    public void populateBean(Chart chart)
    {
        if(chart == null)
            return;
        Plot plot = chart.getPlot();
        if(plot == null)
            return;
        refreshContentPane(plot);
        if(plot.is3D())
            plotNoImagePane.populate(plot);
        else
            plotBackgroundPane.populate(plot);
        if(axisAreaPane != null)
            axisAreaPane.populateBean(plot);
        if(plotBorderPane != null)
            plotBorderPane.populate(plot);
    }

    private JPanel initIntervalBackPane(Plot plot)
    {
        if(plot.isSupportIntervalBackground())
        {
            if(plot.isOnlyIntervalBackground())
                axisAreaPane = new RadarAxisAreaPane();
            else
            if(plot.is3D())
                axisAreaPane = new Plot3DAxisAreaPane();
            else
                axisAreaPane = new DefaultAxisAreaPane();
        } else
        if(axisAreaPane != null)
            axisAreaPane = null;
        return axisAreaPane;
    }

    private JPanel initPlotBackgroundPane(Plot plot)
    {
        return ((JPanel) (plot.is3D() ? plotNoImagePane : plotBackgroundPane));
    }

    private JPanel initPlotBorderPane(Plot plot)
    {
        return plot.isSupportBorder() ? plotBorderPane : null;
    }

    private void refreshContentPane(Plot plot)
    {
        contentPane.removeAll();
        contentPane.setLayout(new BorderLayout());
        double d = -2D;
        double d1 = -1D;
        double ad[] = {
            d1
        };
        double ad1[] = {
            d, d, d
        };
        Component acomponent[][] = {
            {
                initPlotBorderPane(plot)
            }, {
                initPlotBackgroundPane(plot)
            }, {
                initIntervalBackPane(plot)
            }
        };
        JPanel jpanel = TableLayoutHelper.createTableLayoutPane(acomponent, ad1, ad);
        contentPane.add(jpanel, "Center");
        parent.initAllListeners();
    }

    public void updateBean(Chart chart)
    {
        Plot plot = chart.getPlot();
        if(plot.is3D())
            plotNoImagePane.update(plot);
        else
            plotBackgroundPane.update(plot);
        if(axisAreaPane != null)
            axisAreaPane.updateBean(plot);
        if(plotBorderPane != null)
            plotBorderPane.update(plot);
    }

    public Chart updateBean()
    {
        return null;
    }

    public volatile Object updateBean()
    {
        return updateBean();
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
