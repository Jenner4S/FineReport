// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.actions.columnrow;

import com.fr.design.mainframe.ElementCasePane;
import com.fr.general.Inter;

// Referenced classes of package com.fr.design.actions.columnrow:
//            ColumnRowRepeatAction

public class HeadColumnAction extends ColumnRowRepeatAction
{

    public HeadColumnAction(ElementCasePane elementcasepane)
    {
        super(elementcasepane);
        setName(Inter.getLocText("Set_Column_Title_Start"));
    }

    protected boolean isColumn()
    {
        return true;
    }

    protected boolean isFoot()
    {
        return false;
    }
}
