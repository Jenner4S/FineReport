// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.chart.report;

import com.fr.chart.chartdata.MapMoreLayerTableDefinition;
import com.fr.design.beans.FurtherBasicBeanPane;
import com.fr.design.data.tabledata.wrapper.TableDataWrapper;
import com.fr.general.Inter;
import java.awt.BorderLayout;

// Referenced classes of package com.fr.design.chart.report:
//            MapMoreTableIndexPane

public class MapTableCubeDataPane4Chart extends FurtherBasicBeanPane
{

    private MapMoreTableIndexPane tablePane;
    private TableDataWrapper tableDataWrapper;

    public MapTableCubeDataPane4Chart()
    {
        setLayout(new BorderLayout());
        tablePane = new MapMoreTableIndexPane();
        add(tablePane, "Center");
    }

    public void refreshAreaNameBox()
    {
        TableDataWrapper tabledatawrapper = tableDataWrapper;
        if(tabledatawrapper == null)
        {
            return;
        } else
        {
            java.util.List list = tabledatawrapper.calculateColumnNameList();
            tablePane.initAreaComBox(list.toArray(new String[list.size()]));
            return;
        }
    }

    public void setTableDataWrapper(TableDataWrapper tabledatawrapper)
    {
        tableDataWrapper = tabledatawrapper;
    }

    public boolean accept(Object obj)
    {
        return true;
    }

    public void reset()
    {
    }

    public String title4PopupWindow()
    {
        return Inter.getLocText("FR-Chart-Table_Data");
    }

    public void populateBean(MapMoreLayerTableDefinition mapmorelayertabledefinition)
    {
        if(mapmorelayertabledefinition != null)
        {
            com.fr.chart.chartdata.MapSingleLayerTableDefinition amapsinglelayertabledefinition[] = mapmorelayertabledefinition.getNameValues();
            if(amapsinglelayertabledefinition != null && amapsinglelayertabledefinition.length > 0)
                tablePane.populateBean(amapsinglelayertabledefinition[0]);
        }
    }

    public MapMoreLayerTableDefinition updateBean()
    {
        MapMoreLayerTableDefinition mapmorelayertabledefinition = new MapMoreLayerTableDefinition();
        TableDataWrapper tabledatawrapper = tableDataWrapper;
        if(tabledatawrapper != null)
        {
            mapmorelayertabledefinition.setTableData(tabledatawrapper.getTableData());
            mapmorelayertabledefinition.clearNameValues();
            mapmorelayertabledefinition.addNameValue(tablePane.updateBean());
        }
        return mapmorelayertabledefinition;
    }

    public volatile Object updateBean()
    {
        return updateBean();
    }

    public volatile void populateBean(Object obj)
    {
        populateBean((MapMoreLayerTableDefinition)obj);
    }
}
