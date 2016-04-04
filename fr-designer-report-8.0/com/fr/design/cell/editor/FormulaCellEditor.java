// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.cell.editor;

import com.fr.base.Formula;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.formula.FormulaFactory;
import com.fr.design.formula.UIFormula;
import com.fr.design.mainframe.ElementCasePane;
import com.fr.grid.Grid;
import com.fr.report.cell.TemplateCellElement;
import java.awt.Component;
import javax.swing.SwingUtilities;

// Referenced classes of package com.fr.design.cell.editor:
//            AbstractCellEditor

public class FormulaCellEditor extends AbstractCellEditor
{

    private UIFormula formulaEditorPane;

    public FormulaCellEditor(ElementCasePane elementcasepane)
    {
        super(elementcasepane);
        formulaEditorPane = null;
    }

    public Object getCellEditorValue()
        throws Exception
    {
        Formula formula = formulaEditorPane.update();
        if(formula.getContent() != null && formula.getContent().trim().length() > 1)
            return formula;
        else
            return "";
    }

    public Component getCellEditorComponent(Grid grid, TemplateCellElement templatecellelement, int i)
    {
        Object obj = null;
        if(templatecellelement != null)
            obj = templatecellelement.getValue();
        if(obj == null || !(obj instanceof Formula))
            obj = new Formula("");
        formulaEditorPane = FormulaFactory.createFormulaPaneWhenReserveFormula();
        formulaEditorPane.populate((Formula)obj);
        return formulaEditorPane.showLargeWindow(SwingUtilities.getWindowAncestor(grid), new DialogActionAdapter() {

            final FormulaCellEditor this$0;

            public void doOk()
            {
                stopCellEditing();
            }

            public void doCancel()
            {
                cancelCellEditing();
            }

            
            {
                this$0 = FormulaCellEditor.this;
                super();
            }
        }
);
    }
}
