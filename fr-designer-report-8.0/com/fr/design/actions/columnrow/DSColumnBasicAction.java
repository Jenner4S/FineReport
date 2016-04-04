// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.actions.columnrow;

import com.fr.base.BaseUtils;
import com.fr.design.actions.CellSelectionAction;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.dscolumn.DSColumnBasicPane;
import com.fr.design.mainframe.ElementCasePane;
import com.fr.general.Inter;
import com.fr.grid.selection.CellSelection;
import com.fr.report.cell.DefaultTemplateCellElement;
import com.fr.report.cell.TemplateCellElement;
import com.fr.report.core.SheetUtils;
import com.fr.report.elementcase.TemplateElementCase;
import javax.swing.SwingUtilities;

public class DSColumnBasicAction extends CellSelectionAction
{
    public static class CellElementDialogActionListenr extends DialogActionAdapter
    {

        TemplateCellElement editCellElement;

        public CellElementDialogActionListenr(TemplateCellElement templatecellelement)
        {
            editCellElement = templatecellelement;
        }
    }


    public DSColumnBasicAction(ElementCasePane elementcasepane)
    {
        super(elementcasepane);
        setName(Inter.getLocText("Basic"));
        setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/expand/cellAttr.gif"));
    }

    protected boolean executeActionReturnUndoRecordNeededWithCellSelection(CellSelection cellselection)
    {
        ElementCasePane elementcasepane = (ElementCasePane)getEditingComponent();
        final DSColumnBasicPane dsColumnBasicPane = new DSColumnBasicPane();
        BasicDialog basicdialog = dsColumnBasicPane.showWindow(SwingUtilities.getWindowAncestor(elementcasepane));
        final TemplateCellElement final_templatecellelement = null;
        final TemplateElementCase report = elementcasepane.getEditingElementCase();
        final_templatecellelement = report.getTemplateCellElement(cellselection.getColumn(), cellselection.getRow());
        dsColumnBasicPane.putElementcase((ElementCasePane)getEditingComponent());
        dsColumnBasicPane.putCellElement(final_templatecellelement);
        if(report != null)
            SheetUtils.calculateDefaultParent(report);
        dsColumnBasicPane.populate(null, final_templatecellelement);
        final CellSelection finalCS = cellselection;
        basicdialog.addDialogActionListener(new CellElementDialogActionListenr(elementcasepane) {

            final CellSelection val$finalCS;
            final TemplateElementCase val$report;
            final DSColumnBasicPane val$dsColumnBasicPane;
            final ElementCasePane val$reportPane;
            final DSColumnBasicAction this$0;

            public void doOk()
            {
                for(int i = 0; i < finalCS.getRowSpan(); i++)
                {
                    for(int j = 0; j < finalCS.getColumnSpan(); j++)
                    {
                        int k = j + finalCS.getColumn();
                        int l = i + finalCS.getRow();
                        editCellElement = report.getTemplateCellElement(k, l);
                        if(editCellElement == null)
                        {
                            editCellElement = new DefaultTemplateCellElement(k, l);
                            report.addCellElement(editCellElement);
                        }
                        dsColumnBasicPane.update(editCellElement);
                    }

                }

                reportPane.fireTargetModified();
            }

            
            {
                this$0 = DSColumnBasicAction.this;
                finalCS = cellselection;
                report = templateelementcase;
                dsColumnBasicPane = dscolumnbasicpane;
                reportPane = elementcasepane;
                super(final_templatecellelement);
            }
        }
);
        basicdialog.setVisible(true);
        return false;
    }
}
