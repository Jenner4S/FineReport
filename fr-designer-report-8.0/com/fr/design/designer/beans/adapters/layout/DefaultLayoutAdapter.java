// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.designer.beans.adapters.layout;

import com.fr.design.designer.beans.HoverPainter;
import com.fr.design.designer.creator.XCreator;
import com.fr.design.designer.creator.XLayoutContainer;
import com.fr.design.mainframe.FormDesigner;

// Referenced classes of package com.fr.design.designer.beans.adapters.layout:
//            AbstractLayoutAdapter

public class DefaultLayoutAdapter extends AbstractLayoutAdapter
{

    public DefaultLayoutAdapter(FormDesigner formdesigner, XLayoutContainer xlayoutcontainer)
    {
        super(xlayoutcontainer);
    }

    public HoverPainter getPainter()
    {
        return null;
    }

    public void addComp(XCreator xcreator, int i, int j)
    {
    }

    public boolean accept(XCreator xcreator, int i, int j)
    {
        return false;
    }
}
