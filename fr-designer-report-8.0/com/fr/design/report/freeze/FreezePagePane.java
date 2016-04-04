// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.report.freeze;

import com.fr.design.gui.ilable.UILabel;
import com.fr.general.Inter;
import com.fr.stable.FT;
import com.fr.stable.StableUtils;

// Referenced classes of package com.fr.design.report.freeze:
//            FreezeAndRepeatPane

public class FreezePagePane extends FreezeAndRepeatPane
{

    private boolean isNumber;

    public FreezePagePane(boolean flag)
    {
        isNumber = flag;
        start = new UILabel(flag ? (new StringBuilder()).append(Inter.getLocText(new String[] {
            "Frozen", "N.O."
        })).append(" 1").toString() : (new StringBuilder()).append(Inter.getLocText(new String[] {
            "Frozen", "N.O."
        })).append(" A").toString(), 0);
        end = new UILabel(flag ? (new StringBuilder()).append(" 1").append(Inter.getLocText("Row")).toString() : (new StringBuilder()).append(" A").append(Inter.getLocText("Column")).toString(), 0);
        super.initComponent();
    }

    protected String title4PopupWindow()
    {
        return "FreezePage";
    }

    public void populateBean(FT ft)
    {
        if(isNumber)
            ((UILabel)end).setText((new StringBuilder()).append(String.valueOf(ft.getTo() + 1)).append(Inter.getLocText("Row")).toString());
        else
            ((UILabel)end).setText((new StringBuilder()).append(StableUtils.convertIntToABC(ft.getTo() + 1)).append(Inter.getLocText("Column")).toString());
    }

    public FT updateBean()
    {
        return null;
    }

    public String getLabeshow()
    {
        return isNumber ? Inter.getLocText("RowTo") : Inter.getLocText("ColumnTo");
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
