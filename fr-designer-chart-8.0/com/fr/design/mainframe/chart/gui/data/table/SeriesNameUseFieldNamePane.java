// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.chart.gui.data.table;

import com.fr.base.Utils;
import com.fr.base.chart.chartdata.TopDefinitionProvider;
import com.fr.chart.chartattr.Chart;
import com.fr.chart.chartattr.ChartCollection;
import com.fr.chart.chartdata.ChartSummaryColumn;
import com.fr.chart.chartdata.MoreNameCDDefinition;
import com.fr.data.util.function.DataFunction;
import com.fr.data.util.function.NoneFunction;
import com.fr.design.beans.FurtherBasicBeanPane;
import com.fr.design.event.UIObserver;
import com.fr.design.event.UIObserverListener;
import com.fr.design.gui.frpane.UICorrelationPane;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.ilable.BoldFontTextLabel;
import com.fr.design.gui.itable.UITable;
import com.fr.design.gui.itable.UITableEditor;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.chart.gui.data.CalculateComboBox;
import com.fr.general.ComparatorUtils;
import com.fr.general.FRLogger;
import com.fr.general.Inter;
import com.fr.stable.StringUtils;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import javax.swing.ComboBoxModel;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.event.ChangeEvent;

public class SeriesNameUseFieldNamePane extends FurtherBasicBeanPane
{
    private class InnerTableEditor extends UITableEditor
    {

        private JComponent editorComponent;
        final SeriesNameUseFieldNamePane this$0;

        public Object getCellEditorValue()
        {
            if(editorComponent instanceof UIComboBox)
                return ((UIComboBox)editorComponent).getSelectedItem();
            if(editorComponent instanceof UITextField)
                return ((UITextField)editorComponent).getText();
            if(editorComponent instanceof CalculateComboBox)
                return ((CalculateComboBox)editorComponent).getSelectedItem();
            else
                return super.getCellEditorValue();
        }

        public Component getTableCellEditorComponent(JTable jtable, Object obj, boolean flag, int i, int j)
        {
            switch(j)
            {
            case 0: // '\0'
                return createComboxEdit(i, obj);

            case 1: // '\001'
                return createTextEdit(obj);
            }
            CalculateComboBox calculatecombobox = new CalculateComboBox();
            if(obj != null)
                calculatecombobox.setSelectedItem(obj);
            calculatecombobox.addItemListener(new ItemListener() {

                final InnerTableEditor this$1;

                public void itemStateChanged(ItemEvent itemevent)
                {
                    fireStop();
                }

                
                {
                    this$1 = InnerTableEditor.this;
                    super();
                }
            }
);
            editorComponent = calculatecombobox;
            return calculatecombobox;
        }

        private UIComboBox createComboxEdit(final int row, Object obj)
        {
            UIComboBox uicombobox = new UIComboBox(field.toArray());
            uicombobox.addItemListener(new ItemListener() {

                final int val$row;
                final InnerTableEditor this$1;

                public void itemStateChanged(ItemEvent itemevent)
                {
                    fireStop();
                    checkRow(row);
                    seriesDataPane.fireTargetChanged();
                }

                
                {
                    this$1 = InnerTableEditor.this;
                    row = i;
                    super();
                }
            }
);
            editorComponent = uicombobox;
            if(obj != null && StringUtils.isNotEmpty(obj.toString()))
                uicombobox.getModel().setSelectedItem(obj);
            else
                uicombobox.getModel().setSelectedItem(obj);
            return uicombobox;
        }

        private UITextField createTextEdit(Object obj)
        {
            UITextField uitextfield = new UITextField();
            editorComponent = uitextfield;
            if(obj != null)
                uitextfield.setText(obj.toString());
            uitextfield.registerChangeListener(new UIObserverListener() {

                final InnerTableEditor this$1;

                public void doChange()
                {
                    seriesDataPane.fireTargetChanged();
                }

                
                {
                    this$1 = InnerTableEditor.this;
                    super();
                }
            }
);
            return uitextfield;
        }

