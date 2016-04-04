// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.chart.gui.data.table;

import com.fr.base.chart.chartdata.TopDefinitionProvider;
import com.fr.chart.chartattr.Chart;
import com.fr.chart.chartattr.ChartCollection;
import com.fr.chart.chartdata.OneValueCDDefinition;
import com.fr.data.util.function.AbstractDataFunction;
import com.fr.data.util.function.NoneFunction;
import com.fr.design.beans.FurtherBasicBeanPane;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.ilable.BoldFontTextLabel;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.chart.gui.data.CalculateComboBox;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.general.ComparatorUtils;
import com.fr.general.Inter;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.BorderFactory;
import javax.swing.ComboBoxModel;
import javax.swing.JPanel;
import javax.swing.JSeparator;

public class SeriesNameUseFieldValuePane extends FurtherBasicBeanPane
{

    private UIComboBox seriesName;
    private UIComboBox seriesValue;
    private CalculateComboBox calculateCombox;
    private JPanel centerPane;
    private boolean isNeedSummary;

    public SeriesNameUseFieldValuePane()
    {
        isNeedSummary = true;
        seriesName = new UIComboBox();
        seriesValue = new UIComboBox();
        calculateCombox = new CalculateComboBox();
        calculateCombox.reset();
        isNeedSummary = true;
        seriesName.setPreferredSize(new Dimension(100, 75));
        seriesValue.setPreferredSize(new Dimension(100, 75));
        calculateCombox.setPreferredSize(new Dimension(100, 75));
        seriesName.addItemListener(new ItemListener() {

            final SeriesNameUseFieldValuePane this$0;

            public void itemStateChanged(ItemEvent itemevent)
            {
                if(seriesName.getSelectedItem() != null)
                    seriesName.setToolTipText(seriesName.getSelectedItem().toString());
                else
                    seriesName.setToolTipText(null);
            }

            
            {
                this$0 = SeriesNameUseFieldValuePane.this;
                super();
            }
        }
);
        seriesValue.addItemListener(new ItemListener() {

            final SeriesNameUseFieldValuePane this$0;

            public void itemStateChanged(ItemEvent itemevent)
            {
                calculateCombox.setEnabled(seriesValue.getSelectedItem() != null);
                if(seriesValue.getSelectedItem() != null)
                    seriesValue.setToolTipText(seriesValue.getSelectedItem().toString());
                else
                    seriesValue.setToolTipText(null);
            }

            
            {
                this$0 = SeriesNameUseFieldValuePane.this;
                super();
            }
        }
);
        initCenterPane();
    }

