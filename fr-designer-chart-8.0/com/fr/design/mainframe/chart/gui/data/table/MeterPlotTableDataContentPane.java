// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.chart.gui.data.table;

import com.fr.base.Utils;
import com.fr.chart.chartattr.Chart;
import com.fr.chart.chartattr.ChartCollection;
import com.fr.chart.chartattr.MeterPlot;
import com.fr.chart.chartdata.MeterTableDefinition;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.ilable.BoldFontTextLabel;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.chart.gui.ChartDataPane;
import com.fr.design.mainframe.chart.gui.data.ChartDataFilterPane;
import com.fr.general.Inter;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import javax.swing.JSeparator;

// Referenced classes of package com.fr.design.mainframe.chart.gui.data.table:
//            AbstractTableDataContentPane

public class MeterPlotTableDataContentPane extends AbstractTableDataContentPane
{

    private static final String METER_NAME = (new StringBuilder()).append(Inter.getLocText("Chart-Category_Use_Name")).append(":").toString();
    private static final String METER_VALUE = (new StringBuilder()).append(Inter.getLocText("Chart-Pointer_Value")).append(":").toString();
    private UIComboBox nameBox;
    private UIComboBox valueBox;
    private ChartDataFilterPane filterPane;

    public MeterPlotTableDataContentPane(ChartDataPane chartdatapane)
    {
        setLayout(new BorderLayout());
        nameBox = new UIComboBox();
        valueBox = new UIComboBox();
        filterPane = new ChartDataFilterPane(new MeterPlot(), chartdatapane);
        nameBox.setPreferredSize(new Dimension(100, 20));
        valueBox.setPreferredSize(new Dimension(100, 20));
        double d = -2D;
        double d1 = -1D;
        double ad[] = {
            d, d1
        };
        double ad1[] = {
            d, d, d, d, d, d, d, d, d
        };
        Component acomponent[][] = {
            {
                new BoldFontTextLabel(METER_NAME, 4), nameBox
            }, {
                new BoldFontTextLabel(METER_VALUE, 4), valueBox
            }, {
                new JSeparator(), null
            }, {
                new BoldFontTextLabel(Inter.getLocText("Chart-Data_Filter"))
            }, {
                filterPane, null
            }
        };
        javax.swing.JPanel jpanel = TableLayoutHelper.createTableLayoutPane(acomponent, ad1, ad);
        add(jpanel, "Center");
        nameBox.addItemListener(tooltipListener);
        valueBox.addItemListener(tooltipListener);
    }

    protected void refreshBoxListWithSelectTableData(java.util.List list)
    {
        refreshBoxItems(nameBox, list);
        refreshBoxItems(valueBox, list);
    }

    public void clearAllBoxList()
    {
        clearBoxItems(nameBox);
        clearBoxItems(valueBox);
    }

    public void populateBean(ChartCollection chartcollection)
    {
        if(chartcollection != null && (chartcollection.getSelectedChart().getFilterDefinition() instanceof MeterTableDefinition))
        {
            MeterTableDefinition metertabledefinition = (MeterTableDefinition)chartcollection.getSelectedChart().getFilterDefinition();
            nameBox.setSelectedItem(metertabledefinition.getName());
            valueBox.setSelectedItem(metertabledefinition.getValue());
            filterPane.populateBean(chartcollection);
        }
    }

    public void updateBean(ChartCollection chartcollection)
    {
        if(chartcollection != null)
        {
            MeterTableDefinition metertabledefinition = new MeterTableDefinition();
            chartcollection.getSelectedChart().setFilterDefinition(metertabledefinition);
            metertabledefinition.setName(Utils.objectToString(nameBox.getSelectedItem()));
            metertabledefinition.setValue(Utils.objectToString(valueBox.getSelectedItem()));
            filterPane.updateBean(chartcollection);
        }
    }

    public void redoLayoutPane()
    {
        filterPane.relayoutPane(isNeedSummaryCaculateMethod());
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
