// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.widget.wrappers.primitive;

import com.fr.design.Exception.ValidationException;
import com.fr.design.designer.properties.Decoder;
import com.fr.design.designer.properties.Encoder;

public class LongWrapper
    implements Encoder, Decoder
{

    public LongWrapper()
    {
    }

    public String encode(Object obj)
    {
        if(obj == null)
            return "0";
        else
            return obj.toString();
    }

    public Object decode(String s)
    {
        if(s == null)
            return Long.valueOf(0L);
        else
            return Long.valueOf(Long.parseLong(s));
    }

    public void validate(String s)
        throws ValidationException
    {
        try
        {
            Long.parseLong(s);
        }
        catch(NumberFormatException numberformatexception)
        {
            throw new ValidationException(numberformatexception.getMessage());
        }
    }
}
