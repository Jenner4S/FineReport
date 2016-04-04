// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.widget.wrappers;

import com.fr.design.Exception.ValidationException;
import com.fr.design.designer.properties.Decoder;
import com.fr.design.designer.properties.Encoder;
import com.fr.general.Inter;
import com.fr.js.NameJavaScriptGroup;

public class HyperlinkGroupWrapper
    implements Encoder, Decoder
{

    public HyperlinkGroupWrapper()
    {
    }

    public Object decode(String s)
    {
        return null;
    }

    public String encode(Object obj)
    {
        if(obj == null)
        {
            return Inter.getLocText(new String[] {
                "HF-Undefined", "Hyperlink"
            });
        } else
        {
            NameJavaScriptGroup namejavascriptgroup = (NameJavaScriptGroup)obj;
            return (new StringBuilder()).append(Inter.getLocText(new String[] {
                "Total", "Has"
            })).append(namejavascriptgroup.size()).append(Inter.getLocText(new String[] {
                "SpecifiedG-Groups", "Hyperlink"
            })).toString();
        }
    }

    public void validate(String s)
        throws ValidationException
    {
    }
}
