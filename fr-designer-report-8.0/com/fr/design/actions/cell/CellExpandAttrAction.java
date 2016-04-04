// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.actions.cell;

import com.fr.base.BaseUtils;
import com.fr.design.menu.KeySetUtils;
import com.fr.design.menu.MenuKeySet;
import com.fr.general.Inter;

// Referenced classes of package com.fr.design.actions.cell:
//            CellAttributeTableAction

public class CellExpandAttrAction extends CellAttributeTableAction
{

    public CellExpandAttrAction()
    {
        setMenuKeySet(KeySetUtils.CELL_EXPAND_ATTR);
        setName(getMenuKeySet().getMenuKeySetName());
        setMnemonic(getMenuKeySet().getMnemonic());
        setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/expand/cellAttr.gif"));
    }

    public String getID()
    {
        return Inter.getLocText("ExpandD-Expand_Attribute");
    }
}
