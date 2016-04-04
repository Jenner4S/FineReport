// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.widget.wrappers;

import com.fr.design.Exception.ValidationException;
import com.fr.design.designer.properties.Decoder;
import com.fr.design.designer.properties.Encoder;
import com.fr.stable.StringUtils;
import java.awt.Rectangle;
import java.util.StringTokenizer;

// Referenced classes of package com.fr.design.mainframe.widget.wrappers:
//            WrapperUtils

public class RectangleWrapper
    implements Encoder, Decoder
{

    public RectangleWrapper()
    {
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
            return new Rectangle(Integer.parseInt(stringtokenizer.nextToken().trim()), Integer.parseInt(stringtokenizer.nextToken().trim()), Integer.parseInt(stringtokenizer.nextToken().trim()), Integer.parseInt(stringtokenizer.nextToken().trim()));
        }
    }

    public String encode(Object obj)
    {
        if(obj == null)
        {
            return null;
        } else
        {
            Rectangle rectangle = (Rectangle)obj;
            return (new StringBuilder()).append("[").append(rectangle.x).append(", ").append(rectangle.y).append(", ").append(rectangle.width).append(", ").append(rectangle.height).append("]").toString();
        }
    }

    public void validate(String s)
        throws ValidationException
    {
        WrapperUtils.validateIntegerTxtFomat(s, 4, newValidationException());
    }

    private ValidationException newValidationException()
    {
        return new ValidationException("Rectangle string takes form like: [X, Y, width, height]!");
    }
}
