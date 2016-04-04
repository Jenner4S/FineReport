// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.designer.properties.items;

import com.fr.base.Utils;
import com.fr.form.ui.Widget;
import com.fr.form.ui.container.WLayout;

// Referenced classes of package com.fr.design.designer.properties.items:
//            Item, ItemProvider

public class LayoutIndexItems
    implements ItemProvider
{

    private WLayout layout;
    private boolean chooseIndexNotName;

    public LayoutIndexItems(WLayout wlayout, boolean flag)
    {
        layout = wlayout;
        chooseIndexNotName = flag;
    }

    public Item[] getItems()
    {
        int i = layout.getWidgetCount();
        Item aitem[] = new Item[i];
        for(int j = 0; j < i; j++)
            if(chooseIndexNotName)
                aitem[j] = new Item(Utils.doubleToString(j + 1), Integer.valueOf(j + 1));
            else
                aitem[j] = new Item(layout.getWidget(j).getWidgetName(), Integer.valueOf(j));

        return aitem;
    }
}
