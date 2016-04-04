// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.widget.accessibles;

import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.mainframe.widget.wrappers.FontWrapper;
import com.fr.design.style.FRFontPane;
import com.fr.general.FRFont;
import javax.swing.SwingUtilities;

// Referenced classes of package com.fr.design.mainframe.widget.accessibles:
//            UneditableAccessibleEditor

public class AccessibleFontEditor extends UneditableAccessibleEditor
{

    private FRFontPane frFontPane;

    public AccessibleFontEditor()
    {
        super(new FontWrapper());
    }

    public FRFont getValue()
    {
        if(super.getValue() == null)
            return FRFont.getInstance();
        else
            return (FRFont)super.getValue();
    }

    protected void showEditorPane()
    {
        if(frFontPane == null)
            frFontPane = new FRFontPane();
        BasicDialog basicdialog = frFontPane.showWindow(SwingUtilities.getWindowAncestor(this));
        basicdialog.addDialogActionListener(new DialogActionAdapter() {

            final AccessibleFontEditor this$0;

            public void doOk()
            {
                setValue(frFontPane.update());
                fireStateChanged();
            }

            
            {
                this$0 = AccessibleFontEditor.this;
                super();
            }
        }
);
        frFontPane.populate(getValue());
        basicdialog.setVisible(true);
    }

    public volatile Object getValue()
    {
        return getValue();
    }

}
