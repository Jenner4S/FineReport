// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.chart.series.SeriesCondition;

import com.fr.design.chart.series.SeriesCondition.impl.BubblePlotDataSeriesConditionPane;
import com.fr.design.chart.series.SeriesCondition.impl.GanttPlotDataSeriesConditionPane;
import com.fr.design.chart.series.SeriesCondition.impl.MapPlotDataSeriesConditionPane;
import com.fr.design.chart.series.SeriesCondition.impl.Pie3DPlotDataSeriesConditionPane;
import com.fr.design.chart.series.SeriesCondition.impl.PiePlotDataSeriesConditionPane;
import com.fr.design.chart.series.SeriesCondition.impl.XYScatterPlotDataSeriesConditionPane;
import java.util.HashMap;
import java.util.Map;

// Referenced classes of package com.fr.design.chart.series.SeriesCondition:
//            ChartConditionPane, GanttPlotChartConditionPane, MapPlotChartConditionPane, PiePlotChartConditionPane, 
//            XYPlotChartConditionPane, BubblePlotChartConditionPane

public class ChartConditionPaneFactory
{

    private static Map map;

    private ChartConditionPaneFactory()
    {
    }

    public static ChartConditionPane createChartConditionPane(Class class1)
    {
        String s = class1.getName();
        Object obj = (Class)map.get(s);
        obj = obj == null ? com/fr/design/chart/series/SeriesCondition/ChartConditionPane : obj;
        try
        {
            return (ChartConditionPane)((Class) (obj)).newInstance();
        }
        catch(Exception exception)
        {
            return new ChartConditionPane();
        }
    }

    static 
    {
        map = new HashMap();
        map.put(com/fr/design/chart/series/SeriesCondition/impl/GanttPlotDataSeriesConditionPane.getName(), com/fr/design/chart/series/SeriesCondition/GanttPlotChartConditionPane);
        map.put(com/fr/design/chart/series/SeriesCondition/impl/MapPlotDataSeriesConditionPane.getName(), com/fr/design/chart/series/SeriesCondition/MapPlotChartConditionPane);
        map.put(com/fr/design/chart/series/SeriesCondition/impl/PiePlotDataSeriesConditionPane.getName(), com/fr/design/chart/series/SeriesCondition/PiePlotChartConditionPane);
        map.put(com/fr/design/chart/series/SeriesCondition/impl/Pie3DPlotDataSeriesConditionPane.getName(), com/fr/design/chart/series/SeriesCondition/PiePlotChartConditionPane);
        map.put(com/fr/design/chart/series/SeriesCondition/impl/XYScatterPlotDataSeriesConditionPane.getName(), com/fr/design/chart/series/SeriesCondition/XYPlotChartConditionPane);
        map.put(com/fr/design/chart/series/SeriesCondition/impl/BubblePlotDataSeriesConditionPane.getName(), com/fr/design/chart/series/SeriesCondition/BubblePlotChartConditionPane);
    }
}
