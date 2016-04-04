// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.chart.gui.data.report;

import com.fr.base.chart.chartdata.TopDefinitionProvider;
import com.fr.chart.chartattr.*;
import com.fr.chart.chartdata.NormalReportDataDefinition;
import com.fr.chart.chartdata.SeriesDefinition;
import com.fr.design.gui.ilable.BoldFontTextLabel;
import com.fr.design.mainframe.chart.gui.ChartDataPane;
import com.fr.design.mainframe.chart.gui.data.ChartDataFilterPane;
import com.fr.general.Inter;
import java.util.ArrayList;
import java.util.List;

// Referenced classes of package com.fr.design.mainframe.chart.gui.data.report:
//            AbstractReportDataContentPane

public class PiePlotReportDataContentPane extends AbstractReportDataContentPane
{

    private ChartDataFilterPane filterPane;

    public PiePlotReportDataContentPane(ChartDataPane chartdatapane)
    {
        initEveryPane();
        add(new BoldFontTextLabel(Inter.getLocText("Data_Filter")), "0,4,2,4");
        add(filterPane = new ChartDataFilterPane(new PiePlot(), chartdatapane), "0,6,2,4");
    }

    protected String[] columnNames()
    {
        return (new String[] {
            Inter.getLocText(new String[] {
                "Chart_Legend(Series)", "WF-Name"
            }), Inter.getLocText(new String[] {
                "Chart_Legend(Series)", "Values"
            })
        });
    }

    public void populateBean(ChartCollection chartcollection)
    {
        TopDefinitionProvider topdefinitionprovider = chartcollection.getSelectedChart().getFilterDefinition();
        if(topdefinitionprovider instanceof NormalReportDataDefinition)
        {
            NormalReportDataDefinition normalreportdatadefinition = (NormalReportDataDefinition)topdefinitionprovider;
            List list = getEntryList(normalreportdatadefinition);
            if(!list.isEmpty())
                populateList(list);
        }
        filterPane.populateBean(chartcollection);
    }

    private List getEntryList(NormalReportDataDefinition normalreportdatadefinition)
    {
        ArrayList arraylist = new ArrayList();
        for(int i = 0; i < normalreportdatadefinition.size(); i++)
        {
            SeriesDefinition seriesdefinition = (SeriesDefinition)normalreportdatadefinition.get(i);
            Object aobj[] = new Object[2];
            aobj[0] = seriesdefinition.getSeriesName();
            aobj[1] = seriesdefinition.getValue();
            if(aobj[0] != null && aobj[1] != null)
                arraylist.add(((Object) (aobj)));
        }

        return arraylist;
    }

    public void updateBean(ChartCollection chartcollection)
    {
        chartcollection.getSelectedChart().setFilterDefinition(new NormalReportDataDefinition());
        TopDefinitionProvider topdefinitionprovider = chartcollection.getSelectedChart().getFilterDefinition();
        if(topdefinitionprovider instanceof NormalReportDataDefinition)
        {
            NormalReportDataDefinition normalreportdatadefinition = (NormalReportDataDefinition)topdefinitionprovider;
            normalreportdatadefinition.setCategoryName("");
            List list = updateList();
            for(int i = 0; i < list.size(); i++)
            {
                Object aobj[] = (Object[])(Object[])list.get(i);
                SeriesDefinition seriesdefinition = new SeriesDefinition();
                seriesdefinition.setSeriesName(canBeFormula(aobj[0]));
                seriesdefinition.setValue(canBeFormula(aobj[1]));
                normalreportdatadefinition.add(seriesdefinition);
            }

        }
        filterPane.updateBean(chartcollection);
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
