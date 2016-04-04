// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.chart.report;

import com.fr.base.Utils;
import com.fr.chart.chartdata.MapSingleLayerTableDefinition;
import com.fr.chart.chartdata.SeriesDefinition;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.event.UIObserver;
import com.fr.design.event.UIObserverListener;
import com.fr.design.gui.frpane.UICorrelationPane;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itable.UITableEditor;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.general.Inter;
import com.fr.stable.StringUtils;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import javax.swing.ComboBoxModel;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableModel;

public class MapMoreTableIndexPane extends BasicBeanPane
    implements UIObserver
{
    private class InnerTableEditor extends UITableEditor
    {

        private JComponent editorComponent;
        final MapMoreTableIndexPane this$0;

        public Object getCellEditorValue()
        {
            if(editorComponent instanceof UIComboBox)
                return ((UIComboBox)editorComponent).getSelectedItem();
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
                    uitextfield.setText(obj.toString());
            } else
            {
                UIComboBox uicombobox = new UIComboBox(boxItems);
                uicombobox.addItemListener(new ItemListener() {

                    final InnerTableEditor this$1;

                    public void itemStateChanged(ItemEvent itemevent)
                    {
                        tabPane.fireTargetChanged();
                        tabPane.stopCellEditing();
                    }

                
                {
                    this$1 = InnerTableEditor.this;
                    super();
                }
                }
);
                if(obj != null && StringUtils.isNotEmpty(obj.toString()))
                    uicombobox.setSelectedItem(obj);
                else
                    uicombobox.setSelectedItem(obj);
                editorComponent = uicombobox;
            }
            return editorComponent;
        }

        private InnerTableEditor()
        {
            this$0 = MapMoreTableIndexPane.this;
            super();
        }

    }


    private static final long serialVersionUID = 0x70e6f31373a9a128L;
    private String title;
    private UIComboBox areaNameBox;
    private UICorrelationPane tabPane;
    private Object boxItems[] = {
        ""
    };

    public MapMoreTableIndexPane()
    {
        title = "";
        initPane();
    }

    public MapMoreTableIndexPane(String s)
    {
        title = "";
        title = s;
        initPane();
    }

    protected String title4PopupWindow()
    {
        return title;
    }

    private void initPane()
    {
        setLayout(new BorderLayout());
        JPanel jpanel = new JPanel();
        add(jpanel, "North");
        jpanel.setLayout(new FlowLayout(0));
        jpanel.add(new UILabel((new StringBuilder()).append(Inter.getLocText("Area_Name")).append(":").toString()));
        areaNameBox = new UIComboBox();
        areaNameBox.setPreferredSize(new Dimension(120, 20));
        jpanel.add(areaNameBox);
        tabPane = new UICorrelationPane(new String[] {
            Inter.getLocText(new String[] {
                "Filed", "Title"
            }), Inter.getLocText("Area_Value")
        }) {

            final MapMoreTableIndexPane this$0;

            public UITableEditor createUITableEditor()
            {
                return new InnerTableEditor();
            }

            transient 
            {
                this$0 = MapMoreTableIndexPane.this;
                super(as);
            }
        }
;
        add(tabPane, "Center");
    }

    public void initAreaComBox(Object aobj[])
    {
        Object obj = areaNameBox.getSelectedItem();
        areaNameBox.removeAllItems();
        boxItems = aobj;
        if(aobj != null)
        {
            int i = 0;
            for(int j = aobj.length; i < j; i++)
                areaNameBox.addItem(aobj[i]);

        }
        areaNameBox.getModel().setSelectedItem(obj);
    }

    public void populateBean(MapSingleLayerTableDefinition mapsinglelayertabledefinition)
    {
        if(mapsinglelayertabledefinition != null)
        {
            areaNameBox.setSelectedItem(mapsinglelayertabledefinition.getAreaName());
            ArrayList arraylist = new ArrayList();
            int i = mapsinglelayertabledefinition.getTitleValueSize();
            for(int j = 0; j < i; j++)
            {
                SeriesDefinition seriesdefinition = mapsinglelayertabledefinition.getTitleValueWithIndex(j);
                if(seriesdefinition != null && seriesdefinition.getSeriesName() != null && seriesdefinition.getValue() != null)
                    arraylist.add(((Object) (new Object[] {
                        seriesdefinition.getSeriesName(), seriesdefinition.getValue()
                    })));
            }

            if(!arraylist.isEmpty())
                tabPane.populateBean(arraylist);
        }
    }

    public MapSingleLayerTableDefinition updateBean()
    {
        MapSingleLayerTableDefinition mapsinglelayertabledefinition = new MapSingleLayerTableDefinition();
        if(areaNameBox.getSelectedItem() != null)
            mapsinglelayertabledefinition.setAreaName(Utils.objectToString(areaNameBox.getSelectedItem()));
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
                mapsinglelayertabledefinition.addTitleValue(seriesdefinition);
            }
        }

        return mapsinglelayertabledefinition;
    }

    public void registerChangeListener(UIObserverListener uiobserverlistener)
    {
        if(areaNameBox != null)
            areaNameBox.registerChangeListener(uiobserverlistener);
        if(tabPane != null)
            tabPane.registerChangeListener(uiobserverlistener);
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
        populateBean((MapSingleLayerTableDefinition)obj);
    }


}