        private InnerTableEditor()
        {
            this$0 = SeriesNameUseFieldNamePane.this;
            super();
        }

    }


    private static final String HEADS[] = {
        Inter.getLocText("FR-Chart-Field_Name"), Inter.getLocText("FR-Chart-Series_Name"), Inter.getLocText("FR-Chart-Data_Summary")
    };
    private static final String HEADS_NO_SUMMARY[] = {
        Inter.getLocText("FR-Chart-Field_Name"), Inter.getLocText("FR-Chart-Series_Name")
    };
    private UICorrelationPane seriesDataPane;
    private java.util.List field;
    private JPanel centerPane;
    private boolean isNeedSummary;
    private UIObserverListener observerListener;

    public SeriesNameUseFieldNamePane()
    {
        field = new ArrayList();
        isNeedSummary = true;
        isNeedSummary = true;
        initCenterPane(HEADS);
    }

    private void initCenterPane(final String final_as[])
    {
        seriesDataPane = new UICorrelationPane(final_as) {

            final String val$heads[];
            final SeriesNameUseFieldNamePane this$0;

            protected ActionListener getAddButtonListener()
            {
                return new ActionListener() {

                    final _cls1 this$1;

                    public void actionPerformed(ActionEvent actionevent)
                    {
                        String as[] = heads.length != 3 ? (new String[] {
                            "", ""
                        }) : (new String[] {
                            "", "", Inter.getLocText("FR-Chart-Data_None")
                        });
                        _cls1.this.this$1.addLine(as);
                        fireTargetChanged();
                    }

                    
                    {
                        this$1 = _cls1.this;
                        super();
                    }
                }
;
            }

            public UITableEditor createUITableEditor()
            {
                return new InnerTableEditor();
            }

            public void stopPaneEditing(ChangeEvent changeevent)
            {
                fireTargetChanged();
            }

            public void registerChangeListener(UIObserverListener uiobserverlistener)
            {
                super.registerChangeListener(uiobserverlistener);
                observerListener = uiobserverlistener;
            }


            transient 
            {
                this$0 = SeriesNameUseFieldNamePane.this;
                heads = as1;
                super(final_as);
            }
        }
;
        double d = -2D;
        double d1 = -1D;
        double ad[] = {
            d1
        };
        double ad1[] = {
            d, d
        };
        Component acomponent[][] = {
            {
                seriesDataPane
            }, {
                new BoldFontTextLabel(Inter.getLocText("FR-Chart-Data_Filter"))
            }
        };
        centerPane = TableLayoutHelper.createTableLayoutPane(acomponent, ad1, ad);
        setLayout(new BorderLayout());
        add(centerPane, "Center");
    }

    public boolean accept(Object obj)
    {
        ChartCollection chartcollection = (ChartCollection)obj;
        return chartcollection.getSelectedChart().getFilterDefinition() instanceof MoreNameCDDefinition;
    }

    public String title4PopupWindow()
    {
        return Inter.getLocText(new String[] {
            "FR-Chart-Data_Use", "FR-Chart-Field_Name"
        });
    }

    public void reset()
    {
    }

    public void refreshBoxListWithSelectTableData(java.util.List list)
    {
        field.clear();
        int i = 0;
        for(int j = list.size(); i < j; i++)
        {
            Object obj = list.get(i);
            if(obj != null)
                field.add(obj.toString());
        }

    }

    public void clearAllBoxList()
    {
        field.clear();
    }

    private void fireStop()
    {
        seriesDataPane.stopCellEditing();
        seriesDataPane.fireTargetChanged();
    }

    public void relayoutPane(boolean flag)
    {
        if(isNeedSummary != flag)
        {
            remove(centerPane);
            initCenterPane(flag ? HEADS : HEADS_NO_SUMMARY);
            initListener(this);
            validate();
            isNeedSummary = flag;
        }
    }

    private void initListener(Container container)
    {
        for(int i = 0; i < container.getComponentCount(); i++)
        {
            Component component = container.getComponent(i);
            if(component instanceof Container)
                initListener((Container)component);
            if(component instanceof UIObserver)
                ((UIObserver)component).registerChangeListener(observerListener);
        }

    }

