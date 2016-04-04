// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.actions.columnrow;

import com.fr.design.actions.CellSelectionAction;
import com.fr.design.mainframe.ElementCasePane;
import com.fr.general.Inter;
import com.fr.grid.selection.CellSelection;
import com.fr.page.ReportPageAttrProvider;
import com.fr.report.elementcase.ElementCase;

public class CancelRowAction extends CellSelectionAction
{

    public CancelRowAction(ElementCasePane elementcasepane)
    {
        super(elementcasepane);
        setName(Inter.getLocText("Cancel_Repeat_Attributes"));
    }

    protected boolean executeActionReturnUndoRecordNeededWithCellSelection(CellSelection cellselection)
    {
        ElementCasePane elementcasepane = (ElementCasePane)getEditingComponent();
        com.fr.report.elementcase.TemplateElementCase templateelementcase = elementcasepane.getEditingElementCase();
        int ai[] = cellselection.getSelectedRows();
        if(templateelementcase.getReportPageAttr() == null)
            return false;
        ReportPageAttrProvider reportpageattrprovider = templateelementcase.getReportPageAttr();
        int i = ai[0];
        if(i == reportpageattrprovider.getRepeatHeaderRowFrom() || i == reportpageattrprovider.getRepeatHeaderRowTo())
        {
            reportpageattrprovider.setRepeatHeaderRowFrom(-1);
            reportpageattrprovider.setRepeatHeaderRowTo(-1);
        }
        if(i == reportpageattrprovider.getRepeatFooterRowFrom() || i == reportpageattrprovider.getRepeatFooterRowTo())
        {
            reportpageattrprovider.setRepeatFooterRowFrom(-1);
            reportpageattrprovider.setRepeatFooterRowTo(-1);
        }
        return true;
    }
}
