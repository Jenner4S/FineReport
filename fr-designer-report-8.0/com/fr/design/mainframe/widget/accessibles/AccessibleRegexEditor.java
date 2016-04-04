// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.widget.accessibles;

import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.gui.frpane.RegPane;
import com.fr.design.mainframe.widget.wrappers.RegexWrapper;
import com.fr.form.ui.reg.RegExp;
import javax.swing.SwingUtilities;

// Referenced classes of package com.fr.design.mainframe.widget.accessibles:
//            UneditableAccessibleEditor

public class AccessibleRegexEditor extends UneditableAccessibleEditor
{
    public static class AccessibleRegexEditor4TextArea extends AccessibleRegexEditor
    {

        protected RegPane initRegPane()
        {
            return new RegPane(RegPane.TEXTAREA_REG_TYPE);
        }

        public AccessibleRegexEditor4TextArea()
        {
        }
    }


    private RegPane regPane;

    public AccessibleRegexEditor()
    {
        super(new RegexWrapper());
    }

    protected RegPane initRegPane()
    {
        return new RegPane();
    }

    protected void showEditorPane()
    {
        if(regPane == null)
            regPane = initRegPane();
        BasicDialog basicdialog = regPane.showWindow(SwingUtilities.getWindowAncestor(this));
        regPane.populate((RegExp)getValue());
        basicdialog.addDialogActionListener(new DialogActionAdapter() {

            final AccessibleRegexEditor this$0;

            public void doOk()
            {
                RegExp regexp = regPane.update();
                setValue(regexp);
                fireStateChanged();
            }

            
            {
                this$0 = AccessibleRegexEditor.this;
                super();
            }
        }
);
        basicdialog.setVisible(true);
    }

}
