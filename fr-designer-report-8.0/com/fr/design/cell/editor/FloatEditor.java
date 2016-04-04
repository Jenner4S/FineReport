// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.cell.editor;

import com.fr.grid.Grid;
import com.fr.grid.event.FloatEditorListener;
import com.fr.report.cell.FloatElement;
import java.awt.Component;
import java.awt.event.KeyEvent;

public interface FloatEditor
{

    public abstract Object getFloatEditorValue()
        throws Exception;

    public abstract Component getFloatEditorComponent(Grid grid, FloatElement floatelement, int i);

    public abstract boolean acceptKeyEventToStartFloatEditing(KeyEvent keyevent);

    public abstract boolean stopFloatEditing();

    public abstract void cancelFloatEditing();

    public abstract void addFloatEditorListener(FloatEditorListener floateditorlistener);

    public abstract void removeFloatEditorListener(FloatEditorListener floateditorlistener);
}
