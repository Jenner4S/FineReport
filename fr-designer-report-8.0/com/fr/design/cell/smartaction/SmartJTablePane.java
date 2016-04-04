// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.cell.smartaction;

import com.fr.design.dialog.*;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.mainframe.ElementCasePane;
import com.fr.design.selection.SelectionListener;
import com.fr.general.Inter;
import java.awt.Window;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;

// Referenced classes of package com.fr.design.cell.smartaction:
//            SmartJTablePaneAction

public abstract class SmartJTablePane extends BasicPane
{

    public static final int OK = 0;
    public static final int CANCEL = 1;
    protected ElementCasePane actionReportPane;
    protected AbstractTableModel model;
    protected SelectionListener gridSelectionChangeL;
    protected SmartJTablePaneAction action;
    protected JTable table;
    protected JScrollPane scrollPane;
    protected boolean old_editable;
    protected int editingRowIndex;

    public SmartJTablePane(AbstractTableModel abstracttablemodel, ElementCasePane elementcasepane)
    {
        old_editable = true;
        editingRowIndex = 0;
        model = abstracttablemodel;
        actionReportPane = elementcasepane;
        old_editable = elementcasepane.isEditable();
        initComponents();
    }

    public void initComponents()
    {
        setLayout(FRGUIPaneFactory.createBorderLayout());
        add(new UILabel(Inter.getLocText("RWA-Click_Cell_To_Edit_Value")), "North");
        table = new JTable(model);
        add(scrollPane = new JScrollPane(table), "Center");
        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            final SmartJTablePane this$0;

            public void valueChanged(ListSelectionEvent listselectionevent)
            {
                int i = table.getSelectedRow();
                if(i >= 0)
                {
                    setEditingRowIndex(i);
                    table.repaint();
                }
            }

            
            {
                this$0 = SmartJTablePane.this;
                super();
            }
        }
);
        setCellRenderer();
        actionReportPane.addSelectionChangeListener(gridSelectionChangeL);
    }

    public void changeGridSelectionChangeListener(SelectionListener selectionlistener)
    {
        actionReportPane.removeSelectionChangeListener(gridSelectionChangeL);
        gridSelectionChangeL = selectionlistener;
        actionReportPane.addSelectionChangeListener(gridSelectionChangeL);
    }

    public void changeSmartJTablePaneAction(SmartJTablePaneAction smartjtablepaneaction)
    {
        action = smartjtablepaneaction;
    }

    public abstract void setCellRenderer();

    protected void setEditingRowIndex(int i)
    {
        editingRowIndex = i;
        table.scrollRectToVisible(table.getCellRect(editingRowIndex, 2, true));
    }

    public BasicDialog showWindow(Window window)
    {
        BasicDialog basicdialog = super.showSmallWindow(window, new DialogActionListener() {

            final SmartJTablePane this$0;

            public void doOk()
            {
                action.doDialogExit(0);
            }

            public void doCancel()
            {
                action.doDialogExit(1);
            }

            
            {
                this$0 = SmartJTablePane.this;
                super();
            }
        }
);
        return basicdialog;
    }
}
