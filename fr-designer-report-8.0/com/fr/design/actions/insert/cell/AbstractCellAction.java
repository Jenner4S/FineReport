// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.actions.insert.cell;

import com.fr.design.actions.CellSelectionAction;
import com.fr.design.mainframe.ElementCasePane;
import com.fr.grid.Grid;
import com.fr.grid.selection.CellSelection;
import com.fr.report.core.SheetUtils;
import com.fr.report.elementcase.TemplateElementCase;

// Referenced classes of package com.fr.design.actions.insert.cell:
//            DSColumnCellAction

public abstract class AbstractCellAction extends CellSelectionAction
{

    protected AbstractCellAction(ElementCasePane elementcasepane)
    {
        super(elementcasepane);
    }

    public abstract Class getCellValueClass();

    protected boolean executeActionReturnUndoRecordNeededWithCellSelection(CellSelection cellselection)
    {
        ElementCasePane elementcasepane = (ElementCasePane)getEditingComponent();
        TemplateElementCase templateelementcase = elementcasepane.getEditingElementCase();
        if(templateelementcase != null && (this instanceof DSColumnCellAction))
            SheetUtils.calculateDefaultParent(templateelementcase);
        return elementcasepane.getGrid().startCellEditingAt_DEC(cellselection.getColumn(), cellselection.getRow(), getCellValueClass(), false);
    }

    public void update()
    {
        super.update();
        ElementCasePane elementcasepane = (ElementCasePane)getEditingComponent();
        setEnabled(elementcasepane.isSelectedOneCell());
    }
}
