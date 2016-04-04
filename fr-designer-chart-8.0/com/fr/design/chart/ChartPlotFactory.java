// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.chart;

import com.fr.base.FRContext;
import com.fr.chart.chartattr.*;
import com.fr.design.chart.axis.BinaryChartStyleAxisPane;
import com.fr.design.chart.axis.ChartStyleAxisPane;
import com.fr.design.chart.axis.CustomChartStyleAxisPane;
import com.fr.design.chart.axis.GanntChartStyleAxisPane;
import com.fr.design.chart.axis.RadarChartStyleAxisPane;
import com.fr.design.chart.axis.TernaryChartStyleAxisPane;
import com.fr.design.chart.axis.ValueChartStyleAxisPane;
import com.fr.design.chart.axis.XYChartStyleAxisPane;
import com.fr.design.chart.series.SeriesCondition.dlp.AreaDataLabelPane;
import com.fr.design.chart.series.SeriesCondition.dlp.Bar2DDataLabelPane;
import com.fr.design.chart.series.SeriesCondition.dlp.BubbleDataLabelPane;
import com.fr.design.chart.series.SeriesCondition.dlp.DataLabelPane;
import com.fr.design.chart.series.SeriesCondition.dlp.LineDataLabelPane;
import com.fr.design.chart.series.SeriesCondition.dlp.MapDataLabelPane;
import com.fr.design.chart.series.SeriesCondition.dlp.MeterDataLabelPane;
import com.fr.design.chart.series.SeriesCondition.dlp.PieDataLabelPane;
import com.fr.design.chart.series.SeriesCondition.dlp.RadarDataLabelPane;
import com.fr.design.chart.series.SeriesCondition.dlp.RangeDataLabelPane;
import com.fr.design.chart.series.SeriesCondition.dlp.StockDataLabelPane;
import com.fr.design.chart.series.SeriesCondition.dlp.XYDataLabelPane;
import com.fr.design.mainframe.chart.gui.style.axis.*;
import com.fr.general.FRLogger;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

// Referenced classes of package com.fr.design.chart:
//            FactoryObject

public class ChartPlotFactory
{

    private static Map map;
    private static Map axisPane = new HashMap() {

            
            {
                put(com/fr/design/mainframe/chart/gui/style/axis/ChartValuePane.getName(), com/fr/design/mainframe/chart/gui/style/axis/ChartValueNoFormulaPane);
                put(com/fr/design/mainframe/chart/gui/style/axis/ChartSecondValuePane.getName(), com/fr/design/mainframe/chart/gui/style/axis/ChartSecondValueNoFormulaPane);
                put(com/fr/design/mainframe/chart/gui/style/axis/ChartPercentValuePane.getName(), com/fr/design/mainframe/chart/gui/style/axis/ChartPercentValueNoFormulaPane);
                put(com/fr/design/mainframe/chart/gui/style/axis/ChartCategoryPane.getName(), com/fr/design/mainframe/chart/gui/style/axis/ChartCategoryNoFormulaPane);
            }
    }
;

    private ChartPlotFactory()
    {
    }

    public static ChartStyleAxisPane createChartStyleAxisPaneByPlot(Plot plot)
    {
        FactoryObject factoryobject = (FactoryObject)map.get(plot.getClass().getName());
        if(factoryobject != null && factoryobject.getAxisPaneClass() != null)
            try
            {
                Constructor constructor = factoryobject.getAxisPaneClass().getConstructor(new Class[] {
                    com/fr/chart/chartattr/Plot
                });
                return (ChartStyleAxisPane)constructor.newInstance(new Object[] {
                    plot
                });
            }
            catch(Exception exception)
            {
                FRContext.getLogger().error(exception.getMessage(), exception);
            }
        return new BinaryChartStyleAxisPane(plot);
    }

    public static DataLabelPane createDataLabelPane4Plot(Class class1)
    {
        FactoryObject factoryobject = (FactoryObject)map.get(class1.getName());
        if(factoryobject != null && factoryobject.getDataLabelPaneClass() != null)
            try
            {
                Constructor constructor = factoryobject.getDataLabelPaneClass().getConstructor(new Class[0]);
                return (DataLabelPane)constructor.newInstance(new Object[0]);
            }
            catch(Exception exception)
            {
                FRContext.getLogger().error(exception.getMessage(), exception);
            }
        return new DataLabelPane();
    }

    public static ChartAxisUsePane getNoFormulaPane(ChartAxisUsePane chartaxisusepane)
    {
        Class class1 = (Class)axisPane.get(chartaxisusepane.getClass().getName());
        if(class1 != null)
            try
            {
                Constructor constructor = class1.getConstructor(new Class[0]);
                return (ChartAxisUsePane)constructor.newInstance(new Object[0]);
            }
            catch(Exception exception)
            {
                FRContext.getLogger().error(exception.getMessage(), exception);
            }
        return chartaxisusepane;
    }

