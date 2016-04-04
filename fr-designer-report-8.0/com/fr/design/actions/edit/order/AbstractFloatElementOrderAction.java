// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.actions.edit.order;

import com.fr.design.actions.FloatSelectionAction;
import com.fr.design.mainframe.ElementCasePane;
import com.fr.grid.selection.FloatSelection;
import com.fr.report.cell.FloatElement;
import com.fr.report.elementcase.ElementCase;

public abstract class AbstractFloatElementOrderAction extends FloatSelectionAction
{

    protected AbstractFloatElementOrderAction(ElementCasePane elementcasepane)
    {
        super(elementcasepane);
    }

    public boolean executeActionReturnUndoRecordNeededWithFloatSelection(FloatSelection floatselection)
    {
        ElementCasePane elementcasepane = (ElementCasePane)getEditingComponent();
        com.fr.report.elementcase.TemplateElementCase templateelementcase = elementcasepane.getEditingElementCase();
        FloatElement floatelement = templateelementcase.getFloatElement(floatselection.getSelectedFloatName());
        orderWithSelectedFloatElement(templateelementcase, floatelement);
        return true;
    }

    public abstract void orderWithSelectedFloatElement(ElementCase elementcase, FloatElement floatelement);
}
