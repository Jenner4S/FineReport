// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.widget.accessibles;

import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.mainframe.widget.editors.DimensionEditingPane;
import com.fr.design.mainframe.widget.wrappers.DimensionWrapper;
import java.awt.Dimension;
import javax.swing.SwingUtilities;

// Referenced classes of package com.fr.design.mainframe.widget.accessibles:
//            BaseAccessibleEditor

public class AccessibleDimensionEditor extends BaseAccessibleEditor
{

    private DimensionEditingPane dimensionPane;

    public AccessibleDimensionEditor()
    {
        super(new DimensionWrapper(), new DimensionWrapper(), true);
    }

    protected void showEditorPane()
    {
        if(dimensionPane == null)
            dimensionPane = new DimensionEditingPane();
        BasicDialog basicdialog = dimensionPane.showWindow(SwingUtilities.getWindowAncestor(this));
        basicdialog.addDialogActionListener(new DialogActionAdapter() {

            final AccessibleDimensionEditor this$0;

            public void doOk()
            {
                setValue(dimensionPane.update());
                fireStateChanged();
            }

            
            {
                this$0 = AccessibleDimensionEditor.this;
                super();
            }
        }
);
        dimensionPane.populate((Dimension)getValue());
        basicdialog.setVisible(true);
    }

}
