// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.widget.wrappers;

import com.fr.design.Exception.ValidationException;
import com.fr.design.designer.properties.Decoder;
import com.fr.design.designer.properties.Encoder;

public class DateWrapper
    implements Encoder, Decoder
{

    public DateWrapper()
    {
    }

    public Object decode(String s)
    {
        return s;
    }

    public void validate(String s)
        throws ValidationException
    {
    }

    public String encode(Object obj)
    {
        return (String)obj;
    }
}
