// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.chart.report;

import com.fr.base.chart.chartdata.TopDefinitionProvider;
import com.fr.chart.chartdata.MapMoreLayerReportDefinition;
import com.fr.chart.chartdata.MapMoreLayerTableDefinition;
import com.fr.design.gui.frpane.UIComboBoxPane;
import com.fr.design.gui.ilable.BoldFontTextLabel;
import com.fr.general.Inter;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.ArrayList;
import javax.swing.JPanel;

// Referenced classes of package com.fr.design.chart.report:
//            MapTableCubeDataPane, MapReportCubeDataPane

public class MapCubeDataPane extends UIComboBoxPane
{

    private MapReportCubeDataPane reportPane;
    private MapTableCubeDataPane tablePane;

    public MapCubeDataPane()
    {
    }

    protected void initLayout()
    {
        setLayout(new BorderLayout(0, 0));
        JPanel jpanel = new JPanel(new FlowLayout(0, 0, 0));
        jpanel.add(new BoldFontTextLabel((new StringBuilder()).append(Inter.getLocText("ChartF-Data-Resource")).append(":").toString()));
        jpanel.add(jcb);
        add(jpanel, "North");
        add(cardPane, "Center");
    }

    protected java.util.List initPaneList()
    {
        ArrayList arraylist = new ArrayList();
        arraylist.add(tablePane = new MapTableCubeDataPane());
        arraylist.add(reportPane = new MapReportCubeDataPane());
        return arraylist;
    }

    protected String title4PopupWindow()
    {
        return Inter.getLocText("LayerData");
    }

    public void populateBean(TopDefinitionProvider topdefinitionprovider)
    {
        if(topdefinitionprovider instanceof MapMoreLayerReportDefinition)
        {
            setSelectedIndex(1);
            MapMoreLayerReportDefinition mapmorelayerreportdefinition = (MapMoreLayerReportDefinition)topdefinitionprovider;
            reportPane.populateBean(mapmorelayerreportdefinition);
        } else
        if(topdefinitionprovider instanceof MapMoreLayerTableDefinition)
        {
            MapMoreLayerTableDefinition mapmorelayertabledefinition = (MapMoreLayerTableDefinition)topdefinitionprovider;
            setSelectedIndex(0);
            tablePane.populateBean(mapmorelayertabledefinition);
        }
    }

    public TopDefinitionProvider update()
    {
        if(getSelectedIndex() == 0)
            return tablePane.updateBean();
        else
            return reportPane.updateBean();
    }

    public volatile void populateBean(Object obj)
    {
        populateBean((TopDefinitionProvider)obj);
    }
}
