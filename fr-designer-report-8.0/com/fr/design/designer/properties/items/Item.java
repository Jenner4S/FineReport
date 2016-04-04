// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.designer.properties.items;


public class Item
{

    private Object value;
    private String name;

    public Item(String s, Object obj)
    {
        name = s;
        value = obj;
    }

    public Object getValue()
    {
        return value;
    }

    public String getName()
    {
        return name;
    }

    public String toString()
    {
        return name;
    }

    public boolean equals(Object obj)
    {
        if(obj == null)
            return false;
        if(obj instanceof Item)
        {
            Item item = (Item)obj;
            Object obj1 = item.getValue();
            if(value == null)
                return obj1 == null;
            if(obj1 == null)
                return false;
            else
                return value.equals(obj1);
        } else
        {
            return false;
        }
    }
}
