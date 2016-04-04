// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.report.freeze;

import com.fr.design.gui.ilable.UILabel;
import com.fr.general.Inter;
import com.fr.stable.FT;
import com.fr.stable.StableUtils;

// Referenced classes of package com.fr.design.report.freeze:
//            FreezeAndRepeatPane, RowSpinner

public class FreezeWriteRowPane extends FreezeAndRepeatPane
{

    public FreezeWriteRowPane()
    {
        start = new UILabel((new StringBuilder()).append(Inter.getLocText(new String[] {
            "Frozen", "N.O."
        })).append(" 1").toString(), 0);
        end = new RowSpinner(1.0D, 2147483647D, 1.0D, 1.0D);
        super.initComponent();
        add(new UILabel(Inter.getLocText("Row")));
    }

    protected String title4PopupWindow()
    {
        return "FreezeRow";
    }

    public void populateBean(FT ft)
    {
        ((UILabel)start).setText((new StringBuilder()).append(Inter.getLocText(new String[] {
            "Frozen", "N.O."
        })).append(String.valueOf(ft.getFrom())).toString());
        ((RowSpinner)end).setValue(ft.getTo() + 1);
    }

    public FT updateBean()
    {
        return new FT(StableUtils.convertABCToInt(((UILabel)start).getText()) - 1, (int)(((RowSpinner)end).getValue() - 1.0D));
    }

    public String getLabeshow()
    {
        return Inter.getLocText("RowTo");
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
