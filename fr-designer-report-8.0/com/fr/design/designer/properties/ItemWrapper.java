// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.designer.properties;

import com.fr.design.Exception.ValidationException;
import com.fr.design.designer.properties.items.Item;
import com.fr.design.designer.properties.items.ItemProvider;

// Referenced classes of package com.fr.design.designer.properties:
//            Encoder, Decoder

public class ItemWrapper
    implements Encoder, Decoder
{

    private Item items[];

    public ItemWrapper(ItemProvider itemprovider)
    {
        this(itemprovider.getItems());
    }

    public ItemWrapper(Item aitem[])
    {
        items = aitem;
    }

    public Object decode(String s)
    {
        Item aitem[] = items;
        int i = aitem.length;
        for(int j = 0; j < i; j++)
        {
            Item item = aitem[j];
            if(s.equals(item.getName()))
                return item.getValue();
        }

        return null;
    }

    public String encode(Object obj)
    {
        Item aitem[] = items;
        int i = aitem.length;
        for(int j = 0; j < i; j++)
        {
            Item item = aitem[j];
            if(item.getValue().equals(obj))
                return item.getName();
        }

        return null;
    }

    public void validate(String s)
        throws ValidationException
    {
        Item aitem[] = items;
        int i = aitem.length;
        for(int j = 0; j < i; j++)
        {
            Item item = aitem[j];
            if(s.equals(item.getName()))
                return;
        }

        throw new ValidationException((new StringBuilder()).append("No such element:").append(s).toString());
    }
}
