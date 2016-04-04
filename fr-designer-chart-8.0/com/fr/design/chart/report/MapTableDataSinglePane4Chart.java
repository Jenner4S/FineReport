// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.chart.report;

import com.fr.base.Utils;
import com.fr.chart.chartdata.MapSingleLayerTableDefinition;
import com.fr.chart.chartdata.SeriesDefinition;
import com.fr.design.beans.FurtherBasicBeanPane;
import com.fr.design.data.tabledata.wrapper.TableDataWrapper;
import com.fr.design.event.UIObserver;
import com.fr.design.event.UIObserverListener;
import com.fr.design.gui.frpane.UICorrelationPane;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.ilable.BoldFontTextLabel;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itable.UITableEditor;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.general.Inter;
import com.fr.stable.StringUtils;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import javax.swing.ComboBoxModel;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.TableModel;

public class MapTableDataSinglePane4Chart extends FurtherBasicBeanPane
    implements UIObserver
{
    private class InnerTableEditor extends UITableEditor
    {

        private JComponent editorComponent;
        final MapTableDataSinglePane4Chart this$0;

        public Object getCellEditorValue()
        {
            if(editorComponent instanceof UITextField)
            {
                UITextField uitextfield = (UITextField)editorComponent;
                return uitextfield.getText();
            }
            if(editorComponent instanceof UIComboBox)
            {
                UIComboBox uicombobox = (UIComboBox)editorComponent;
                return uicombobox.getSelectedItem();
            } else
            {
                return super.getCellEditorValue();
            }
        }

        public Component getTableCellEditorComponent(JTable jtable, Object obj, boolean flag, int i, int j)
        {
            if(j == jtable.getModel().getColumnCount())
                return null;
            if(j == 0)
            {
                UITextField uitextfield = new UITextField();
                if(obj != null)
                    uitextfield.setText(Utils.objectToString(obj));
                uitextfield.registerChangeListener(new UIObserverListener() {

                    final InnerTableEditor this$1;

                    public void doChange()
                    {
                        titleValuePane.fireTargetChanged();
                    }

                
                {
                    this$1 = InnerTableEditor.this;
                    super();
                }
                }
);
                editorComponent = uitextfield;
            } else
            {
                UIComboBox uicombobox = new UIComboBox(initNames);
                uicombobox.addItemListener(new ItemListener() {

                    final InnerTableEditor this$1;

                    public void itemStateChanged(ItemEvent itemevent)
                    {
                        titleValuePane.fireTargetChanged();
                        titleValuePane.stopCellEditing();
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
            this$0 = MapTableDataSinglePane4Chart.this;
            super();
        }

    }


    private static final int LABEL_WIDTH_GAP = 5;
    private static final int COM_HEIGHT = 20;
    private ArrayList changeListeners;
    private String initNames[] = {
        ""
    };
    private UIComboBox areaNameBox;
    private UICorrelationPane titleValuePane;
    private TableDataWrapper tableDataWrapper;

    public MapTableDataSinglePane4Chart()
    {
        changeListeners = new ArrayList();
        setLayout(new BorderLayout());
        JPanel jpanel = new JPanel();
        add(jpanel, "Center");
        jpanel.setLayout(new BorderLayout());
        jpanel.add(getAreaNamePane(), "North");
        String as[] = {
            Inter.getLocText("FR-Chart-Area_Title"), Inter.getLocText("FR-Chart-Area_Value")
        };
        titleValuePane = new UICorrelationPane(as) {

            final MapTableDataSinglePane4Chart this$0;

            public UITableEditor createUITableEditor()
            {
                return new InnerTableEditor();
            }

            transient 
            {
                this$0 = MapTableDataSinglePane4Chart.this;
                super(as);
            }
        }
;
        jpanel.add(titleValuePane, "Center");
    }

    private JPanel getAreaNamePane()
    {
        BoldFontTextLabel boldfonttextlabel = new BoldFontTextLabel((new StringBuilder()).append(Inter.getLocText("FR-Chart-Map_ShowWay")).append(":").toString());
        UILabel uilabel = new UILabel(4, boldfonttextlabel) {

            final UILabel val$label;
            final MapTableDataSinglePane4Chart this$0;

            public Dimension getPreferredSize()
            {
                return new Dimension(label.getPreferredSize().width + 5, 20);
            }

            
            {
                this$0 = MapTableDataSinglePane4Chart.this;
                label = uilabel;
                super(final_s, i);
            }
        }
;
        areaNameBox = new UIComboBox();
        areaNameBox.setPreferredSize(new Dimension(80, 20));
        double d = -2D;
        double d1 = -1D;
        double ad[] = {
            d, d1
        };
        double ad1[] = {
            d
        };
        Component acomponent[][] = {
            {
                uilabel, areaNameBox
            }
        };
        return TableLayoutHelper.createTableLayoutPane(acomponent, ad1, ad);
    }

    public void refreshAreaNameBox()
    {
        TableDataWrapper tabledatawrapper = tableDataWrapper;
        if(tabledatawrapper == null)
            return;
        java.util.List list = tabledatawrapper.calculateColumnNameList();
        initNames = (String[])list.toArray(new String[list.size()]);
        Object obj = areaNameBox.getSelectedItem();
        areaNameBox.removeAllItems();
        int i = 0;
        for(int j = initNames.length; i < j; i++)
            areaNameBox.addItem(initNames[i]);

        areaNameBox.getModel().setSelectedItem(obj);
        stopEditing();
    }

    public boolean accept(Object obj)
    {
        return false;
    }

    public void reset()
    {
    }

    public String title4PopupWindow()
    {
        return Inter.getLocText("FR-Chart-Table_Data");
    }

    private void stopEditing()
    {
    }

    public void populateBean(MapSingleLayerTableDefinition mapsinglelayertabledefinition)
    {
        stopEditing();
        if(mapsinglelayertabledefinition instanceof MapSingleLayerTableDefinition)
        {
            MapSingleLayerTableDefinition mapsinglelayertabledefinition1 = mapsinglelayertabledefinition;
            areaNameBox.setSelectedItem(mapsinglelayertabledefinition1.getAreaName());
            ArrayList arraylist = new ArrayList();
            int i = mapsinglelayertabledefinition1.getTitleValueSize();
            for(int j = 0; j < i; j++)
            {
                SeriesDefinition seriesdefinition = mapsinglelayertabledefinition1.getTitleValueWithIndex(j);
                if(seriesdefinition != null && seriesdefinition.getSeriesName() != null && seriesdefinition.getValue() != null)
                    arraylist.add(((Object) (new Object[] {
                        seriesdefinition.getSeriesName(), seriesdefinition.getValue()
                    })));
            }

            if(!arraylist.isEmpty())
                titleValuePane.populateBean(arraylist);
        }
    }

    public MapSingleLayerTableDefinition updateBean()
    {
        stopEditing();
        MapSingleLayerTableDefinition mapsinglelayertabledefinition = new MapSingleLayerTableDefinition();
        TableDataWrapper tabledatawrapper = tableDataWrapper;
        if(tabledatawrapper == null || areaNameBox.getSelectedItem() == null)
            return null;
        mapsinglelayertabledefinition.setTableData(tableDataWrapper.getTableData());
        mapsinglelayertabledefinition.setAreaName(Utils.objectToString(areaNameBox.getSelectedItem()));
        java.util.List list = titleValuePane.updateBean();
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

    public void registerChangeListener(final UIObserverListener listener)
    {
        changeListeners.add(new ChangeListener() {

            final UIObserverListener val$listener;
            final MapTableDataSinglePane4Chart this$0;

            public void stateChanged(ChangeEvent changeevent)
            {
                listener.doChange();
            }

            
            {
                this$0 = MapTableDataSinglePane4Chart.this;
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

    public void setTableDataWrapper(TableDataWrapper tabledatawrapper)
    {
        tableDataWrapper = tabledatawrapper;
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
