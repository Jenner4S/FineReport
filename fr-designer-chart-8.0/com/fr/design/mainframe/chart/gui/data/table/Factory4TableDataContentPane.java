// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.chart.gui.data.table;

import com.fr.base.FRContext;
import com.fr.chart.chartattr.*;
import com.fr.design.mainframe.chart.gui.ChartDataPane;
import com.fr.general.FRLogger;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

// Referenced classes of package com.fr.design.mainframe.chart.gui.data.table:
//            CategoryPlotTableDataContentPane, AbstractTableDataContentPane, PiePlotTableDataContentPane, BubblePlotTableDataContentPane, 
//            XYScatterPlotTableDataContentPane, GanttPlotTableDataContentPane, StockPlotTableDataContentPane, MeterPlotTableDataContentPane

public class Factory4TableDataContentPane
{

    private static Map map;

    public Factory4TableDataContentPane()
    {
    }

    public static AbstractTableDataContentPane createTableDataContenetPaneWithPlotType(Plot plot, ChartDataPane chartdatapane)
    {
        Class class1;
        class1 = (Class)map.get(plot.getClass());
        if(class1 == null)
            return new CategoryPlotTableDataContentPane(chartdatapane);
        Constructor constructor = class1.getConstructor(new Class[] {
            com/fr/design/mainframe/chart/gui/ChartDataPane
        });
        if(constructor != null)
            return (AbstractTableDataContentPane)constructor.newInstance(new Object[] {
                chartdatapane
            });
        try
        {
            return new CategoryPlotTableDataContentPane(chartdatapane);
        }
        catch(InstantiationException instantiationexception)
        {
            FRContext.getLogger().error(instantiationexception.getMessage(), instantiationexception);
        }
        catch(Exception exception)
        {
            FRContext.getLogger().error(exception.getMessage(), exception);
        }
        return new CategoryPlotTableDataContentPane(chartdatapane);
    }

    static 
    {
        map = new HashMap();
        map.put(com/fr/chart/chartattr/PiePlot, com/fr/design/mainframe/chart/gui/data/table/PiePlotTableDataContentPane);
        map.put(com/fr/chart/chartattr/Pie3DPlot, com/fr/design/mainframe/chart/gui/data/table/PiePlotTableDataContentPane);
        map.put(com/fr/chart/chartattr/BubblePlot, com/fr/design/mainframe/chart/gui/data/table/BubblePlotTableDataContentPane);
        map.put(com/fr/chart/chartattr/XYScatterPlot, com/fr/design/mainframe/chart/gui/data/table/XYScatterPlotTableDataContentPane);
        map.put(com/fr/chart/chartattr/GanttPlot, com/fr/design/mainframe/chart/gui/data/table/GanttPlotTableDataContentPane);
        map.put(com/fr/chart/chartattr/StockPlot, com/fr/design/mainframe/chart/gui/data/table/StockPlotTableDataContentPane);
        map.put(com/fr/chart/chartattr/MeterPlot, com/fr/design/mainframe/chart/gui/data/table/MeterPlotTableDataContentPane);
        map.put(com/fr/chart/chartattr/MeterBluePlot, com/fr/design/mainframe/chart/gui/data/table/MeterPlotTableDataContentPane);
        map.put(com/fr/chart/chartattr/SimpleMeterPlot, com/fr/design/mainframe/chart/gui/data/table/MeterPlotTableDataContentPane);
    }
}
