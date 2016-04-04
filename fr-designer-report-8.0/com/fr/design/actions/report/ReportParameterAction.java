// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.actions.report;

import com.fr.base.BaseUtils;
import com.fr.design.actions.JWorkBookAction;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.mainframe.JWorkBook;
import com.fr.design.menu.KeySetUtils;
import com.fr.design.menu.MenuKeySet;
import com.fr.design.parameter.ParameterArrayPane;
import com.fr.main.TemplateWorkBook;
import com.fr.main.parameter.ReportParameterAttr;
import java.awt.event.ActionEvent;
import javax.swing.SwingUtilities;

public class ReportParameterAction extends JWorkBookAction
{

    public ReportParameterAction(JWorkBook jworkbook)
    {
        super(jworkbook);
        setMenuKeySet(KeySetUtils.REPORT_PARAMETER_ATTR);
        setName((new StringBuilder()).append(getMenuKeySet().getMenuKeySetName()).append("...").toString());
        setMnemonic(getMenuKeySet().getMnemonic());
        setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/m_report/p.png"));
    }

    public void actionPerformed(ActionEvent actionevent)
    {
        final JWorkBook jwb = (JWorkBook)getEditingComponent();
        if(jwb == null)
        {
            return;
        } else
        {
            TemplateWorkBook templateworkbook = (TemplateWorkBook)jwb.getTarget();
            final ParameterArrayPane parameterArrayPane = new ParameterArrayPane();
            BasicDialog basicdialog = parameterArrayPane.showWindow(SwingUtilities.getWindowAncestor(jwb));
            basicdialog.setModal(true);
            final ReportParameterAttr copyReportParameterAttr = getParameter(templateworkbook);
            parameterArrayPane.populate(copyReportParameterAttr.getParameters());
            basicdialog.addDialogActionListener(new DialogActionAdapter() {

                final ReportParameterAttr val$copyReportParameterAttr;
                final ParameterArrayPane val$parameterArrayPane;
                final JWorkBook val$jwb;
                final ReportParameterAction this$0;

                public void doOk()
                {
                    copyReportParameterAttr.clearParameters();
                    com.fr.base.Parameter aparameter[] = parameterArrayPane.updateParameters();
                    for(int i = 0; i < aparameter.length; i++)
                        copyReportParameterAttr.addParameter(aparameter[i]);

                    jwb.fireTargetModified();
                    jwb.updateReportParameterAttr();
                    jwb.populateReportParameterAttr();
                }

            
            {
                this$0 = ReportParameterAction.this;
                copyReportParameterAttr = reportparameterattr;
                parameterArrayPane = parameterarraypane;
                jwb = jworkbook;
                super();
            }
            }
);
            basicdialog.setVisible(true);
            return;
        }
    }

    private ReportParameterAttr getParameter(TemplateWorkBook templateworkbook)
    {
        ReportParameterAttr reportparameterattr = templateworkbook.getReportParameterAttr();
        if(reportparameterattr == null)
        {
            reportparameterattr = new ReportParameterAttr();
            templateworkbook.setReportParameterAttr(reportparameterattr);
        }
        return reportparameterattr;
    }
}
