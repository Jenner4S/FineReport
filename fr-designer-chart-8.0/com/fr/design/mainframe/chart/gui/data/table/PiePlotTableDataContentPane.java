// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.chart.gui.data.table;

import com.fr.chart.chartattr.ChartCollection;
import com.fr.chart.chartattr.PiePlot;
import com.fr.design.mainframe.chart.gui.ChartDataPane;
import java.awt.BorderLayout;

// Referenced classes of package com.fr.design.mainframe.chart.gui.data.table:
//            AbstractTableDataContentPane, SeriesTypeUseComboxPane

public class PiePlotTableDataContentPane extends AbstractTableDataContentPane
{

    private SeriesTypeUseComboxPane typeChoosePane;

    public PiePlotTableDataContentPane(ChartDataPane chartdatapane)
    {
        typeChoosePane = new SeriesTypeUseComboxPane(chartdatapane, new PiePlot());
        setLayout(new BorderLayout());
        add(typeChoosePane, "Center");
    }

    public void checkBoxUse(boolean flag)
    {
        typeChoosePane.checkUseBox(flag);
    }

    protected void refreshBoxListWithSelectTableData(java.util.List list)
    {
        typeChoosePane.refreshBoxListWithSelectTableData(list);
    }

    public void clearAllBoxList()
    {
        typeChoosePane.clearAllBoxList();
    }

    public void updateBean(ChartCollection chartcollection)
    {
        typeChoosePane.updateBean(chartcollection);
    }

    public void populateBean(ChartCollection chartcollection)
    {
        typeChoosePane.populateBean(chartcollection, isNeedSummaryCaculateMethod());
    }

    public void redoLayoutPane()
    {
        typeChoosePane.relayoutPane(isNeedSummaryCaculateMethod());
    }

    public volatile void updateBean(Object obj)
    {
        updateBean((ChartCollection)obj);
    }

    public volatile void populateBean(Object obj)
    {
        populateBean((ChartCollection)obj);
    }
}
