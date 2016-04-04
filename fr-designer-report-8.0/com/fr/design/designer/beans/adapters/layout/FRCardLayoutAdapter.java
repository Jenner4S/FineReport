// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.designer.beans.adapters.layout;

import com.fr.design.beans.GroupModel;
import com.fr.design.designer.beans.ConstraintsGroupModel;
import com.fr.design.designer.creator.XCreator;
import com.fr.design.designer.creator.XLayoutContainer;
import com.fr.design.designer.creator.cardlayout.XWCardLayout;
import com.fr.design.designer.properties.CardLayoutConstraints;
import com.fr.design.designer.properties.CardLayoutPropertiesGroupModel;
import com.fr.design.utils.ComponentUtils;
import com.fr.design.utils.gui.LayoutUtils;
import com.fr.form.ui.Widget;
import java.awt.CardLayout;

// Referenced classes of package com.fr.design.designer.beans.adapters.layout:
//            AbstractLayoutAdapter

public class FRCardLayoutAdapter extends AbstractLayoutAdapter
{

    public FRCardLayoutAdapter(XLayoutContainer xlayoutcontainer)
    {
        super(xlayoutcontainer);
    }

    public boolean accept(XCreator xcreator, int i, int j)
    {
        return true;
    }

    public void addComp(XCreator xcreator, int i, int j)
    {
        container.add(xcreator, xcreator.toData().getWidgetName());
        LayoutUtils.layoutRootContainer(container);
    }

    public void addNextComponent(XCreator xcreator)
    {
        addComp(xcreator, -1, -1);
    }

    public void addBefore(XCreator xcreator, XCreator xcreator1)
    {
        int i = ComponentUtils.indexOfComponent(container, xcreator);
        if(i == -1)
            container.add(xcreator1, xcreator1.toData().getWidgetName(), 0);
        else
            container.add(xcreator1, xcreator1.toData().getWidgetName(), i);
        LayoutUtils.layoutRootContainer(container);
    }

    public void addAfter(XCreator xcreator, XCreator xcreator1)
    {
        int i = ComponentUtils.indexOfComponent(container, xcreator);
        if(i == -1)
            container.add(xcreator1, xcreator1.toData().getWidgetName());
        else
        if(++i >= container.getComponentCount())
            container.add(xcreator1, xcreator1.toData().getWidgetName());
        else
            container.add(xcreator1, xcreator1.toData().getWidgetName(), i);
        LayoutUtils.layoutRootContainer(container);
    }

    public void showComponent(XCreator xcreator)
    {
        java.awt.LayoutManager layoutmanager = container.getLayout();
        CardLayout cardlayout = (CardLayout)layoutmanager;
        cardlayout.show(container, xcreator.toData().getWidgetName());
    }

    public ConstraintsGroupModel getLayoutConstraints(XCreator xcreator)
    {
        return new CardLayoutConstraints((XWCardLayout)container, xcreator);
    }

    public GroupModel getLayoutProperties()
    {
        return new CardLayoutPropertiesGroupModel((XWCardLayout)container);
    }
}
