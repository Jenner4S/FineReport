// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.chart.gui.data.table;

import com.fr.chart.base.ChartConstants;
import com.fr.chart.chartattr.Bar2DPlot;
import com.fr.chart.chartattr.Chart;
import com.fr.chart.chartattr.ChartCollection;
import com.fr.chart.chartdata.NormalTableDataDefinition;
import com.fr.chart.chartdata.TopDefinition;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.ilable.BoldFontTextLabel;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.mainframe.chart.gui.ChartDataPane;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.general.ComparatorUtils;
import com.fr.general.Inter;
import com.fr.stable.ArrayUtils;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.BorderFactory;
import javax.swing.ComboBoxModel;
import javax.swing.JPanel;

// Referenced classes of package com.fr.design.mainframe.chart.gui.data.table:
//            AbstractTableDataContentPane, SeriesTypeUseComboxPane

public class CategoryPlotTableDataContentPane extends AbstractTableDataContentPane
{

    private static final long serialVersionUID = 0x65163ee2756ec129L;
    protected UIComboBox categoryCombox;
    protected SeriesTypeUseComboxPane seriesTypeComboxPane;

    public CategoryPlotTableDataContentPane()
    {
    }

    public CategoryPlotTableDataContentPane(ChartDataPane chartdatapane)
    {
        categoryCombox = new UIComboBox();
        JPanel jpanel = new JPanel(new BorderLayout(4, 0));
        jpanel.setBorder(BorderFactory.createMatteBorder(0, 0, 6, 1, getBackground()));
        BoldFontTextLabel boldfonttextlabel = new BoldFontTextLabel((new StringBuilder()).append(Inter.getLocText("FR-Chart-Category_Name")).append(":").toString(), 4);
        boldfonttextlabel.setPreferredSize(new Dimension(75, 20));
        categoryCombox.setPreferredSize(new Dimension(100, 20));
        categoryCombox.addItem(Inter.getLocText("Chart-Use_None"));
        jpanel.add(GUICoreUtils.createBorderLayoutPane(new Component[] {
            categoryCombox, null, null, boldfonttextlabel, null
        }));
        setLayout(new BorderLayout());
        add(jpanel, "North");
        seriesTypeComboxPane = new SeriesTypeUseComboxPane(chartdatapane, new Bar2DPlot());
        add(seriesTypeComboxPane, "South");
        categoryCombox.addItemListener(new ItemListener() {

            final CategoryPlotTableDataContentPane this$0;

            public void itemStateChanged(ItemEvent itemevent)
            {
                checkSeriseUse(categoryCombox.getSelectedItem() != null);
                makeToolTipUse(categoryCombox);
            }

            
            {
                this$0 = CategoryPlotTableDataContentPane.this;
                super();
            }
        }
);
    }

    protected void makeToolTipUse(UIComboBox uicombobox)
    {
        if(uicombobox.getSelectedItem() != null)
            uicombobox.setToolTipText(uicombobox.getSelectedItem().toString());
        else
            uicombobox.setToolTipText(null);
    }

    public void checkBoxUse(boolean flag)
    {
        categoryCombox.setEnabled(flag);
        checkSeriseUse(flag);
    }

    protected void checkSeriseUse(boolean flag)
    {
        if(seriesTypeComboxPane != null)
            seriesTypeComboxPane.checkUseBox(flag && categoryCombox.getSelectedItem() != null);
    }

    protected void refreshBoxListWithSelectTableData(java.util.List list)
    {
        refreshBoxItems(categoryCombox, list);
        categoryCombox.addItem(Inter.getLocText("Chart-Use_None"));
        seriesTypeComboxPane.refreshBoxListWithSelectTableData(list);
    }

    public void clearAllBoxList()
    {
        clearBoxItems(categoryCombox);
        categoryCombox.addItem(Inter.getLocText("Chart-Use_None"));
        seriesTypeComboxPane.clearAllBoxList();
    }

    public void updateBean(ChartCollection chartcollection)
    {
        seriesTypeComboxPane.updateBean(chartcollection);
        NormalTableDataDefinition normaltabledatadefinition = (NormalTableDataDefinition)chartcollection.getSelectedChart().getFilterDefinition();
        if(normaltabledatadefinition == null)
            return;
        Object obj = categoryCombox.getSelectedItem();
        if(ArrayUtils.contains(ChartConstants.NONE_KEYS, obj))
            normaltabledatadefinition.setCategoryName("");
        else
            normaltabledatadefinition.setCategoryName(obj != null ? obj.toString() : null);
    }

    public void populateBean(ChartCollection chartcollection)
    {
        super.populateBean(chartcollection);
        TopDefinition topdefinition = (TopDefinition)chartcollection.getSelectedChart().getFilterDefinition();
        if(!(topdefinition instanceof NormalTableDataDefinition))
            return;
        NormalTableDataDefinition normaltabledatadefinition = (NormalTableDataDefinition)topdefinition;
        if(normaltabledatadefinition == null || ComparatorUtils.equals(normaltabledatadefinition.getCategoryName(), ""))
            categoryCombox.setSelectedItem(Inter.getLocText("Chart-Use_None"));
        else
        if(normaltabledatadefinition != null && !boxItemsContainsObject(categoryCombox, normaltabledatadefinition.getCategoryName()))
            categoryCombox.setSelectedItem(null);
        else
            combineCustomEditValue(categoryCombox, normaltabledatadefinition != null ? normaltabledatadefinition.getCategoryName() : null);
        seriesTypeComboxPane.populateBean(chartcollection, isNeedSummaryCaculateMethod());
    }

    private boolean boxItemsContainsObject(UIComboBox uicombobox, Object obj)
    {
        if(uicombobox == null)
            return false;
        ComboBoxModel comboboxmodel = uicombobox.getModel();
        for(int i = 0; i < comboboxmodel.getSize(); i++)
            if(ComparatorUtils.equals(comboboxmodel.getElementAt(i), obj))
                return true;

        return false;
    }

    public void redoLayoutPane()
    {
        seriesTypeComboxPane.relayoutPane(isNeedSummaryCaculateMethod());
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
