// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.widget.wrappers;

import com.fr.design.Exception.ValidationException;
import com.fr.design.designer.properties.Decoder;
import com.fr.design.designer.properties.Encoder;
import com.fr.stable.StringUtils;
import java.awt.Point;
import java.util.StringTokenizer;

// Referenced classes of package com.fr.design.mainframe.widget.wrappers:
//            WrapperUtils

public class PointWrapper
    implements Encoder, Decoder
{

    public PointWrapper()
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
            return new Point(Integer.parseInt(stringtokenizer.nextToken().trim()), Integer.parseInt(stringtokenizer.nextToken().trim()));
        }
    }

    public String encode(Object obj)
    {
        if(obj == null)
        {
            return null;
        } else
        {
            Point point = (Point)obj;
            return (new StringBuilder()).append("[").append(point.x).append(", ").append(point.y).append("]").toString();
        }
    }

    public void validate(String s)
        throws ValidationException
    {
        WrapperUtils.validateIntegerTxtFomat(s, 2, newValidateException());
    }

    private ValidationException newValidateException()
    {
        return new ValidationException("Point string takes form like: [width, height]!");
    }
}
