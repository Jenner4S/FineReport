// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.widget.wrappers;

import com.fr.design.Exception.ValidationException;
import com.fr.design.designer.properties.Decoder;
import com.fr.design.designer.properties.Encoder;
import com.fr.general.FRFont;
import com.fr.stable.StringUtils;
import java.awt.Font;
import java.util.StringTokenizer;

// Referenced classes of package com.fr.design.mainframe.widget.wrappers:
//            WrapperUtils

public class FontWrapper
    implements Encoder, Decoder
{

    public FontWrapper()
    {
    }

    public Object decode(String s)
    {
        if(StringUtils.isEmpty(s))
        {
            return FRFont.getInstance();
        } else
        {
            s = s.trim();
            s = s.substring(1, s.length() - 1).trim();
            StringTokenizer stringtokenizer = new StringTokenizer(s, ",");
            return FRFont.getInstance(new Font(stringtokenizer.nextToken().trim(), Integer.parseInt(stringtokenizer.nextToken().trim()), Integer.parseInt(stringtokenizer.nextToken().trim())));
        }
    }

    public String encode(Object obj)
    {
        if(obj == null)
        {
            return null;
        } else
        {
            FRFont frfont = (FRFont)obj;
            return (new StringBuilder()).append("[").append(frfont.getFamily()).append(", ").append(frfont.getStyleName()).append(", ").append(frfont.getSize()).append("]").toString();
        }
    }

    public void validate(String s)
        throws ValidationException
    {
        WrapperUtils.validateIntegerTxtFomat(s, 3, newValidateException());
    }

    private ValidationException newValidateException()
        throws ValidationException
    {
        return new ValidationException("Font string takes form like: [family, style, size]!");
    }
}
