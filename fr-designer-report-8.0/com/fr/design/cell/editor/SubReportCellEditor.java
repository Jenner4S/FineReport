// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.cell.editor;

import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.DialogActionListener;
import com.fr.design.mainframe.ElementCasePane;
import com.fr.design.report.SubReportPane;
import com.fr.grid.Grid;
import com.fr.report.cell.TemplateCellElement;
import com.fr.report.core.SheetUtils;
import com.fr.report.elementcase.TemplateElementCase;
import com.fr.report.poly.PolyECBlock;
import com.fr.report.worksheet.WorkSheet;
import java.awt.Component;
import javax.swing.SwingUtilities;

// Referenced classes of package com.fr.design.cell.editor:
//            AbstractCellEditor

public class SubReportCellEditor extends AbstractCellEditor
    implements DialogActionListener
{

    private SubReportPane subReportPane;

    public SubReportCellEditor(ElementCasePane elementcasepane)
    {
        super(elementcasepane);
    }

    public Component getCellEditorComponent(Grid grid, TemplateCellElement templatecellelement, int i)
    {
        TemplateElementCase templateelementcase = grid.getElementCasePane().getEditingElementCase();
        if(templateelementcase != null && ((templateelementcase instanceof WorkSheet) || (templateelementcase instanceof PolyECBlock)))
            SheetUtils.calculateDefaultParent(templateelementcase);
        subReportPane = new SubReportPane();
        BasicDialog basicdialog = subReportPane.showWindow(SwingUtilities.getWindowAncestor(grid));
        basicdialog.addDialogActionListener(this);
        subReportPane.populate(templateelementcase, templatecellelement);
        return basicdialog;
    }

    public Object getCellEditorValue()
        throws Exception
    {
        return subReportPane.update();
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
