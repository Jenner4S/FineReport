// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.cell.editor;

import com.fr.design.mainframe.ElementCasePane;
import com.fr.grid.event.CellEditorEvent;
import com.fr.grid.event.CellEditorListener;
import java.awt.Point;
import javax.swing.event.EventListenerList;

// Referenced classes of package com.fr.design.cell.editor:
//            CellEditor

public abstract class AbstractCellEditor
    implements CellEditor
{

    private EventListenerList listenerList;
    private CellEditorEvent cellEditEvent;
    private Point locationOnCellElement;
    private ElementCasePane ePane;

    public AbstractCellEditor()
    {
        listenerList = new EventListenerList();
        cellEditEvent = null;
        locationOnCellElement = null;
    }

    public AbstractCellEditor(ElementCasePane elementcasepane)
    {
        listenerList = new EventListenerList();
        cellEditEvent = null;
        locationOnCellElement = null;
        ePane = elementcasepane;
    }

    public Point getLocationOnCellElement()
    {
        return locationOnCellElement;
    }

    public void setLocationOnCellElement(Point point)
    {
        locationOnCellElement = point;
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

    public void addCellEditorListener(CellEditorListener celleditorlistener)
    {
        listenerList.add(com/fr/grid/event/CellEditorListener, celleditorlistener);
    }

    public void removeCellEditorListener(CellEditorListener celleditorlistener)
    {
        listenerList.remove(com/fr/grid/event/CellEditorListener, celleditorlistener);
    }

    protected void fireEditingStopped()
    {
        Object aobj[] = listenerList.getListenerList();
        for(int i = aobj.length - 2; i >= 0; i -= 2)
        {
            if(aobj[i] != com/fr/grid/event/CellEditorListener)
                continue;
            if(cellEditEvent == null)
                cellEditEvent = new CellEditorEvent(this);
            ((CellEditorListener)aobj[i + 1]).editingStopped(cellEditEvent);
        }

        if(ePane != null)
            ePane.fireSelectionChangeListener();
    }

    protected void fireEditingCanceled()
    {
        Object aobj[] = listenerList.getListenerList();
        for(int i = aobj.length - 2; i >= 0; i -= 2)
        {
            if(aobj[i] != com/fr/grid/event/CellEditorListener)
                continue;
            if(cellEditEvent == null)
                cellEditEvent = new CellEditorEvent(this);
            ((CellEditorListener)aobj[i + 1]).editingCanceled(cellEditEvent);
        }

    }
}
