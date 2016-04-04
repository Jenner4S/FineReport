// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.actions.cell;

import com.fr.design.menu.KeySetUtils;
import com.fr.design.menu.MenuKeySet;
import com.fr.general.Inter;

// Referenced classes of package com.fr.design.actions.cell:
//            CellAttributeTableAction

public class CellAttributeAction extends CellAttributeTableAction
{

    public CellAttributeAction()
    {
        setMenuKeySet(KeySetUtils.CELL_OTHER_ATTR);
        setName((new StringBuilder()).append(getMenuKeySet().getMenuKeySetName()).append("...").toString());
        setMnemonic(getMenuKeySet().getMnemonic());
    }

    protected String getID()
    {
        return Inter.getLocText("Datasource-Other_Attributes");
    }
}
