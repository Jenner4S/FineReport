// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.widget.accessibles;

import com.fr.base.Formula;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.formula.FormulaFactory;
import com.fr.design.formula.UIFormula;
import com.fr.design.mainframe.widget.wrappers.FormulaWrapper;
import javax.swing.SwingUtilities;

// Referenced classes of package com.fr.design.mainframe.widget.accessibles:
//            BaseAccessibleEditor

public class AccessibleFormulaEditor extends BaseAccessibleEditor
{

    private UIFormula formulaPane;

    public AccessibleFormulaEditor()
    {
        super(new FormulaWrapper(), new FormulaWrapper(), true);
    }

    protected void showEditorPane()
    {
        if(formulaPane == null)
            formulaPane = FormulaFactory.createFormulaPane();
        BasicDialog basicdialog = formulaPane.showLargeWindow(SwingUtilities.getWindowAncestor(this), new DialogActionAdapter() {

            final AccessibleFormulaEditor this$0;

            public void doOk()
            {
                setValue(formulaPane.update());
                fireStateChanged();
            }

            
            {
                this$0 = AccessibleFormulaEditor.this;
                super();
            }
        }
);
        formulaPane.populate((Formula)getValue());
        basicdialog.setVisible(true);
    }

}
