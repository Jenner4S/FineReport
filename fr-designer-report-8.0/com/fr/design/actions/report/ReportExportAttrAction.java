// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.actions.report;

import com.fr.base.BaseUtils;
import com.fr.design.actions.JWorkBookAction;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.mainframe.JWorkBook;
import com.fr.design.menu.KeySetUtils;
import com.fr.design.menu.MenuKeySet;
import com.fr.design.report.ReportExportAttrPane;
import com.fr.main.TemplateWorkBook;
import java.awt.event.ActionEvent;

public class ReportExportAttrAction extends JWorkBookAction
{

    public ReportExportAttrAction(JWorkBook jworkbook)
    {
        super(jworkbook);
        setMenuKeySet(KeySetUtils.REPORT_EXPORT_ATTR);
        setName((new StringBuilder()).append(getMenuKeySet().getMenuKeySetName()).append("...").toString());
        setMnemonic(getMenuKeySet().getMnemonic());
        setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/m_report/exportAttr.png"));
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
            final ReportExportAttrPane dialog = new ReportExportAttrPane();
            dialog.populate(wbTpl.getReportExportAttr());
            dialog.showWindow(DesignerContext.getDesignerFrame(), new DialogActionAdapter() {

                final TemplateWorkBook val$wbTpl;
                final ReportExportAttrPane val$dialog;
                final JWorkBook val$jwb;
                final ReportExportAttrAction this$0;

                public void doOk()
                {
                    wbTpl.setReportExportAttr(dialog.update());
                    jwb.fireTargetModified();
                }

            
            {
                this$0 = ReportExportAttrAction.this;
                wbTpl = templateworkbook;
                dialog = reportexportattrpane;
                jwb = jworkbook;
                super();
            }
            }
).setVisible(true);
            return;
        }
    }
}
