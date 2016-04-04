// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.designer.properties.items;

import com.fr.general.Inter;

// Referenced classes of package com.fr.design.designer.properties.items:
//            Item, ItemProvider

public class FRFitConstraintsItems
    implements ItemProvider
{

    public static final Item ITEMS[] = {
        new Item(Inter.getLocText("Adaptive_Full_Area"), Integer.valueOf(0)), new Item(Inter.getLocText("Adaptive_Original_Scale"), Integer.valueOf(1))
    };

    public FRFitConstraintsItems()
    {
    }

    public Item[] getItems()
    {
        return ITEMS;
    }

}
