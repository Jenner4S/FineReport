// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.actions.insert.cell;

import com.fr.base.BaseUtils;
import com.fr.design.mainframe.ElementCasePane;
import com.fr.design.menu.KeySetUtils;
import com.fr.design.menu.MenuKeySet;
import com.fr.report.cell.cellattr.core.group.DSColumn;

// Referenced classes of package com.fr.design.actions.insert.cell:
//            AbstractCellAction

public class DSColumnCellAction extends AbstractCellAction
{

    public DSColumnCellAction(ElementCasePane elementcasepane)
    {
        super(elementcasepane);
        setMenuKeySet(KeySetUtils.INSERT_DATA_COLUMN);
        setName((new StringBuilder()).append(getMenuKeySet().getMenuKeySetName()).append("...").toString());
        setMnemonic(getMenuKeySet().getMnemonic());
        setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/m_insert/bindColumn.png"));
    }

    public Class getCellValueClass()
    {
        return com/fr/report/cell/cellattr/core/group/DSColumn;
    }
}
