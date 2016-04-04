// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.actions.columnrow;

import com.fr.design.mainframe.ElementCasePane;
import com.fr.general.Inter;

// Referenced classes of package com.fr.design.actions.columnrow:
//            ColumnRowRepeatAction

public class HeadRowAction extends ColumnRowRepeatAction
{

    public HeadRowAction(ElementCasePane elementcasepane)
    {
        super(elementcasepane);
        setName(Inter.getLocText("Set_Row_Title_Start"));
    }

    protected boolean isColumn()
    {
        return false;
    }

    protected boolean isFoot()
    {
        return false;
    }
}
