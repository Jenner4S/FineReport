// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.widget.accessibles;

import com.fr.base.background.ImageBackground;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.gui.frpane.ImgChoosePane;
import com.fr.design.mainframe.widget.wrappers.BackgroundWrapper;
import java.awt.Dimension;
import javax.swing.SwingUtilities;

// Referenced classes of package com.fr.design.mainframe.widget.accessibles:
//            UneditableAccessibleEditor

public class AccessibleImgBackgroundEditor extends UneditableAccessibleEditor
{

    private ImgChoosePane choosePane;

    public AccessibleImgBackgroundEditor()
    {
        super(new BackgroundWrapper());
    }

    protected void showEditorPane()
    {
        if(choosePane == null)
        {
            choosePane = new ImgChoosePane();
            choosePane.setPreferredSize(new Dimension(600, 400));
        }
        BasicDialog basicdialog = choosePane.showWindow(SwingUtilities.getWindowAncestor(this));
        basicdialog.addDialogActionListener(new DialogActionAdapter() {

            final AccessibleImgBackgroundEditor this$0;

            public void doOk()
            {
                setValue(choosePane.update());
                fireStateChanged();
            }

            
            {
                this$0 = AccessibleImgBackgroundEditor.this;
                super();
            }
        }
);
        choosePane.populate((getValue() instanceof ImageBackground) ? (ImageBackground)getValue() : null);
        basicdialog.setVisible(true);
    }

}
