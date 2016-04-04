// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.chart.report;

import com.fr.base.Formula;
import com.fr.base.Utils;
import com.fr.chart.chartdata.BaseSeriesDefinition;
import com.fr.chart.chartdata.MapSingleLayerReportDefinition;
import com.fr.chart.chartdata.SeriesDefinition;
import com.fr.design.beans.BasicBeanPane;
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
import javax.swing.table.TableModel;

public class MapMoreReportIndexPane extends BasicBeanPane
    implements UIObserver
{
    private class InnerTableEditor extends UITableEditor
    {

        private JComponent editorComponent;
        final MapMoreReportIndexPane this$0;

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
                editorComponent = uitextfield;
                if(obj != null)
                    uitextfield.setText(Utils.objectToString(obj));
            } else
            {
                TinyFormulaPane tinyformulapane = new TinyFormulaPane() {

                    final InnerTableEditor this$1;

                    public void okEvent()
                    {
                        tabPane.stopCellEditing();
                        tabPane.fireTargetChanged();
                    }

                
                {
                    this$1 = InnerTableEditor.this;
                    super();
                }
                }
;
                tinyformulapane.setBackground(UIConstants.FLESH_BLUE);
                tinyformulapane.getUITextField().addFocusListener(new FocusAdapter() {

                    final InnerTableEditor this$1;

                    public void focusLost(FocusEvent focusevent)
                    {
                        tabPane.fireTargetChanged();
                    }

                
                {
                    this$1 = InnerTableEditor.this;
                    super();
                }
                }
);
                if(obj != null)
                    tinyformulapane.getUITextField().setText(Utils.objectToString(obj));
                editorComponent = tinyformulapane;
            }
            return editorComponent;
        }

        private InnerTableEditor()
        {
            this$0 = MapMoreReportIndexPane.this;
            super();
        }

    }


    private String title;
    private TinyFormulaPane areaNamePane;
    private UICorrelationPane tabPane;

    public MapMoreReportIndexPane()
    {
        title = "";
        initPane();
    }

    public MapMoreReportIndexPane(String s)
    {
        title = "";
        title = s;
        initPane();
    }

    private void initPane()
    {
        setLayout(new BorderLayout(0, 0));
        JPanel jpanel = new JPanel();
        add(jpanel, "North");
        jpanel.setLayout(new FlowLayout(0));
        jpanel.add(new UILabel((new StringBuilder()).append(Inter.getLocText("Area_Name")).append(":").toString()));
        areaNamePane = new TinyFormulaPane();
        areaNamePane.setPreferredSize(new Dimension(120, 20));
        jpanel.add(areaNamePane);
        tabPane = new UICorrelationPane(new String[] {
            Inter.getLocText(new String[] {
                "Filed", "Title"
            }), Inter.getLocText("Area_Value")
        }) {

            final MapMoreReportIndexPane this$0;

            public UITableEditor createUITableEditor()
            {
                return new InnerTableEditor();
            }

            transient 
            {
                this$0 = MapMoreReportIndexPane.this;
                super(as);
            }
        }
;
        add(tabPane, "Center");
    }

    public void populateBean(MapSingleLayerReportDefinition mapsinglelayerreportdefinition)
    {
        if(mapsinglelayerreportdefinition != null && mapsinglelayerreportdefinition.getCategoryName() != null)
        {
            areaNamePane.populateBean(Utils.objectToString(mapsinglelayerreportdefinition.getCategoryName()));
            ArrayList arraylist = new ArrayList();
            int i = mapsinglelayerreportdefinition.getTitleValueSize();
            for(int j = 0; j < i; j++)
            {
                SeriesDefinition seriesdefinition = mapsinglelayerreportdefinition.getTitleValueWithIndex(j);
                if(seriesdefinition != null && seriesdefinition.getSeriesName() != null && seriesdefinition.getValue() != null)
                    arraylist.add(((Object) (new Object[] {
                        seriesdefinition.getSeriesName(), seriesdefinition.getValue()
                    })));
            }

            if(!arraylist.isEmpty())
                tabPane.populateBean(arraylist);
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
        java.util.List list = tabPane.updateBean();
        int i = 0;
        for(int j = list.size(); i < j; i++)
        {
            Object aobj[] = (Object[])(Object[])list.get(i);
            if(aobj.length == 2)
            {
                SeriesDefinition seriesdefinition = new SeriesDefinition();
                seriesdefinition.setSeriesName(aobj[0]);
                seriesdefinition.setValue(aobj[1]);
                mapsinglelayerreportdefinition.addTitleValue(seriesdefinition);
            }
        }

        return mapsinglelayerreportdefinition;
    }

    protected String title4PopupWindow()
    {
        return title;
    }

    public void registerChangeListener(UIObserverListener uiobserverlistener)
    {
        if(tabPane != null)
            tabPane.registerChangeListener(uiobserverlistener);
        if(areaNamePane != null)
            areaNamePane.registerChangeListener(uiobserverlistener);
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
