// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.widget.wrappers;

import com.fr.design.designer.properties.Encoder;
import com.fr.design.designer.properties.NameWithListeners;
import com.fr.general.Inter;

public class EventHandlerWrapper
    implements Encoder
{

    public EventHandlerWrapper()
    {
    }

    public String encode(Object obj)
    {
        if(obj == null)
        {
            return null;
        } else
        {
            NameWithListeners namewithlisteners = (NameWithListeners)obj;
            return (new StringBuilder()).append(Inter.getLocText("Page_Total")).append(namewithlisteners.getCountOfListeners4ThisName()).append(Inter.getLocText("Ge")).append(namewithlisteners.getName()).append(Inter.getLocText("Event")).toString();
        }
    }
}
