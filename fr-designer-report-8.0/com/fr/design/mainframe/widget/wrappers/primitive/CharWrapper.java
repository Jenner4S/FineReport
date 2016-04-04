// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.widget.wrappers.primitive;

import com.fr.design.Exception.ValidationException;
import com.fr.design.designer.properties.Decoder;
import com.fr.design.designer.properties.Encoder;

public class CharWrapper
    implements Encoder, Decoder
{

    public CharWrapper()
    {
    }

    public String encode(Object obj)
    {
        if(obj == null)
            return "\\0";
        else
            return obj.toString();
    }

    public Object decode(String s)
    {
        if(s == null || s.length() == 0)
            return Character.valueOf('\0');
        if(s.equals("\\0"))
            return Character.valueOf('\0');
        else
            return Character.valueOf(s.charAt(0));
    }

    public void validate(String s)
        throws ValidationException
    {
        if(s == null || s.length() != 1)
            throw new ValidationException("Character should be 1 character long!");
        else
            return;
    }
}
