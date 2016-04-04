// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.widget.wrappers;

import com.fr.design.Exception.ValidationException;
import com.fr.stable.StringUtils;
import java.util.StringTokenizer;

public abstract class WrapperUtils
{

    private WrapperUtils()
    {
    }

    public static void validateIntegerTxtFomat(String s, int i, ValidationException validationexception)
        throws ValidationException
    {
        if(StringUtils.isEmpty(s))
            return;
        s = s.trim();
        if(s.length() < i * 2 + 1)
            throw validationexception;
        char c = s.charAt(0);
        if(c != '[')
            throw validationexception;
        c = s.charAt(s.length() - 1);
        if(c != ']')
        {
            throw validationexception;
        } else
        {
            s = s.substring(1, s.length() - 1).trim();
            StringTokenizer stringtokenizer = new StringTokenizer(s, ",");
            validateTokenizerParseInt(stringtokenizer, i, validationexception);
            return;
        }
    }

    public static void validateTokenizerParseInt(StringTokenizer stringtokenizer, int i, ValidationException validationexception)
        throws ValidationException
    {
        if(stringtokenizer.hasMoreTokens())
        {
            try
            {
                Integer.parseInt(stringtokenizer.nextToken().trim());
            }
            catch(NumberFormatException numberformatexception)
            {
                throw validationexception;
            }
            validateTokenizerParseInt(stringtokenizer, i - 1, validationexception);
        }
    }
}
