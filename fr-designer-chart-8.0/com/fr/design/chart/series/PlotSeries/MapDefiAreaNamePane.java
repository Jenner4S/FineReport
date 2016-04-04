// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.chart.series.PlotSeries;

import com.fr.base.Utils;
import com.fr.chart.base.MapSvgAttr;
import com.fr.chart.base.MapSvgXMLHelper;
import com.fr.data.TableDataSource;
import com.fr.data.core.DataCoreUtils;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.data.DesignTableDataManager;
import com.fr.design.data.tabledata.wrapper.TableDataWrapper;
import com.fr.design.editor.ValueEditorPane;
import com.fr.design.editor.ValueEditorPaneFactory;
import com.fr.design.editor.editor.Editor;
import com.fr.design.editor.editor.TextEditor;
import com.fr.design.gui.icombobox.FilterComboBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itableeditorpane.UIArrayTableModel;
import com.fr.design.gui.itableeditorpane.UITableEditorPane;
import com.fr.design.gui.xcombox.ComboBoxUseEditor;
import com.fr.design.mainframe.chart.gui.data.DatabaseTableDataPane;
import com.fr.general.Inter;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.AbstractCellEditor;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;

// Referenced classes of package com.fr.design.chart.series.PlotSeries:
//            AbstrctMapAttrEditPane

