// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.actions.report;

import com.fr.base.BaseUtils;
import com.fr.design.actions.ReportComponentAction;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.mainframe.*;
import com.fr.design.menu.KeySetUtils;
import com.fr.design.menu.MenuKeySet;
import com.fr.design.webattr.ReportWriteAttrPane;
import com.fr.report.worksheet.WorkSheet;

public class ReportWriteAttrAction extends ReportComponentAction
{

    private boolean isChange;
    private boolean hasActionPerformed;

    public ReportWriteAttrAction(WorkSheetDesigner worksheetdesigner)
    {
        super(worksheetdesigner);
        setMenuKeySet(KeySetUtils.REPORT_WRITE);
        setName((new StringBuilder()).append(getMenuKeySet().getMenuKeySetName()).append("...").toString());
        setMnemonic(getMenuKeySet().getMnemonic());
        setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/m_report/reportWriteAttr.png"));
    }

    public boolean executeActionReturnUndoRecordNeeded()
    {
        hasActionPerformed = false;
        WorkSheetDesigner worksheetdesigner = (WorkSheetDesigner)getEditingComponent();
        if(worksheetdesigner == null)
        {
            return false;
        } else
        {
            final WorkSheet tplEC = (WorkSheet)worksheetdesigner.getTemplateReport();
            final ReportWriteAttrPane reportWritePane = new ReportWriteAttrPane(tplEC) {

                final WorkSheet val$tplEC;
                final ReportWriteAttrAction this$0;

                public void complete()
                {
                    populate(tplEC);
                }

            
            {
                this$0 = ReportWriteAttrAction.this;
                tplEC = worksheet;
                super(final_elementcasepane);
            }
            }
;
            BasicDialog basicdialog = reportWritePane.showWindow(DesignerContext.getDesignerFrame(), new DialogActionAdapter() {

                final WorkSheet val$tplEC;
                final ReportWriteAttrPane val$reportWritePane;
                final ReportWriteAttrAction this$0;

                public void doOk()
                {
                    isChange = true;
                    tplEC.setReportWriteAttr(reportWritePane.update());
                    if(hasActionPerformed)
                    {
                        JTemplate jtemplate = DesignerContext.getDesignerFrame().getSelectedJTemplate();
                        if(jtemplate != null)
                        {
                            jtemplate.fireTargetModified();
                            jtemplate.requestFocus();
                        }
                    }
                }

                public void doCancel()
                {
                    isChange = false;
                }

            
            {
                this$0 = ReportWriteAttrAction.this;
                tplEC = worksheet;
                reportWritePane = reportwriteattrpane;
                super();
            }
            }
);
            DesignerContext.setReportWritePane(basicdialog);
            basicdialog.setVisible(true);
            hasActionPerformed = true;
            return isChange;
        }
    }


}
