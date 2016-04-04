// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.actions.columnrow;

import com.fr.design.actions.CellSelectionAction;
import com.fr.design.mainframe.ElementCasePane;
import com.fr.grid.selection.CellSelection;
import com.fr.page.ReportPageAttrProvider;
import com.fr.report.elementcase.ElementCase;
import com.fr.stable.bridge.StableFactory;

public abstract class ColumnRowRepeatAction extends CellSelectionAction
{

    public ColumnRowRepeatAction(ElementCasePane elementcasepane)
    {
        super(elementcasepane);
    }

    protected boolean executeActionReturnUndoRecordNeededWithCellSelection(CellSelection cellselection)
    {
        ElementCasePane elementcasepane = (ElementCasePane)getEditingComponent();
        com.fr.report.elementcase.TemplateElementCase templateelementcase = elementcasepane.getEditingElementCase();
        int ai[];
        if(isColumn())
            ai = cellselection.getSelectedColumns();
        else
            ai = cellselection.getSelectedRows();
        doReportPageAttrSet(isColumn(), isFoot(), templateelementcase, ai[0], ai[ai.length - 1]);
        return true;
    }

    protected abstract boolean isColumn();

    protected abstract boolean isFoot();

    private void doReportPageAttrSet(boolean flag, boolean flag1, ElementCase elementcase, int i, int j)
    {
        ReportPageAttrProvider reportpageattrprovider = elementcase.getReportPageAttr();
        if(reportpageattrprovider == null)
        {
            reportpageattrprovider = (ReportPageAttrProvider)StableFactory.createXmlObject("ReportPageAttr");
            elementcase.setReportPageAttr(reportpageattrprovider);
        }
        reportpageattrprovider.setRepeatFromTo(flag1, flag, i, j);
    }
}
