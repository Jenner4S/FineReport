// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.widget.accessibles;

import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.mainframe.widget.editors.PaddingMarginPane;
import com.fr.design.mainframe.widget.wrappers.PaddingMarginWrapper;
import com.fr.form.ui.PaddingMargin;
import javax.swing.SwingUtilities;

// Referenced classes of package com.fr.design.mainframe.widget.accessibles:
//            BaseAccessibleEditor

public class AccessilePaddingMarginEditor extends BaseAccessibleEditor
{

    private PaddingMarginPane pane;

    public AccessilePaddingMarginEditor()
    {
        super(new PaddingMarginWrapper(), new PaddingMarginWrapper(), true);
    }

    protected void showEditorPane()
    {
        if(pane == null)
            pane = new PaddingMarginPane();
        BasicDialog basicdialog = pane.showSmallWindow(SwingUtilities.getWindowAncestor(this), new DialogActionAdapter() {

            final AccessilePaddingMarginEditor this$0;

            public void doOk()
            {
                setValue(pane.update());
                fireStateChanged();
            }

            
            {
                this$0 = AccessilePaddingMarginEditor.this;
                super();
            }
        }
);
        pane.populate((PaddingMargin)getValue());
        basicdialog.setVisible(true);
    }

}
