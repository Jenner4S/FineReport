// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.designer.properties;

import com.fr.design.designer.properties.items.LayoutIndexItems;
import com.fr.form.ui.container.WCardLayout;

// Referenced classes of package com.fr.design.designer.properties:
//            ItemWrapper

public class CardDefaultShowWrapper extends ItemWrapper
{

    public CardDefaultShowWrapper(WCardLayout wcardlayout)
    {
        super(new LayoutIndexItems(wcardlayout, false));
    }
}
