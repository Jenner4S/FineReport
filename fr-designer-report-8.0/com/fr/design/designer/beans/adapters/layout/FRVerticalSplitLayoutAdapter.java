// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.designer.beans.adapters.layout;

import com.fr.design.beans.GroupModel;
import com.fr.design.designer.creator.*;
import com.fr.design.designer.properties.HorizontalSplitProperties;
import com.fr.design.form.layout.FRSplitLayout;
import com.fr.design.utils.gui.LayoutUtils;
import com.fr.form.ui.container.WVerticalSplitLayout;
import java.awt.Component;

// Referenced classes of package com.fr.design.designer.beans.adapters.layout:
//            AbstractLayoutAdapter

public class FRVerticalSplitLayoutAdapter extends AbstractLayoutAdapter
{

    public FRVerticalSplitLayoutAdapter(XLayoutContainer xlayoutcontainer)
    {
        super(xlayoutcontainer);
    }

    public boolean accept(XCreator xcreator, int i, int j)
    {
        String s = getPlacement(xcreator, i, j);
        FRSplitLayout frsplitlayout = (FRSplitLayout)container.getLayout();
        Component component = frsplitlayout.getLayoutComponent(s);
        return component == null;
    }

    protected void addComp(XCreator xcreator, int i, int j)
    {
        String s = getPlacement(xcreator, i, j);
        container.add(xcreator, s);
        LayoutUtils.layoutRootContainer(container);
    }

    public GroupModel getLayoutProperties()
    {
        XAbstractSplitLayout xabstractsplitlayout = (XAbstractSplitLayout)container;
        return new HorizontalSplitProperties(xabstractsplitlayout.toData());
    }

    protected String getPlacement(XCreator xcreator, int i, int j)
    {
        int k = container.getHeight();
        WVerticalSplitLayout wverticalsplitlayout = ((XWVerticalSplitLayout)container).toData();
        int l = (int)((double)k * wverticalsplitlayout.getRatio());
        if(j > l)
            return "center";
        else
            return "aside";
    }
}
