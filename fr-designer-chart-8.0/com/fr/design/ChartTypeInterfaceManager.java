// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design;

import com.fr.chart.chartattr.Plot;
import com.fr.chart.charttypes.ChartTypeManager;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.chart.fun.IndependentChartUIProvider;
import com.fr.design.chartinterface.AreaIndependentChartInterface;
import com.fr.design.chartinterface.BarIndependentChartInterface;
import com.fr.design.chartinterface.BubbleIndependentChartInterface;
import com.fr.design.chartinterface.ColumnIndependentChartInterface;
import com.fr.design.chartinterface.CustomIndependentChartInterface;
import com.fr.design.chartinterface.DonutIndependentChartInterface;
import com.fr.design.chartinterface.FunnelIndependentChartInterface;
import com.fr.design.chartinterface.GanttIndependentChartInterface;
import com.fr.design.chartinterface.GisMapIndependentChartInterface;
import com.fr.design.chartinterface.LineIndependentChartInterface;
import com.fr.design.chartinterface.MapIndependentChartInterface;
import com.fr.design.chartinterface.MeterIndependentChartInterface;
import com.fr.design.chartinterface.PieIndependentChartInterface;
import com.fr.design.chartinterface.RadarIndependentChartInterface;
import com.fr.design.chartinterface.RangeIndependentChartInterface;
import com.fr.design.chartinterface.StockIndependentChartInterface;
import com.fr.design.chartinterface.XYScatterIndependentChartInterface;
import com.fr.design.condition.ConditionAttributesPane;
import com.fr.design.gui.frpane.AttributeChangeListener;
import com.fr.design.mainframe.chart.AbstractChartAttrPane;
import com.fr.design.mainframe.chart.gui.ChartDataPane;
import com.fr.design.mainframe.chart.gui.ChartStylePane;
import com.fr.design.mainframe.chart.gui.data.report.AbstractReportDataContentPane;
import com.fr.design.mainframe.chart.gui.data.table.AbstractTableDataContentPane;
import com.fr.file.XMLFileManager;
import com.fr.general.FRLogger;
import com.fr.general.GeneralContext;
import com.fr.plugin.*;
import com.fr.stable.EnvChangedListener;
import com.fr.stable.StringUtils;
import com.fr.stable.fun.Authorize;
import com.fr.stable.plugin.ExtraChartDesignClassManagerProvider;
import com.fr.stable.plugin.PluginSimplify;
import com.fr.stable.xml.XMLPrintWriter;
import com.fr.stable.xml.XMLableReader;
import java.util.*;

