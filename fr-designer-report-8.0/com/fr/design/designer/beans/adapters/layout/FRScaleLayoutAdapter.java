// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.designer.beans.adapters.layout;

import com.fr.design.beans.GroupModel;
import com.fr.design.designer.creator.XCreator;
import com.fr.design.designer.creator.XLayoutContainer;

// Referenced classes of package com.fr.design.designer.beans.adapters.layout:
//            AbstractLayoutAdapter

public class FRScaleLayoutAdapter extends AbstractLayoutAdapter
{

    public FRScaleLayoutAdapter(XLayoutContainer xlayoutcontainer)
    {
        super(xlayoutcontainer);
    }

    public boolean accept(XCreator xcreator, int i, int j)
    {
        return false;
    }

    protected void addComp(XCreator xcreator, int i, int j)
    {
    }

    public GroupModel getLayoutProperties()
    {
        return null;
    }
}
