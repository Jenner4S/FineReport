// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.chart.gui;

import com.fr.chart.chartattr.*;
import com.fr.design.chart.report.GisMapDataPane;
import com.fr.design.chart.report.MapDataPane;
import com.fr.design.gui.frpane.AttributeChangeListener;
import com.fr.design.mainframe.chart.AbstractChartAttrPane;
import com.fr.design.mainframe.chart.PaneTitleConstants;
import com.fr.design.mainframe.chart.gui.data.DataContentsPane;
import com.fr.design.mainframe.chart.gui.data.NormalChartDataPane;
import java.awt.BorderLayout;
import javax.swing.JPanel;

public class ChartDataPane extends AbstractChartAttrPane
{

    protected DataContentsPane contentsPane;
    protected AttributeChangeListener listener;
    private boolean supportCellData;

    public ChartDataPane(AttributeChangeListener attributechangelistener)
    {
        supportCellData = true;
        listener = attributechangelistener;
    }

    protected JPanel createContentPane()
    {
        contentsPane = new NormalChartDataPane(listener, this);
        return contentsPane;
    }

    public String getIconPath()
    {
        return "com/fr/design/images/chart/ChartData.png";
    }

    public String title4PopupWindow()
    {
        return PaneTitleConstants.CHART_DATA_TITLE;
    }

    protected void repeatLayout(ChartCollection chartcollection)
    {
        if(contentsPane != null)
            remove(contentsPane);
        setLayout(new BorderLayout(0, 0));
        if(chartcollection != null && chartcollection.getChartCount() <= 0)
            contentsPane = new NormalChartDataPane(listener, this);
        else
        if(chartcollection.getSelectedChart().getPlot() instanceof MapPlot)
            contentsPane = new MapDataPane(listener);
        else
        if(chartcollection.getSelectedChart().getPlot() instanceof GisMapPlot)
            contentsPane = new GisMapDataPane(listener);
        else
            contentsPane = new NormalChartDataPane(listener, this);
        if(contentsPane != null)
            contentsPane.setSupportCellData(supportCellData);
    }

    public void setSupportCellData(boolean flag)
    {
        supportCellData = flag;
    }

    public void populate(ChartCollection chartcollection)
    {
        repeatLayout(chartcollection);
        contentsPane.populate(chartcollection);
        add(contentsPane, "Center");
        validate();
    }

    public void update(ChartCollection chartcollection)
    {
        if(contentsPane != null)
            contentsPane.update(chartcollection);
    }

    public void refreshChartDataPane(ChartCollection chartcollection)
    {
        populate(chartcollection);
    }
}
