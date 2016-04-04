// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.cell.editor;

import com.fr.base.FRContext;
import com.fr.design.data.DesignTableDataManager;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.DialogActionListener;
import com.fr.design.dscolumn.DSColumnPane;
import com.fr.design.mainframe.ElementCasePane;
import com.fr.general.FRLogger;
import com.fr.grid.Grid;
import com.fr.report.cell.TemplateCellElement;
import com.fr.report.core.SheetUtils;
import com.fr.report.elementcase.TemplateElementCase;
import com.fr.report.poly.PolyECBlock;
import com.fr.report.worksheet.WorkSheet;
import com.fr.stable.ProductConstants;
import java.awt.Component;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

// Referenced classes of package com.fr.design.cell.editor:
//            AbstractCellEditor

public class DSColumnCellEditor extends AbstractCellEditor
    implements DialogActionListener
{

    private DSColumnPane dsColumnPane;

    public DSColumnCellEditor(ElementCasePane elementcasepane)
    {
        super(elementcasepane);
        dsColumnPane = null;
    }

    public Object getCellEditorValue()
        throws Exception
    {
        return dsColumnPane.update();
    }

    public Component getCellEditorComponent(Grid grid, TemplateCellElement templatecellelement, int i)
    {
        TemplateElementCase templateelementcase = grid.getElementCasePane().getEditingElementCase();
        if(templateelementcase != null && ((templateelementcase instanceof WorkSheet) || (templateelementcase instanceof PolyECBlock)))
            SheetUtils.calculateDefaultParent(templateelementcase);
        dsColumnPane = new DSColumnPane();
        dsColumnPane.putElementcase(grid.getElementCasePane());
        dsColumnPane.putCellElement(templatecellelement);
        BasicDialog basicdialog = dsColumnPane.showWindow(SwingUtilities.getWindowAncestor(grid));
        basicdialog.addDialogActionListener(this);
        try
        {
            dsColumnPane.populate(DesignTableDataManager.getEditingTableDataSource(), templatecellelement);
        }
        catch(Exception exception)
        {
            JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(grid), exception.getMessage(), ProductConstants.APP_NAME, 0);
            FRContext.getLogger().error(exception.getMessage(), exception);
        }
        return basicdialog;
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
