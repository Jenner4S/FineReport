// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.widget.editors;

import java.text.NumberFormat;

// Referenced classes of package com.fr.design.mainframe.widget.editors:
//            FormattedEditor

public class DoubleEditor extends FormattedEditor
{

    public DoubleEditor()
    {
        super(NumberFormat.getNumberInstance());
    }

    public Object getValue()
    {
        Object obj = super.getValue();
        if(obj == null)
            return new Double(0.0D);
        if(obj instanceof Number)
            return new Double(((Number)obj).doubleValue());
        else
            return obj;
    }
}
