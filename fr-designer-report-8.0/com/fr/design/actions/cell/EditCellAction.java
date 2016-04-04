// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.actions.cell;

import com.fr.base.BaseUtils;
import com.fr.design.actions.ElementCaseAction;
import com.fr.design.mainframe.ElementCasePane;
import com.fr.general.Inter;
import com.fr.grid.Grid;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;

public class EditCellAction extends ElementCaseAction
{

    public EditCellAction(ElementCasePane elementcasepane)
    {
        super(elementcasepane);
        setName(Inter.getLocText("Edit"));
        setMnemonic('I');
        setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/control/edit.png"));
    }

    public void actionPerformed(ActionEvent actionevent)
    {
        ElementCasePane elementcasepane = (ElementCasePane)getEditingComponent();
        if(elementcasepane.isSelectedOneCell())
            elementcasepane.getGrid().startEditing();
        else
            Toolkit.getDefaultToolkit().beep();
    }

    public boolean executeActionReturnUndoRecordNeeded()
    {
        return false;
    }
}
