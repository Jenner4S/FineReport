// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.widget.editors;

import com.fr.base.BaseUtils;
import com.fr.design.actions.UpdateAction;
import com.fr.design.gui.ilable.UILabel;
import com.fr.form.data.DataTableConfig;
import com.fr.general.Inter;
import com.fr.stable.core.PropertyChangeListener;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;

public class DataTableConfigPane extends JComponent
    implements PropertyChangeListener
{
    class DataEditingTable extends JTable
    {
        class MouseAdapterListener extends MouseAdapter
        {
            class CutAction extends UpdateAction
            {

                final MouseAdapterListener this$2;

                public void actionPerformed(ActionEvent actionevent)
                {
                    int i = table.getSelectedRow();
                    int j = table.getSelectedColumn();
                    table.getColumnModel().removeColumn(table.getColumn(table.getColumnName(j)));
                    propertyChange();
                }

                public CutAction()
                {
                    this$2 = MouseAdapterListener.this;
                    super();
                    setName(Inter.getLocText("M_Edit-Cut"));
                    setMnemonic('T');
                    setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/m_edit/cut.png"));
                    setAccelerator(KeyStroke.getKeyStroke(88, 2));
                }
            }


            private JTable table;
            int oldY;
            int newY;
            int row;
            int oldHeight;
            boolean drag;
            int increase;
            JPopupMenu popupMenu;
            final DataEditingTable this$1;

            public void mouseMoved(MouseEvent mouseevent)
            {
                int i = table.rowAtPoint(mouseevent.getPoint());
                int j = 0;
                for(int k = 0; k <= i; k++)
                    j += table.getRowHeight(k);

                if(j - mouseevent.getY() < 3)
                {
                    drag = true;
                    table.setCursor(new Cursor(8));
                } else
                {
                    drag = false;
                    table.setCursor(new Cursor(0));
                }
            }

            private void trigger_popup(MouseEvent mouseevent)
            {
                popupMenu.show(table, mouseevent.getX(), mouseevent.getY());
            }

            public void mouseDragged(MouseEvent mouseevent)
            {
                if(drag)
                {
                    int i = (oldHeight + mouseevent.getY()) - oldY;
                    if(i < 30)
                        table.setRowHeight(row, 30);
                    else
                        table.setRowHeight(row, (oldHeight + mouseevent.getY()) - oldY);
                    propertyChange();
                }
            }

            public void mousePressed(MouseEvent mouseevent)
            {
                oldY = mouseevent.getY();
                row = table.rowAtPoint(mouseevent.getPoint());
                oldHeight = table.getRowHeight(row);
                if(mouseevent.getButton() == 3)
                    trigger_popup(mouseevent);
            }

            public void mouseReleased(MouseEvent mouseevent)
            {
                newY = mouseevent.getY();
                table.setCursor(new Cursor(0));
            }


            public MouseAdapterListener(JTable jtable)
            {
                this$1 = DataEditingTable.this;
                super();
                oldY = 0;
                newY = 0;
                row = 0;
                oldHeight = 0;
                drag = false;
                increase = 0;
                table = jtable;
                popupMenu = new JPopupMenu();
                popupMenu.add(new CutAction());
                popupMenu.add(new CutAction());
                popupMenu.add(new CutAction());
                popupMenu.add(new CutAction());
            }
        }

        public class BeanTableModel extends AbstractTableModel
        {

            final DataEditingTable this$1;

            public int getColumnCount()
            {
                return config.getColumnCount();
            }

            public int getRowCount()
            {
                return 1;
            }

            public String getColumnName(int i)
            {
                return config.getColumnName(i);
            }

            public Object getValueAt(int i, int j)
            {
                return (new StringBuilder()).append(config.getTableDataName()).append(".").append(config.getColumnName(j)).toString();
            }

            public BeanTableModel()
            {
                this$1 = DataEditingTable.this;
                super();
            }
        }


        private DataTableConfig config;
        private BeanTableModel model;
        private TableColumnModelListener modeListener;
        final DataTableConfigPane this$0;

        public TableCellRenderer getCellRenderer(int i, int j)
        {
            TableCellRenderer tablecellrenderer = super.getCellRenderer(i, j);
            if(tablecellrenderer instanceof UILabel)
                ((UILabel)tablecellrenderer).setHorizontalAlignment(0);
            return tablecellrenderer;
        }

        public void populate(DataTableConfig datatableconfig)
        {
            getTableHeader().getColumnModel().removeColumnModelListener(modeListener);
            if(datatableconfig == null)
                datatableconfig = DataTableConfig.DEFAULT_TABLE_DATA_CONFIG;
            config = datatableconfig;
            model = new BeanTableModel();
            setModel(model);
            setRowHeight(0, datatableconfig.getRowHeight());
            for(int i = 0; i < datatableconfig.getColumnCount(); i++)
                getColumn(getColumnName(i)).setPreferredWidth(datatableconfig.getColumnWidth(i));

            getTableHeader().getColumnModel().addColumnModelListener(modeListener);
            doLayout();
            repaint();
        }

        public DataTableConfig update()
        {
            config.setRowHeight(getRowHeight(0));
            model = new BeanTableModel();
            String as[] = new String[getColumnCount()];
            for(int i = 0; i < getColumnCount(); i++)
            {
                config.setColumnWidth(i, getColumn(getColumnName(i)).getWidth());
                as[i] = getColumnName(i);
            }

            config.setColumns(as);
            return config;
        }


        public DataEditingTable()
        {
            this$0 = DataTableConfigPane.this;
            super();
            setBorder(BorderFactory.createLineBorder(new Color(210, 210, 210), 1));
            setColumnSelectionAllowed(true);
            setRowSelectionAllowed(true);
            MouseAdapterListener mouseadapterlistener = new MouseAdapterListener(this);
            addMouseListener(mouseadapterlistener);
            addMouseMotionListener(mouseadapterlistener);
            model = new BeanTableModel();
            modeListener = new TableColumnModelListener() {

                final DataTableConfigPane val$this$0;
                final DataEditingTable this$1;

                public void columnAdded(TableColumnModelEvent tablecolumnmodelevent)
                {
                }

                public void columnMarginChanged(ChangeEvent changeevent)
                {
                    propertyChange();
                }

                public void columnMoved(TableColumnModelEvent tablecolumnmodelevent)
                {
                    propertyChange();
                }

                public void columnRemoved(TableColumnModelEvent tablecolumnmodelevent)
                {
                }

                public void columnSelectionChanged(ListSelectionEvent listselectionevent)
                {
                }


// JavaClassFileOutputException: Invalid index accessing method local variables table of <init>
            }
;
        }
    }

    class DataTableLayout extends BorderLayout
    {

        final DataTableConfigPane this$0;

        public void layoutContainer(Container container)
        {
            super.layoutContainer(container);
            table.doLayout();
        }

        DataTableLayout()
        {
            this$0 = DataTableConfigPane.this;
            super();
        }
    }


    private DataEditingTable table;
    private ArrayList changetList;

    public DataTableConfigPane()
    {
        changetList = new ArrayList();
        table = new DataEditingTable();
        JScrollPane jscrollpane = new JScrollPane(table);
        setLayout(new DataTableLayout());
        add(jscrollpane, "Center");
    }

    public void populate(DataTableConfig datatableconfig)
    {
        table.populate(datatableconfig);
    }

    public DataTableConfig update()
    {
        return table.update();
    }

    public void addpropertyChangeListener(PropertyChangeListener propertychangelistener)
    {
        changetList.add(propertychangelistener);
    }

    public void propertyChange()
    {
        PropertyChangeListener propertychangelistener;
        for(Iterator iterator = changetList.iterator(); iterator.hasNext(); propertychangelistener.propertyChange())
            propertychangelistener = (PropertyChangeListener)iterator.next();

    }

    public void propertyChange(Object obj)
    {
    }

    public void propertyChange(Object aobj[])
    {
    }

}
