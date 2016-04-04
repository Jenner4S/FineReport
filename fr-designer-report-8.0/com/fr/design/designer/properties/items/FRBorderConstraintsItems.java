// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.designer.properties.items;

import com.fr.general.Inter;
import com.fr.stable.ArrayUtils;

// Referenced classes of package com.fr.design.designer.properties.items:
//            Item, ItemProvider

public class FRBorderConstraintsItems
    implements ItemProvider
{

    private Item VALUE_ITEMS[];

    public FRBorderConstraintsItems(String as[])
    {
        Item aitem[] = createItems(as);
        VALUE_ITEMS = (Item[])(Item[])ArrayUtils.add(aitem, new Item(Inter.getLocText("BorderLayout-Center"), "Center"));
    }

    public Item[] getItems()
    {
        return VALUE_ITEMS;
    }

    public static Item[] createItems(String as[])
    {
        Item aitem[] = new Item[as.length];
        for(int i = 0; i < as.length; i++)
        {
            if("North" == as[i])
            {
                aitem[i] = new Item(Inter.getLocText("BorderLayout-North"), "North");
                continue;
            }
            if("South" == as[i])
            {
                aitem[i] = new Item(Inter.getLocText("BorderLayout-South"), "South");
                continue;
            }
            if("West" == as[i])
            {
                aitem[i] = new Item(Inter.getLocText("BorderLayout-West"), "West");
                continue;
            }
            if("East" == as[i])
                aitem[i] = new Item(Inter.getLocText("BorderLayout-East"), "East");
        }

        return aitem;
    }
}
