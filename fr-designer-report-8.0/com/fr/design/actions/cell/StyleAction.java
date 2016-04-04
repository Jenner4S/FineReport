// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.actions.cell;

import com.fr.base.BaseUtils;
import com.fr.design.actions.UpdateAction;
import com.fr.design.mainframe.CellElementPropertyPane;
import com.fr.design.menu.KeySetUtils;
import com.fr.design.menu.MenuKeySet;
import com.fr.general.Inter;
import java.awt.event.ActionEvent;

public class StyleAction extends UpdateAction
{

    public StyleAction()
    {
        setMenuKeySet(KeySetUtils.GLOBAL_STYLE);
        setName(getMenuKeySet().getMenuKeySetName());
        setMnemonic(getMenuKeySet().getMnemonic());
        setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/m_format/cell.png"));
    }

    public void actionPerformed(ActionEvent actionevent)
    {
        CellElementPropertyPane.getInstance().GoToPane(new String[] {
            Inter.getLocText("Style"), Inter.getLocText("Custom")
        });
    }
}
