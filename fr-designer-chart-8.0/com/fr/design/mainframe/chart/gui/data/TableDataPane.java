// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.chart.gui.data;

import com.fr.base.TableData;
import com.fr.chart.chartattr.*;
import com.fr.chart.chartdata.TableDataDefinition;
import com.fr.chart.chartdata.TopDefinition;
import com.fr.data.impl.NameTableData;
import com.fr.design.ChartTypeInterfaceManager;
import com.fr.design.beans.FurtherBasicBeanPane;
import com.fr.design.data.tabledata.wrapper.TableDataWrapper;
import com.fr.design.gui.ilable.BoldFontTextLabel;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.mainframe.chart.gui.ChartDataPane;
import com.fr.design.mainframe.chart.gui.data.table.AbstractTableDataContentPane;
import com.fr.general.Inter;
import java.awt.Dimension;
import javax.swing.BorderFactory;

// Referenced classes of package com.fr.design.mainframe.chart.gui.data:
//            DatabaseTableDataPane

public class TableDataPane extends FurtherBasicBeanPane
{

    private static final long serialVersionUID = 0x41c9803077c2240bL;
    private static final int TOP = -5;
    private DatabaseTableDataPane tableDataPane;
    private AbstractTableDataContentPane dataContentPane;
    private ChartDataPane parent;

    public TableDataPane(ChartDataPane chartdatapane)
    {
        parent = chartdatapane;
        initDataPane();
    }

    private void initDataPane()
    {
        BoldFontTextLabel boldfonttextlabel = new BoldFontTextLabel((new StringBuilder()).append(Inter.getLocText("Chart-DS_TableData")).append(":").toString(), 4);
        boldfonttextlabel.setPreferredSize(new Dimension(75, 20));
        tableDataPane = new DatabaseTableDataPane(boldfonttextlabel) {

            final TableDataPane this$0;

            protected void userEvent()
            {
                onSelectTableData();
                checkBoxUse();
            }

            
            {
                this$0 = TableDataPane.this;
                super(uilabel);
            }
        }
;
        tableDataPane.setBorder(BorderFactory.createMatteBorder(0, 6, 0, 0, getBackground()));
        tableDataPane.setBorder(BorderFactory.createEmptyBorder(0, 1, 0, 1));
        tableDataPane.setPreferredSize(new Dimension(205, 20));
        setBorder(BorderFactory.createEmptyBorder(-5, 0, 0, 0));
        add(tableDataPane, "North");
    }

    public void checkBoxUse()
    {
        TableDataWrapper tabledatawrapper = tableDataPane.getTableDataWrapper();
        if(dataContentPane != null)
            dataContentPane.checkBoxUse(tabledatawrapper != null);
    }

    private void onSelectTableData()
    {
        TableDataWrapper tabledatawrapper = tableDataPane.getTableDataWrapper();
        if(tabledatawrapper == null)
            return;
        if(dataContentPane != null)
            dataContentPane.onSelectTableData(tabledatawrapper);
    }

    private AbstractTableDataContentPane getContentPane(Plot plot)
    {
        return ChartTypeInterfaceManager.getInstance().getTableDataSourcePane(plot, parent);
    }

    public String title4PopupWindow()
    {
        return Inter.getLocText(new String[] {
            "TableData", "Data"
        });
    }

    public boolean accept(Object obj)
    {
        return (obj instanceof ChartCollection) && (((ChartCollection)obj).getSelectedChart().getFilterDefinition() instanceof TableDataDefinition);
    }

    public void reset()
    {
    }

    public void refreshContentPane(ChartCollection chartcollection)
    {
        if(dataContentPane != null)
            remove(dataContentPane);
        dataContentPane = getContentPane(chartcollection.getSelectedChart().getPlot());
        if(dataContentPane != null)
            add(dataContentPane, "Center");
    }

    public void populateBean(ChartCollection chartcollection)
    {
        if(chartcollection == null)
            return;
        TableDataDefinition tabledatadefinition = (TableDataDefinition)chartcollection.getSelectedChart().getFilterDefinition();
        TableData tabledata = null;
        if(tabledatadefinition != null)
            tabledata = tabledatadefinition.getTableData();
        onSelectTableData();
        checkBoxUse();
        tableDataPane.populateBean(tabledata);
        if(dataContentPane != null)
            dataContentPane.populateBean(chartcollection);
    }

    public void updateBean(ChartCollection chartcollection)
    {
        if(dataContentPane != null)
            dataContentPane.updateBean(chartcollection);
        TopDefinition topdefinition = (TopDefinition)chartcollection.getSelectedChart().getFilterDefinition();
        if(topdefinition instanceof TableDataDefinition)
        {
            TableDataWrapper tabledatawrapper = tableDataPane.getTableDataWrapper();
            if(topdefinition != null && tabledatawrapper != null)
            {
                NameTableData nametabledata = new NameTableData(tabledatawrapper.getTableDataName());
                ((TableDataDefinition)topdefinition).setTableData(nametabledata);
            }
        }
    }

    public ChartCollection updateBean()
    {
        return null;
    }

    public volatile void updateBean(Object obj)
    {
        updateBean((ChartCollection)obj);
    }

    public volatile Object updateBean()
    {
        return updateBean();
    }

    public volatile void populateBean(Object obj)
    {
        populateBean((ChartCollection)obj);
    }

}
