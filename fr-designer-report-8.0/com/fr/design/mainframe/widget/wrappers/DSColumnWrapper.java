// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.widget.wrappers;

import com.fr.design.Exception.ValidationException;
import com.fr.design.designer.properties.Decoder;
import com.fr.design.designer.properties.Encoder;

public class DSColumnWrapper
    implements Encoder, Decoder
{

    public DSColumnWrapper()
    {
    }

    public String encode(Object obj)
    {
        if(obj == null)
            return "";
        else
            return obj.toString();
    }

    public Object decode(String s)
    {
        return s;
    }

    public void validate(String s)
        throws ValidationException
    {
    }
}
