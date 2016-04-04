// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.chart.report;

import com.fr.base.Formula;
import com.fr.base.Utils;
import com.fr.chart.chartdata.BaseSeriesDefinition;
import com.fr.chart.chartdata.MapSingleLayerReportDefinition;
import com.fr.chart.chartdata.SeriesDefinition;
import com.fr.design.beans.FurtherBasicBeanPane;
import com.fr.design.constants.UIConstants;
import com.fr.design.event.UIObserver;
import com.fr.design.event.UIObserverListener;
import com.fr.design.formula.TinyFormulaPane;
import com.fr.design.gui.frpane.UICorrelationPane;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itable.UITableEditor;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.general.Inter;
import com.fr.stable.StableUtils;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.ArrayList;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.TableModel;

public class MapReportDataSinglePane extends FurtherBasicBeanPane
    implements UIObserver
{
    private class InnerTableEditor extends UITableEditor
    {

        private JComponent editorComponent;
        final MapReportDataSinglePane this$0;

        public Object getCellEditorValue()
        {
            if(editorComponent instanceof TinyFormulaPane)
                return ((TinyFormulaPane)editorComponent).getUITextField().getText();
            if(editorComponent instanceof UITextField)
                return ((UITextField)editorComponent).getText();
            else
                return super.getCellEditorValue();
        }

        public Component getTableCellEditorComponent(JTable jtable, Object obj, boolean flag, int i, int j)
        {
            if(j == jtable.getModel().getColumnCount())
                return null;
            else
                return getEditorComponent(j, obj);
        }

        private JComponent getEditorComponent(int i, Object obj)
        {
            if(i == 0)
            {
                UITextField uitextfield = new UITextField();
                addListener4UITextFiled(uitextfield);
                if(obj != null)
                    uitextfield.setText(Utils.objectToString(obj));
                editorComponent = uitextfield;
            } else
            {
                TinyFormulaPane tinyformulapane = new TinyFormulaPane() {

                    final InnerTableEditor this$1;

                    public void okEvent()
                    {
                        seriesPane.stopCellEditing();
                        seriesPane.fireTargetChanged();
                    }

                
                {
                    this$1 = InnerTableEditor.this;
                    super();
                }
                }
;
                tinyformulapane.setBackground(UIConstants.FLESH_BLUE);
                addListener4UITextFiled(tinyformulapane.getUITextField());
                if(obj != null)
                    tinyformulapane.getUITextField().setText(Utils.objectToString(obj));
                editorComponent = tinyformulapane;
            }
            return editorComponent;
        }

        private void addListener4UITextFiled(UITextField uitextfield)
        {
            uitextfield.addFocusListener(new FocusAdapter() {

                final InnerTableEditor this$1;

                public void focusLost(FocusEvent focusevent)
                {
                    seriesPane.fireTargetChanged();
                }

                
                {
                    this$1 = InnerTableEditor.this;
                    super();
                }
            }
);
        }

        private InnerTableEditor()
        {
            this$0 = MapReportDataSinglePane.this;
            super();
        }

    }


    private TinyFormulaPane areaNamePane;
    private UICorrelationPane seriesPane;
    private ArrayList changeListeners;

    public MapReportDataSinglePane()
    {
        changeListeners = new ArrayList();
        initCom();
    }

    private void initCom()
    {
        setLayout(new BorderLayout(0, 0));
        JPanel jpanel = new JPanel();
        add(jpanel, "North");
        jpanel.setLayout(new FlowLayout(1));
        jpanel.add(new UILabel((new StringBuilder()).append(Inter.getLocText("Area_Name")).append(":").toString(), 4));
        areaNamePane = new TinyFormulaPane();
        areaNamePane.setPreferredSize(new Dimension(120, 20));
        jpanel.add(areaNamePane);
        String as[] = {
            Inter.getLocText(new String[] {
                "Filed", "Title"
            }), Inter.getLocText("Area_Value")
        };
        seriesPane = new UICorrelationPane(as) {

            final MapReportDataSinglePane this$0;

            public UITableEditor createUITableEditor()
            {
                return new InnerTableEditor();
            }

            transient 
            {
                this$0 = MapReportDataSinglePane.this;
                super(as);
            }
        }
;
        add(seriesPane, "Center");
    }

    public boolean accept(Object obj)
    {
        return true;
    }

    public void reset()
    {
    }

    public String title4PopupWindow()
    {
        return Inter.getLocText("Cell");
    }

    public void populateBean(MapSingleLayerReportDefinition mapsinglelayerreportdefinition)
    {
        if(mapsinglelayerreportdefinition.getCategoryName() != null)
        {
            areaNamePane.populateBean(Utils.objectToString(mapsinglelayerreportdefinition.getCategoryName()));
            int i = mapsinglelayerreportdefinition.getTitleValueSize();
            ArrayList arraylist = new ArrayList();
            for(int j = 0; j < i; j++)
            {
                SeriesDefinition seriesdefinition = mapsinglelayerreportdefinition.getTitleValueWithIndex(j);
                if(seriesdefinition != null && seriesdefinition.getSeriesName() != null && seriesdefinition.getValue() != null)
                    arraylist.add(((Object) (new Object[] {
                        seriesdefinition.getSeriesName(), seriesdefinition.getValue()
                    })));
            }

            if(!arraylist.isEmpty())
                seriesPane.populateBean(arraylist);
        }
    }

    public MapSingleLayerReportDefinition updateBean()
    {
        MapSingleLayerReportDefinition mapsinglelayerreportdefinition = new MapSingleLayerReportDefinition();
        String s = areaNamePane.updateBean();
        if(StableUtils.canBeFormula(s))
            mapsinglelayerreportdefinition.setCategoryName(new Formula(s));
        else
            mapsinglelayerreportdefinition.setCategoryName(s);
        java.util.List list = seriesPane.updateBean();
        if(list != null && !list.isEmpty())
        {
            int i = 0;
            for(int j = list.size(); i < j; i++)
            {
                Object aobj[] = (Object[])(Object[])list.get(i);
                Object obj = aobj[0];
                Object obj1 = aobj[1];
                if(StableUtils.canBeFormula(obj1))
                    obj1 = new Formula(Utils.objectToString(obj1));
                SeriesDefinition seriesdefinition = new SeriesDefinition(obj, obj1);
                mapsinglelayerreportdefinition.addTitleValue(seriesdefinition);
            }

        }
        return mapsinglelayerreportdefinition;
    }

    public void registerChangeListener(final UIObserverListener listener)
    {
        changeListeners.add(new ChangeListener() {

            final UIObserverListener val$listener;
            final MapReportDataSinglePane this$0;

            public void stateChanged(ChangeEvent changeevent)
            {
                listener.doChange();
            }

            
            {
                this$0 = MapReportDataSinglePane.this;
                listener = uiobserverlistener;
                super();
            }
        }
);
    }

    public boolean shouldResponseChangeListener()
    {
        return true;
    }

    public volatile Object updateBean()
    {
        return updateBean();
    }

    public volatile void populateBean(Object obj)
    {
        populateBean((MapSingleLayerReportDefinition)obj);
    }

}
