// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.widget.wrappers;

import com.fr.design.Exception.ValidationException;
import com.fr.design.designer.properties.Decoder;
import com.fr.design.designer.properties.Encoder;
import com.fr.stable.StringUtils;
import java.awt.Color;
import java.util.StringTokenizer;

// Referenced classes of package com.fr.design.mainframe.widget.wrappers:
//            WrapperUtils

public class ColorWrapper
    implements Encoder, Decoder
{

    public ColorWrapper()
    {
    }

    public Object decode(String s)
    {
        if(StringUtils.isEmpty(s))
            return null;
        if(s.equals("black"))
            return Color.black;
        if(s.equals("blue"))
            return Color.blue;
        if(s.equals("cyan"))
            return Color.cyan;
        if(s.equals("darkGray"))
            return Color.darkGray;
        if(s.equals("gray"))
            return Color.gray;
        if(s.equals("green"))
            return Color.green;
        if(s.equals("lightGray"))
            return Color.lightGray;
        if(s.equals("magenta"))
            return Color.magenta;
        if(s.equals("orange"))
            return Color.orange;
        if(s.equals("pink"))
            return Color.pink;
        if(s.equals("red"))
            return Color.red;
        if(s.equals("white"))
            return Color.white;
        if(s.equals("yellow"))
        {
            return Color.yellow;
        } else
        {
            s = s.trim();
            s = s.substring(1, s.length() - 1).trim();
            StringTokenizer stringtokenizer = new StringTokenizer(s, ",");
            return new Color(Integer.parseInt(stringtokenizer.nextToken().trim()), Integer.parseInt(stringtokenizer.nextToken().trim()), Integer.parseInt(stringtokenizer.nextToken().trim()));
        }
    }

    public String encode(Object obj)
    {
        if(obj == null)
            return "";
        Color color = (Color)obj;
        if(color.equals(Color.black))
            return "black";
        if(color.equals(Color.blue))
            return "blue";
        if(color.equals(Color.cyan))
            return "cyan";
        if(color.equals(Color.darkGray))
            return "darkGray";
        if(color.equals(Color.gray))
            return "gray";
        if(color.equals(Color.green))
            return "green";
        if(color.equals(Color.lightGray))
            return "lightGray";
        if(color.equals(Color.magenta))
            return "magenta";
        if(color.equals(Color.orange))
            return "orange";
        if(color.equals(Color.pink))
            return "pink";
        if(color.equals(Color.red))
            return "red";
        if(color.equals(Color.white))
            return "white";
        if(color.equals(Color.yellow))
            return "yellow";
        else
            return (new StringBuilder()).append("[").append(color.getRed()).append(", ").append(color.getGreen()).append(", ").append(color.getBlue()).append("]").toString();
    }

    public void validate(String s)
        throws ValidationException
    {
        if(StringUtils.isEmpty(s))
            return;
        if(s.equals("null"))
            return;
        if(s.equals("black"))
            return;
        if(s.equals("blue"))
            return;
        if(s.equals("cyan"))
            return;
        if(s.equals("darkGray"))
            return;
        if(s.equals("gray"))
            return;
        if(s.equals("green"))
            return;
        if(s.equals("lightGray"))
            return;
        if(s.equals("magenta"))
            return;
        if(s.equals("orange"))
            return;
        if(s.equals("pink"))
            return;
        if(s.equals("red"))
            return;
        if(s.equals("white"))
            return;
        if(s.equals("yellow"))
        {
            return;
        } else
        {
            WrapperUtils.validateIntegerTxtFomat(s, 3, newValidateException());
            return;
        }
    }

    private ValidationException newValidateException()
    {
        return new ValidationException("Color string takes form like: [red, green, blue], or null, black, white, red, green, blue etc.!");
    }
}
