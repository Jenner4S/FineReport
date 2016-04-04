// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.actions.report;

import com.fr.base.BaseUtils;
import com.fr.base.FRContext;
import com.fr.design.actions.ReportComponentAction;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.headerfooter.EditFooterPane;
import com.fr.design.mainframe.ReportComponent;
import com.fr.design.menu.KeySetUtils;
import com.fr.design.menu.MenuKeySet;
import com.fr.general.FRLogger;
import com.fr.page.ReportHFProvider;
import com.fr.page.ReportSettingsProvider;
import com.fr.report.core.ReportHF;
import com.fr.report.core.ReportUtils;
import com.fr.report.report.Report;
import com.fr.report.report.TemplateReport;
import com.fr.report.stable.ReportConstants;
import java.util.Hashtable;
import javax.swing.SwingUtilities;

public class ReportFooterAction extends ReportComponentAction
{

    public ReportFooterAction(ReportComponent reportcomponent)
    {
        super(reportcomponent);
        setMenuKeySet(KeySetUtils.REPORT_FOOTER);
        setName((new StringBuilder()).append(getMenuKeySet().getMenuKeySetName()).append("...").toString());
        setMnemonic(getMenuKeySet().getMnemonic());
        setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/m_report/footer.png"));
    }

    public boolean executeActionReturnUndoRecordNeeded()
    {
        final ReportComponent reportPane = (ReportComponent)getEditingComponent();
        if(reportPane == null)
            return false;
        final TemplateReport report = reportPane.getTemplateReport();
        final EditFooterPane footerEditDialog = new EditFooterPane();
        Hashtable hashtable = new Hashtable();
        for(int i = 0; i != ReportConstants.PAGE_INFO.length; i++)
            cloneReportHFHashFoottable(hashtable, report, ReportConstants.PAGE_INFO[i]);

        final ReportSettingsProvider set = ReportUtils.getReportSettings(report);
        footerEditDialog.populate(set, false);
        footerEditDialog.populate(hashtable);
        footerEditDialog.showWindow(SwingUtilities.getWindowAncestor(reportPane), new DialogActionAdapter() {

            final EditFooterPane val$footerEditDialog;
            final TemplateReport val$report;
            final ReportSettingsProvider val$set;
            final ReportComponent val$reportPane;
            final ReportFooterAction this$0;

            public void doOk()
            {
                Hashtable hashtable1 = footerEditDialog.update();
                report.setFooter(0, (ReportHF)hashtable1.get(new Integer(0)));
                report.setFooter(1, (ReportHF)hashtable1.get(new Integer(1)));
                report.setFooter(2, (ReportHF)hashtable1.get(new Integer(2)));
                report.setFooter(3, (ReportHF)hashtable1.get(new Integer(3)));
                report.setFooter(4, (ReportHF)hashtable1.get(new Integer(4)));
                set.setFooterHeight(footerEditDialog.updateReportSettings());
                reportPane.fireTargetModified();
            }

            
            {
                this$0 = ReportFooterAction.this;
                footerEditDialog = editfooterpane;
                report = templatereport;
                set = reportsettingsprovider;
                reportPane = reportcomponent;
                super();
            }
        }
).setVisible(true);
        return false;
    }

    private void cloneReportHFHashFoottable(Hashtable hashtable, Report report, int i)
    {
        if(report.getFooter(i) != null)
            try
            {
                hashtable.put(new Integer(i), report.getFooter(i).clone());
            }
            catch(Exception exception)
            {
                FRContext.getLogger().error(exception.getMessage(), exception);
            }
    }
}
