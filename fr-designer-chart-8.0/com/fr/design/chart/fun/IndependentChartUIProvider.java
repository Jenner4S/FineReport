// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.chart.fun;

import com.fr.chart.chartattr.Plot;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.condition.ConditionAttributesPane;
import com.fr.design.gui.frpane.AttributeChangeListener;
import com.fr.design.mainframe.chart.AbstractChartAttrPane;
import com.fr.design.mainframe.chart.gui.ChartDataPane;
import com.fr.design.mainframe.chart.gui.ChartStylePane;
import com.fr.design.mainframe.chart.gui.data.report.AbstractReportDataContentPane;
import com.fr.design.mainframe.chart.gui.data.table.AbstractTableDataContentPane;
import com.fr.design.mainframe.chart.gui.type.AbstractChartTypePane;
import com.fr.stable.fun.Level;

public interface IndependentChartUIProvider
    extends Level
{

    public static final String XML_TAG = "IndependentChartUIProvider";
    public static final int CURRENT_API_LEVEL = 2;

    public abstract AbstractChartTypePane getPlotTypePane();

    public abstract AbstractTableDataContentPane getTableDataSourcePane(Plot plot, ChartDataPane chartdatapane);

    public abstract AbstractReportDataContentPane getReportDataSourcePane(Plot plot, ChartDataPane chartdatapane);

    public abstract ConditionAttributesPane getPlotConditionPane(Plot plot);

    public abstract BasicBeanPane getPlotSeriesPane(ChartStylePane chartstylepane, Plot plot);

    public abstract AbstractChartAttrPane[] getAttrPaneArray(AttributeChangeListener attributechangelistener);

    public abstract boolean isUseDefaultPane();

    public abstract String getIconPath();
}
