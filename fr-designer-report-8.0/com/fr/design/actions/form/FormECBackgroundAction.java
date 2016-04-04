// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.actions.form;

import com.fr.base.BaseUtils;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.mainframe.form.FormElementCasePaneDelegate;
import com.fr.design.menu.KeySetUtils;
import com.fr.design.menu.MenuKeySet;
import com.fr.design.report.ReportBackgroundPane;

// Referenced classes of package com.fr.design.actions.form:
//            AbastractFormECAction

public class FormECBackgroundAction extends AbastractFormECAction
{

    public FormECBackgroundAction(FormElementCasePaneDelegate formelementcasepanedelegate)
    {
        super(formelementcasepanedelegate);
        setMenuKeySet(KeySetUtils.REPORT_BACKGROUND);
        setName((new StringBuilder()).append(getMenuKeySet().getMenuKeySetName()).append("...").toString());
        setMnemonic(getMenuKeySet().getMnemonic());
        setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/m_report/background.png"));
    }

    public boolean executeActionReturnUndoRecordNeeded()
    {
        final FormElementCasePaneDelegate elementCasePane = (FormElementCasePaneDelegate)getEditingComponent();
        if(elementCasePane == null)
        {
            return false;
        } else
        {
            final ReportBackgroundPane bPane = new ReportBackgroundPane();
            bPane.populate(elementCasePane.getReportSettings());
            bPane.showWindow(DesignerContext.getDesignerFrame(), new DialogActionAdapter() {

                final ReportBackgroundPane val$bPane;
                final FormElementCasePaneDelegate val$elementCasePane;
                final FormECBackgroundAction this$0;

                public void doOk()
                {
                    bPane.update(elementCasePane.getReportSettings());
                    elementCasePane.fireTargetModified();
                }

            
            {
                this$0 = FormECBackgroundAction.this;
                bPane = reportbackgroundpane;
                elementCasePane = formelementcasepanedelegate;
                super();
            }
            }
).setVisible(true);
            return false;
        }
    }
}
