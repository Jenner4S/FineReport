// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.chart.gui.data;

import com.fr.base.TableData;
import com.fr.design.data.tabledata.wrapper.TableDataWrapper;
import com.fr.design.data.tabledata.wrapper.TemplateTableDataWrapper;
import com.fr.design.dialog.BasicPane;
import com.fr.design.mainframe.AbstractChartDataPane4Chart;

public abstract class ChartDesignDataLoadPane extends BasicPane
{

    private AbstractChartDataPane4Chart parentPane;

    public ChartDesignDataLoadPane(AbstractChartDataPane4Chart abstractchartdatapane4chart)
    {
        parentPane = abstractchartdatapane4chart;
    }

    public abstract void populateChartTableData(TableData tabledata);

    public abstract TableData getTableData();

    protected abstract String getNamePrefix();

    protected void fireChange()
    {
        parentPane.fireTableDataChange();
    }

    public TableDataWrapper getTableDataWrapper()
    {
        return new TemplateTableDataWrapper(getTableData());
    }
}
