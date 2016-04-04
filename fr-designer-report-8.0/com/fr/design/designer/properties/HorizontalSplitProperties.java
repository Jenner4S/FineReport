// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.designer.properties;

import com.fr.form.ui.container.WSplitLayout;
import com.fr.general.Inter;

// Referenced classes of package com.fr.design.designer.properties:
//            VerticalSplitProperties

public class HorizontalSplitProperties extends VerticalSplitProperties
{

    public HorizontalSplitProperties(WSplitLayout wsplitlayout)
    {
        super(wsplitlayout);
    }

    public String getGroupName()
    {
        return Inter.getLocText("Horizontal-Split_Layout");
    }
}
