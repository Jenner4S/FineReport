// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.cell.editor;

import com.fr.grid.Grid;
import com.fr.grid.event.CellEditorListener;
import com.fr.report.cell.TemplateCellElement;
import java.awt.Component;
import java.awt.Point;

public interface CellEditor
{

    public abstract Object getCellEditorValue()
        throws Exception;

    public abstract Component getCellEditorComponent(Grid grid, TemplateCellElement templatecellelement, int i);

    public abstract Point getLocationOnCellElement();

    public abstract void setLocationOnCellElement(Point point);

    public abstract boolean stopCellEditing();

    public abstract void cancelCellEditing();

    public abstract void addCellEditorListener(CellEditorListener celleditorlistener);

    public abstract void removeCellEditorListener(CellEditorListener celleditorlistener);
}
