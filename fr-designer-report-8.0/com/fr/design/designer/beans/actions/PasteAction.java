// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.designer.beans.actions;

import com.fr.base.BaseUtils;
import com.fr.design.mainframe.FormDesigner;
import com.fr.general.Inter;
import javax.swing.KeyStroke;

// Referenced classes of package com.fr.design.designer.beans.actions:
//            FormEditAction

public class PasteAction extends FormEditAction
{

    public PasteAction(FormDesigner formdesigner)
    {
        super(formdesigner);
        setName(Inter.getLocText("M_Edit-Paste"));
        setMnemonic('P');
        setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/m_edit/paste.png"));
        setAccelerator(KeyStroke.getKeyStroke(86, 2));
    }

    public boolean executeActionReturnUndoRecordNeeded()
    {
        FormDesigner formdesigner = (FormDesigner)getEditingComponent();
        if(formdesigner == null)
            return false;
        else
            return formdesigner.paste();
    }

    public void update()
    {
        setEnabled(true);
    }
}
