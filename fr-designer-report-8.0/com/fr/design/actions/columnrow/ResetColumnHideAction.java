// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.actions.columnrow;

import com.fr.design.mainframe.ElementCasePane;
import com.fr.general.Inter;
import com.fr.grid.selection.CellSelection;
import com.fr.grid.selection.Selection;
import com.fr.report.elementcase.ElementCase;
import com.fr.stable.unit.FU;
import com.fr.stable.unit.UNITConstants;

// Referenced classes of package com.fr.design.actions.columnrow:
//            AbstractColumnRowIndexAction

public class ResetColumnHideAction extends AbstractColumnRowIndexAction
{

    public ResetColumnHideAction(ElementCasePane elementcasepane, int i)
    {
        super(elementcasepane, i);
        setName(Inter.getLocText(new String[] {
            "MConfig-CancelButton", "Hide"
        }));
    }

    protected boolean executeActionReturnUndoRecordNeededWithCellSelection(CellSelection cellselection)
    {
        ElementCasePane elementcasepane = (ElementCasePane)getEditingComponent();
        com.fr.report.elementcase.TemplateElementCase templateelementcase = elementcasepane.getEditingElementCase();
        int ai[] = cellselection.getSelectedColumns();
        boolean flag = false;
        int ai1[] = ai;
        int i = ai1.length;
        for(int j = 0; j < i; j++)
        {
            int k = ai1[j];
            if(templateelementcase.getColumnWidth(k).equal_zero())
            {
                templateelementcase.setColumnWidth(k, UNITConstants.DEFAULT_COL_WIDTH);
                flag = true;
            }
        }

        if(flag)
        {
            elementcasepane.repaint();
            elementcasepane.fireTargetModified();
        }
        return false;
    }

    public void update()
    {
        ElementCasePane elementcasepane = (ElementCasePane)getEditingComponent();
        com.fr.report.elementcase.TemplateElementCase templateelementcase = elementcasepane.getEditingElementCase();
        Selection selection = elementcasepane.getSelection();
        if(selection instanceof CellSelection)
        {
            CellSelection cellselection = (CellSelection)selection;
            int ai[] = cellselection.getSelectedRows();
            for(int i = 0; i < ai.length; i++)
                if(templateelementcase.getColumnWidth(i).equal_zero())
                {
                    setEnabled(true);
                    return;
                }

        }
        setEnabled(false);
    }
}
