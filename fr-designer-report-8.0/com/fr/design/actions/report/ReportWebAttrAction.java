// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.actions.report;

import com.fr.base.*;
import com.fr.design.actions.JWorkBookAction;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.mainframe.JWorkBook;
import com.fr.design.menu.KeySetUtils;
import com.fr.design.menu.MenuKeySet;
import com.fr.design.webattr.ReportWebAttrPane;
import com.fr.general.FRLogger;
import com.fr.main.TemplateWorkBook;
import java.awt.event.ActionEvent;

public class ReportWebAttrAction extends JWorkBookAction
{

    public ReportWebAttrAction(JWorkBook jworkbook)
    {
        super(jworkbook);
        setMenuKeySet(KeySetUtils.REPORT_WEB_ATTR);
        setName((new StringBuilder()).append(getMenuKeySet().getMenuKeySetName()).append("...").toString());
        setMnemonic(getMenuKeySet().getMnemonic());
        setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/m_report/webreportattribute.png"));
    }

    public void actionPerformed(ActionEvent actionevent)
    {
        final JWorkBook jwb = (JWorkBook)getEditingComponent();
        if(jwb == null)
        {
            return;
        } else
        {
            final TemplateWorkBook wbTpl = (TemplateWorkBook)jwb.getTarget();
            final ReportWebAttrPane reportWebAttrPane = new ReportWebAttrPane() {

                final TemplateWorkBook val$wbTpl;
                final ReportWebAttrAction this$0;

                public void complete()
                {
                    populate(wbTpl.getReportWebAttr());
                }

            
            {
                this$0 = ReportWebAttrAction.this;
                wbTpl = templateworkbook;
                super();
            }
            }
;
            BasicDialog basicdialog = reportWebAttrPane.showWindow(DesignerContext.getDesignerFrame());
            basicdialog.addDialogActionListener(new DialogActionAdapter() {

                final TemplateWorkBook val$wbTpl;
                final ReportWebAttrPane val$reportWebAttrPane;
                final JWorkBook val$jwb;
                final ReportWebAttrAction this$0;

                public void doOk()
                {
                    wbTpl.setReportWebAttr(reportWebAttrPane.update());
                    ConfigManagerProvider configmanagerprovider = ConfigManager.getProviderInstance();
                    Env env = FRContext.getCurrentEnv();
                    try
                    {
                        env.writeResource(configmanagerprovider);
                    }
                    catch(Exception exception)
                    {
                        FRContext.getLogger().error(exception.getMessage(), exception);
                    }
                    jwb.fireTargetModified();
                }

            
            {
                this$0 = ReportWebAttrAction.this;
                wbTpl = templateworkbook;
                reportWebAttrPane = reportwebattrpane;
                jwb = jworkbook;
                super();
            }
            }
);
            basicdialog.setVisible(true);
            return;
        }
    }
}