    public void populateBean(ChartCollection chartcollection, boolean flag)
    {
        relayoutPane(flag);
        TopDefinitionProvider topdefinitionprovider = chartcollection.getSelectedChart().getFilterDefinition();
        if(topdefinitionprovider instanceof MoreNameCDDefinition)
        {
            MoreNameCDDefinition morenamecddefinition = (MoreNameCDDefinition)topdefinitionprovider;
            ChartSummaryColumn achartsummarycolumn[] = morenamecddefinition.getChartSummaryColumn();
            if(achartsummarycolumn == null || achartsummarycolumn.length == 0)
                return;
            ArrayList arraylist = new ArrayList();
            for(int i = 0; i < achartsummarycolumn.length; i++)
            {
                ChartSummaryColumn chartsummarycolumn = achartsummarycolumn[i];
                String as[] = {
                    chartsummarycolumn.getName(), chartsummarycolumn.getCustomName(), getFunctionString(chartsummarycolumn.getFunction())
                };
                arraylist.add(as);
            }

            seriesDataPane.populateBean(arraylist);
        }
    }

    public void populateBean(ChartCollection chartcollection)
    {
        populateBean(chartcollection, true);
    }

    private String getFunctionString(DataFunction datafunction)
    {
        for(int i = 0; i < CalculateComboBox.CLASS_ARRAY.length; i++)
        {
            Class class1 = datafunction.getClass();
            if(ComparatorUtils.equals(class1, CalculateComboBox.CLASS_ARRAY[i]))
                return CalculateComboBox.CALCULATE_ARRAY[i];
        }

        return CalculateComboBox.CALCULATE_ARRAY[0];
    }

    public void updateBean(ChartCollection chartcollection)
    {
        TopDefinitionProvider topdefinitionprovider = chartcollection.getSelectedChart().getFilterDefinition();
        MoreNameCDDefinition morenamecddefinition = null;
        if(topdefinitionprovider instanceof MoreNameCDDefinition)
            morenamecddefinition = (MoreNameCDDefinition)topdefinitionprovider;
        else
            morenamecddefinition = new MoreNameCDDefinition();
        java.util.List list = seriesDataPane.updateBean();
        ChartSummaryColumn achartsummarycolumn[] = new ChartSummaryColumn[list.size()];
        for(int i = 0; i < achartsummarycolumn.length; i++)
        {
            Object aobj[] = (Object[])list.get(i);
            String s = Utils.objectToString(aobj[0]);
            String s1 = Utils.objectToString(aobj[1]);
            if(isNeedSummary)
            {
                String s2 = Utils.objectToString(aobj[2]);
                achartsummarycolumn[i] = new ChartSummaryColumn(s, s1, getFcuntionByName(s2));
            } else
            {
                achartsummarycolumn[i] = new ChartSummaryColumn(s, s1, new NoneFunction());
            }
        }

        morenamecddefinition.setChartSummaryColumn(achartsummarycolumn);
        chartcollection.getSelectedChart().setFilterDefinition(morenamecddefinition);
    }

    private DataFunction getFcuntionByName(String s)
    {
        int i = 0;
        for(int j = 0; j < CalculateComboBox.CALCULATE_ARRAY.length; j++)
            if(ComparatorUtils.equals(s, CalculateComboBox.CALCULATE_ARRAY[j]))
                i = j;

        try
        {
            return (DataFunction)CalculateComboBox.CLASS_ARRAY[i].newInstance();
        }
        catch(InstantiationException instantiationexception)
        {
            FRLogger.getLogger().error("Function Error");
        }
        catch(IllegalAccessException illegalaccessexception)
        {
            FRLogger.getLogger().error("Function Error");
        }
        return new NoneFunction();
    }

    public ChartCollection updateBean()
    {
        return null;
    }

    private void checkRow(int i)
    {
        UITable uitable = seriesDataPane.getTable();
        Object obj = uitable.getValueAt(i, 0);
        if(obj != null)
            uitable.setValueAt(obj, i, 1);
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
