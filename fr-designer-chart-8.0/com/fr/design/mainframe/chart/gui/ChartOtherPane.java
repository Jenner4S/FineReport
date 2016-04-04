// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.chart.gui;

import com.fr.chart.chartattr.Chart;
import com.fr.chart.chartattr.ChartCollection;
import com.fr.chart.chartattr.Plot;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.mainframe.chart.AbstractChartAttrPane;
import com.fr.design.mainframe.chart.ChartEditPane;
import com.fr.design.mainframe.chart.PaneTitleConstants;
import com.fr.design.mainframe.chart.gui.other.ChartConditionAttrPane;
import com.fr.design.mainframe.chart.gui.other.ChartInteractivePane;
import com.fr.design.mainframe.chart.gui.type.ChartTabPane;
import java.awt.BorderLayout;
import java.util.ArrayList;
import javax.swing.JPanel;

public class ChartOtherPane extends AbstractChartAttrPane
{
    protected class TabPane extends ChartTabPane
    {

        protected BasicBeanPane interactivePane;
        protected BasicBeanPane conditionAttrPane;
        final ChartOtherPane this$0;

        protected java.util.List initPaneList()
        {
            ArrayList arraylist = new ArrayList();
            interactivePane = createInteractivePane();
            arraylist.add(interactivePane);
            if(isHaveCondition())
            {
                conditionAttrPane = createConditionAttrPane();
                arraylist.add(conditionAttrPane);
            }
            return arraylist;
        }

        public void populateBean(Chart chart)
        {
            interactivePane.populateBean(chart);
            if(isHaveCondition())
                conditionAttrPane.populateBean(chart);
        }

        public void updateBean(Chart chart)
        {
            if(chart == null)
                return;
            interactivePane.updateBean(chart);
            if(isHaveCondition())
                conditionAttrPane.updateBean(chart);
        }

        public void registerChartEditPane(ChartEditPane charteditpane)
        {
        }

        public Chart updateBean()
        {
            return null;
        }

        public volatile void updateBean(Object obj)
        {
            updateBean((Chart)obj);
        }

        public volatile void populateBean(Object obj)
        {
            populateBean((Chart)obj);
        }

        public volatile Object updateBean()
        {
            return updateBean();
        }

        protected TabPane()
        {
            this$0 = ChartOtherPane.this;
            super();
        }
    }


    private static final long serialVersionUID = 0xb21e018b97d31892L;
    protected TabPane otherPane;
    protected boolean hasCondition;

    public ChartOtherPane()
    {
        hasCondition = false;
    }

    protected JPanel createContentPane()
    {
        JPanel jpanel = new JPanel(new BorderLayout());
        otherPane = new TabPane();
        jpanel.add(otherPane, "Center");
        return jpanel;
    }

    public void populate(ChartCollection chartcollection)
    {
        Plot plot = chartcollection.getSelectedChart().getPlot();
        hasCondition = plot.isSupportDataSeriesCondition();
        removeAll();
        initAll();
        validate();
        otherPane.populateBean(chartcollection.getSelectedChart());
    }

    public void update(ChartCollection chartcollection)
    {
        otherPane.updateBean(chartcollection.getSelectedChart());
    }

    protected BasicBeanPane createInteractivePane()
    {
        return new ChartInteractivePane(this);
    }

    protected BasicBeanPane createConditionAttrPane()
    {
        return new ChartConditionAttrPane();
    }

    public void registerChartEditPane(ChartEditPane charteditpane)
    {
        otherPane.registerChartEditPane(charteditpane);
    }

    public String getIconPath()
    {
        return "com/fr/design/images/chart/InterAttr.png";
    }

    public String title4PopupWindow()
    {
        return PaneTitleConstants.CHART_OTHER_TITLE;
    }

    private boolean isHaveCondition()
    {
        return hasCondition;
    }

    public transient void setSelectedByIds(int i, String as[])
    {
        otherPane.setSelectedByIds(i, as);
    }

}
