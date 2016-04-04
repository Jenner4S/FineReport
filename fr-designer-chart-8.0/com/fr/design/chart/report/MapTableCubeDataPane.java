// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.chart.report;

import com.fr.chart.chartdata.MapMoreLayerTableDefinition;
import com.fr.data.impl.NameTableData;
import com.fr.design.beans.FurtherBasicBeanPane;
import com.fr.design.data.tabledata.wrapper.TableDataWrapper;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.mainframe.chart.gui.data.DatabaseTableDataPane;
import com.fr.general.Inter;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import javax.swing.JPanel;

// Referenced classes of package com.fr.design.chart.report:
//            MapMoreTableIndexPane

public class MapTableCubeDataPane extends FurtherBasicBeanPane
{

    private DatabaseTableDataPane dataFromBox;
    private MapMoreTableIndexPane tablePane;

    public MapTableCubeDataPane()
    {
        setLayout(new BorderLayout());
        JPanel jpanel = new JPanel();
        add(jpanel, "North");
        jpanel.setLayout(new FlowLayout(0));
        UILabel uilabel = new UILabel((new StringBuilder()).append(Inter.getLocText("Select_Data_Set")).append(":").toString(), 4);
        dataFromBox = new DatabaseTableDataPane(uilabel) {

            final MapTableCubeDataPane this$0;

            protected void userEvent()
            {
                refreshAreaNameBox();
            }

            
            {
                this$0 = MapTableCubeDataPane.this;
                super(uilabel);
            }
        }
;
        dataFromBox.setPreferredSize(new Dimension(180, 20));
        jpanel.add(dataFromBox);
        tablePane = new MapMoreTableIndexPane();
        add(tablePane, "Center");
    }

    private void refreshAreaNameBox()
    {
        TableDataWrapper tabledatawrapper = dataFromBox.getTableDataWrapper();
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

    public boolean accept(Object obj)
    {
        return true;
    }

    public void reset()
    {
    }

    public String title4PopupWindow()
    {
        return Inter.getLocText("DS-TableData");
    }

    public void populateBean(MapMoreLayerTableDefinition mapmorelayertabledefinition)
    {
        if(mapmorelayertabledefinition != null)
        {
            dataFromBox.populateBean(mapmorelayertabledefinition.getTableData());
            com.fr.chart.chartdata.MapSingleLayerTableDefinition amapsinglelayertabledefinition[] = mapmorelayertabledefinition.getNameValues();
            if(amapsinglelayertabledefinition != null && amapsinglelayertabledefinition.length > 0)
                tablePane.populateBean(amapsinglelayertabledefinition[0]);
        }
    }

    public MapMoreLayerTableDefinition updateBean()
    {
        MapMoreLayerTableDefinition mapmorelayertabledefinition = new MapMoreLayerTableDefinition();
        TableDataWrapper tabledatawrapper = dataFromBox.getTableDataWrapper();
        if(tabledatawrapper != null)
        {
            mapmorelayertabledefinition.setTableData(new NameTableData(tabledatawrapper.getTableDataName()));
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
