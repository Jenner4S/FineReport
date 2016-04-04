// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.actions.report;

import com.fr.base.BaseUtils;
import com.fr.design.DesignerEnvManager;
import com.fr.design.actions.ReportComponentAction;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.mainframe.ReportComponent;
import com.fr.design.menu.KeySetUtils;
import com.fr.design.menu.MenuKeySet;
import com.fr.design.report.PageSetupPane;
import com.fr.report.report.TemplateReport;
import javax.swing.SwingUtilities;

public class ReportPageSetupAction extends ReportComponentAction
{

    private boolean returnValue;

    public ReportPageSetupAction(ReportComponent reportcomponent)
    {
        super(reportcomponent);
        setMenuKeySet(KeySetUtils.REPORT_PAGE_SETUP);
        setName((new StringBuilder()).append(getMenuKeySet().getMenuKeySetName()).append("...").toString());
        setMnemonic(getMenuKeySet().getMnemonic());
        setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/m_file/pageSetup.png"));
    }

    public boolean executeActionReturnUndoRecordNeeded()
    {
        ReportComponent reportcomponent = (ReportComponent)getEditingComponent();
        if(reportcomponent == null)
        {
            return false;
        } else
        {
            final TemplateReport report = reportcomponent.getTemplateReport();
            final PageSetupPane pageSetupPane = new PageSetupPane();
            pageSetupPane.populate(report, DesignerEnvManager.getEnvManager().getPageLengthUnit());
            BasicDialog basicdialog = pageSetupPane.showWindow(SwingUtilities.getWindowAncestor(reportcomponent));
            basicdialog.addDialogActionListener(new DialogActionAdapter() {

                final PageSetupPane val$pageSetupPane;
                final TemplateReport val$report;
                final ReportPageSetupAction this$0;

                public void doOk()
                {
                    pageSetupPane.update(report);
                    returnValue = true;
                }

                public void doCancel()
                {
                    returnValue = false;
                }

            
            {
                this$0 = ReportPageSetupAction.this;
                pageSetupPane = pagesetuppane;
                report = templatereport;
                super();
            }
            }
);
            basicdialog.setVisible(true);
            return returnValue;
        }
    }

}
