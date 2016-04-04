// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.widget.wrappers;

import com.fr.design.Exception.ValidationException;
import com.fr.design.designer.properties.Decoder;
import com.fr.design.designer.properties.Encoder;
import com.fr.form.ui.PaddingMargin;
import com.fr.stable.StringUtils;
import java.util.StringTokenizer;

public class PaddingMarginWrapper
    implements Encoder, Decoder
{

    public PaddingMarginWrapper()
    {
    }

    public String encode(Object obj)
    {
        if(obj == null)
        {
            return null;
        } else
        {
            PaddingMargin paddingmargin = (PaddingMargin)obj;
            return paddingmargin.toString();
        }
    }

    public Object decode(String s)
    {
        if(StringUtils.isEmpty(s))
        {
            return null;
        } else
        {
            s = s.trim();
            s = s.substring(1, s.length() - 1).trim();
            StringTokenizer stringtokenizer = new StringTokenizer(s, ",");
            return new PaddingMargin(Integer.parseInt(stringtokenizer.nextToken().trim()), Integer.parseInt(stringtokenizer.nextToken().trim()), Integer.parseInt(stringtokenizer.nextToken().trim()), Integer.parseInt(stringtokenizer.nextToken().trim()));
        }
    }

    public void validate(String s)
        throws ValidationException
    {
        if(StringUtils.isEmpty(s))
            return;
        s = s.trim();
        if(s.length() < 9)
            throw new ValidationException("String takes form like: [top, left, bottom, right]!");
        else
            return;
    }
}
