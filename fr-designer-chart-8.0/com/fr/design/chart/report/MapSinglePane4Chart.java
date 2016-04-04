// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.chart.report;

import com.fr.base.chart.chartdata.TopDefinitionProvider;
import com.fr.chart.chartdata.MapSingleLayerTableDefinition;
import com.fr.chart.chartdata.TopDefinition;
import com.fr.design.beans.FurtherBasicBeanPane;
import com.fr.design.data.tabledata.wrapper.TableDataWrapper;
import com.fr.general.Inter;
import java.awt.BorderLayout;

// Referenced classes of package com.fr.design.chart.report:
//            MapTableDataSinglePane4Chart

public class MapSinglePane4Chart extends FurtherBasicBeanPane
{

    private MapTableDataSinglePane4Chart tableSinglePane;

    public MapSinglePane4Chart()
    {
        initCom();
    }

    private void initCom()
    {
        setLayout(new BorderLayout());
        add(tableSinglePane = new MapTableDataSinglePane4Chart(), "Center");
    }

    public boolean accept(Object obj)
    {
        return obj instanceof TopDefinition;
    }

    public void reset()
    {
    }

    public String title4PopupWindow()
    {
        return Inter.getLocText(new String[] {
            "SingleLayer", "Chart-Map"
        });
    }

    public void populateBean(TopDefinitionProvider topdefinitionprovider)
    {
        if(topdefinitionprovider instanceof MapSingleLayerTableDefinition)
            tableSinglePane.populateBean((MapSingleLayerTableDefinition)topdefinitionprovider);
    }

    public TopDefinitionProvider updateBean()
    {
        return tableSinglePane.updateBean();
    }

    public void fireTableDataChanged(TableDataWrapper tabledatawrapper)
    {
        tableSinglePane.setTableDataWrapper(tabledatawrapper);
        tableSinglePane.refreshAreaNameBox();
    }

    public volatile Object updateBean()
    {
        return updateBean();
    }

    public volatile void populateBean(Object obj)
    {
        populateBean((TopDefinitionProvider)obj);
    }
}
