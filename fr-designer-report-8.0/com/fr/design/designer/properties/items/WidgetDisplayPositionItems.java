// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.designer.properties.items;

import com.fr.general.Inter;

// Referenced classes of package com.fr.design.designer.properties.items:
//            Item, ItemProvider

public class WidgetDisplayPositionItems
    implements ItemProvider
{

    private static Item VALUE_ITEMS[] = {
        new Item(Inter.getLocText("StyleAlignment-Left"), Integer.valueOf(0)), new Item(Inter.getLocText("Center"), Integer.valueOf(1)), new Item(Inter.getLocText("StyleAlignment-Right"), Integer.valueOf(2))
    };

    public WidgetDisplayPositionItems()
    {
    }

    public Item[] getItems()
    {
        return VALUE_ITEMS;
    }

}
