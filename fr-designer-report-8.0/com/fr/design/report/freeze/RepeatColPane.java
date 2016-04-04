// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.report.freeze;

import com.fr.general.Inter;
import com.fr.stable.FT;

// Referenced classes of package com.fr.design.report.freeze:
//            FreezeAndRepeatPane, ColSpinner

public class RepeatColPane extends FreezeAndRepeatPane
{

    public RepeatColPane()
    {
        start = new ColSpinner(1.0D, 2147483647D, 1.0D, 1.0D);
        end = new ColSpinner(1.0D, 2147483647D, 1.0D, 1.0D);
        super.initComponent();
    }

    protected String title4PopupWindow()
    {
        return "repeatcolumn";
    }

    public void populateBean(FT ft)
    {
        ((ColSpinner)start).setValue(ft.getFrom() + 1);
        ((ColSpinner)end).setValue(ft.getTo() + 1);
    }

    public FT updateBean()
    {
        return new FT((int)((ColSpinner)start).getValue() - 1, (int)((ColSpinner)end).getValue() - 1);
    }

    public String getLabeshow()
    {
        return (new StringBuilder()).append(" ").append(Inter.getLocText("ColumnTo")).append(" ").toString();
    }

    public volatile Object updateBean()
    {
        return updateBean();
    }

    public volatile void populateBean(Object obj)
    {
        populateBean((FT)obj);
    }
}