    static 
    {
        map = new HashMap();
        map.put(com/fr/chart/chartattr/AreaPlot.getName(), (new FactoryObject()).setDataLabelPaneClass(com/fr/design/chart/series/SeriesCondition/dlp/AreaDataLabelPane));
        map.put(com/fr/chart/chartattr/Area3DPlot.getName(), (new FactoryObject()).setDataLabelPaneClass(com/fr/design/chart/series/SeriesCondition/dlp/AreaDataLabelPane));
        map.put(com/fr/chart/chartattr/Bar2DPlot.getName(), (new FactoryObject()).setDataLabelPaneClass(com/fr/design/chart/series/SeriesCondition/dlp/Bar2DDataLabelPane));
        map.put(com/fr/chart/chartattr/Bar3DPlot.getName(), (new FactoryObject()).setDataLabelPaneClass(com/fr/design/chart/series/SeriesCondition/dlp/Bar2DDataLabelPane));
        map.put(com/fr/chart/chartattr/BubblePlot.getName(), (new FactoryObject()).setAxisPaneCls(com/fr/design/chart/axis/XYChartStyleAxisPane).setDataLabelPaneClass(com/fr/design/chart/series/SeriesCondition/dlp/BubbleDataLabelPane));
        map.put(com/fr/chart/chartattr/CustomPlot.getName(), (new FactoryObject()).setAxisPaneCls(com/fr/design/chart/axis/CustomChartStyleAxisPane).setDataLabelPaneClass(com/fr/design/chart/series/SeriesCondition/dlp/Bar2DDataLabelPane));
        map.put(com/fr/chart/chartattr/GanttPlot.getName(), (new FactoryObject()).setAxisPaneCls(com/fr/design/chart/axis/GanntChartStyleAxisPane));
        map.put(com/fr/chart/chartattr/LinePlot.getName(), (new FactoryObject()).setDataLabelPaneClass(com/fr/design/chart/series/SeriesCondition/dlp/LineDataLabelPane));
        map.put(com/fr/chart/chartattr/MapPlot.getName(), (new FactoryObject()).setDataLabelPaneClass(com/fr/design/chart/series/SeriesCondition/dlp/MapDataLabelPane));
        map.put(com/fr/chart/chartattr/MeterBluePlot.getName(), (new FactoryObject()).setDataLabelPaneClass(com/fr/design/chart/series/SeriesCondition/dlp/MeterDataLabelPane));
        map.put(com/fr/chart/chartattr/MeterPlot.getName(), (new FactoryObject()).setDataLabelPaneClass(com/fr/design/chart/series/SeriesCondition/dlp/MeterDataLabelPane));
        map.put(com/fr/chart/chartattr/PiePlot.getName(), (new FactoryObject()).setDataLabelPaneClass(com/fr/design/chart/series/SeriesCondition/dlp/PieDataLabelPane));
        map.put(com/fr/chart/chartattr/Pie3DPlot.getName(), (new FactoryObject()).setDataLabelPaneClass(com/fr/design/chart/series/SeriesCondition/dlp/PieDataLabelPane));
        map.put(com/fr/chart/chartattr/RadarPlot.getName(), (new FactoryObject()).setAxisPaneCls(com/fr/design/chart/axis/RadarChartStyleAxisPane).setDataLabelPaneClass(com/fr/design/chart/series/SeriesCondition/dlp/RadarDataLabelPane));
        map.put(com/fr/chart/chartattr/StockPlot.getName(), (new FactoryObject()).setAxisPaneCls(com/fr/design/chart/axis/TernaryChartStyleAxisPane).setDataLabelPaneClass(com/fr/design/chart/series/SeriesCondition/dlp/StockDataLabelPane));
        map.put(com/fr/chart/chartattr/RangePlot.getName(), (new FactoryObject()).setDataLabelPaneClass(com/fr/design/chart/series/SeriesCondition/dlp/RangeDataLabelPane).setAxisPaneCls(com/fr/design/chart/axis/ValueChartStyleAxisPane));
        map.put(com/fr/chart/chartattr/XYScatterPlot.getName(), (new FactoryObject()).setAxisPaneCls(com/fr/design/chart/axis/XYChartStyleAxisPane).setDataLabelPaneClass(com/fr/design/chart/series/SeriesCondition/dlp/XYDataLabelPane));
        map.put(com/fr/chart/chartattr/FunnelPlot.getName(), (new FactoryObject()).setDataLabelPaneClass(com/fr/design/chart/series/SeriesCondition/dlp/PieDataLabelPane));
    }
}
