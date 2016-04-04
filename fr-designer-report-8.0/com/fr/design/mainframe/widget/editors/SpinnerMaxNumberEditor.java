// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.widget.editors;

import com.fr.form.ui.NumberEditor;

// Referenced classes of package com.fr.design.mainframe.widget.editors:
//            SpinnerNumberEditor

public class SpinnerMaxNumberEditor extends SpinnerNumberEditor
{

    public SpinnerMaxNumberEditor(Object obj)
    {
        super(obj);
    }

    protected Double getDefaultLimit()
    {
        return Double.valueOf(1.7976931348623157E+308D);
    }

    protected Double getLimitValue()
    {
        return Double.valueOf(widget.getMaxValue());
    }
}
