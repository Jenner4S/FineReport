// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.expand;

import com.fr.stable.ColumnRow;

public class Expand
{

    private boolean _default;
    private ColumnRow columnRow;

    public Expand()
    {
    }

    public Expand(boolean flag, ColumnRow columnrow)
    {
        _default = flag;
        columnRow = columnrow;
    }

    public boolean isDefault()
    {
        return _default;
    }

    public void setDefault(boolean flag)
    {
        _default = flag;
    }

    public ColumnRow getParentColumnRow()
    {
        return columnRow;
    }

    public void setParentColumnRow(ColumnRow columnrow)
    {
        columnRow = columnrow;
    }
}
