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

public class CancelColumnAction extends CellSelectionAction
{

    public CancelColumnAction(ElementCasePane elementcasepane)
    {
        super(elementcasepane);
        setName(Inter.getLocText("Cancel_Repeat_Attributes"));
    }

    protected boolean executeActionReturnUndoRecordNeededWithCellSelection(CellSelection cellselection)
    {
        ElementCasePane elementcasepane = (ElementCasePane)getEditingComponent();
        com.fr.report.elementcase.TemplateElementCase templateelementcase = elementcasepane.getEditingElementCase();
        if(templateelementcase.getReportPageAttr() == null)
            return false;
        int ai[] = cellselection.getSelectedColumns();
        ReportPageAttrProvider reportpageattrprovider = templateelementcase.getReportPageAttr();
        int i = ai[0];
        if(i == reportpageattrprovider.getRepeatHeaderColumnFrom() || i == reportpageattrprovider.getRepeatHeaderColumnTo())
        {
            reportpageattrprovider.setRepeatHeaderColumnFrom(-1);
            reportpageattrprovider.setRepeatHeaderColumnTo(-1);
        }
        if(i == reportpageattrprovider.getRepeatFooterColumnFrom() || i == reportpageattrprovider.getRepeatFooterColumnTo())
        {
            reportpageattrprovider.setRepeatFooterColumnFrom(-1);
            reportpageattrprovider.setRepeatFooterColumnTo(-1);
        }
        return true;
    }
}
