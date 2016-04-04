// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.designer.beans.adapters.layout;

import com.fr.design.designer.creator.*;
import com.fr.form.ui.container.WHorizontalSplitLayout;

// Referenced classes of package com.fr.design.designer.beans.adapters.layout:
//            FRVerticalSplitLayoutAdapter

public class FRHorizontalSplitLayoutAdapter extends FRVerticalSplitLayoutAdapter
{

    public FRHorizontalSplitLayoutAdapter(XLayoutContainer xlayoutcontainer)
    {
        super(xlayoutcontainer);
    }

    protected String getPlacement(XCreator xcreator, int i, int j)
    {
        int k = container.getWidth();
        WHorizontalSplitLayout whorizontalsplitlayout = ((XWHorizontalSplitLayout)container).toData();
        int l = (int)((double)k * whorizontalsplitlayout.getRatio());
        if(i > l)
            return "center";
        else
            return "aside";
    }
}
