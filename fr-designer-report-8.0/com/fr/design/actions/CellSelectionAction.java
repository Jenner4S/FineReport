// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.actions;

import com.fr.design.mainframe.ElementCasePane;
import com.fr.grid.selection.CellSelection;
import com.fr.grid.selection.Selection;

// Referenced classes of package com.fr.design.actions:
//            ElementCaseAction

public abstract class CellSelectionAction extends ElementCaseAction
{

    protected CellSelectionAction(ElementCasePane elementcasepane)
    {
        super(elementcasepane);
    }

    public boolean executeActionReturnUndoRecordNeeded()
    {
        ElementCasePane elementcasepane = (ElementCasePane)getEditingComponent();
        Selection selection = elementcasepane.getSelection();
        if(selection instanceof CellSelection)
            return executeActionReturnUndoRecordNeededWithCellSelection((CellSelection)selection);
        else
            return false;
    }

    protected abstract boolean executeActionReturnUndoRecordNeededWithCellSelection(CellSelection cellselection);

    public void update()
    {
        super.update();
        if(isEnabled())
            setEnabled(((ElementCasePane)getEditingComponent()).getSelection() instanceof CellSelection);
    }
}
