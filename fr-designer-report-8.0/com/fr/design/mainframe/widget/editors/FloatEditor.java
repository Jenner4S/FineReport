// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.widget.editors;

import java.text.NumberFormat;

// Referenced classes of package com.fr.design.mainframe.widget.editors:
//            FormattedEditor

public class FloatEditor extends FormattedEditor
{

    public FloatEditor()
    {
        super(NumberFormat.getNumberInstance());
    }

    public Object getValue()
    {
        Object obj = super.getValue();
        if(obj == null)
            return new Float(0.0F);
        if(obj instanceof Number)
            return new Float(((Number)obj).floatValue());
        else
            return obj;
    }
}
