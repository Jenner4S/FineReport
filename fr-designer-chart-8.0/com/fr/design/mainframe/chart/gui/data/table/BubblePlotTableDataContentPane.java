// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.chart.gui.data.table;

import com.fr.base.chart.chartdata.TopDefinitionProvider;
import com.fr.chart.base.ChartConstants;
import com.fr.chart.chartattr.BubblePlot;
import com.fr.chart.chartattr.Chart;
import com.fr.chart.chartattr.ChartCollection;
import com.fr.chart.chartdata.BubbleTableDefinition;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.ilable.BoldFontTextLabel;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.chart.gui.ChartDataPane;
import com.fr.design.mainframe.chart.gui.data.ChartDataFilterPane;
import com.fr.general.ComparatorUtils;
import com.fr.general.Inter;
import com.fr.stable.ArrayUtils;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JSeparator;

// Referenced classes of package com.fr.design.mainframe.chart.gui.data.table:
//            AbstractTableDataContentPane

public class BubblePlotTableDataContentPane extends AbstractTableDataContentPane
{

    private UIComboBox seriesName;
    private UIComboBox xCombox;
    private UIComboBox yCombox;
    private UIComboBox bubbleSize;
    private ChartDataFilterPane dataScreeningPane;

    public BubblePlotTableDataContentPane(ChartDataPane chartdatapane)
    {
        seriesName = new UIComboBox();
        xCombox = new UIComboBox();
        yCombox = new UIComboBox();
        bubbleSize = new UIComboBox();
        dataScreeningPane = new ChartDataFilterPane(new BubblePlot(), chartdatapane);
        seriesName.setPreferredSize(new Dimension(100, 20));
        xCombox.setPreferredSize(new Dimension(100, 20));
        yCombox.setPreferredSize(new Dimension(100, 20));
        bubbleSize.setPreferredSize(new Dimension(100, 20));
        seriesName.addItem(Inter.getLocText("Chart-Use_None"));
        double d = -2D;
        double d1 = -1D;
        double ad[] = {
            d1
        };
        double ad1[] = {
            d, d, d, d
        };
        double ad2[] = {
            d, d1
        };
        double ad3[] = {
            d, d, d, d
        };
        Component acomponent[][] = {
            {
                new UILabel((new StringBuilder()).append("   ").append(Inter.getLocText("Chart-Series_Name")).append(":").toString()), seriesName
            }, {
                new UILabel((new StringBuilder()).append("  ").append(Inter.getLocText("Chart-Bubble_Value")).append("x :").toString()), xCombox
            }, {
                new UILabel((new StringBuilder()).append("  ").append(Inter.getLocText("Chart-Bubble_Value")).append("y :").toString()), yCombox
            }, {
                new UILabel((new StringBuilder()).append(" ").append(Inter.getLocText("Chart-Bubble_Size")).append(":").toString()), bubbleSize
            }
        };
        JPanel jpanel = TableLayoutHelper.createTableLayoutPane(acomponent, ad3, ad2);
        jpanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 1));
        Component acomponent1[][] = {
            {
                jpanel
            }, {
                new JSeparator()
            }, {
                new BoldFontTextLabel(Inter.getLocText("Chart-Data_Filter"))
            }, {
                dataScreeningPane
            }
        };
        JPanel jpanel1 = TableLayoutHelper.createTableLayoutPane(acomponent1, ad1, ad);
        setLayout(new BorderLayout());
        add(jpanel1, "Center");
        seriesName.addItemListener(tooltipListener);
        xCombox.addItemListener(tooltipListener);
        yCombox.addItemListener(tooltipListener);
        bubbleSize.addItemListener(tooltipListener);
    }

    public void checkBoxUse(boolean flag)
    {
    }

    protected void refreshBoxListWithSelectTableData(java.util.List list)
    {
        refreshBoxItems(seriesName, list);
        seriesName.addItem(Inter.getLocText("Chart-Use_None"));
        refreshBoxItems(xCombox, list);
        refreshBoxItems(yCombox, list);
        refreshBoxItems(bubbleSize, list);
    }

    public void clearAllBoxList()
    {
        clearBoxItems(seriesName);
        seriesName.addItem(Inter.getLocText("Chart-Use_None"));
        clearBoxItems(xCombox);
        clearBoxItems(yCombox);
        clearBoxItems(bubbleSize);
    }

    public void populateBean(ChartCollection chartcollection)
    {
        super.populateBean(chartcollection);
        TopDefinitionProvider topdefinitionprovider = chartcollection.getSelectedChart().getFilterDefinition();
        if(!(topdefinitionprovider instanceof BubbleTableDefinition))
            return;
        BubbleTableDefinition bubbletabledefinition = (BubbleTableDefinition)topdefinitionprovider;
        if(bubbletabledefinition.getSeriesName() == null || ComparatorUtils.equals("", bubbletabledefinition.getSeriesName()))
            seriesName.setSelectedItem(Inter.getLocText("Chart-Use_None"));
        else
            combineCustomEditValue(seriesName, bubbletabledefinition.getSeriesName());
        combineCustomEditValue(xCombox, bubbletabledefinition.getBubbleX());
        combineCustomEditValue(yCombox, bubbletabledefinition.getBubbleY());
        combineCustomEditValue(bubbleSize, bubbletabledefinition.getBubbleSize());
        dataScreeningPane.populateBean(chartcollection);
    }

    public void updateBean(ChartCollection chartcollection)
    {
        BubbleTableDefinition bubbletabledefinition = new BubbleTableDefinition();
        chartcollection.getSelectedChart().setFilterDefinition(bubbletabledefinition);
        Object obj = seriesName.getSelectedItem();
        Object obj1 = xCombox.getSelectedItem();
        Object obj2 = yCombox.getSelectedItem();
        Object obj3 = bubbleSize.getSelectedItem();
        if(obj == null || ArrayUtils.contains(ChartConstants.NONE_KEYS, obj))
            bubbletabledefinition.setSeriesName("");
        else
            bubbletabledefinition.setSeriesName(obj.toString());
        if(obj1 != null)
            bubbletabledefinition.setBubbleX(obj1.toString());
        if(obj2 != null)
            bubbletabledefinition.setBubbleY(obj2.toString());
        if(obj3 != null)
            bubbletabledefinition.setBubbleSize(obj3.toString());
        dataScreeningPane.updateBean(chartcollection);
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
