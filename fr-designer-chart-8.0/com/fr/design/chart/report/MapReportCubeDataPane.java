// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.chart.report;

import com.fr.chart.chartdata.MapMoreLayerReportDefinition;
import com.fr.design.beans.FurtherBasicBeanPane;
import com.fr.general.Inter;
import java.awt.BorderLayout;

// Referenced classes of package com.fr.design.chart.report:
//            MapMoreReportIndexPane

public class MapReportCubeDataPane extends FurtherBasicBeanPane
{

    private MapMoreReportIndexPane reportPane;

    public MapReportCubeDataPane()
    {
        setLayout(new BorderLayout(0, 0));
        reportPane = new MapMoreReportIndexPane();
        add(reportPane, "Center");
    }

    public boolean accept(Object obj)
    {
        return obj instanceof MapMoreLayerReportDefinition;
    }

    public void reset()
    {
    }

    public String title4PopupWindow()
    {
        return Inter.getLocText("Cell");
    }

    public void populateBean(MapMoreLayerReportDefinition mapmorelayerreportdefinition)
    {
        if(mapmorelayerreportdefinition != null)
        {
            com.fr.chart.chartdata.MapSingleLayerReportDefinition amapsinglelayerreportdefinition[] = mapmorelayerreportdefinition.getNameValues();
            if(amapsinglelayerreportdefinition != null && amapsinglelayerreportdefinition.length > 0)
                reportPane.populateBean(amapsinglelayerreportdefinition[0]);
        }
    }

    public MapMoreLayerReportDefinition updateBean()
    {
        MapMoreLayerReportDefinition mapmorelayerreportdefinition = new MapMoreLayerReportDefinition();
        mapmorelayerreportdefinition.clearNameValues();
        mapmorelayerreportdefinition.addNameValue(reportPane.updateBean());
        return mapmorelayerreportdefinition;
    }

    public volatile Object updateBean()
    {
        return updateBean();
    }

    public volatile void populateBean(Object obj)
    {
        populateBean((MapMoreLayerReportDefinition)obj);
    }
}
