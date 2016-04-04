// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.chart.series.SeriesCondition.impl;

import com.fr.chart.chartattr.*;
import com.fr.design.chart.series.SeriesCondition.DataSeriesConditionPane;
import com.fr.design.chart.series.SeriesCondition.DataSeriesCustomConditionPane;
import java.util.HashMap;
import java.util.Map;

// Referenced classes of package com.fr.design.chart.series.SeriesCondition.impl:
//            Area3DPlotDataSeriesConditionPane, AreaPlotDataSeriesCondtionPane, BarPlotDataSeriesConditionPane, Bar3DPlotDataSeriesConditionPane, 
//            Bar2DTrendLineDSConditionPane, BubblePlotDataSeriesConditionPane, GanttPlotDataSeriesConditionPane, LinePlotDataSeriesConditionPane, 
//            PiePlotDataSeriesConditionPane, Pie3DPlotDataSeriesConditionPane, RadarPlotDataSeriesConditionPane, StockPlotDataSeriesConditionPane, 
//            XYScatterPlotDataSeriesConditionPane, MapPlotDataSeriesConditionPane, Donut2DPlotDataSeriesConditionPane

public class DataSeriesConditionPaneFactory
{

    private static final String TREND = ".TrendLine";
    private static Map map;

    private DataSeriesConditionPaneFactory()
    {
    }

    public static Class findConfitionPane4DataSeries(Plot plot)
    {
        if(plot == null)
        {
            return com/fr/design/chart/series/SeriesCondition/DataSeriesConditionPane;
        } else
        {
            boolean flag = plot.isSupportTrendLine();
            return searchSuitableClass(plot.getClass(), flag);
        }
    }

    private static Class searchSuitableClass(Class class1, boolean flag)
    {
        if(class1 == null)
            return com/fr/design/chart/series/SeriesCondition/DataSeriesConditionPane;
        String s = class1.getName();
        if(flag)
            s = (new StringBuilder()).append(s).append(".TrendLine").toString();
        Class class2 = (Class)map.get(s);
        if(class2 != null)
            return class2;
        else
            return searchSuitableClass(class1.getSuperclass(), flag);
    }

    static 
    {
        map = new HashMap();
        map.put(com/fr/chart/chartattr/Area3DPlot.getName(), com/fr/design/chart/series/SeriesCondition/impl/Area3DPlotDataSeriesConditionPane);
        map.put(com/fr/chart/chartattr/AreaPlot.getName(), com/fr/design/chart/series/SeriesCondition/impl/AreaPlotDataSeriesCondtionPane);
        map.put(com/fr/chart/chartattr/BarPlot.getName(), com/fr/design/chart/series/SeriesCondition/impl/BarPlotDataSeriesConditionPane);
        map.put(com/fr/chart/chartattr/Bar3DPlot.getName(), com/fr/design/chart/series/SeriesCondition/impl/Bar3DPlotDataSeriesConditionPane);
        map.put((new StringBuilder()).append(com/fr/chart/chartattr/Bar2DPlot.getName()).append(".TrendLine").toString(), com/fr/design/chart/series/SeriesCondition/impl/Bar2DTrendLineDSConditionPane);
        map.put(com/fr/chart/chartattr/BubblePlot.getName(), com/fr/design/chart/series/SeriesCondition/impl/BubblePlotDataSeriesConditionPane);
        map.put(com/fr/chart/chartattr/GanttPlot.getName(), com/fr/design/chart/series/SeriesCondition/impl/GanttPlotDataSeriesConditionPane);
        map.put((new StringBuilder()).append(com/fr/chart/chartattr/LinePlot.getName()).append(".TrendLine").toString(), com/fr/design/chart/series/SeriesCondition/impl/LinePlotDataSeriesConditionPane);
        map.put(com/fr/chart/chartattr/PiePlot.getName(), com/fr/design/chart/series/SeriesCondition/impl/PiePlotDataSeriesConditionPane);
        map.put(com/fr/chart/chartattr/Pie3DPlot.getName(), com/fr/design/chart/series/SeriesCondition/impl/Pie3DPlotDataSeriesConditionPane);
        map.put(com/fr/chart/chartattr/RadarPlot.getName(), com/fr/design/chart/series/SeriesCondition/impl/RadarPlotDataSeriesConditionPane);
        map.put(com/fr/chart/chartattr/StockPlot.getName(), com/fr/design/chart/series/SeriesCondition/impl/StockPlotDataSeriesConditionPane);
        map.put((new StringBuilder()).append(com/fr/chart/chartattr/XYScatterPlot.getName()).append(".TrendLine").toString(), com/fr/design/chart/series/SeriesCondition/impl/XYScatterPlotDataSeriesConditionPane);
        map.put(com/fr/chart/chartattr/MapPlot.getName(), com/fr/design/chart/series/SeriesCondition/impl/MapPlotDataSeriesConditionPane);
        map.put(com/fr/chart/chartattr/CustomPlot.getName(), com/fr/design/chart/series/SeriesCondition/DataSeriesCustomConditionPane);
        map.put(com/fr/chart/chartattr/Donut2DPlot.getName(), com/fr/design/chart/series/SeriesCondition/impl/Donut2DPlotDataSeriesConditionPane);
    }
}
