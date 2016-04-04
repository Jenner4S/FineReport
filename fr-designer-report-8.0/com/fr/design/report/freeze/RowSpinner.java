// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.report.freeze;

import com.fr.design.gui.ispinner.UISpinner;
import com.fr.design.gui.itextfield.UINumberField;

// Referenced classes of package com.fr.design.report.freeze:
//            UIIntNumberField

public class RowSpinner extends UISpinner
{

    public RowSpinner(double d, double d1, double d2)
    {
        super(d, d1, d2);
    }

    public RowSpinner(double d, double d1, double d2, double d3)
    {
        super(d, d1, d2, d3);
    }

    protected UINumberField initNumberField()
    {
        return new UIIntNumberField();
    }
}
