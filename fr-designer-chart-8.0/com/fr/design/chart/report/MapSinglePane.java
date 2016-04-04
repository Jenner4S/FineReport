// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.chart.report;

import com.fr.base.chart.chartdata.TopDefinitionProvider;
import com.fr.chart.chartdata.MapSingleLayerReportDefinition;
import com.fr.chart.chartdata.MapSingleLayerTableDefinition;
import com.fr.chart.chartdata.TopDefinition;
import com.fr.design.beans.FurtherBasicBeanPane;
import com.fr.design.gui.frpane.UIComboBoxPane;
import com.fr.design.gui.ilable.BoldFontTextLabel;
import com.fr.general.Inter;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.ArrayList;
import javax.swing.JPanel;

// Referenced classes of package com.fr.design.chart.report:
//            MapReportDataSinglePane, MapTableDataSinglePane

public class MapSinglePane extends FurtherBasicBeanPane
{

    private UIComboBoxPane dataFromPane;
    private MapReportDataSinglePane reportSinglePane;
    private MapTableDataSinglePane tableSinglePane;

    public MapSinglePane()
    {
        initCom();
    }

    private void initCom()
    {
        setLayout(new BorderLayout());
        add(dataFromPane = new UIComboBoxPane() {

            final MapSinglePane this$0;

            protected void initLayout()
            {
                setLayout(new BorderLayout(0, 6));
                JPanel jpanel = new JPanel(new FlowLayout(0));
                jpanel.add(new BoldFontTextLabel((new StringBuilder()).append(Inter.getLocText("ChartF-Data-Resource")).append(":").toString()));
                jpanel.add(jcb);
                add(jpanel, "North");
                add(cardPane, "Center");
            }

            protected java.util.List initPaneList()
            {
                ArrayList arraylist = new ArrayList();
                arraylist.add(tableSinglePane = new MapTableDataSinglePane());
                arraylist.add(reportSinglePane = new MapReportDataSinglePane());
                return arraylist;
            }

            protected String title4PopupWindow()
            {
                return Inter.getLocText("Data_Setting");
            }

            
            {
                this$0 = MapSinglePane.this;
                super();
            }
        }
, "Center");
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
        if(topdefinitionprovider instanceof MapSingleLayerReportDefinition)
        {
            dataFromPane.setSelectedIndex(1);
            reportSinglePane.populateBean((MapSingleLayerReportDefinition)topdefinitionprovider);
        } else
        if(topdefinitionprovider instanceof MapSingleLayerTableDefinition)
        {
            dataFromPane.setSelectedIndex(0);
            tableSinglePane.populateBean((MapSingleLayerTableDefinition)topdefinitionprovider);
        }
    }

    public TopDefinitionProvider updateBean()
    {
        if(dataFromPane.getSelectedIndex() == 0)
            return tableSinglePane.updateBean();
        else
            return reportSinglePane.updateBean();
    }

    public void setSurpportCellData(boolean flag)
    {
        dataFromPane.justSupportOneSelect(flag);
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
