// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.chart.report;

import com.fr.design.editor.ValueEditorPane;
import com.fr.design.editor.ValueEditorPaneFactory;
import com.fr.design.editor.editor.*;
import com.fr.general.Inter;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class MapReportRenderer extends DefaultTableCellRenderer
{

    private ValueEditorPane cellEditor;

    public MapReportRenderer()
    {
        Editor aeditor[] = {
            new TextEditor(), new FormulaEditor(Inter.getLocText("Parameter-Formula"))
        };
        cellEditor = ValueEditorPaneFactory.createValueEditorPane(aeditor);
    }

    public Component getTableCellRendererComponent(JTable jtable, Object obj, boolean flag, boolean flag1, int i, int j)
    {
        cellEditor.populate(obj != null ? obj : "");
        return cellEditor;
    }
}
