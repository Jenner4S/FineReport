// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.widget.accessibles;

import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.gui.frpane.ReportletParameterViewPane;
import com.fr.design.mainframe.widget.wrappers.ParameterWrapper;
import com.fr.stable.ParameterProvider;
import java.util.List;
import javax.swing.SwingUtilities;

// Referenced classes of package com.fr.design.mainframe.widget.accessibles:
//            UneditableAccessibleEditor

public class AccessibleParameterEditor extends UneditableAccessibleEditor
{

    private ReportletParameterViewPane parameterPane;

    public AccessibleParameterEditor()
    {
        super(new ParameterWrapper());
    }

    protected void showEditorPane()
    {
        if(parameterPane == null)
            parameterPane = new ReportletParameterViewPane(-1);
        BasicDialog basicdialog = parameterPane.showWindow(SwingUtilities.getWindowAncestor(this));
        parameterPane.populate((ParameterProvider[])(ParameterProvider[])getValue());
        basicdialog.addDialogActionListener(new DialogActionAdapter() {

            final AccessibleParameterEditor this$0;

            public void doOk()
            {
                List list = parameterPane.update();
                setValue(((Object) (list.toArray(new ParameterProvider[list.size()]))));
                fireStateChanged();
            }

            
            {
                this$0 = AccessibleParameterEditor.this;
                super();
            }
        }
);
        basicdialog.setVisible(true);
    }

}
