// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.widget.wrappers;

import com.fr.design.designer.properties.Encoder;
import com.fr.general.Inter;

public class GridWidgetWrapper
    implements Encoder
{

    public GridWidgetWrapper()
    {
    }

    public String encode(Object obj)
    {
        if(obj != null)
            return Inter.getLocText(new String[] {
                "Widget", "Set"
            });
        else
            return null;
    }
}