public class MapDefiAreaNamePane extends BasicBeanPane
    implements AbstrctMapAttrEditPane
{
    private class DefaultComboBoxRenderer extends DefaultTableCellRenderer
    {

        private ValueEditorPane cellEditor;
        final MapDefiAreaNamePane this$0;

        public Component getTableCellRendererComponent(JTable jtable, Object obj, boolean flag, boolean flag1, int i, int j)
        {
            if(j == 0)
                cellEditor = ValueEditorPaneFactory.createValueEditorPane(new Editor[] {
                    new TextEditor()
                });
            else
                cellEditor = ValueEditorPaneFactory.createValueEditorPane(new Editor[] {
                    new ComboBoxUseEditor(initNames)
                });
            cellEditor.populate(obj != null ? obj : "");
            return cellEditor;
        }

        public DefaultComboBoxRenderer()
        {
            this$0 = MapDefiAreaNamePane.this;
            super();
            cellEditor = ValueEditorPaneFactory.createValueEditorPane(new Editor[] {
                new ComboBoxUseEditor(initNames)
            });
        }
    }

    private class DefaultComboBoxEditor extends AbstractCellEditor
        implements TableCellEditor
    {

        private ValueEditorPane cellEditor;
        final MapDefiAreaNamePane this$0;

        public Component getTableCellEditorComponent(JTable jtable, Object obj, boolean flag, int i, int j)
        {
            if(j == 0)
                cellEditor = ValueEditorPaneFactory.createValueEditorPane(new Editor[] {
                    new TextEditor()
                });
            else
                cellEditor = ValueEditorPaneFactory.createValueEditorPane(new Editor[] {
                    new ComboBoxUseEditor(initNames)
                });
            cellEditor.populate(obj != null ? obj : "");
            return cellEditor;
        }

        public Object getCellEditorValue()
        {
            return cellEditor.update();
        }

        public DefaultComboBoxEditor()
        {
            this$0 = MapDefiAreaNamePane.this;
            super();
            cellEditor = ValueEditorPaneFactory.createValueEditorPane(new Editor[] {
                new ComboBoxUseEditor(initNames)
            });
        }
    }


    private DatabaseTableDataPane tableDataBox;
    private FilterComboBox columnBox;
    private UITableEditorPane tableEditorPane;
    private UIArrayTableModel tableEditorModel;
    private String initNames[];
    private String editName;
    private boolean isNeedDataSource;
    private MapSvgAttr currentSvg;
    ItemListener columnChange = new ItemListener() {

        final MapDefiAreaNamePane this$0;

        public void itemStateChanged(ItemEvent itemevent)
        {
            if(columnBox.getSelectedItem() != null)
            {
                String s = Utils.objectToString(columnBox.getSelectedItem());
                TableDataWrapper tabledatawrapper = tableDataBox.getTableDataWrapper();
                TableDataSource tabledatasource = DesignTableDataManager.getEditingTableDataSource();
                if(tabledatawrapper == null || tabledatasource == null)
                    return;
                initNames = DataCoreUtils.getColValuesInData(tabledatasource, tabledatawrapper.getTableDataName(), s);
                if(tableEditorModel != null)
                    tableEditorModel.stopCellEditing();
            }
        }

            
            {
                this$0 = MapDefiAreaNamePane.this;
                super();
            }
    }
;

    public MapDefiAreaNamePane(boolean flag)
    {
        initNames = new String[0];
        editName = "";
        isNeedDataSource = true;
        isNeedDataSource = flag;
        initCom();
    }

    public MapDefiAreaNamePane()
    {
        initNames = new String[0];
        editName = "";
        isNeedDataSource = true;
        initCom();
    }

    private void initCom()
    {
        setLayout(new BorderLayout(0, 0));
        JPanel jpanel = new JPanel();
        if(isNeedDataSource)
            add(jpanel, "North");
        jpanel.setLayout(new FlowLayout(0));
        UILabel uilabel = new UILabel((new StringBuilder()).append(Inter.getLocText("Chart-DS_TableData")).append(":").toString(), 4);
        tableDataBox = new DatabaseTableDataPane(uilabel) {

            final MapDefiAreaNamePane this$0;

            protected void userEvent()
            {
                refreshAreaNameBox();
            }

            
            {
                this$0 = MapDefiAreaNamePane.this;
                super(uilabel);
            }
        }
;
        tableDataBox.setPreferredSize(new Dimension(200, 20));
        jpanel.add(tableDataBox);
        columnBox = new FilterComboBox();
        columnBox.setPreferredSize(new Dimension(40, 20));
        columnBox.addItemListener(columnChange);
        jpanel.add(new UILabel((new StringBuilder()).append(Inter.getLocText("FR-Chart-Map_Field")).append(":").toString()));
        jpanel.add(columnBox);
        tableEditorModel = new UIArrayTableModel(new String[] {
            Inter.getLocText("FR-Chart-Map_Use_Field"), Inter.getLocText("FR-Chart-Area_Name")
        }, new int[0]) {

            final MapDefiAreaNamePane this$0;

            public boolean isCellEditable(int i, int j)
            {
                return j != 0;
            }

            
            {
                this$0 = MapDefiAreaNamePane.this;
                super(as, ai);
            }
        }
;
        tableEditorModel.setDefaultEditor(java/lang/Object, new DefaultComboBoxEditor());
        tableEditorModel.setDefaultRenderer(java/lang/Object, new DefaultComboBoxRenderer());
        tableEditorPane = new UITableEditorPane(tableEditorModel);
        add(tableEditorPane);
    }

    private void refreshAreaNameBox()
    {
        TableDataWrapper tabledatawrapper = tableDataBox.getTableDataWrapper();
        if(tabledatawrapper == null)
        {
            return;
        } else
        {
            java.util.List list = tabledatawrapper.calculateColumnNameList();
            columnBox.setItemList(list);
            return;
        }
    }

    public void populateBean(String s)
    {
        if(MapSvgXMLHelper.getInstance().containsMapName(s))
        {
            MapSvgAttr mapsvgattr = MapSvgXMLHelper.getInstance().getMapAttr(s);
            editName = s;
            populateMapAttr(mapsvgattr);
        }
    }

    public String updateBean()
    {
        updateMapAttr();
        MapSvgXMLHelper.getInstance().removeMapAttr(currentSvg.getName());
        MapSvgXMLHelper.getInstance().pushMapAttr(currentSvg.getName(), currentSvg);
        return "";
    }

    private void updateMapAttr()
    {
        if(currentSvg != null)
        {
            tableEditorModel.stopCellEditing();
            java.util.List list = tableEditorPane.update();
            int i = 0;
            for(int j = list.size(); i < j; i++)
            {
                Object aobj[] = (Object[])(Object[])list.get(i);
                String s = Utils.objectToString(aobj[0]);
                String s1 = Utils.objectToString(aobj[1]);
                currentSvg.setNameTo(s, s1);
            }

        }
    }

    protected String title4PopupWindow()
    {
        return Inter.getLocText(new String[] {
            "Define", "Area_Name"
        });
    }

    public void populateMapAttr(MapSvgAttr mapsvgattr)
    {
        ArrayList arraylist = new ArrayList();
        if(mapsvgattr == null)
        {
            currentSvg = null;
            tableEditorPane.populate(arraylist.toArray());
            return;
        }
        currentSvg = mapsvgattr;
        ArrayList arraylist1 = new ArrayList();
        for(Iterator iterator = mapsvgattr.shapeValuesIterator(); iterator.hasNext(); arraylist1.add(iterator.next()));
        for(int i = 0; i < arraylist1.size(); i++)
        {
            Object obj = arraylist1.get(i);
            String s = mapsvgattr.getNameToValue(Utils.objectToString(obj));
            arraylist.add(((Object) (new Object[] {
                obj, s
            })));
        }

        tableEditorPane.populate(arraylist.toArray());
    }

    public MapSvgAttr updateCurrentAttr()
    {
        updateMapAttr();
        return currentSvg;
    }

    public volatile Object updateBean()
    {
        return updateBean();
    }

    public volatile void populateBean(Object obj)
    {
        populateBean((String)obj);
    }






}
