// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.designer.properties.items;

import com.fr.general.Inter;

// Referenced classes of package com.fr.design.designer.properties.items:
//            Item, ItemProvider

public class UnderlineItems
    implements ItemProvider
{

    private static Item VALUE_ITEMS[] = {
        new Item(Inter.getLocText("DataFunction-None"), ""), new Item(Inter.getLocText("StyleAlignment-Top"), "overline"), new Item(Inter.getLocText("Center"), "line-through"), new Item(Inter.getLocText("StyleAlignment-Bottom"), "underline")
    };

    public UnderlineItems()
    {
    }

    public Item[] getItems()
    {
        return VALUE_ITEMS;
    }

}
