// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.actions.columnrow;

import com.fr.design.mainframe.ElementCasePane;
import com.fr.general.Inter;
import com.fr.grid.selection.CellSelection;
import com.fr.report.elementcase.ElementCase;
import com.fr.stable.unit.UNIT;

// Referenced classes of package com.fr.design.actions.columnrow:
//            AbstractColumnRowIndexAction

public class ColumnHideAction extends AbstractColumnRowIndexAction
{

    public ColumnHideAction(ElementCasePane elementcasepane, int i)
    {
        super(elementcasepane, i);
        setName(Inter.getLocText("Hide"));
    }

    protected boolean executeActionReturnUndoRecordNeededWithCellSelection(CellSelection cellselection)
    {
        ElementCasePane elementcasepane = (ElementCasePane)getEditingComponent();
        com.fr.report.elementcase.TemplateElementCase templateelementcase = elementcasepane.getEditingElementCase();
        int ai[] = cellselection.getSelectedColumns();
        for(int i = 0; i < ai.length; i++)
            templateelementcase.setColumnWidth(ai[i], UNIT.ZERO);

        elementcasepane.fireTargetModified();
        elementcasepane.repaint();
        return false;
    }
}
