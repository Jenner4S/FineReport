// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.widget.accessibles;

import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.mainframe.widget.wrappers.BackgroundWrapper;
import com.fr.design.style.background.BackgroundPane;
import com.fr.general.Background;
import java.awt.Dimension;
import javax.swing.SwingUtilities;

// Referenced classes of package com.fr.design.mainframe.widget.accessibles:
//            UneditableAccessibleEditor

public class AccessibleBackgroundEditor extends UneditableAccessibleEditor
{

    private BackgroundPane backgroundPane;

    public AccessibleBackgroundEditor()
    {
        super(new BackgroundWrapper());
    }

    protected void showEditorPane()
    {
        if(backgroundPane == null)
        {
            backgroundPane = new BackgroundPane();
            backgroundPane.setPreferredSize(new Dimension(600, 400));
        }
        BasicDialog basicdialog = backgroundPane.showWindow(SwingUtilities.getWindowAncestor(this));
        basicdialog.addDialogActionListener(new DialogActionAdapter() {

            final AccessibleBackgroundEditor this$0;

            public void doOk()
            {
                setValue(backgroundPane.update());
                fireStateChanged();
            }

            
            {
                this$0 = AccessibleBackgroundEditor.this;
                super();
            }
        }
);
        backgroundPane.populate((Background)getValue());
        basicdialog.setVisible(true);
    }

}
