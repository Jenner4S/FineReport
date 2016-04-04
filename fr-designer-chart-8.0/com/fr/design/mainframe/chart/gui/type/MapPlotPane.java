// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.chart.gui.type;

import com.fr.base.FRContext;
import com.fr.chart.base.ChartEnumDefinitions;
import com.fr.chart.chartattr.Chart;
import com.fr.chart.chartattr.Legend;
import com.fr.chart.chartattr.MapPlot;
import com.fr.chart.chartattr.Plot;
import com.fr.chart.charttypes.MapIndependentChart;
import com.fr.design.chart.series.PlotSeries.MapGroupExtensionPane;
import com.fr.design.chart.series.PlotStyle.ChartSelectDemoPane;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.general.FRLogger;
import com.fr.general.Inter;
import java.awt.BorderLayout;
import java.util.Iterator;
import javax.swing.JPanel;

// Referenced classes of package com.fr.design.mainframe.chart.gui.type:
//            AbstractChartTypePane, ChartImagePane

public class MapPlotPane extends AbstractChartTypePane
{

    private boolean isSvgMap;
    private MapGroupExtensionPane groupExtensionPane;

    public MapPlotPane()
    {
        isSvgMap = true;
        typeDemo = createTypeDemoList();
        groupExtensionPane = new MapGroupExtensionPane();
        JPanel jpanel = FRGUIPaneFactory.createNColumnGridInnerContainer_S_Pane(4);
        for(int i = 0; i < typeDemo.size(); i++)
        {
            ChartImagePane chartimagepane = (ChartImagePane)typeDemo.get(i);
            jpanel.add(chartimagepane);
            chartimagepane.setDemoGroup((ChartSelectDemoPane[])typeDemo.toArray(new ChartSelectDemoPane[typeDemo.size()]));
        }

        setLayout(new BorderLayout());
        add(jpanel, "North");
        add(groupExtensionPane, "Center");
    }

    protected String[] getTypeIconPath()
    {
        return (new String[] {
            "/com/fr/design/images/chart/MapPlot/type/0.png", "/com/fr/design/images/chart/MapPlot/type/1.png", "/com/fr/design/images/chart/MapPlot/type/2.png", "/com/fr/design/images/chart/MapPlot/type/3.png"
        });
    }

    protected String[] getTypeTipName()
    {
        return (new String[] {
            Inter.getLocText("FR-Chart-Map_Normal"), Inter.getLocText("FR-Chart-Map_Bubble"), Inter.getLocText("FR-Chart-Map_Pie"), Inter.getLocText("FR-Chart-Map_Column")
        });
    }

    protected String[] getTypeLayoutPath()
    {
        return new String[0];
    }

    protected String[] getTypeLayoutTipName()
    {
        return new String[0];
    }

    public String title4PopupWindow()
    {
        return Inter.getLocText("FR-Chart-Map_Map");
    }

    public void updateBean(Chart chart)
    {
        if(needsResetChart(chart))
            resetChart(chart);
        MapPlot mapplot = getNewMapPlot();
        if(chart.getPlot().accept(com/fr/chart/chartattr/MapPlot))
            try
            {
                mapplot = (MapPlot)chart.getPlot().clone();
            }
            catch(CloneNotSupportedException clonenotsupportedexception)
            {
                FRContext.getLogger().error(clonenotsupportedexception.getMessage(), clonenotsupportedexception);
            }
        mapplot.setMapName(groupExtensionPane.updateBean(mapplot));
        if(((ChartImagePane)typeDemo.get(com.fr.chart.base.ChartEnumDefinitions.MapType.Map_Normal.ordinal())).isPressing)
            mapplot.setMapType(com.fr.chart.base.ChartEnumDefinitions.MapType.Map_Normal);
        else
        if(((ChartImagePane)typeDemo.get(com.fr.chart.base.ChartEnumDefinitions.MapType.Map_Bubble.ordinal())).isPressing)
            mapplot.setMapType(com.fr.chart.base.ChartEnumDefinitions.MapType.Map_Bubble);
        else
        if(((ChartImagePane)typeDemo.get(com.fr.chart.base.ChartEnumDefinitions.MapType.Map_Pie.ordinal())).isPressing)
            mapplot.setMapType(com.fr.chart.base.ChartEnumDefinitions.MapType.Map_Pie);
        else
        if(((ChartImagePane)typeDemo.get(com.fr.chart.base.ChartEnumDefinitions.MapType.Map_Column.ordinal())).isPressing)
            mapplot.setMapType(com.fr.chart.base.ChartEnumDefinitions.MapType.Map_Column);
        if(mapplot.getMapType() != com.fr.chart.base.ChartEnumDefinitions.MapType.Map_Normal)
            mapplot.setHeatMap(false);
        chart.setPlot(mapplot);
        checkDemosBackground();
    }

    protected String getPlotTypeID()
    {
        return "FineReportMapChart";
    }

    private MapPlot getNewMapPlot()
    {
        MapPlot mapplot = new MapPlot();
        mapplot.setLegend(new Legend());
        mapplot.setSvgMap(isSvgMap);
        return mapplot;
    }

    public void populateBean(Chart chart)
    {
        Plot plot = chart.getPlot();
        if(plot instanceof MapPlot)
        {
            MapPlot mapplot = (MapPlot)plot;
            isSvgMap = mapplot.isSvgMap();
            groupExtensionPane.populateBean(mapplot);
            for(Iterator iterator = typeDemo.iterator(); iterator.hasNext();)
            {
                ChartImagePane chartimagepane = (ChartImagePane)iterator.next();
                chartimagepane.isPressing = false;
            }

            ((ChartImagePane)typeDemo.get(mapplot.getMapType().ordinal())).isPressing = true;
            lastTypeIndex = mapplot.getMapType().ordinal();
        }
        checkDemosBackground();
        checkState();
    }

    private void checkState()
    {
        for(int i = 0; i < typeDemo.size(); i++)
            ((ChartImagePane)typeDemo.get(i)).setEnabled(isSvgMap);

        groupExtensionPane.setEnabled(isSvgMap);
    }

    public Chart getDefaultChart()
    {
        return MapIndependentChart.mapChartTypes[0];
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
