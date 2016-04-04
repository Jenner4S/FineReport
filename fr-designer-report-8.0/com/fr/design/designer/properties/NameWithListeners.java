// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.designer.properties;

import com.fr.form.event.Listener;
import com.fr.general.ComparatorUtils;

public class NameWithListeners
{

    private String name;
    private Listener ls[];

    public NameWithListeners(String s, Listener alistener[])
    {
        name = s;
        ls = alistener != null ? alistener : new Listener[0];
    }

    public String getName()
    {
        return name;
    }

    public Listener[] getListeners()
    {
        return ls;
    }

    public int getCountOfListeners4ThisName()
    {
        int i = 0;
        for(int j = 0; j < ls.length; j++)
            if(ComparatorUtils.equals(name, ls[j].getEventName()))
                i++;

        return i;
    }
}
