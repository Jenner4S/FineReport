// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.cell.editor;

import com.fr.design.dialog.DialogActionListener;
import com.fr.design.report.SelectImagePane;
import com.fr.grid.Grid;
import com.fr.report.cell.FloatElement;
import java.awt.Component;
import java.awt.Image;
import javax.swing.SwingUtilities;

// Referenced classes of package com.fr.design.cell.editor:
//            AbstractFloatEditor

public class ImageFloatEditor extends AbstractFloatEditor
    implements DialogActionListener
{

    private SelectImagePane imageEditorPane;

    public ImageFloatEditor()
    {
        imageEditorPane = null;
    }

    public Object getFloatEditorValue()
        throws Exception
    {
        return imageEditorPane.update();
    }

    public Component getFloatEditorComponent(Grid grid, FloatElement floatelement, int i)
    {
        Object obj = floatelement.getValue();
        if(obj == null || !(obj instanceof Image))
            obj = null;
        imageEditorPane = new SelectImagePane();
        imageEditorPane.populate(floatelement);
        return imageEditorPane.showWindow(SwingUtilities.getWindowAncestor(grid), this);
    }

    public void doOk()
    {
        stopFloatEditing();
    }

    public void doCancel()
    {
        cancelFloatEditing();
    }
}
