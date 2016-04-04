// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.chart.report;

import com.fr.design.editor.ValueEditorPane;
import com.fr.design.editor.ValueEditorPaneFactory;
import com.fr.design.editor.editor.Editor;
import com.fr.design.editor.editor.FormulaEditor;
import com.fr.design.editor.editor.TextEditor;
import com.fr.general.Inter;
import java.util.ArrayList;
import java.util.List;
import javax.swing.AbstractCellEditor;
import javax.swing.JTable;
import javax.swing.event.ChangeListener;
import javax.swing.table.TableCellEditor;

public class MapReportEditor extends AbstractCellEditor
    implements TableCellEditor
{

    private ValueEditorPane cellEditor;
    private List list;

    public MapReportEditor()
    {
        list = new ArrayList();
    }

    public java.awt.Component getTableCellEditorComponent(JTable jtable, Object obj, boolean flag, int i, int j)
    {
        cellEditor = createPane();
        cellEditor.populate(obj != null ? obj : "");
        return cellEditor;
    }

    public Object getCellEditorValue()
    {
        return cellEditor.update();
    }

    public void addChangeListener(ChangeListener changelistener)
    {
        list.add(changelistener);
    }

    private ValueEditorPane createPane()
    {
        TextEditor texteditor = new TextEditor();
        initListeners(texteditor);
        FormulaEditor formulaeditor = new FormulaEditor(Inter.getLocText("Parameter-Formula"));
        initListeners(formulaeditor);
        Editor aeditor[] = {
            texteditor, formulaeditor
        };
        cellEditor = ValueEditorPaneFactory.createValueEditorPane(aeditor);
        return cellEditor;
    }

    private void initListeners(Editor editor)
    {
        for(int i = 0; i < list.size(); i++)
            editor.addChangeListener((ChangeListener)list.get(i));

    }
}
