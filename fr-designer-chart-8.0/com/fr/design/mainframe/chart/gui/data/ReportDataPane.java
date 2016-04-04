// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.chart.gui.data;

import com.fr.chart.chartattr.Chart;
import com.fr.chart.chartattr.ChartCollection;
import com.fr.chart.chartdata.ReportDataDefinition;
import com.fr.design.ChartTypeInterfaceManager;
import com.fr.design.beans.FurtherBasicBeanPane;
import com.fr.design.mainframe.chart.gui.ChartDataPane;
import com.fr.design.mainframe.chart.gui.data.report.AbstractReportDataContentPane;
import com.fr.general.Inter;
import java.awt.BorderLayout;

public class ReportDataPane extends FurtherBasicBeanPane
{

    private AbstractReportDataContentPane contentPane;
    private ChartDataPane parent;

    public ReportDataPane(ChartDataPane chartdatapane)
    {
        parent = chartdatapane;
    }

    private AbstractReportDataContentPane getContentPane(Chart chart)
    {
        if(chart == null)
        {
            return null;
        } else
        {
            com.fr.chart.chartattr.Plot plot = chart.getPlot();
            return ChartTypeInterfaceManager.getInstance().getReportDataSourcePane(plot, parent);
        }
    }

    public String title4PopupWindow()
    {
        return Inter.getLocText("FR-Chart_Cell_Data");
    }

    public boolean accept(Object obj)
    {
        return (obj instanceof ChartCollection) && (((ChartCollection)obj).getSelectedChart().getFilterDefinition() instanceof ReportDataDefinition);
    }

    public void reset()
    {
    }

    public void refreshContentPane(ChartCollection chartcollection)
    {
        removeAll();
        setLayout(new BorderLayout());
        contentPane = getContentPane(chartcollection.getSelectedChart());
        if(contentPane != null)
            add(contentPane, "Center");
    }

    public void checkBoxUse()
    {
        if(contentPane != null)
            contentPane.checkBoxUse();
    }

    public void populateBean(ChartCollection chartcollection)
    {
        if(chartcollection == null)
            return;
        if(contentPane != null)
            contentPane.populateBean(chartcollection);
    }

    public void updateBean(ChartCollection chartcollection)
    {
        if(contentPane != null)
            contentPane.updateBean(chartcollection);
    }

    public ChartCollection updateBean()
    {
        return null;
    }

    public volatile void updateBean(Object obj)
    {
        updateBean((ChartCollection)obj);
    }

    public volatile Object updateBean()
    {
        return updateBean();
    }

    public volatile void populateBean(Object obj)
    {
        populateBean((ChartCollection)obj);
    }
}
