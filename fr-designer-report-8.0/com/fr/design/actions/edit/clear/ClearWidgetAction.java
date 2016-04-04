// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.actions.edit.clear;

import com.fr.design.mainframe.ElementCasePane;
import com.fr.general.Inter;

// Referenced classes of package com.fr.design.actions.edit.clear:
//            ClearAction

public class ClearWidgetAction extends ClearAction
{

    public ClearWidgetAction(ElementCasePane elementcasepane)
    {
        super(elementcasepane);
        setName(Inter.getLocText("Widget"));
        setMnemonic('W');
    }

    public boolean executeActionReturnUndoRecordNeeded()
    {
        ElementCasePane elementcasepane = (ElementCasePane)getEditingComponent();
        if(elementcasepane == null)
            return false;
        else
            return elementcasepane.clearWidget();
    }
}
