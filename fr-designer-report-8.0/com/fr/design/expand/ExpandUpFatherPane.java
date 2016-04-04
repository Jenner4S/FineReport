// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.expand;

import com.fr.report.cell.cellattr.CellExpandAttr;
import com.fr.stable.ColumnRow;

// Referenced classes of package com.fr.design.expand:
//            ExpandFatherPane

public class ExpandUpFatherPane extends ExpandFatherPane
{

    public ExpandUpFatherPane()
    {
    }

    protected ColumnRow getColumnRow(CellExpandAttr cellexpandattr)
    {
        return cellexpandattr.getUpParentColumnRow();
    }

    protected boolean isParentDefault(CellExpandAttr cellexpandattr)
    {
        return cellexpandattr.isUpParentDefault();
    }

    protected void setValue(CellExpandAttr cellexpandattr, boolean flag, ColumnRow columnrow)
    {
        cellexpandattr.setUpParentDefault(flag);
        cellexpandattr.setUpParentColumnRow(columnrow);
    }
}
