// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.cell.editor;

import com.fr.design.dialog.DialogActionListener;
import com.fr.design.mainframe.ElementCasePane;
import com.fr.design.report.SelectImagePane;
import com.fr.grid.Grid;
import com.fr.report.cell.TemplateCellElement;
import java.awt.Component;
import java.awt.Image;
import javax.swing.SwingUtilities;

// Referenced classes of package com.fr.design.cell.editor:
//            AbstractCellEditor

public class ImageCellEditor extends AbstractCellEditor
    implements DialogActionListener
{

    private SelectImagePane imageEditorPane;

    public ImageCellEditor(ElementCasePane elementcasepane)
    {
        super(elementcasepane);
        imageEditorPane = null;
    }

    public Object getCellEditorValue()
        throws Exception
    {
        return imageEditorPane.update();
    }

    public Component getCellEditorComponent(Grid grid, TemplateCellElement templatecellelement, int i)
    {
        Object obj = null;
        if(templatecellelement != null)
            obj = templatecellelement.getValue();
        if(obj == null || !(obj instanceof Image))
            obj = null;
        imageEditorPane = new SelectImagePane();
        imageEditorPane.populate(templatecellelement);
        return imageEditorPane.showWindow(SwingUtilities.getWindowAncestor(grid), this);
    }

    public void doOk()
    {
        stopCellEditing();
    }

    public void doCancel()
    {
        cancelCellEditing();
    }
}
