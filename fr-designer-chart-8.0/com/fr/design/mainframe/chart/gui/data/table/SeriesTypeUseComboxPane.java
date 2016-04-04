// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.chart.gui.data.table;

import com.fr.base.chart.chartdata.TopDefinitionProvider;
import com.fr.chart.chartattr.Chart;
import com.fr.chart.chartattr.ChartCollection;
import com.fr.chart.chartattr.Plot;
import com.fr.chart.chartdata.MoreNameCDDefinition;
import com.fr.chart.chartdata.OneValueCDDefinition;
import com.fr.design.gui.frpane.UIComboBoxPane;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.mainframe.chart.gui.ChartDataPane;
import com.fr.design.mainframe.chart.gui.data.ChartDataFilterPane;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.general.Inter;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

// Referenced classes of package com.fr.design.mainframe.chart.gui.data.table:
//            SeriesNameUseFieldValuePane, SeriesNameUseFieldNamePane

public class SeriesTypeUseComboxPane extends UIComboBoxPane
{

    private SeriesNameUseFieldValuePane nameFieldValuePane;
    private SeriesNameUseFieldNamePane nameFieldNamePane;
    private ChartDataFilterPane dataScreeningPane;
    private ChartDataPane parent;
    private Plot initplot;
    private boolean isNeedSummary;

    public SeriesTypeUseComboxPane(ChartDataPane chartdatapane, Plot plot)
    {
        isNeedSummary = true;
        initplot = plot;
        parent = chartdatapane;
        cards = initPaneList();
        isNeedSummary = true;
        initComponents();
    }

    protected void initLayout()
    {
        setLayout(new BorderLayout(4, 6));
        JPanel jpanel = new JPanel(new BorderLayout(4, 0));
        UILabel uilabel = new UILabel((new StringBuilder()).append(Inter.getLocText("ChartF-Series_Name_From")).append(":").toString(), 4);
        uilabel.setPreferredSize(new Dimension(75, 20));
        jpanel.add(GUICoreUtils.createBorderLayoutPane(new Component[] {
            jcb, null, null, uilabel, null
        }));
        jpanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 1));
        add(jpanel, "North");
        add(cardPane, "Center");
        add(dataScreeningPane = new ChartDataFilterPane(initplot, parent), "South");
    }

    public void checkUseBox(boolean flag)
    {
        jcb.setEnabled(flag);
        nameFieldValuePane.checkUse(flag);
        dataScreeningPane.checkBoxUse();
    }

    public void refreshBoxListWithSelectTableData(java.util.List list)
    {
        nameFieldValuePane.refreshBoxListWithSelectTableData(list);
        nameFieldNamePane.refreshBoxListWithSelectTableData(list);
    }

    public void clearAllBoxList()
    {
        nameFieldValuePane.clearAllBoxList();
        nameFieldNamePane.clearAllBoxList();
    }

    protected String title4PopupWindow()
    {
        return Inter.getLocText("ChartF-Series_Name_From");
    }

    protected java.util.List initPaneList()
    {
        nameFieldValuePane = new SeriesNameUseFieldValuePane();
        nameFieldNamePane = new SeriesNameUseFieldNamePane();
        ArrayList arraylist = new ArrayList();
        arraylist.add(nameFieldValuePane);
        arraylist.add(nameFieldNamePane);
        return arraylist;
    }

    public void populateBean(ChartCollection chartcollection, boolean flag)
    {
        isNeedSummary = flag;
        TopDefinitionProvider topdefinitionprovider = chartcollection.getSelectedChart().getFilterDefinition();
        if(topdefinitionprovider instanceof OneValueCDDefinition)
        {
            setSelectedIndex(0);
            nameFieldValuePane.populateBean(chartcollection, flag);
        } else
        if(topdefinitionprovider instanceof MoreNameCDDefinition)
        {
            setSelectedIndex(1);
            nameFieldNamePane.populateBean(chartcollection, flag);
        }
        dataScreeningPane.populateBean(chartcollection, flag);
    }

    public void relayoutPane(boolean flag)
    {
        isNeedSummary = flag;
        if(jcb.getSelectedIndex() == 0)
            nameFieldValuePane.relayoutPane(isNeedSummary);
        else
            nameFieldNamePane.relayoutPane(isNeedSummary);
        dataScreeningPane.relayoutPane(isNeedSummary);
    }

    protected void comboBoxItemStateChanged()
    {
        if(jcb.getSelectedIndex() == 0)
            nameFieldValuePane.relayoutPane(isNeedSummary);
        else
            nameFieldNamePane.relayoutPane(isNeedSummary);
    }

    public void populateBean(ChartCollection chartcollection)
    {
        populateBean(chartcollection, true);
    }

    public void updateBean(ChartCollection chartcollection)
    {
        if(getSelectedIndex() == 0)
            nameFieldValuePane.updateBean(chartcollection);
        else
            nameFieldNamePane.updateBean(chartcollection);
        dataScreeningPane.updateBean(chartcollection);
    }

    public volatile void populateBean(Object obj)
    {
        populateBean((ChartCollection)obj);
    }

    public volatile void updateBean(Object obj)
    {
        updateBean((ChartCollection)obj);
    }
}
