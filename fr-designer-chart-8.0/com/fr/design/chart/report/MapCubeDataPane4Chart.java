// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.chart.report;

import com.fr.base.chart.chartdata.TopDefinitionProvider;
import com.fr.chart.chartdata.MapMoreLayerTableDefinition;
import com.fr.design.data.tabledata.wrapper.TableDataWrapper;
import com.fr.design.gui.frpane.UIComboBoxPane;
import com.fr.general.Inter;
import java.awt.BorderLayout;
import java.util.ArrayList;

// Referenced classes of package com.fr.design.chart.report:
//            MapTableCubeDataPane4Chart

public class MapCubeDataPane4Chart extends UIComboBoxPane
{

    private MapTableCubeDataPane4Chart tablePane;

    public MapCubeDataPane4Chart()
    {
    }

    protected void initLayout()
    {
        setLayout(new BorderLayout(0, 0));
        add(cardPane, "Center");
    }

    protected java.util.List initPaneList()
    {
        ArrayList arraylist = new ArrayList();
        arraylist.add(tablePane = new MapTableCubeDataPane4Chart());
        return arraylist;
    }

    protected String title4PopupWindow()
    {
        return Inter.getLocText("FR-Chart-Map_LayerData");
    }

    public void populateBean(TopDefinitionProvider topdefinitionprovider)
    {
        if(topdefinitionprovider instanceof MapMoreLayerTableDefinition)
        {
            MapMoreLayerTableDefinition mapmorelayertabledefinition = (MapMoreLayerTableDefinition)topdefinitionprovider;
            setSelectedIndex(0);
            tablePane.populateBean(mapmorelayertabledefinition);
        }
    }

    public TopDefinitionProvider update()
    {
        return tablePane.updateBean();
    }

    public void fireTableDataChanged(TableDataWrapper tabledatawrapper)
    {
        tablePane.setTableDataWrapper(tabledatawrapper);
        tablePane.refreshAreaNameBox();
    }

    public volatile void populateBean(Object obj)
    {
        populateBean((TopDefinitionProvider)obj);
    }
}
