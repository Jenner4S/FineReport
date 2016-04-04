// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.designer.properties;

import java.awt.Component;
import java.awt.event.*;
import java.io.Serializable;
import java.util.EventObject;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.TableCellEditor;

public class DelegateEditor extends AbstractCellEditor
    implements TableCellEditor
{
    protected class EditorDelegate
        implements ActionListener, ItemListener, ChangeListener, Serializable
    {

        protected Object value;
        final DelegateEditor this$0;

        public Object getCellEditorValue()
        {
            return value;
        }

        public void setValue(Object obj)
        {
            value = obj;
        }

        public boolean isCellEditable(EventObject eventobject)
        {
            if(eventobject instanceof MouseEvent)
                return ((MouseEvent)eventobject).getClickCount() >= clickCountToStart;
            else
                return true;
        }

        public boolean shouldSelectCell(EventObject eventobject)
        {
            return true;
        }

        public boolean startCellEditing(EventObject eventobject)
        {
            return true;
        }

        public boolean stopCellEditing()
        {
            fireEditingStopped();
            return true;
        }

        public void cancelCellEditing()
        {
            fireEditingCanceled();
        }

        public void actionPerformed(ActionEvent actionevent)
        {
            DelegateEditor.this.stopCellEditing();
        }

        public void itemStateChanged(ItemEvent itemevent)
        {
            DelegateEditor.this.stopCellEditing();
        }

        public void stateChanged(ChangeEvent changeevent)
        {
            DelegateEditor.this.stopCellEditing();
        }

        protected EditorDelegate()
        {
            this$0 = DelegateEditor.this;
            super();
        }
    }


    protected JComponent editorComponent;
    protected EditorDelegate _flddelegate;
    protected int clickCountToStart;

    public DelegateEditor()
    {
        clickCountToStart = 1;
    }

    public Component getComponent()
    {
        return editorComponent;
    }

    public void setClickCountToStart(int i)
    {
        clickCountToStart = i;
    }

    public int getClickCountToStart()
    {
        return clickCountToStart;
    }

    public Object getCellEditorValue()
    {
        return _flddelegate.getCellEditorValue();
    }

    public boolean isCellEditable(EventObject eventobject)
    {
        return _flddelegate.isCellEditable(eventobject);
    }

    public boolean shouldSelectCell(EventObject eventobject)
    {
        return _flddelegate.shouldSelectCell(eventobject);
    }

    public boolean stopCellEditing()
    {
        return _flddelegate.stopCellEditing();
    }

    public void cancelCellEditing()
    {
        _flddelegate.cancelCellEditing();
    }

    public Component getTableCellEditorComponent(JTable jtable, Object obj, boolean flag, int i, int j)
    {
        _flddelegate.setValue(obj);
        return editorComponent;
    }


}
