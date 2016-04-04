// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.chart.gui.data.table;

import com.fr.base.chart.chartdata.TopDefinitionProvider;
import com.fr.chart.base.ChartConstants;
import com.fr.chart.chartattr.Chart;
import com.fr.chart.chartattr.ChartCollection;
import com.fr.chart.chartattr.XYScatterPlot;
import com.fr.chart.chartdata.ScatterTableDefinition;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.ilable.BoldFontTextLabel;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.chart.gui.ChartDataPane;
import com.fr.design.mainframe.chart.gui.data.ChartDataFilterPane;
import com.fr.general.ComparatorUtils;
import com.fr.general.Inter;
import com.fr.stable.ArrayUtils;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import javax.swing.JSeparator;

// Referenced classes of package com.fr.design.mainframe.chart.gui.data.table:
//            AbstractTableDataContentPane

public class XYScatterPlotTableDataContentPane extends AbstractTableDataContentPane
{

    private UIComboBox seriesName;
    private UIComboBox xCombox;
    private UIComboBox yCombox;
    private ChartDataFilterPane dataScreeningPane;

    public XYScatterPlotTableDataContentPane(ChartDataPane chartdatapane)
    {
        seriesName = new UIComboBox();
        xCombox = new UIComboBox();
        yCombox = new UIComboBox();
        dataScreeningPane = new ChartDataFilterPane(new XYScatterPlot(), chartdatapane);
        seriesName.addItem(Inter.getLocText("Chart-Use_None"));
        seriesName.setPreferredSize(new Dimension(100, 20));
        xCombox.setPreferredSize(new Dimension(100, 20));
        yCombox.setPreferredSize(new Dimension(100, 20));
        double d = -2D;
        double d1 = -1D;
        double ad[] = {
            d, d1
        };
        double ad1[] = {
            d, d, d, d, d, d
        };
        Component acomponent[][] = {
            {
                new BoldFontTextLabel((new StringBuilder()).append(" ").append(Inter.getLocText("Chart-Series_Name")).append(":").toString()), seriesName
            }, {
                new BoldFontTextLabel((new StringBuilder()).append("  ").append(Inter.getLocText("Chart-Scatter_Name")).append("x").append(":").toString()), xCombox
            }, {
                new BoldFontTextLabel((new StringBuilder()).append("  ").append(Inter.getLocText("Chart-Scatter_Name")).append("y").append(":").toString()), yCombox
            }, {
                new JSeparator(), null
            }, {
                new BoldFontTextLabel(Inter.getLocText("Chart-Data_Filter"))
            }, {
                dataScreeningPane, null
            }
        };
        javax.swing.JPanel jpanel = TableLayoutHelper.createTableLayoutPane(acomponent, ad1, ad);
        setLayout(new BorderLayout());
        add(jpanel, "Center");
        seriesName.addItemListener(tooltipListener);
        xCombox.addItemListener(tooltipListener);
        yCombox.addItemListener(tooltipListener);
    }

    public void checkBoxUse(boolean flag)
    {
        seriesName.setEnabled(flag);
        xCombox.setEnabled(flag);
        yCombox.setEnabled(flag);
    }

    protected void refreshBoxListWithSelectTableData(java.util.List list)
    {
        refreshBoxItems(seriesName, list);
        seriesName.addItem(Inter.getLocText("Chart-Use_None"));
        refreshBoxItems(xCombox, list);
        refreshBoxItems(yCombox, list);
    }

    public void clearAllBoxList()
    {
        clearBoxItems(seriesName);
        seriesName.addItem(Inter.getLocText("Chart-Use_None"));
        clearBoxItems(xCombox);
        clearBoxItems(yCombox);
    }

    public void updateBean(ChartCollection chartcollection)
    {
        ScatterTableDefinition scattertabledefinition = new ScatterTableDefinition();
        chartcollection.getSelectedChart().setFilterDefinition(scattertabledefinition);
        Object obj = seriesName.getSelectedItem();
        Object obj1 = xCombox.getSelectedItem();
        Object obj2 = yCombox.getSelectedItem();
        if(obj == null || ArrayUtils.contains(ChartConstants.NONE_KEYS, obj))
            scattertabledefinition.setSeriesName("");
        else
            scattertabledefinition.setSeriesName(obj.toString());
        if(obj1 != null)
            scattertabledefinition.setScatterX(obj1.toString());
        if(obj2 != null)
            scattertabledefinition.setScatterY(obj2.toString());
        dataScreeningPane.updateBean(chartcollection);
    }

    public void populateBean(ChartCollection chartcollection)
    {
        TopDefinitionProvider topdefinitionprovider = chartcollection.getSelectedChart().getFilterDefinition();
        if(topdefinitionprovider instanceof ScatterTableDefinition)
        {
            ScatterTableDefinition scattertabledefinition = (ScatterTableDefinition)topdefinitionprovider;
            if(scattertabledefinition.getSeriesName() == null || ComparatorUtils.equals("", scattertabledefinition.getSeriesName()))
                seriesName.setSelectedItem(Inter.getLocText("Chart-Use_None"));
            else
                combineCustomEditValue(seriesName, scattertabledefinition.getSeriesName());
            combineCustomEditValue(xCombox, scattertabledefinition.getScatterX());
            combineCustomEditValue(yCombox, scattertabledefinition.getScatterY());
        }
        dataScreeningPane.populateBean(chartcollection);
    }

    public void redoLayoutPane()
    {
        dataScreeningPane.relayoutPane(isNeedSummaryCaculateMethod());
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
