// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.widget.wrappers;

import com.fr.base.Formula;
import com.fr.design.Exception.ValidationException;
import com.fr.design.designer.properties.Decoder;
import com.fr.design.designer.properties.Encoder;
import com.fr.general.Inter;
import com.fr.stable.StringUtils;

public class FormulaWrapper
    implements Encoder, Decoder
{

    public FormulaWrapper()
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
        return new Formula(s);
    }

    public void validate(String s)
        throws ValidationException
    {
        if(StringUtils.isBlank(s))
            return;
        if(s.length() > 0 && s.charAt(0) == '=')
            return;
        else
            throw new ValidationException(Inter.getLocText("Formula_Tips"));
    }
}
