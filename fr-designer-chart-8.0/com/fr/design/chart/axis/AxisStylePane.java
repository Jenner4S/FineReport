// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.chart.axis;

import com.fr.design.dialog.BasicPane;
import com.fr.general.Inter;

public abstract class AxisStylePane extends BasicPane
{

    private static final long serialVersionUID = 0x7c7b4ebc9641f3f1L;

    public AxisStylePane()
    {
    }

    public abstract void populate(Object obj);

    public abstract void update(Object obj);

    protected String title4PopupWindow()
    {
        return Inter.getLocText(new String[] {
            "Set", "ChartF-Axis", "Format"
        });
    }
}
