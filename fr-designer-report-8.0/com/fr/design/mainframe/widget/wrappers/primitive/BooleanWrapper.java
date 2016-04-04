// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.widget.wrappers.primitive;

import com.fr.design.Exception.ValidationException;
import com.fr.design.designer.properties.Decoder;
import com.fr.design.designer.properties.Encoder;

public class BooleanWrapper
    implements Encoder, Decoder
{

    public BooleanWrapper()
    {
    }

    public String encode(Object obj)
    {
        if(obj == null)
        {
            return "false";
        } else
        {
            Boolean boolean1 = (Boolean)obj;
            return boolean1.booleanValue() ? "true" : "false";
        }
    }

    public Object decode(String s)
    {
        if(s == null)
            return Boolean.valueOf(false);
        else
            return Boolean.valueOf(s.equals("true"));
    }

    public void validate(String s)
        throws ValidationException
    {
        if(s == null)
            throw new ValidationException("Boolean value should be either true or false!");
        if(!s.equals("true") && !s.equals("false"))
            throw new ValidationException("Boolean value should be either true or false!");
        else
            return;
    }
}
