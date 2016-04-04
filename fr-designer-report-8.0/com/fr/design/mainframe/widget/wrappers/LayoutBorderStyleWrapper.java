// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.widget.wrappers;

import com.fr.design.Exception.ValidationException;
import com.fr.design.designer.properties.Decoder;
import com.fr.design.designer.properties.Encoder;
import com.fr.design.gui.xpane.LayoutBorderPane;
import com.fr.form.ui.LayoutBorderStyle;

public class LayoutBorderStyleWrapper
    implements Encoder, Decoder
{

    public LayoutBorderStyleWrapper()
    {
    }

    public String encode(Object obj)
    {
        if(obj == null)
        {
            return null;
        } else
        {
            LayoutBorderStyle layoutborderstyle = (LayoutBorderStyle)obj;
            return LayoutBorderPane.BORDER_TYPE[layoutborderstyle.getType()];
        }
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
