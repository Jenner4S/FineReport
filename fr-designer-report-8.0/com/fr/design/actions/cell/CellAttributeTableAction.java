// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.actions.cell;

import com.fr.design.actions.UpdateAction;
import com.fr.design.mainframe.CellElementPropertyPane;
import java.awt.event.ActionEvent;

public abstract class CellAttributeTableAction extends UpdateAction
{

    public CellAttributeTableAction()
    {
    }

    protected abstract String getID();

    public void actionPerformed(ActionEvent actionevent)
    {
        CellElementPropertyPane.getInstance().GoToPane(new String[] {
            getID()
        });
    }
}
