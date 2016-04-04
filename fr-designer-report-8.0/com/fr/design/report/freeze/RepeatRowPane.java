// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.report.freeze;

import com.fr.general.Inter;
import com.fr.stable.FT;

// Referenced classes of package com.fr.design.report.freeze:
//            FreezeAndRepeatPane, RowSpinner

public class RepeatRowPane extends FreezeAndRepeatPane
{

    public RepeatRowPane()
    {
        start = new RowSpinner(1.0D, 2147483647D, 1.0D, 1.0D);
        end = new RowSpinner(1.0D, 2147483647D, 1.0D, 1.0D);
        super.initComponent();
    }

    protected String title4PopupWindow()
    {
        return "repeatcolumn";
    }

    public void populateBean(FT ft)
    {
        ((RowSpinner)start).setValue(ft.getFrom() + 1);
        ((RowSpinner)end).setValue(ft.getTo() + 1);
    }

    public FT updateBean()
    {
        return new FT((int)((RowSpinner)start).getValue() - 1, (int)((RowSpinner)end).getValue() - 1);
    }

    public String getLabeshow()
    {
        return (new StringBuilder()).append(" ").append(Inter.getLocText("RowTo")).append(" ").toString();
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
