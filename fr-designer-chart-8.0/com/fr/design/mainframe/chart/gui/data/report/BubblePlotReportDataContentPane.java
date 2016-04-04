// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.chart.gui.data.report;

import com.fr.chart.chartattr.*;
import com.fr.chart.chartdata.BubbleReportDefinition;
import com.fr.chart.chartdata.BubbleSeriesValue;
import com.fr.design.gui.frpane.UICorrelationPane;
import com.fr.design.gui.ilable.BoldFontTextLabel;
import com.fr.design.mainframe.chart.gui.ChartDataPane;
import com.fr.design.mainframe.chart.gui.data.ChartDataFilterPane;
import com.fr.general.Inter;
import java.util.ArrayList;
import java.util.List;

// Referenced classes of package com.fr.design.mainframe.chart.gui.data.report:
//            AbstractReportDataContentPane

public class BubblePlotReportDataContentPane extends AbstractReportDataContentPane
{

    private static final int BUBBLE = 4;
    private ChartDataFilterPane filterPane;

    public BubblePlotReportDataContentPane(ChartDataPane chartdatapane)
    {
        initEveryPane();
        add(new BoldFontTextLabel(Inter.getLocText("Data_Filter")), "0,4,2,4");
        add(filterPane = new ChartDataFilterPane(new BubblePlot(), chartdatapane), "0,6,2,4");
    }

    protected String[] columnNames()
    {
        return (new String[] {
            Inter.getLocText("Bubble-Series_Name"), Inter.getLocText("ChartF-X_Axis"), Inter.getLocText("ChartF-Y_Axis"), Inter.getLocText("FRFont-Size")
        });
    }

    public void checkBoxUse()
    {
    }

    public void populateBean(ChartCollection chartcollection)
    {
        if(chartcollection != null)
        {
            com.fr.base.chart.chartdata.TopDefinitionProvider topdefinitionprovider = chartcollection.getSelectedChart().getFilterDefinition();
            if(topdefinitionprovider instanceof BubbleReportDefinition)
                seriesPane.populateBean(populateSeriesEntryList((BubbleReportDefinition)topdefinitionprovider));
        }
        filterPane.populateBean(chartcollection);
    }

    private List populateSeriesEntryList(BubbleReportDefinition bubblereportdefinition)
    {
        ArrayList arraylist = new ArrayList();
        for(int i = 0; i < bubblereportdefinition.size(); i++)
        {
            BubbleSeriesValue bubbleseriesvalue = (BubbleSeriesValue)bubblereportdefinition.get(i);
            Object aobj[] = new Object[4];
            aobj[0] = bubbleseriesvalue.getBubbleSereisName();
            aobj[1] = bubbleseriesvalue.getBubbleSeriesX();
            aobj[2] = bubbleseriesvalue.getBubbleSeriesY();
            aobj[3] = bubbleseriesvalue.getBubbleSeriesSize();
            arraylist.add(((Object) (aobj)));
        }

        return arraylist;
    }

    public void updateBean(ChartCollection chartcollection)
    {
        if(chartcollection != null)
        {
            BubbleReportDefinition bubblereportdefinition = new BubbleReportDefinition();
            chartcollection.getSelectedChart().setFilterDefinition(bubblereportdefinition);
            updateSeriesEntryList(bubblereportdefinition, seriesPane.updateBean());
            filterPane.updateBean(chartcollection);
        }
    }

    private void updateSeriesEntryList(BubbleReportDefinition bubblereportdefinition, List list)
    {
        for(int i = 0; i < list.size(); i++)
        {
            Object aobj[] = (Object[])list.get(i);
            BubbleSeriesValue bubbleseriesvalue = new BubbleSeriesValue();
            bubbleseriesvalue.setBubbleSeriesName(canBeFormula(aobj[0]));
            bubbleseriesvalue.setBubbleSeriesX(canBeFormula(aobj[1]));
            bubbleseriesvalue.setBubbleSeriesY(canBeFormula(aobj[2]));
            bubbleseriesvalue.setBubbleSeriesSize(canBeFormula(aobj[3]));
            bubblereportdefinition.add(bubbleseriesvalue);
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
