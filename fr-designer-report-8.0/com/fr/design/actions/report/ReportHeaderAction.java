// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.actions.report;

import com.fr.base.BaseUtils;
import com.fr.base.FRContext;
import com.fr.design.actions.ReportComponentAction;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.headerfooter.EditHeaderPane;
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

public class ReportHeaderAction extends ReportComponentAction
{

    private boolean returnVal;

    public ReportHeaderAction(ReportComponent reportcomponent)
    {
        super(reportcomponent);
        returnVal = false;
        setMenuKeySet(KeySetUtils.REPORT_HEADER);
        setName((new StringBuilder()).append(getMenuKeySet().getMenuKeySetName()).append("...").toString());
        setMnemonic(getMenuKeySet().getMnemonic());
        setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/m_report/header.png"));
    }

    public boolean executeActionReturnUndoRecordNeeded()
    {
        final ReportComponent reportPane = (ReportComponent)getEditingComponent();
        if(reportPane == null)
            return false;
        final TemplateReport report = reportPane.getTemplateReport();
        final EditHeaderPane headerEditDialog = new EditHeaderPane();
        final ReportSettingsProvider set = ReportUtils.getReportSettings(report);
        Hashtable hashtable = new Hashtable();
        for(int i = 0; i != ReportConstants.PAGE_INFO.length; i++)
            cloneReportHFHashHeadtable(hashtable, report, ReportConstants.PAGE_INFO[i]);

        headerEditDialog.populate(set);
        headerEditDialog.populate(hashtable);
        headerEditDialog.showWindow(SwingUtilities.getWindowAncestor(reportPane), new DialogActionAdapter() {

            final EditHeaderPane val$headerEditDialog;
            final TemplateReport val$report;
            final ReportSettingsProvider val$set;
            final ReportComponent val$reportPane;
            final ReportHeaderAction this$0;

            public void doOk()
            {
                Hashtable hashtable1 = headerEditDialog.update();
                report.setHeader(0, (ReportHF)hashtable1.get(new Integer(0)));
                report.setHeader(1, (ReportHF)hashtable1.get(new Integer(1)));
                report.setHeader(2, (ReportHF)hashtable1.get(new Integer(2)));
                report.setHeader(3, (ReportHF)hashtable1.get(new Integer(3)));
                report.setHeader(4, (ReportHF)hashtable1.get(new Integer(4)));
                set.setHeaderHeight(headerEditDialog.updateReportSettings());
                returnVal = true;
                reportPane.fireTargetModified();
            }

            
            {
                this$0 = ReportHeaderAction.this;
                headerEditDialog = editheaderpane;
                report = templatereport;
                set = reportsettingsprovider;
                reportPane = reportcomponent;
                super();
            }
        }
).setVisible(true);
        return returnVal;
    }

    private void cloneReportHFHashHeadtable(Hashtable hashtable, Report report, int i)
    {
        if(report.getHeader(i) != null)
            try
            {
                hashtable.put(new Integer(i), report.getHeader(i).clone());
            }
            catch(Exception exception)
            {
                FRContext.getLogger().error(exception.getMessage(), exception);
            }
    }

}
