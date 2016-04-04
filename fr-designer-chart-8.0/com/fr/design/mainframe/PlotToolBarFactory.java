// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe;

import com.fr.chart.base.ChartTypeValueCollection;
import com.fr.design.mainframe.chart.gui.type.AreaPlotPane4ToolBar;
import com.fr.design.mainframe.chart.gui.type.BarPlotPane4ToolBar;
import com.fr.design.mainframe.chart.gui.type.BubblePlotPane4ToolBar;
import com.fr.design.mainframe.chart.gui.type.ColumnPlotPane4ToolBar;
import com.fr.design.mainframe.chart.gui.type.CustomPlotPane4ToolBar;
import com.fr.design.mainframe.chart.gui.type.DonutPlotPane4ToolBar;
import com.fr.design.mainframe.chart.gui.type.GanttPlotPane4ToolBar;
import com.fr.design.mainframe.chart.gui.type.LinePlotPane4ToolBar;
import com.fr.design.mainframe.chart.gui.type.MeterPlotPane4ToolBar;
import com.fr.design.mainframe.chart.gui.type.PiePlotPane4ToolBar;
import com.fr.design.mainframe.chart.gui.type.PlotPane4ToolBar;
import com.fr.design.mainframe.chart.gui.type.RadarPlotPane4ToolBar;
import com.fr.design.mainframe.chart.gui.type.RangePlotPane4ToolBar;
import com.fr.design.mainframe.chart.gui.type.StockPlotPane4ToolBar;
import com.fr.design.mainframe.chart.gui.type.XYSCatterPlotPane4ToolBar;
import com.fr.general.FRLogger;
import java.lang.reflect.Constructor;
import java.util.HashMap;

// Referenced classes of package com.fr.design.mainframe:
//            ChartDesigner, MapPlotPane4ToolBar, AbstractMapPlotPane4ToolBar, GisMapPlotPane4ToolBar

public class PlotToolBarFactory
{

    private static HashMap panes4NormalPlot;
    private static HashMap panes4MapPlot;

    private PlotToolBarFactory()
    {
    }

    public static PlotPane4ToolBar createToolBar4NormalPlot(ChartTypeValueCollection charttypevaluecollection, ChartDesigner chartdesigner)
    {
        if(!panes4NormalPlot.containsKey(charttypevaluecollection))
            return new ColumnPlotPane4ToolBar(chartdesigner);
        try
        {
            Class class1 = (Class)panes4NormalPlot.get(charttypevaluecollection);
            Constructor constructor = class1.getConstructor(new Class[] {
                com/fr/design/mainframe/ChartDesigner
            });
            return (PlotPane4ToolBar)constructor.newInstance(new Object[] {
                chartdesigner
            });
        }
        catch(Exception exception)
        {
            FRLogger.getLogger().error(exception.getMessage());
        }
        return new ColumnPlotPane4ToolBar(chartdesigner);
    }

    public static AbstractMapPlotPane4ToolBar createToolBar4MapPlot(ChartTypeValueCollection charttypevaluecollection, ChartDesigner chartdesigner)
    {
        if(!panes4MapPlot.containsKey(charttypevaluecollection))
            return new MapPlotPane4ToolBar(chartdesigner);
        try
        {
            Class class1 = (Class)panes4MapPlot.get(charttypevaluecollection);
            Constructor constructor = class1.getConstructor(new Class[] {
                com/fr/design/mainframe/ChartDesigner
            });
            return (AbstractMapPlotPane4ToolBar)constructor.newInstance(new Object[] {
                chartdesigner
            });
        }
        catch(Exception exception)
        {
            FRLogger.getLogger().error(exception.getMessage());
        }
        return new MapPlotPane4ToolBar(chartdesigner);
    }

    static 
    {
        panes4NormalPlot = new HashMap();
        panes4MapPlot = new HashMap();
        panes4NormalPlot.put(ChartTypeValueCollection.COLUMN, com/fr/design/mainframe/chart/gui/type/ColumnPlotPane4ToolBar);
        panes4NormalPlot.put(ChartTypeValueCollection.LINE, com/fr/design/mainframe/chart/gui/type/LinePlotPane4ToolBar);
        panes4NormalPlot.put(ChartTypeValueCollection.BAR, com/fr/design/mainframe/chart/gui/type/BarPlotPane4ToolBar);
        panes4NormalPlot.put(ChartTypeValueCollection.PIE, com/fr/design/mainframe/chart/gui/type/PiePlotPane4ToolBar);
        panes4NormalPlot.put(ChartTypeValueCollection.AREA, com/fr/design/mainframe/chart/gui/type/AreaPlotPane4ToolBar);
        panes4NormalPlot.put(ChartTypeValueCollection.XYSCATTER, com/fr/design/mainframe/chart/gui/type/XYSCatterPlotPane4ToolBar);
        panes4NormalPlot.put(ChartTypeValueCollection.BUBBLE, com/fr/design/mainframe/chart/gui/type/BubblePlotPane4ToolBar);
        panes4NormalPlot.put(ChartTypeValueCollection.RADAR, com/fr/design/mainframe/chart/gui/type/RadarPlotPane4ToolBar);
        panes4NormalPlot.put(ChartTypeValueCollection.STOCK, com/fr/design/mainframe/chart/gui/type/StockPlotPane4ToolBar);
        panes4NormalPlot.put(ChartTypeValueCollection.METER, com/fr/design/mainframe/chart/gui/type/MeterPlotPane4ToolBar);
        panes4NormalPlot.put(ChartTypeValueCollection.RANGE, com/fr/design/mainframe/chart/gui/type/RangePlotPane4ToolBar);
        panes4NormalPlot.put(ChartTypeValueCollection.COMB, com/fr/design/mainframe/chart/gui/type/CustomPlotPane4ToolBar);
        panes4NormalPlot.put(ChartTypeValueCollection.GANTT, com/fr/design/mainframe/chart/gui/type/GanttPlotPane4ToolBar);
        panes4NormalPlot.put(ChartTypeValueCollection.DONUT, com/fr/design/mainframe/chart/gui/type/DonutPlotPane4ToolBar);
        panes4MapPlot.put(ChartTypeValueCollection.MAP, com/fr/design/mainframe/MapPlotPane4ToolBar);
        panes4MapPlot.put(ChartTypeValueCollection.GIS, com/fr/design/mainframe/GisMapPlotPane4ToolBar);
    }
}
