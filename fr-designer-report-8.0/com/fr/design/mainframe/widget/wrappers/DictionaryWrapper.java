// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.widget.wrappers;

import com.fr.design.Exception.ValidationException;
import com.fr.design.designer.properties.Decoder;
import com.fr.design.designer.properties.Encoder;

public class DictionaryWrapper
    implements Encoder, Decoder
{

    public DictionaryWrapper()
    {
    }

    public Object decode(String s)
    {
        return s;
    }

    public String encode(Object obj)
    {
        if(obj == null)
            return "";
        else
            return obj.toString();
    }

    public void validate(String s)
        throws ValidationException
    {
    }
}
