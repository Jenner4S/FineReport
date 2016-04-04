// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.chart.gui.style.datalabel;

import com.fr.chart.base.AttrContents;
import com.fr.chart.base.DataSeriesCondition;
import com.fr.chart.chartattr.*;
import com.fr.chart.chartglyph.ConditionAttr;
import com.fr.chart.chartglyph.ConditionCollection;
import com.fr.design.dialog.BasicScrollPane;
import com.fr.design.mainframe.chart.PaneTitleConstants;
import com.fr.design.mainframe.chart.gui.ChartStylePane;
import com.fr.design.mainframe.chart.gui.style.*;
import com.fr.general.Inter;
import java.awt.BorderLayout;
import javax.swing.JPanel;

public class ChartDataLabelPane extends BasicScrollPane
{

    private ChartDatapointLabelPane labelPane;
    private Chart chart;
    private ChartStylePane parent;
    private static final long serialVersionUID = 0xb4603836d4f928e9L;

    public ChartDataLabelPane(ChartStylePane chartstylepane)
    {
        parent = chartstylepane;
    }

    protected JPanel createContentPane()
    {
        JPanel jpanel = new JPanel(new BorderLayout());
        if(chart == null)
        {
            return jpanel;
        } else
        {
            labelPane = getLabelPane();
            jpanel.add(labelPane, "North");
            return jpanel;
        }
    }

    public void populateBean(Chart chart1)
    {
        chart = chart1;
        if(labelPane == null)
        {
            remove(leftcontentPane);
            layoutContentPane();
            parent.initAllListeners();
        }
        Plot plot = chart.getPlot();
        ConditionAttr conditionattr = plot.getConditionCollection().getDefaultAttr();
        DataSeriesCondition dataseriescondition = conditionattr.getExisted(com/fr/chart/base/AttrContents);
        if(labelPane != null)
            labelPane.populate(dataseriescondition);
    }

    public void updateBean(Chart chart1)
    {
        if(chart1 == null)
            return;
        ConditionAttr conditionattr = chart1.getPlot().getConditionCollection().getDefaultAttr();
        DataSeriesCondition dataseriescondition = conditionattr.getExisted(com/fr/chart/base/AttrContents);
        if(dataseriescondition != null)
            conditionattr.remove(dataseriescondition);
        AttrContents attrcontents = labelPane.update();
        conditionattr.addDataSeriesCondition(attrcontents);
    }

    protected String title4PopupWindow()
    {
        return PaneTitleConstants.CHART_STYLE_LABEL_TITLE;
    }

    protected ChartDatapointLabelPane getLabelPane()
    {
        if(chart.getPlot().isMapKindLabel())
            return new ChartDataPointLabel4MapPane(parent);
        if(chart.getPlot().isGisKindLabel())
            return new ChartDataPointLabel4GisPane(parent);
        else
            return new ChartDatapointLabelPane(getLabelLocationNameArray(), getLabelLocationValueArray(), chart.getPlot(), parent);
    }

    protected String[] getLabelLocationNameArray()
    {
        Plot plot = chart.getPlot();
        if(plot instanceof BarPlot)
            return (new String[] {
                Inter.getLocText("BarInside"), Inter.getLocText("BarOutSide"), Inter.getLocText("Center")
            });
        if(plot instanceof PiePlot)
            return (new String[] {
                Inter.getLocText("Chart_In_Pie"), Inter.getLocText("Chart_Out_Pie")
            });
        if(plot instanceof RangePlot)
            return (new String[] {
                Inter.getLocText("StyleAlignment-Top"), Inter.getLocText("StyleAlignment-Bottom"), Inter.getLocText("Center")
            });
        if(plot instanceof BubblePlot)
            return (new String[] {
                Inter.getLocText("Chart_Bubble_Inside"), Inter.getLocText("Chart_Bubble_Outside")
            });
        else
            return new String[0];
    }

    protected Integer[] getLabelLocationValueArray()
    {
        Plot plot = chart.getPlot();
        if(plot instanceof BarPlot)
            return (new Integer[] {
                Integer.valueOf(5), Integer.valueOf(6), Integer.valueOf(0)
            });
        if(plot instanceof PiePlot)
            return (new Integer[] {
                Integer.valueOf(5), Integer.valueOf(6)
            });
        if(plot instanceof RangePlot)
            return (new Integer[] {
                Integer.valueOf(1), Integer.valueOf(3), Integer.valueOf(0)
            });
        if(plot instanceof BubblePlot)
            return (new Integer[] {
                Integer.valueOf(5), Integer.valueOf(6)
            });
        else
            return new Integer[0];
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
