// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.widget.accessibles;

import java.awt.Component;
import java.awt.event.MouseEvent;
import java.beans.*;
import java.io.Serializable;
import java.util.EventObject;
import javax.swing.AbstractCellEditor;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

public class AccessiblePropertyCellEditor extends AbstractCellEditor
    implements TableCellEditor, PropertyChangeListener, Serializable
{

    private PropertyEditor editor;
    protected Component editorComponent;
    protected int clickCountToStart;
    protected Object value;

    public AccessiblePropertyCellEditor(PropertyEditor propertyeditor)
    {
        editor = propertyeditor;
        editorComponent = editor.getCustomEditor();
        clickCountToStart = 1;
        editor.addPropertyChangeListener(this);
    }

    public Object getCellEditorValue()
    {
        return editor.getValue();
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

    public boolean stopCellEditing()
    {
        fireEditingStopped();
        return true;
    }

    public void cancelCellEditing()
    {
        fireEditingCanceled();
    }

    public Component getTableCellEditorComponent(JTable jtable, Object obj, boolean flag, int i, int j)
    {
        editorComponent.setForeground(jtable.getSelectionForeground());
        editorComponent.setBackground(jtable.getSelectionBackground());
        editorComponent.setFont(jtable.getFont());
        editor.setValue(obj);
        return editorComponent;
    }

    public void propertyChange(PropertyChangeEvent propertychangeevent)
    {
        stopCellEditing();
    }
}
