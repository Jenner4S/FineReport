// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.actions.cell;

import com.fr.base.BaseUtils;
import com.fr.design.actions.ElementCaseAction;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.mainframe.ElementCasePane;
import com.fr.design.menu.KeySetUtils;
import com.fr.design.menu.MenuKeySet;
import com.fr.design.report.ReportStylePane;
import javax.swing.SwingUtilities;

public class FloatStyleAction extends ElementCaseAction
{

    boolean okreturn;

    public FloatStyleAction(ElementCasePane elementcasepane)
    {
        super(elementcasepane);
        okreturn = false;
        setMenuKeySet(KeySetUtils.GLOBAL_STYLE);
        setName(getMenuKeySet().getMenuKeySetName());
        setMnemonic(getMenuKeySet().getMnemonic());
        setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/m_format/cell.png"));
    }

    public boolean executeActionReturnUndoRecordNeeded()
    {
        final ElementCasePane reportPane = (ElementCasePane)getEditingComponent();
        if(reportPane == null)
        {
            return false;
        } else
        {
            final ReportStylePane reportStylePane = new ReportStylePane();
            BasicDialog basicdialog = reportStylePane.showWindow(SwingUtilities.getWindowAncestor(reportPane));
            reportStylePane.populate(reportPane);
            basicdialog.addDialogActionListener(new DialogActionAdapter() {

                final ReportStylePane val$reportStylePane;
                final ElementCasePane val$reportPane;
                final FloatStyleAction this$0;

                public void doOk()
                {
                    reportStylePane.update(reportPane);
                    okreturn = true;
                }

            
            {
                this$0 = FloatStyleAction.this;
                reportStylePane = reportstylepane;
                reportPane = elementcasepane;
                super();
            }
            }
);
            basicdialog.setVisible(true);
            return okreturn;
        }
    }
}
