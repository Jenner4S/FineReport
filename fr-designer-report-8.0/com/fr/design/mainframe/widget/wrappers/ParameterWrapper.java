// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.widget.wrappers;

import com.fr.design.Exception.ValidationException;
import com.fr.design.designer.properties.Decoder;
import com.fr.design.designer.properties.Encoder;
import com.fr.general.Inter;

public class ParameterWrapper
    implements Encoder, Decoder
{

    public ParameterWrapper()
    {
    }

    public String encode(Object obj)
    {
        if(obj == null)
            return "";
        else
            return (new StringBuilder()).append(Inter.getLocText("Total")).append(((com.fr.stable.ParameterProvider[])(com.fr.stable.ParameterProvider[])obj).length).append(Inter.getLocText("Parameters")).toString();
    }

    public Object decode(String s)
    {
        return null;
    }

    public void validate(String s)
        throws ValidationException
    {
    }
}
