// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.widget.accessibles;

import com.fr.design.designer.properties.Encoder;

// Referenced classes of package com.fr.design.mainframe.widget.accessibles:
//            BaseAccessibleEditor

public abstract class UneditableAccessibleEditor extends BaseAccessibleEditor
{

    protected Object value;

    public UneditableAccessibleEditor(Encoder encoder)
    {
        super(encoder, null, true);
    }

    public Object getValue()
    {
        return value;
    }

    public void setValue(Object obj)
    {
        value = obj;
        super.setValue(obj);
    }
}
