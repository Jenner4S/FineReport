// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.widget.accessibles;

import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.mainframe.widget.wrappers.DateWrapper;
import com.fr.design.style.FormatPane;
import java.text.SimpleDateFormat;
import javax.swing.SwingUtilities;

// Referenced classes of package com.fr.design.mainframe.widget.accessibles:
//            UneditableAccessibleEditor

public class AccessibleDateEditor extends UneditableAccessibleEditor
{

    private FormatPane formatPane;

    public AccessibleDateEditor()
    {
        super(new DateWrapper());
    }

    protected void showEditorPane()
    {
        if(formatPane == null)
            formatPane = new FormatPane();
        formatPane.setStyle4Pane(new int[] {
            5, 6
        });
        BasicDialog basicdialog = formatPane.showWindow(SwingUtilities.getWindowAncestor(this));
        formatPane.populate(new SimpleDateFormat((String)getValue()));
        basicdialog.addDialogActionListener(new DialogActionAdapter() {

            final AccessibleDateEditor this$0;

            public void doOk()
            {
                java.text.Format format = formatPane.update();
                setValue(((SimpleDateFormat)format).toPattern());
                fireStateChanged();
            }

            
            {
                this$0 = AccessibleDateEditor.this;
                super();
            }
        }
);
        basicdialog.setVisible(true);
    }

}
