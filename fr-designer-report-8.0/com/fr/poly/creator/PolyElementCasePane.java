// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.poly.creator;

import com.fr.design.mainframe.ElementCasePane;
import com.fr.grid.selection.CellSelection;
import com.fr.report.poly.PolyECBlock;

public abstract class PolyElementCasePane extends ElementCasePane
{

    public PolyElementCasePane(PolyECBlock polyecblock)
    {
        super(polyecblock);
        setSelection(new CellSelection(0, 0, 1, 1));
        setHorizontalScrollBarVisible(false);
        setVerticalScrollBarVisible(false);
    }

    protected boolean supportRepeatedHeaderFooter()
    {
        return false;
    }
}
