// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.designer.properties;

import com.fr.design.designer.properties.items.LayoutIndexItems;
import com.fr.form.ui.container.WLayout;

// Referenced classes of package com.fr.design.designer.properties:
//            EnumerationEditor

public class LayoutConstraintsEditor extends EnumerationEditor
{

    public LayoutConstraintsEditor(WLayout wlayout)
    {
        super(new LayoutIndexItems(wlayout, true));
    }
}
