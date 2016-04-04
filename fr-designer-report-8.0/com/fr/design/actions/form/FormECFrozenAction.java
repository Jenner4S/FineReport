// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.actions.form;

import com.fr.base.BaseUtils;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.mainframe.form.FormElementCasePaneDelegate;
import com.fr.design.menu.KeySetUtils;
import com.fr.design.menu.MenuKeySet;
import com.fr.design.report.freeze.FormECRepeatAndFreezeSettingPane;
import com.fr.report.worksheet.FormElementCase;
import javax.swing.SwingUtilities;

// Referenced classes of package com.fr.design.actions.form:
//            AbastractFormECAction

public class FormECFrozenAction extends AbastractFormECAction
{

    private boolean returnValue;

    public FormECFrozenAction(FormElementCasePaneDelegate formelementcasepanedelegate)
    {
        super(formelementcasepanedelegate);
        setMenuKeySet(KeySetUtils.EC_FROZEN);
        setName((new StringBuilder()).append(getMenuKeySet().getMenuKeySetName()).append("...").toString());
        setMnemonic(getMenuKeySet().getMnemonic());
        setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/form/toolbar/ec_frozen.png"));
    }

    public boolean executeActionReturnUndoRecordNeeded()
    {
        FormElementCasePaneDelegate formelementcasepanedelegate = (FormElementCasePaneDelegate)getEditingComponent();
        if(formelementcasepanedelegate == null)
        {
            return false;
        } else
        {
            final FormElementCase fc = (FormElementCase)formelementcasepanedelegate.getTarget();
            final FormECRepeatAndFreezeSettingPane attrPane = new FormECRepeatAndFreezeSettingPane();
            attrPane.populate(fc.getReportPageAttr());
            BasicDialog basicdialog = attrPane.showWindow(SwingUtilities.getWindowAncestor(formelementcasepanedelegate));
            basicdialog.addDialogActionListener(new DialogActionAdapter() {

                final FormElementCase val$fc;
                final FormECRepeatAndFreezeSettingPane val$attrPane;
                final FormECFrozenAction this$0;

                public void doOk()
                {
                    fc.setReportPageAttr(attrPane.update());
                    returnValue = true;
                }

            
            {
                this$0 = FormECFrozenAction.this;
                fc = formelementcase;
                attrPane = formecrepeatandfreezesettingpane;
                super();
            }
            }
);
            basicdialog.setVisible(true);
            return returnValue;
        }
    }

}
