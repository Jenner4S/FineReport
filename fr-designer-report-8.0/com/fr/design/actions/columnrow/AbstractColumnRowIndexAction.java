// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.actions.columnrow;

import com.fr.design.actions.CellSelectionAction;
import com.fr.design.mainframe.ElementCasePane;

public abstract class AbstractColumnRowIndexAction extends CellSelectionAction
{

    private int index;

    protected AbstractColumnRowIndexAction(ElementCasePane elementcasepane, int i)
    {
        super(elementcasepane);
        index = 0;
        index = i;
    }

    public int getIndex()
    {
        return index;
    }
}
