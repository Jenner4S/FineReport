// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.actions.edit;

import com.fr.base.BaseUtils;
import com.fr.design.actions.ElementCaseAction;
import com.fr.design.mainframe.ElementCasePane;
import com.fr.general.Inter;
import com.fr.grid.selection.Selection;

public class DeleteAction extends ElementCaseAction
{

    public DeleteAction(ElementCasePane elementcasepane)
    {
        super(elementcasepane);
        setName((new StringBuilder()).append(Inter.getLocText("M_Edit-Delete")).append("...").toString());
        setMnemonic('D');
        setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/m_report/delete.png"));
    }

    public boolean executeActionReturnUndoRecordNeeded()
    {
        ElementCasePane elementcasepane = (ElementCasePane)getEditingComponent();
        return elementcasepane.getSelection().triggerDeleteAction(elementcasepane);
    }
}
