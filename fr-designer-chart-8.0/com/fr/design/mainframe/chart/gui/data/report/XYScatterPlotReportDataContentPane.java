// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.chart.gui.data.report;

import com.fr.base.chart.chartdata.TopDefinitionProvider;
import com.fr.chart.chartattr.*;
import com.fr.chart.chartdata.ScatterReportDefinition;
import com.fr.chart.chartdata.ScatterSeriesValue;
import com.fr.design.gui.frpane.UICorrelationPane;
import com.fr.design.gui.ilable.BoldFontTextLabel;
import com.fr.design.mainframe.chart.gui.ChartDataPane;
import com.fr.design.mainframe.chart.gui.data.ChartDataFilterPane;
import com.fr.general.Inter;
import java.util.ArrayList;
import java.util.List;

// Referenced classes of package com.fr.design.mainframe.chart.gui.data.report:
//            AbstractReportDataContentPane

public class XYScatterPlotReportDataContentPane extends AbstractReportDataContentPane
{

    private ChartDataFilterPane filterPane;

    public XYScatterPlotReportDataContentPane(ChartDataPane chartdatapane)
    {
        initEveryPane();
        add(new BoldFontTextLabel(Inter.getLocText("Data_Filter")), "0,4,2,4");
        add(filterPane = new ChartDataFilterPane(new XYScatterPlot(), chartdatapane), "0,6,2,4");
    }

    protected String[] columnNames()
    {
        return (new String[] {
            Inter.getLocText("Series_Name"), (new StringBuilder()).append(Inter.getLocText("Chart_Scatter")).append("x").toString(), (new StringBuilder()).append(Inter.getLocText("Chart_Scatter")).append("y").toString()
        });
    }

    public void populateBean(ChartCollection chartcollection)
    {
        TopDefinitionProvider topdefinitionprovider = chartcollection.getSelectedChart().getFilterDefinition();
        if(topdefinitionprovider instanceof ScatterReportDefinition)
        {
            ScatterReportDefinition scatterreportdefinition = (ScatterReportDefinition)topdefinitionprovider;
            List list = populateSeriesEntryList(scatterreportdefinition);
            if(list != null && !list.isEmpty())
                seriesPane.populateBean(list);
        }
        filterPane.populateBean(chartcollection);
    }

    private List populateSeriesEntryList(ScatterReportDefinition scatterreportdefinition)
    {
        ArrayList arraylist = new ArrayList();
        for(int i = 0; i < scatterreportdefinition.size(); i++)
        {
            ScatterSeriesValue scatterseriesvalue = (ScatterSeriesValue)scatterreportdefinition.get(i);
            Object aobj[] = new Object[3];
            aobj[0] = scatterseriesvalue.getScatterSeriesName();
            aobj[1] = scatterseriesvalue.getScatterSeriesX();
            aobj[2] = scatterseriesvalue.getScatterSeriesY();
            arraylist.add(((Object) (aobj)));
        }

        return arraylist;
    }

    public void updateBean(ChartCollection chartcollection)
    {
        ScatterReportDefinition scatterreportdefinition = new ScatterReportDefinition();
        chartcollection.getSelectedChart().setFilterDefinition(scatterreportdefinition);
        updateSeriesEntryList(scatterreportdefinition, seriesPane.updateBean());
        filterPane.updateBean(chartcollection);
    }

    private void updateSeriesEntryList(ScatterReportDefinition scatterreportdefinition, List list)
    {
        for(int i = 0; i < list.size(); i++)
        {
            Object aobj[] = (Object[])list.get(i);
            ScatterSeriesValue scatterseriesvalue = new ScatterSeriesValue();
            scatterseriesvalue.setScatterSeriesName(canBeFormula(aobj[0]));
            scatterseriesvalue.setScatterSeriesX(canBeFormula(aobj[1]));
            scatterseriesvalue.setScatterSeriesY(canBeFormula(aobj[2]));
            scatterreportdefinition.add(scatterseriesvalue);
        }

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
