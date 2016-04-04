// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.actions.report;

import com.fr.base.BaseUtils;
import com.fr.design.actions.ReportComponentAction;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.mainframe.WorkSheetDesigner;
import com.fr.design.menu.KeySetUtils;
import com.fr.design.menu.MenuKeySet;
import com.fr.design.report.ReportColumnsPane;
import com.fr.report.worksheet.WorkSheet;

public class ReportColumnsAction extends ReportComponentAction
{

    public ReportColumnsAction(WorkSheetDesigner worksheetdesigner)
    {
        super(worksheetdesigner);
        setMenuKeySet(KeySetUtils.REPORT_COLUMN);
        setName((new StringBuilder()).append(getMenuKeySet().getMenuKeySetName()).append("...").toString());
        setMnemonic(getMenuKeySet().getMnemonic());
        setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/m_report/linearAttr.png"));
    }

    public boolean executeActionReturnUndoRecordNeeded()
    {
        final WorkSheetDesigner jws = (WorkSheetDesigner)getEditingComponent();
        if(jws == null)
        {
            return false;
        } else
        {
            final WorkSheet workSheet = (WorkSheet)jws.getTemplateReport();
            final ReportColumnsPane cPane = new ReportColumnsPane();
            cPane.populate(workSheet);
            cPane.showWindow(DesignerContext.getDesignerFrame(), new DialogActionAdapter() {

                final ReportColumnsPane val$cPane;
                final WorkSheet val$workSheet;
                final WorkSheetDesigner val$jws;
                final ReportColumnsAction this$0;

                public void doOk()
                {
                    cPane.update(workSheet);
                    jws.fireTargetModified();
                }

            
            {
                this$0 = ReportColumnsAction.this;
                cPane = reportcolumnspane;
                workSheet = worksheet;
                jws = worksheetdesigner;
                super();
            }
            }
).setVisible(true);
            return false;
        }
    }
}
