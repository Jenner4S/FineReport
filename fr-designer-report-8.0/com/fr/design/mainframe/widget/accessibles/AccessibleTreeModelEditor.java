// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.widget.accessibles;

import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.gui.frpane.TreeSettingPane;
import com.fr.design.mainframe.widget.wrappers.TreeModelWrapper;
import javax.swing.SwingUtilities;

// Referenced classes of package com.fr.design.mainframe.widget.accessibles:
//            UneditableAccessibleEditor

public class AccessibleTreeModelEditor extends UneditableAccessibleEditor
{

    private TreeSettingPane treeSettingPane;

    public AccessibleTreeModelEditor()
    {
        super(new TreeModelWrapper());
    }

    protected void showEditorPane()
    {
        if(treeSettingPane == null)
            treeSettingPane = new TreeSettingPane(false);
        BasicDialog basicdialog = treeSettingPane.showWindow(SwingUtilities.getWindowAncestor(this));
        treeSettingPane.populate(getValue());
        basicdialog.addDialogActionListener(new DialogActionAdapter() {

            final AccessibleTreeModelEditor this$0;

            public void doOk()
            {
                Object obj = treeSettingPane.updateTreeNodeAttrs();
                setValue(obj);
                fireStateChanged();
            }

            
            {
                this$0 = AccessibleTreeModelEditor.this;
                super();
            }
        }
);
        basicdialog.setVisible(true);
    }

}
