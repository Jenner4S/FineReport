// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.actions.cell;

import com.fr.design.actions.CellSelectionAction;
import com.fr.design.dialog.*;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.mainframe.ElementCasePane;
import com.fr.grid.GridUtils;
import com.fr.grid.selection.CellSelection;
import com.fr.report.cell.DefaultTemplateCellElement;
import com.fr.report.cell.TemplateCellElement;
import com.fr.report.core.SheetUtils;
import com.fr.report.elementcase.TemplateElementCase;

public abstract class AbstractCellElementAction extends CellSelectionAction
{

    protected AbstractCellElementAction(ElementCasePane elementcasepane)
    {
        super(elementcasepane);
    }

    protected boolean executeActionReturnUndoRecordNeededWithCellSelection(CellSelection cellselection)
    {
        final ElementCasePane ePane = (ElementCasePane)getEditingComponent();
        final TemplateElementCase tplEC = ePane.getEditingElementCase();
        Object obj = tplEC.getTemplateCellElement(cellselection.getColumn(), cellselection.getRow());
        if(obj == null)
        {
            obj = new DefaultTemplateCellElement(cellselection.getColumn(), cellselection.getRow());
            tplEC.addCellElement(((TemplateCellElement) (obj)));
        }
        if(tplEC != null)
            SheetUtils.calculateDefaultParent(tplEC);
        final CellSelection finalCS = cellselection;
        final BasicPane bp = populateBasicPane(((TemplateCellElement) (obj)));
        BasicDialog basicdialog = bp.showWindow(DesignerContext.getDesignerFrame());
        basicdialog.addDialogActionListener(new DialogActionAdapter() {

            final CellSelection val$finalCS;
            final TemplateElementCase val$tplEC;
            final BasicPane val$bp;
            final ElementCasePane val$ePane;
            final AbstractCellElementAction this$0;

            public void doOk()
            {
                for(int i = 0; i < finalCS.getRowSpan(); i++)
                {
                    for(int j = 0; j < finalCS.getColumnSpan(); j++)
                    {
                        int k = j + finalCS.getColumn();
                        int l = i + finalCS.getRow();
                        Object obj1 = tplEC.getTemplateCellElement(k, l);
                        if(obj1 == null)
                        {
                            obj1 = new DefaultTemplateCellElement(k, l);
                            tplEC.addCellElement(((TemplateCellElement) (obj1)));
                        }
                        if(((TemplateCellElement) (obj1)).getColumn() != k || ((TemplateCellElement) (obj1)).getRow() != l)
                            continue;
                        updateBasicPane(bp, ((TemplateCellElement) (obj1)));
                        if(!isNeedShinkToFit())
                            continue;
                        Object obj2 = ((TemplateCellElement) (obj1)).getValue();
                        if(obj2 != null && ((obj2 instanceof String) || (obj2 instanceof Number)))
                            GridUtils.shrinkToFit(3, tplEC, ((TemplateCellElement) (obj1)));
                    }

                }

                ePane.fireTargetModified();
            }

            
            {
                this$0 = AbstractCellElementAction.this;
                finalCS = cellselection;
                tplEC = templateelementcase;
                bp = basicpane;
                ePane = elementcasepane;
                super();
            }
        }
);
        DesignerContext.setReportWritePane(basicdialog);
        basicdialog.setVisible(true);
        return false;
    }

    protected abstract BasicPane populateBasicPane(TemplateCellElement templatecellelement);

    protected abstract void updateBasicPane(BasicPane basicpane, TemplateCellElement templatecellelement);

    protected boolean isNeedShinkToFit()
    {
        return false;
    }
}
