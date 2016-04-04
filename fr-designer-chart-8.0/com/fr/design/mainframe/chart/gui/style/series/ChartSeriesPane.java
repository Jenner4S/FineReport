// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.chart.gui.style.series;

import com.fr.chart.chartattr.Chart;
import com.fr.design.ChartTypeInterfaceManager;
import com.fr.design.dialog.BasicScrollPane;
import com.fr.design.mainframe.chart.PaneTitleConstants;
import com.fr.design.mainframe.chart.gui.ChartStylePane;
import java.awt.BorderLayout;
import javax.swing.JPanel;

// Referenced classes of package com.fr.design.mainframe.chart.gui.style.series:
//            AbstractPlotSeriesPane

public class ChartSeriesPane extends BasicScrollPane
{

    protected AbstractPlotSeriesPane seriesStyleContentPane;
    protected Chart chart;
    protected ChartStylePane parent;

    public ChartSeriesPane(ChartStylePane chartstylepane)
    {
        parent = chartstylepane;
    }

    public String title4PopupWindow()
    {
        return PaneTitleConstants.CHART_STYLE_SERIES_TITLE;
    }

    protected JPanel createContentPane()
    {
        JPanel jpanel = new JPanel(new BorderLayout());
        if(chart == null)
            return jpanel;
        initDifferentPlotPane();
        if(seriesStyleContentPane != null)
            jpanel.add(seriesStyleContentPane, "Center");
        return jpanel;
    }

    public void updateBean(Chart chart1)
    {
        if(chart1 == null)
            return;
        if(seriesStyleContentPane != null)
        {
            seriesStyleContentPane.setCurrentChart(chart1);
            seriesStyleContentPane.updateBean(chart1.getPlot());
        }
    }

    public void populateBean(Chart chart1)
    {
        chart = chart1;
        if(seriesStyleContentPane == null)
        {
            remove(leftcontentPane);
            layoutContentPane();
            parent.initAllListeners();
        }
        if(seriesStyleContentPane != null)
        {
            seriesStyleContentPane.setCurrentChart(chart1);
            seriesStyleContentPane.populateBean(chart1.getPlot());
        }
    }

    public void initDifferentPlotPane()
    {
        seriesStyleContentPane = (AbstractPlotSeriesPane)ChartTypeInterfaceManager.getInstance().getPlotSeriesPane(parent, chart.getPlot());
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
