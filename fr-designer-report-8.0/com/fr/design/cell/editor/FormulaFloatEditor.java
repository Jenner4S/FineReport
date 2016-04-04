// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.cell.editor;

import com.fr.base.Formula;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.formula.FormulaFactory;
import com.fr.design.formula.UIFormula;
import com.fr.grid.Grid;
import com.fr.report.cell.FloatElement;
import java.awt.Component;
import javax.swing.SwingUtilities;

// Referenced classes of package com.fr.design.cell.editor:
//            AbstractFloatEditor

public class FormulaFloatEditor extends AbstractFloatEditor
{

    private UIFormula formulaEditorPane;

    public FormulaFloatEditor()
    {
        formulaEditorPane = null;
    }

    public Object getFloatEditorValue()
        throws Exception
    {
        Formula formula = formulaEditorPane.update();
        if(formula.getContent() != null && formula.getContent().trim().length() > 1)
            return formula;
        else
            return "";
    }

    public Component getFloatEditorComponent(Grid grid, FloatElement floatelement, int i)
    {
        Object obj = floatelement.getValue();
        if(obj == null || !(obj instanceof Formula))
            obj = new Formula("");
        formulaEditorPane = FormulaFactory.createFormulaPane();
        formulaEditorPane.populate((Formula)obj);
        return formulaEditorPane.showLargeWindow(SwingUtilities.getWindowAncestor(grid), new DialogActionAdapter() {

            final FormulaFloatEditor this$0;

            public void doOk()
            {
                stopFloatEditing();
            }

            public void doCancel()
            {
                cancelFloatEditing();
            }

            
            {
                this$0 = FormulaFloatEditor.this;
                super();
            }
        }
);
    }
}
