// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.chart.report;

import com.fr.base.MapXMLHelper;
import com.fr.chart.base.MapSvgXMLHelper;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.editor.ValueEditorPane;
import com.fr.design.editor.ValueEditorPaneFactory;
import com.fr.design.editor.editor.Editor;
import com.fr.design.editor.editor.TextEditor;
import com.fr.design.gui.itableeditorpane.UIArrayTableModel;
import com.fr.design.gui.itableeditorpane.UITableEditorPane;
import com.fr.design.gui.xcombox.ComboBoxUseEditor;
import com.fr.general.Inter;
import java.awt.BorderLayout;
import java.awt.Component;
import javax.swing.AbstractCellEditor;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;

public class MapCubeSetDataPane extends BasicBeanPane
{
    private class DefaultComboBoxRenderer extends DefaultTableCellRenderer
    {

        private static final long serialVersionUID = 0xf659437ec33a3882L;
        private ValueEditorPane cellEditor;
        final MapCubeSetDataPane this$0;

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
            this$0 = MapCubeSetDataPane.this;
            super();
            cellEditor = ValueEditorPaneFactory.createValueEditorPane(new Editor[] {
                new ComboBoxUseEditor(initNames)
            });
        }
    }

    private class DefaultComboBoxEditor extends AbstractCellEditor
        implements TableCellEditor
    {

        private static final long serialVersionUID = 0xd309f48e51dae1a6L;
        private ValueEditorPane cellEditor;
        final MapCubeSetDataPane this$0;

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
            this$0 = MapCubeSetDataPane.this;
            super();
            cellEditor = ValueEditorPaneFactory.createValueEditorPane(new Editor[] {
                new ComboBoxUseEditor(initNames)
            });
        }
    }


    private UITableEditorPane tableEditorPane;
    private String initNames[] = {
        ""
    };

    public MapCubeSetDataPane()
    {
        initCom();
    }

    private void initCom()
    {
        setLayout(new BorderLayout(0, 0));
        UIArrayTableModel uiarraytablemodel = new UIArrayTableModel(new String[] {
            Inter.getLocText("FR-Chart-Area_Name"), Inter.getLocText("FR-Chart-Drill_Map")
        }, new int[0]) {

            final MapCubeSetDataPane this$0;

            public boolean isCellEditable(int i, int j)
            {
                return j != 0;
            }

            
            {
                this$0 = MapCubeSetDataPane.this;
                super(as, ai);
            }
        }
;
        uiarraytablemodel.setDefaultEditor(java/lang/Object, new DefaultComboBoxEditor());
        uiarraytablemodel.setDefaultRenderer(java/lang/Object, new DefaultComboBoxRenderer());
        tableEditorPane = new UITableEditorPane(uiarraytablemodel);
        add(tableEditorPane);
        uiarraytablemodel.addRow(((Object) (new Object[] {
            "", ""
        })));
    }

    public void freshComboxNames()
    {
        initNames = MapSvgXMLHelper.getInstance().mapAllNames();
    }

    public void freshBitMapComboxNames()
    {
        initNames = MapXMLHelper.getInstance().mapAllNames();
    }

    public void populateBean(java.util.List list)
    {
        tableEditorPane.populate(list.toArray());
    }

    public java.util.List updateBean()
    {
        return tableEditorPane.update();
    }

    protected String title4PopupWindow()
    {
        return Inter.getLocText("FR-Chart-Drill_Setting");
    }

    public volatile Object updateBean()
    {
        return updateBean();
    }

    public volatile void populateBean(Object obj)
    {
        populateBean((java.util.List)obj);
    }

}
