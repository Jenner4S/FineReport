// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.quickeditor;

import com.fr.design.mainframe.ElementCasePane;
import com.fr.design.selection.QuickEditor;
import com.fr.grid.selection.FloatSelection;
import com.fr.report.cell.FloatElement;
import com.fr.report.elementcase.TemplateElementCase;

public abstract class FloatQuickEditor extends QuickEditor
{

    protected FloatElement floatElement;

    public FloatQuickEditor()
    {
    }

    protected void refresh()
    {
        FloatSelection floatselection = (FloatSelection)((ElementCasePane)tc).getSelection();
        floatElement = ((ElementCasePane)tc).getEditingElementCase().getFloatElement(floatselection.getSelectedFloatName());
        refreshDetails();
    }

    protected abstract void refreshDetails();
}
