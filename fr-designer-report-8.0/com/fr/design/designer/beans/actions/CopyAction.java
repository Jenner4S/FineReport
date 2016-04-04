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

public class CopyAction extends FormEditAction
{

    public CopyAction(FormDesigner formdesigner)
    {
        super(formdesigner);
        setName(Inter.getLocText("M_Edit-Copy"));
        setMnemonic('C');
        setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/m_edit/copy.png"));
        setAccelerator(KeyStroke.getKeyStroke(67, 2));
    }

    public boolean executeActionReturnUndoRecordNeeded()
    {
        FormDesigner formdesigner = (FormDesigner)getEditingComponent();
        if(formdesigner != null)
            formdesigner.copy();
        return false;
    }
}
