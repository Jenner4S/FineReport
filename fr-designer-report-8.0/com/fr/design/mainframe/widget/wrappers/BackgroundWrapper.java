// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.widget.wrappers;

import com.fr.base.background.*;
import com.fr.design.Exception.ValidationException;
import com.fr.design.designer.properties.Decoder;
import com.fr.design.designer.properties.Encoder;
import com.fr.general.Inter;

public class BackgroundWrapper
    implements Encoder, Decoder
{

    public BackgroundWrapper()
    {
    }

    public String encode(Object obj)
    {
        if(obj instanceof ColorBackground)
            return Inter.getLocText("Color");
        if(obj instanceof TextureBackground)
            return Inter.getLocText("Background-Texture");
        if(obj instanceof PatternBackground)
            return Inter.getLocText("Background-Pattern");
        if(obj instanceof ImageBackground)
            return Inter.getLocText("Image");
        if(obj instanceof GradientBackground)
            return Inter.getLocText("Gradient-Color");
        else
            return Inter.getLocText("None");
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
