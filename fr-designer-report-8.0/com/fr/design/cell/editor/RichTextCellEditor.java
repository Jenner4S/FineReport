// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.cell.editor;

import com.fr.design.dialog.*;
import com.fr.design.mainframe.ElementCasePane;
import com.fr.design.report.RichTextPane;
import com.fr.grid.Grid;
import com.fr.report.cell.CellElement;
import com.fr.report.cell.TemplateCellElement;
import com.fr.report.cell.cellattr.CellGUIAttr;
import com.fr.report.core.SheetUtils;
import com.fr.report.elementcase.TemplateElementCase;
import com.fr.report.poly.PolyECBlock;
import com.fr.report.worksheet.WorkSheet;
import java.awt.Component;
import javax.swing.SwingUtilities;

// Referenced classes of package com.fr.design.cell.editor:
//            AbstractCellEditor

public class RichTextCellEditor extends AbstractCellEditor
    implements DialogActionListener
{

    private RichTextPane richTextPane;

    public RichTextCellEditor(ElementCasePane elementcasepane)
    {
        super(elementcasepane);
    }

    public Component getCellEditorComponent(Grid grid, TemplateCellElement templatecellelement, int i)
    {
        TemplateElementCase templateelementcase = grid.getElementCasePane().getEditingElementCase();
        calculateParent(templateelementcase);
        richTextPane = new RichTextPane();
        BasicDialog basicdialog = richTextPane.showMediumWindow(SwingUtilities.getWindowAncestor(grid), new DialogActionAdapter() {

            final RichTextCellEditor this$0;

            public void doOk()
            {
                fireEditingStopped();
            }

            public void doCancel()
            {
                fireEditingCanceled();
            }

            
            {
                this$0 = RichTextCellEditor.this;
                super();
            }
        }
);
        basicdialog.addDialogActionListener(this);
        richTextPane.populate(templateelementcase, templatecellelement);
        setShowAsHtml(templatecellelement);
        return basicdialog;
    }

    private void setShowAsHtml(CellElement cellelement)
    {
        CellGUIAttr cellguiattr = cellelement.getCellGUIAttr();
        if(cellguiattr == null)
        {
            cellguiattr = new CellGUIAttr();
            cellelement.setCellGUIAttr(cellguiattr);
        }
        cellguiattr.setShowAsHTML(true);
    }

    private void calculateParent(TemplateElementCase templateelementcase)
    {
        if(templateelementcase != null && ((templateelementcase instanceof WorkSheet) || (templateelementcase instanceof PolyECBlock)))
            SheetUtils.calculateDefaultParent(templateelementcase);
    }

    public Object getCellEditorValue()
        throws Exception
    {
        return richTextPane.update();
    }

    public void doOk()
    {
        fireEditingStopped();
    }

    public void doCancel()
    {
        fireEditingCanceled();
    }
}
