// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.designer.beans.adapters.layout;

import com.fr.design.beans.GroupModel;
import com.fr.design.designer.beans.ConstraintsGroupModel;
import com.fr.design.designer.creator.XCreator;
import com.fr.design.designer.creator.XLayoutContainer;
import com.fr.design.designer.properties.FRFormLayoutPropertiesGroupModel;
import com.fr.design.utils.gui.LayoutUtils;

// Referenced classes of package com.fr.design.designer.beans.adapters.layout:
//            AbstractLayoutAdapter

public class FRFormLayoutAdapter extends AbstractLayoutAdapter
{

    public FRFormLayoutAdapter(XLayoutContainer xlayoutcontainer)
    {
        super(xlayoutcontainer);
    }

    public boolean accept(XCreator xcreator, int i, int j)
    {
        return true;
    }

    protected void addComp(XCreator xcreator, int i, int j)
    {
        int k = xcreator.getWidth() / 2;
        int l = xcreator.getHeight() / 2;
        xcreator.setLocation(i - k, j - l);
        container.add(xcreator);
        LayoutUtils.layoutRootContainer(container);
    }

    public ConstraintsGroupModel getLayoutConstraints(XCreator xcreator)
    {
        return null;
    }

    public GroupModel getLayoutProperties()
    {
        return new FRFormLayoutPropertiesGroupModel(container);
    }
}
