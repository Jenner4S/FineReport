// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.actions.edit.merge;

import com.fr.base.BaseUtils;
import com.fr.design.actions.ElementCaseAction;
import com.fr.design.mainframe.ElementCasePane;
import com.fr.design.menu.KeySetUtils;
import com.fr.design.menu.MenuKeySet;

public class UnmergeCellAction extends ElementCaseAction
{

    public UnmergeCellAction(ElementCasePane elementcasepane)
    {
        super(elementcasepane);
        setMenuKeySet(KeySetUtils.UNMERGE_CELL);
        setName(getMenuKeySet().getMenuKeySetName());
        setMnemonic(getMenuKeySet().getMnemonic());
        setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/m_edit/unmerge.png"));
    }

    public void update()
    {
        super.update();
        ElementCasePane elementcasepane = (ElementCasePane)getEditingComponent();
        setEnabled(elementcasepane.canUnMergeCell());
    }

    public boolean executeActionReturnUndoRecordNeeded()
    {
        ElementCasePane elementcasepane = (ElementCasePane)getEditingComponent();
        if(elementcasepane == null)
            return false;
        else
            return elementcasepane.unMergeCell();
    }
}