public class ChartTypeInterfaceManager extends XMLFileManager
    implements ExtraChartDesignClassManagerProvider
{

    private static ClassLoader loader = Thread.currentThread().getContextClassLoader();
    private static ChartTypeInterfaceManager classManager = null;
    private static LinkedHashMap chartTypeInterfaces = new LinkedHashMap();

    public ChartTypeInterfaceManager()
    {
    }

    public static synchronized ChartTypeInterfaceManager getInstance()
    {
        if(classManager == null)
        {
            classManager = new ChartTypeInterfaceManager();
            chartTypeInterfaces.clear();
            ChartTypeInterfaceManager _tmp = classManager;
            readDefault();
            classManager.readXMLFile();
        }
        return classManager;
    }

    private static synchronized void envChanged()
    {
        classManager = null;
    }

    private static void readDefault()
    {
        chartTypeInterfaces.put("FineReportColumnChart", new ColumnIndependentChartInterface());
        chartTypeInterfaces.put("FineReportLineChart", new LineIndependentChartInterface());
        chartTypeInterfaces.put("FineReportBarChart", new BarIndependentChartInterface());
        chartTypeInterfaces.put("FineReportPieChart", new PieIndependentChartInterface());
        chartTypeInterfaces.put("FineReportAreaChart", new AreaIndependentChartInterface());
        chartTypeInterfaces.put("FineReportScatterChart", new XYScatterIndependentChartInterface());
        chartTypeInterfaces.put("FineReportBubbleChart", new BubbleIndependentChartInterface());
        chartTypeInterfaces.put("FineReportRadarChart", new RadarIndependentChartInterface());
        chartTypeInterfaces.put("FineReportStockChart", new StockIndependentChartInterface());
        chartTypeInterfaces.put("FineReportMeterChart", new MeterIndependentChartInterface());
        chartTypeInterfaces.put("FineReportRangeChart", new RangeIndependentChartInterface());
        chartTypeInterfaces.put("FineReportCustomChart", new CustomIndependentChartInterface());
        chartTypeInterfaces.put("FineReportGanttChart", new GanttIndependentChartInterface());
        chartTypeInterfaces.put("FineReportDonutChart", new DonutIndependentChartInterface());
        chartTypeInterfaces.put("FineReportMapChart", new MapIndependentChartInterface());
        chartTypeInterfaces.put("FineReportGisChart", new GisMapIndependentChartInterface());
        chartTypeInterfaces.put("FineReportFunnelChart", new FunnelIndependentChartInterface());
    }

    public String getIconPath(String s)
    {
        return ((IndependentChartUIProvider)chartTypeInterfaces.get(s)).getIconPath();
    }

    public void addChartInterface(String s, String s1, PluginSimplify pluginsimplify)
    {
        if(!StringUtils.isNotBlank(s))
            break MISSING_BLOCK_LABEL_172;
        try
        {
            Class class1 = loader.loadClass(s);
            Authorize authorize = (Authorize)class1.getAnnotation(com/fr/stable/fun/Authorize);
            if(authorize != null)
                PluginLicenseManager.getInstance().registerPaid(authorize, pluginsimplify);
            IndependentChartUIProvider independentchartuiprovider = (IndependentChartUIProvider)class1.newInstance();
            if(PluginCollector.getCollector().isError(independentchartuiprovider, 2, pluginsimplify.getPluginName()) || !containsChart(s1))
                PluginMessage.remindUpdate(s);
            else
            if(!chartTypeInterfaces.containsKey(s1))
                chartTypeInterfaces.put(s1, independentchartuiprovider);
        }
        catch(ClassNotFoundException classnotfoundexception)
        {
            FRLogger.getLogger().error((new StringBuilder()).append("class not found:").append(classnotfoundexception.getMessage()).toString());
        }
        catch(Object obj)
        {
            FRLogger.getLogger().error((new StringBuilder()).append("object create error:").append(((ReflectiveOperationException) (obj)).getMessage()).toString());
        }
    }

    private boolean containsChart(String s)
    {
        return ChartTypeManager.getInstance().containsPlot(s);
    }

    public void addPlotTypePaneList(List list)
    {
        IndependentChartUIProvider independentchartuiprovider;
        for(Iterator iterator = chartTypeInterfaces.entrySet().iterator(); iterator.hasNext(); list.add(independentchartuiprovider.getPlotTypePane()))
        {
            java.util.Map.Entry entry = (java.util.Map.Entry)iterator.next();
            independentchartuiprovider = (IndependentChartUIProvider)entry.getValue();
        }

    }

    public AbstractChartAttrPane[] getAttrPaneArray(String s, AttributeChangeListener attributechangelistener)
    {
        return ((IndependentChartUIProvider)chartTypeInterfaces.get(s)).getAttrPaneArray(attributechangelistener);
    }

    public AbstractTableDataContentPane getTableDataSourcePane(Plot plot, ChartDataPane chartdatapane)
    {
        return ((IndependentChartUIProvider)chartTypeInterfaces.get(plot.getPlotID())).getTableDataSourcePane(plot, chartdatapane);
    }

    public AbstractReportDataContentPane getReportDataSourcePane(Plot plot, ChartDataPane chartdatapane)
    {
        return ((IndependentChartUIProvider)chartTypeInterfaces.get(plot.getPlotID())).getReportDataSourcePane(plot, chartdatapane);
    }

    public ConditionAttributesPane getPlotConditionPane(Plot plot)
    {
        return ((IndependentChartUIProvider)chartTypeInterfaces.get(plot.getPlotID())).getPlotConditionPane(plot);
    }

    public BasicBeanPane getPlotSeriesPane(ChartStylePane chartstylepane, Plot plot)
    {
        return ((IndependentChartUIProvider)chartTypeInterfaces.get(plot.getPlotID())).getPlotSeriesPane(chartstylepane, plot);
    }

    public boolean isUseDefaultPane(String s)
    {
        if(chartTypeInterfaces.containsKey(s))
            return ((IndependentChartUIProvider)chartTypeInterfaces.get(s)).isUseDefaultPane();
        else
            return true;
    }

    public void readXML(XMLableReader xmlablereader)
    {
        readXML(xmlablereader, null, PluginSimplify.NULL);
    }

    public void readXML(XMLableReader xmlablereader, List list, PluginSimplify pluginsimplify)
    {
        if(xmlablereader.isChildNode())
        {
            String s = xmlablereader.getTagName();
            if(list != null)
                list.add(s);
            if("IndependentChartUIProvider".equals(s))
                addChartInterface(xmlablereader.getAttrAsString("class", ""), xmlablereader.getAttrAsString("plotID", ""), pluginsimplify);
        }
    }

    public String fileName()
    {
        return "chart.xml";
    }

    public void writeXML(XMLPrintWriter xmlprintwriter)
    {
    }

    static 
    {
        GeneralContext.addEnvChangedListener(new EnvChangedListener() {

            public void envChanged()
            {
                ChartTypeInterfaceManager.envChanged();
            }

        }
);
    }

}
