// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.widget.wrappers;

import com.fr.design.Exception.ValidationException;
import com.fr.design.designer.properties.Decoder;
import com.fr.design.designer.properties.Encoder;
import com.fr.form.ui.reg.CustomReg;
import com.fr.form.ui.reg.RegExp;
import com.fr.stable.StringUtils;

public class RegexWrapper
    implements Encoder, Decoder
{

    public RegexWrapper()
    {
    }

    public String encode(Object obj)
    {
        if(obj == null)
            return null;
        else
            return ((RegExp)obj).toRegText();
    }

    public Object decode(String s)
    {
        if(StringUtils.isEmpty(s))
            return null;
        else
            return new CustomReg(s);
    }

    public void validate(String s)
        throws ValidationException
    {
    }
}
