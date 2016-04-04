// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.widget.accessibles;

import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.gui.xpane.LayoutBorderPane;
import com.fr.design.mainframe.widget.editors.ITextComponent;
import com.fr.design.mainframe.widget.renderer.LayoutBorderStyleRenderer;
import com.fr.design.mainframe.widget.wrappers.LayoutBorderStyleWrapper;
import com.fr.form.ui.LayoutBorderStyle;
import java.awt.Dimension;
import javax.swing.SwingUtilities;

// Referenced classes of package com.fr.design.mainframe.widget.accessibles:
//            UneditableAccessibleEditor, RendererField

public class AccessibleWLayoutBorderStyleEditor extends UneditableAccessibleEditor
{

    private LayoutBorderPane borderPane;

    public AccessibleWLayoutBorderStyleEditor()
    {
        super(new LayoutBorderStyleWrapper());
    }

    protected ITextComponent createTextField()
    {
        return new RendererField(new LayoutBorderStyleRenderer());
    }

    protected void showEditorPane()
    {
        if(borderPane == null)
        {
            borderPane = new LayoutBorderPane();
            borderPane.setPreferredSize(new Dimension(600, 400));
        }
        BasicDialog basicdialog = borderPane.showWindow(SwingUtilities.getWindowAncestor(this));
        basicdialog.addDialogActionListener(new DialogActionAdapter() {

            final AccessibleWLayoutBorderStyleEditor this$0;

            public void doOk()
            {
                setValue(borderPane.update());
                fireStateChanged();
            }

            
            {
                this$0 = AccessibleWLayoutBorderStyleEditor.this;
                super();
            }
        }
);
        borderPane.populate((LayoutBorderStyle)getValue());
        basicdialog.setVisible(true);
    }

}