    private void initCenterPane()
    {
        double d = -2D;
        double d1 = -1D;
        double ad[] = {
            d1
        };
        double ad1[] = {
            d, d, d, d, d
        };
        UILabel uilabel = new UILabel((new StringBuilder()).append(Inter.getLocText("Chart-Series_Name")).append(":").toString(), 4);
        uilabel.setPreferredSize(new Dimension(75, 20));
        UILabel uilabel1 = new UILabel((new StringBuilder()).append(Inter.getLocText("Chart-Series_Value")).append(":").toString(), 4);
        uilabel1.setPreferredSize(new Dimension(75, 20));
        UILabel uilabel2 = new UILabel((new StringBuilder()).append(Inter.getLocText("Chart-Summary_Method")).append(":").toString(), 4);
        uilabel2.setPreferredSize(new Dimension(75, 20));
        Component acomponent[][] = {
            {
                GUICoreUtils.createBorderLayoutPane(new Component[] {
                    seriesName, null, null, uilabel, null
                })
            }, {
                GUICoreUtils.createBorderLayoutPane(new Component[] {
                    seriesValue, null, null, uilabel1, null
                })
            }, {
                GUICoreUtils.createBorderLayoutPane(new Component[] {
                    calculateCombox, null, null, uilabel2, null
                })
            }, {
                new JSeparator()
            }, {
                new BoldFontTextLabel(Inter.getLocText("Chart-Data_Filter"))
            }
        };
        centerPane = TableLayoutHelper.createGapTableLayoutPane(acomponent, ad1, ad, 4D, 6D);
        centerPane.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 1));
        setLayout(new BorderLayout());
        add(centerPane, "Center");
    }

    private void initCenterPaneWithOutCaculateSummary()
    {
        double d = -2D;
        double d1 = -1D;
        double ad[] = {
            d1
        };
        double ad1[] = {
            d, d, d, d
        };
        UILabel uilabel = new UILabel((new StringBuilder()).append(Inter.getLocText("Chart-Series_Name")).append(":").toString(), 4);
        uilabel.setPreferredSize(new Dimension(75, 20));
        UILabel uilabel1 = new UILabel((new StringBuilder()).append(Inter.getLocText("Chart-Series_Value")).append(":").toString(), 4);
        uilabel1.setPreferredSize(new Dimension(75, 20));
        UILabel uilabel2 = new UILabel((new StringBuilder()).append(Inter.getLocText("Chart-Summary_Method")).append(":").toString(), 4);
        uilabel2.setPreferredSize(new Dimension(75, 20));
        Component acomponent[][] = {
            {
                GUICoreUtils.createBorderLayoutPane(new Component[] {
                    seriesName, null, null, uilabel, null
                })
            }, {
                GUICoreUtils.createBorderLayoutPane(new Component[] {
                    seriesValue, null, null, uilabel1, null
                })
            }, {
                new JSeparator()
            }, {
                new BoldFontTextLabel(Inter.getLocText("Chart-Data_Filter"))
            }
        };
        centerPane = TableLayoutHelper.createGapTableLayoutPane(acomponent, ad1, ad, 4D, 6D);
        centerPane.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 1));
        setLayout(new BorderLayout());
        add(centerPane, "Center");
    }

    public void checkUse(boolean flag)
    {
        seriesName.setEnabled(flag);
        seriesValue.setEnabled(flag);
        calculateCombox.setEnabled(seriesValue.getSelectedItem() != null);
    }

    public void refreshBoxListWithSelectTableData(java.util.List list)
    {
        refreshBoxItems(seriesName, list);
        refreshBoxItems(seriesValue, list);
    }

    public void clearAllBoxList()
    {
        clearBoxItems(seriesName);
        clearBoxItems(seriesValue);
    }

    private void clearBoxItems(UIComboBox uicombobox)
    {
        if(uicombobox != null)
            uicombobox.removeAllItems();
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

    private void refreshBoxItems(UIComboBox uicombobox, java.util.List list)
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

    public boolean accept(Object obj)
    {
        ChartCollection chartcollection = (ChartCollection)obj;
        return chartcollection.getSelectedChart().getFilterDefinition() instanceof OneValueCDDefinition;
    }

    public String title4PopupWindow()
    {
        return Inter.getLocText(new String[] {
            "Use", "Field", "Value"
        });
    }

    public void reset()
    {
    }

    public void relayoutPane(boolean flag)
    {
        if(isNeedSummary != flag)
        {
            remove(centerPane);
            if(flag)
                initCenterPane();
            else
                initCenterPaneWithOutCaculateSummary();
            validate();
            isNeedSummary = flag;
        }
    }

    public void populateBean(ChartCollection chartcollection, boolean flag)
    {
        relayoutPane(flag);
        TopDefinitionProvider topdefinitionprovider = chartcollection.getSelectedChart().getFilterDefinition();
        if(topdefinitionprovider instanceof OneValueCDDefinition)
        {
            OneValueCDDefinition onevaluecddefinition = (OneValueCDDefinition)topdefinitionprovider;
            seriesName.setEditable(true);
            seriesName.setSelectedItem(boxItemsContainsObject(seriesName, onevaluecddefinition.getSeriesColumnName()) ? ((Object) (onevaluecddefinition.getSeriesColumnName())) : null);
            seriesName.setEditable(false);
            seriesValue.setEditable(true);
            seriesValue.setSelectedItem(boxItemsContainsObject(seriesValue, onevaluecddefinition.getValueColumnName()) ? ((Object) (onevaluecddefinition.getValueColumnName())) : null);
            seriesValue.setEditable(false);
            if(isNeedSummary)
                calculateCombox.populateBean((AbstractDataFunction)onevaluecddefinition.getDataFunction());
        }
    }

    public void populateBean(ChartCollection chartcollection)
    {
        populateBean(chartcollection, true);
    }

    public void updateBean(ChartCollection chartcollection)
    {
        OneValueCDDefinition onevaluecddefinition = new OneValueCDDefinition();
        String s = (String)seriesName.getSelectedItem();
        onevaluecddefinition.setSeriesColumnName(s);
        String s1 = (String)seriesValue.getSelectedItem();
        onevaluecddefinition.setValueColumnName(s1);
        if(isNeedSummary)
            onevaluecddefinition.setDataFunction(calculateCombox.updateBean());
        else
            onevaluecddefinition.setDataFunction(new NoneFunction());
        chartcollection.getSelectedChart().setFilterDefinition(onevaluecddefinition);
    }

    public ChartCollection updateBean()
    {
        return null;
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
