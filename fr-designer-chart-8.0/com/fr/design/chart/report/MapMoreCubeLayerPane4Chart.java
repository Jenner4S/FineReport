// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.chart.report;

import com.fr.chart.chartattr.*;
import com.fr.design.data.tabledata.wrapper.TableDataWrapper;
import com.fr.design.dialog.MultiTabPane;
import com.fr.general.Inter;
import java.util.ArrayList;
import java.util.List;

// Referenced classes of package com.fr.design.chart.report:
//            MapCubeLayerPane, MapCubeDataPane4Chart

public class MapMoreCubeLayerPane4Chart extends MultiTabPane
{

    private static final long serialVersionUID = 0xfd94cfa2ba1dc2e1L;
    private MapCubeLayerPane layerPane;
    private MapCubeDataPane4Chart dataPane;

    public MapMoreCubeLayerPane4Chart()
    {
    }

    protected List initPaneList()
    {
        ArrayList arraylist = new ArrayList();
        arraylist.add(layerPane = new MapCubeLayerPane());
        arraylist.add(dataPane = new MapCubeDataPane4Chart());
        return arraylist;
    }

    public ChartCollection updateBean()
    {
        return null;
    }

    public void populateBean(ChartCollection chartcollection)
    {
        Chart chart = chartcollection.getSelectedChart();
        if(chart != null && (chart.getPlot() instanceof MapPlot))
        {
            MapPlot mapplot = (MapPlot)chart.getPlot();
            layerPane.populateBean(mapplot.getMapName());
        }
        dataPane.populateBean(chartcollection.getSelectedChart().getFilterDefinition());
    }

    public void updateBean(ChartCollection chartcollection)
    {
        chartcollection.getSelectedChart().setFilterDefinition(dataPane.update());
        Chart chart = chartcollection.getSelectedChart();
        if(chart != null && (chart.getPlot() instanceof MapPlot))
        {
            MapPlot mapplot = (MapPlot)chart.getPlot();
            layerPane.updateBean(mapplot.getMapName());
        }
    }

    public void init4PopuMapTree(ChartCollection chartcollection)
    {
        Chart chart = chartcollection.getSelectedChart();
        if(chart != null && (chart.getPlot() instanceof MapPlot))
        {
            MapPlot mapplot = (MapPlot)chart.getPlot();
            if(layerPane != null)
                layerPane.initRootTree(mapplot.getMapName());
        }
    }

    public boolean accept(Object obj)
    {
        return true;
    }

    public String title4PopupWindow()
    {
        return Inter.getLocText("FR-Chart-Map_Multilayer");
    }

    public void reset()
    {
    }

    public void setSurpportCellData(boolean flag)
    {
        dataPane.justSupportOneSelect(flag);
    }

    public void fireTableDataChanged(TableDataWrapper tabledatawrapper)
    {
        dataPane.fireTableDataChanged(tabledatawrapper);
    }

    public volatile void updateBean(Object obj)
    {
        updateBean((ChartCollection)obj);
    }

    public volatile void populateBean(Object obj)
    {
        populateBean((ChartCollection)obj);
    }

    public volatile Object updateBean()
    {
        return updateBean();
    }
}
