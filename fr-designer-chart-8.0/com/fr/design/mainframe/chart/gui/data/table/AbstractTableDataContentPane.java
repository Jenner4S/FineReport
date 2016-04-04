// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.chart.gui.data.table;

import com.fr.chart.chartattr.ChartCollection;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.data.tabledata.wrapper.TableDataWrapper;
import com.fr.design.gui.icombobox.UIComboBox;
import java.util.List;
import javax.swing.ComboBoxModel;

public abstract class AbstractTableDataContentPane extends BasicBeanPane
{

    private boolean isNeedSummaryCaculateMethod;
    protected java.awt.event.ItemListener tooltipListener;

    public AbstractTableDataContentPane()
    {
        isNeedSummaryCaculateMethod = true;
        tooltipListener = new java.awt.event.ItemListener() {

            final AbstractTableDataContentPane this$0;

            public void itemStateChanged(java.awt.event.ItemEvent itemevent)
            {
                if(itemevent.getSource() instanceof UIComboBox)
                {
                    UIComboBox uicombobox = (UIComboBox)itemevent.getSource();
                    if(uicombobox.getSelectedItem() != null)
                        uicombobox.setToolTipText(uicombobox.getSelectedItem().toString());
                    else
                        uicombobox.setToolTipText(null);
                }
            }

            
            {
                this$0 = AbstractTableDataContentPane.this;
                super();
            }
        }
;
    }

    public abstract void updateBean(ChartCollection chartcollection);

    public void checkBoxUse(boolean flag)
    {
    }

    public void onSelectTableData(TableDataWrapper tabledatawrapper)
    {
        List list = tabledatawrapper.calculateColumnNameList();
        refreshBoxListWithSelectTableData(list);
    }

    public abstract void clearAllBoxList();

    protected abstract void refreshBoxListWithSelectTableData(List list);

    protected void refreshBoxItems(UIComboBox uicombobox, List list)
    {
        if(uicombobox == null)
            return;
        Object obj = uicombobox.getSelectedItem();
        uicombobox.removeAllItems();
        int i = list.size();
        for(int j = 0; j < i; j++)
            uicombobox.addItem(list.get(j));

        uicombobox.getModel().setSelectedItem(obj);
    }

    protected void clearBoxItems(UIComboBox uicombobox)
    {
        if(uicombobox == null)
        {
            return;
        } else
        {
            uicombobox.removeAllItems();
            return;
        }
    }

    protected void combineCustomEditValue(UIComboBox uicombobox, String s)
    {
        if(uicombobox != null)
        {
            uicombobox.setEditable(true);
            uicombobox.setSelectedItem(s);
            uicombobox.setEditable(false);
        }
    }

    protected String title4PopupWindow()
    {
        return "";
    }

    public void populateBean(ChartCollection chartcollection)
    {
        if(chartcollection == null)
            return;
        else
            return;
    }

    public void redoLayoutPane()
    {
    }

    public ChartCollection updateBean()
    {
        return null;
    }

    public void setNeedSummaryCaculateMethod(boolean flag)
    {
        isNeedSummaryCaculateMethod = flag;
    }

    public boolean isNeedSummaryCaculateMethod()
    {
        return isNeedSummaryCaculateMethod;
    }

    public volatile void updateBean(Object obj)
    {
        updateBean((ChartCollection)obj);
    }

    public volatile Object updateBean()
    {
        return updateBean();
    }

    public volatile void populateBean(Object obj)
    {
        populateBean((ChartCollection)obj);
    }
}
