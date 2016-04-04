// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.chart.gui.data;

import com.fr.base.TableData;
import com.fr.base.chart.chartdata.TopDefinitionProvider;
import com.fr.chart.chartattr.*;
import com.fr.chart.chartdata.TableDataDefinition;
import com.fr.design.gui.frpane.AttributeChangeListener;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.mainframe.AbstractChartDataPane4Chart;
import com.fr.design.mainframe.chart.gui.ChartDataPane;
import com.fr.design.mainframe.chart.gui.data.table.AbstractTableDataContentPane;
import com.fr.design.mainframe.chart.gui.data.table.CategoryPlotMoreCateTableDataContentPane;
import com.fr.design.mainframe.chart.gui.data.table.Factory4TableDataContentPane;
import javax.swing.JPanel;

// Referenced classes of package com.fr.design.mainframe.chart.gui.data:
//            ChartDesignDataLoadPane

public class ImportSetChartDataPane extends AbstractChartDataPane4Chart
{

    private AbstractTableDataContentPane dataContentPane;

    public ImportSetChartDataPane(AttributeChangeListener attributechangelistener, ChartDataPane chartdatapane)
    {
        super(attributechangelistener, chartdatapane);
    }

    public void populate(ChartCollection chartcollection)
    {
        dataContentPane = getContentPane(chartcollection.getSelectedChart().getPlot());
        dataContentPane.setNeedSummaryCaculateMethod(false);
        dataContentPane.redoLayoutPane();
        if(chartcollection != null && chartcollection.getSelectedChart() != null)
        {
            Chart chart = chartcollection.getSelectedChart();
            TopDefinitionProvider topdefinitionprovider = chart.getFilterDefinition();
            if(topdefinitionprovider instanceof TableDataDefinition)
            {
                TableData tabledata = ((TableDataDefinition)topdefinitionprovider).getTableData();
                if(tabledata != null)
                {
                    populateChoosePane(tabledata);
                    fireTableDataChange();
                }
                if(dataContentPane != null)
                    dataContentPane.populateBean(chartcollection);
            }
        }
        remove(leftContentPane);
        initContentPane();
        validate();
        dataSource.addItemListener(dsListener);
        initAllListeners();
        initSelfListener(dataContentPane);
        addAttributeChangeListener(attributeChangeListener);
    }

    protected JPanel getDataContentPane()
    {
        return dataContentPane;
    }

    public void update(ChartCollection chartcollection)
    {
        if(chartcollection != null && chartcollection.getSelectedChart() != null)
        {
            if(dataContentPane != null)
                dataContentPane.updateBean(chartcollection);
            TableDataDefinition tabledatadefinition = (TableDataDefinition)chartcollection.getSelectedChart().getFilterDefinition();
            if(tabledatadefinition != null)
                tabledatadefinition.setTableData(choosePane.getTableData());
        }
    }

    private AbstractTableDataContentPane getContentPane(Plot plot)
    {
        if(plot == null || plot.isSupportMoreCate())
            return new CategoryPlotMoreCateTableDataContentPane(parentPane) {

                final ImportSetChartDataPane this$0;

                public boolean isNeedSummaryCaculateMethod()
                {
                    return false;
                }

            
            {
                this$0 = ImportSetChartDataPane.this;
                super(chartdatapane);
            }
            }
;
        else
            return Factory4TableDataContentPane.createTableDataContenetPaneWithPlotType(plot, parentPane);
    }

    public void fireTableDataChange()
    {
        if(dataContentPane != null)
            dataContentPane.onSelectTableData(choosePane.getTableDataWrapper());
    }

    public void clearTableDataSetting()
    {
        if(dataContentPane != null)
            dataContentPane.clearAllBoxList();
    }
}
