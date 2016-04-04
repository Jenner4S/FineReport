// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.cell.editor;

import com.fr.grid.event.FloatEditorEvent;
import com.fr.grid.event.FloatEditorListener;
import java.awt.event.KeyEvent;
import javax.swing.event.EventListenerList;

// Referenced classes of package com.fr.design.cell.editor:
//            FloatEditor

public abstract class AbstractFloatEditor
    implements FloatEditor
{

    private EventListenerList listenerList;
    private FloatEditorEvent floatEditEvent;

    public AbstractFloatEditor()
    {
        listenerList = new EventListenerList();
        floatEditEvent = null;
    }

    public boolean acceptKeyEventToStartFloatEditing(KeyEvent keyevent)
    {
        return true;
    }

    public boolean stopFloatEditing()
    {
        fireEditingStopped();
        return true;
    }

    public void cancelFloatEditing()
    {
        fireEditingCanceled();
    }

    public void addFloatEditorListener(FloatEditorListener floateditorlistener)
    {
        listenerList.add(com/fr/grid/event/FloatEditorListener, floateditorlistener);
    }

    public void removeFloatEditorListener(FloatEditorListener floateditorlistener)
    {
        listenerList.remove(com/fr/grid/event/FloatEditorListener, floateditorlistener);
    }

    protected void fireEditingStopped()
    {
        Object aobj[] = listenerList.getListenerList();
        for(int i = aobj.length - 2; i >= 0; i -= 2)
        {
            if(aobj[i] != com/fr/grid/event/FloatEditorListener)
                continue;
            if(floatEditEvent == null)
                floatEditEvent = new FloatEditorEvent(this);
            ((FloatEditorListener)aobj[i + 1]).editingStopped(floatEditEvent);
        }

    }

    protected void fireEditingCanceled()
    {
        Object aobj[] = listenerList.getListenerList();
        for(int i = aobj.length - 2; i >= 0; i -= 2)
        {
            if(aobj[i] != com/fr/grid/event/FloatEditorListener)
                continue;
            if(floatEditEvent == null)
                floatEditEvent = new FloatEditorEvent(this);
            ((FloatEditorListener)aobj[i + 1]).editingCanceled(floatEditEvent);
        }

    }
}
