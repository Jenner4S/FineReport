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
import com.fr.design.report.ReportColumnsPane;
import com.fr.report.stable.WorkSheetAttr;
import com.fr.report.worksheet.FormElementCase;

// Referenced classes of package com.fr.design.actions.form:
//            AbastractFormECAction

public class FormECColumnsAction extends AbastractFormECAction
{

    public FormECColumnsAction(FormElementCasePaneDelegate formelementcasepanedelegate)
    {
        super(formelementcasepanedelegate);
        setMenuKeySet(KeySetUtils.EC_COLUMNS);
        setName((new StringBuilder()).append(getMenuKeySet().getMenuKeySetName()).append("...").toString());
        setMnemonic(getMenuKeySet().getMnemonic());
        setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/form/toolbar/ec_columns.png"));
    }

    public boolean executeActionReturnUndoRecordNeeded()
    {
        final FormElementCasePaneDelegate jws = (FormElementCasePaneDelegate)getEditingComponent();
        if(jws == null)
        {
            return false;
        } else
        {
            final FormElementCase elementCase = (FormElementCase)jws.getTarget();
            final ReportColumnsPane cPane = new ReportColumnsPane();
            WorkSheetAttr worksheetattr = elementCase.getWorkSheetAttr();
            int i = elementCase.getRowCount();
            int j = elementCase.getColumnCount();
            cPane.populate(worksheetattr, i, j);
            cPane.showWindow(DesignerContext.getDesignerFrame(), new DialogActionAdapter() {

                final FormElementCase val$elementCase;
                final ReportColumnsPane val$cPane;
                final FormElementCasePaneDelegate val$jws;
                final FormECColumnsAction this$0;

                public void doOk()
                {
                    WorkSheetAttr worksheetattr1 = new WorkSheetAttr();
                    elementCase.setWorkSheetAttr(worksheetattr1);
                    cPane.update(worksheetattr1);
                    jws.fireTargetModified();
                }

            
            {
                this$0 = FormECColumnsAction.this;
                elementCase = formelementcase;
                cPane = reportcolumnspane;
                jws = formelementcasepanedelegate;
                super();
            }
            }
).setVisible(true);
            return false;
        }
    }
}
