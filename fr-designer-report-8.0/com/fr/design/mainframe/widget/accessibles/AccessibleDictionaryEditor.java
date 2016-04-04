// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.widget.accessibles;

import com.fr.data.Dictionary;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.mainframe.widget.wrappers.DictionaryWrapper;
import com.fr.design.present.dict.DictionaryPane;
import javax.swing.SwingUtilities;

// Referenced classes of package com.fr.design.mainframe.widget.accessibles:
//            UneditableAccessibleEditor

public class AccessibleDictionaryEditor extends UneditableAccessibleEditor
{

    private DictionaryPane dictPane;

    public AccessibleDictionaryEditor()
    {
        super(new DictionaryWrapper());
    }

    protected void showEditorPane()
    {
        if(dictPane == null)
            dictPane = new DictionaryPane();
        BasicDialog basicdialog = dictPane.showWindow(SwingUtilities.getWindowAncestor(this));
        basicdialog.addDialogActionListener(new DialogActionAdapter() {

            final AccessibleDictionaryEditor this$0;

            public void doOk()
            {
                Dictionary dictionary = (Dictionary)dictPane.updateBean();
                setValue(dictionary);
                fireStateChanged();
            }

            
            {
                this$0 = AccessibleDictionaryEditor.this;
                super();
            }
        }
);
        dictPane.populateBean((Dictionary)getValue());
        basicdialog.setVisible(true);
    }

}
