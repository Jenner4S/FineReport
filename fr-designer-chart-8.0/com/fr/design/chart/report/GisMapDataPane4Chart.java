// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.chart.report;

import com.fr.base.chart.chartdata.TopDefinitionProvider;
import com.fr.chart.chartattr.Chart;
import com.fr.chart.chartattr.ChartCollection;
import com.fr.chart.chartdata.GisMapTableDefinition;
import com.fr.chart.chartdata.TableDataDefinition;
import com.fr.design.gui.frpane.AttributeChangeListener;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.mainframe.AbstractChartDataPane4Chart;
import com.fr.design.mainframe.chart.gui.ChartDataPane;
import com.fr.design.mainframe.chart.gui.data.ChartDesignDataLoadPane;
import javax.swing.JPanel;

// Referenced classes of package com.fr.design.chart.report:
//            GisMapTableDataContentPane4Chart

public class GisMapDataPane4Chart extends AbstractChartDataPane4Chart
{

    private GisMapTableDataContentPane4Chart tablePane;

    public GisMapDataPane4Chart(AttributeChangeListener attributechangelistener, ChartDataPane chartdatapane)
    {
        super(attributechangelistener, chartdatapane);
        tablePane = new GisMapTableDataContentPane4Chart();
    }

    protected JPanel getDataContentPane()
    {
        return tablePane;
    }

    public void populate(ChartCollection chartcollection)
    {
        tablePane = new GisMapTableDataContentPane4Chart();
        if(chartcollection != null && chartcollection.getSelectedChart() != null)
        {
            Chart chart = chartcollection.getSelectedChart();
            TopDefinitionProvider topdefinitionprovider = chart.getFilterDefinition();
            if(topdefinitionprovider instanceof TableDataDefinition)
            {
                com.fr.base.TableData tabledata = ((TableDataDefinition)topdefinitionprovider).getTableData();
                if(tabledata != null)
                {
                    populateChoosePane(tabledata);
                    fireTableDataChange();
                }
            }
            if(topdefinitionprovider instanceof GisMapTableDefinition)
                tablePane.populateBean((GisMapTableDefinition)topdefinitionprovider);
        }
        remove(leftContentPane);
        initContentPane();
        validate();
        dataSource.addItemListener(dsListener);
        initAllListeners();
        initSelfListener(tablePane);
        addAttributeChangeListener(attributeChangeListener);
    }

    public void update(ChartCollection chartcollection)
    {
        chartcollection.getSelectedChart().setFilterDefinition(tablePane.updateBean());
    }

    public void fireTableDataChange()
    {
        tablePane.fireTableDataChange(choosePane.getTableDataWrapper());
    }
}
