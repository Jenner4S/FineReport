// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.designer.beans.adapters.layout;

import com.fr.design.beans.GroupModel;
import com.fr.design.designer.beans.*;
import com.fr.design.designer.beans.painters.NullPainter;
import com.fr.design.designer.creator.*;
import com.fr.design.utils.ComponentUtils;
import com.fr.design.utils.gui.LayoutUtils;
import com.fr.general.ComparatorUtils;
import java.awt.LayoutManager;

public abstract class AbstractLayoutAdapter
    implements LayoutAdapter
{

    protected XLayoutContainer container;
    protected LayoutManager layout;

    public AbstractLayoutAdapter(XLayoutContainer xlayoutcontainer)
    {
        container = xlayoutcontainer;
        layout = xlayoutcontainer.getLayout();
    }

    public boolean whetherUseBackupSize(XCreator xcreator)
    {
        Class class1 = container.getClass();
        Class class2 = null;
        if(xcreator.getBackupParent() != null)
            class2 = xcreator.getBackupParent().getClass();
        return ComparatorUtils.equals(class2, class1) && supportBackupSize();
    }

    public boolean supportBackupSize()
    {
        return false;
    }

    public void fix(XCreator xcreator)
    {
    }

    public void showComponent(XCreator xcreator)
    {
        xcreator.setVisible(true);
    }

    public boolean addBean(XCreator xcreator, int i, int j)
    {
        if(!accept(xcreator, i, j))
        {
            return false;
        } else
        {
            addComp(xcreator, i, j);
            ((XWidgetCreator)xcreator).recalculateChildrenSize();
            return true;
        }
    }

    public void removeBean(XCreator xcreator, int i, int j)
    {
        delete(xcreator, i, j);
    }

    protected void delete(XCreator xcreator, int i, int j)
    {
    }

    protected abstract void addComp(XCreator xcreator, int i, int j);

    public void addNextComponent(XCreator xcreator)
    {
        container.add(xcreator);
        LayoutUtils.layoutRootContainer(container);
    }

    public void addBefore(XCreator xcreator, XCreator xcreator1)
    {
        int i = ComponentUtils.indexOfComponent(container, xcreator);
        if(i == -1)
            container.add(xcreator1, 0);
        else
            container.add(xcreator1, i);
        LayoutUtils.layoutRootContainer(container);
    }

    public void addAfter(XCreator xcreator, XCreator xcreator1)
    {
        int i = ComponentUtils.indexOfComponent(container, xcreator);
        if(i == -1)
            container.add(xcreator1);
        else
        if(++i >= container.getComponentCount())
            container.add(xcreator1);
        else
            container.add(xcreator1, i);
        LayoutUtils.layoutRootContainer(container);
    }

    public HoverPainter getPainter()
    {
        return new NullPainter(container);
    }

    public boolean canAcceptMoreComponent()
    {
        return true;
    }

    public ConstraintsGroupModel getLayoutConstraints(XCreator xcreator)
    {
        return null;
    }

    public GroupModel getLayoutProperties()
    {
        return null;
    }
}
