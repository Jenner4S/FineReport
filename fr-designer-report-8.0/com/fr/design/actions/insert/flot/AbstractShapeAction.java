// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.actions.insert.flot;

import com.fr.design.actions.ElementCaseAction;
import com.fr.design.mainframe.ElementCasePane;
import com.fr.grid.Grid;
import com.fr.report.cell.FloatElement;

public abstract class AbstractShapeAction extends ElementCaseAction
{

    protected AbstractShapeAction(ElementCasePane elementcasepane)
    {
        super(elementcasepane);
    }

    public void startDraw(FloatElement floatelement)
    {
        ElementCasePane elementcasepane = (ElementCasePane)getEditingComponent();
        if(elementcasepane == null)
        {
            return;
        } else
        {
            elementcasepane.getGrid().setDrawingFloatElement(floatelement);
            return;
        }
    }

    public boolean executeActionReturnUndoRecordNeeded()
    {
        return false;
    }
}
