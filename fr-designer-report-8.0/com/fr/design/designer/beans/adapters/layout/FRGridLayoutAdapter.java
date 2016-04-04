// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.designer.beans.adapters.layout;

import com.fr.design.beans.GroupModel;
import com.fr.design.designer.beans.HoverPainter;
import com.fr.design.designer.beans.painters.FRGridLayoutPainter;
import com.fr.design.designer.creator.XCreator;
import com.fr.design.designer.creator.XLayoutContainer;
import com.fr.design.designer.properties.FRGridLayoutPropertiesGroupModel;
import com.fr.design.form.layout.FRGridLayout;
import com.fr.design.utils.gui.LayoutUtils;
import java.awt.Point;

// Referenced classes of package com.fr.design.designer.beans.adapters.layout:
//            AbstractLayoutAdapter

public class FRGridLayoutAdapter extends AbstractLayoutAdapter
{

    public FRGridLayoutAdapter(XLayoutContainer xlayoutcontainer)
    {
        super(xlayoutcontainer);
    }

    public boolean accept(XCreator xcreator, int i, int j)
    {
        return true;
    }

    protected void addComp(XCreator xcreator, int i, int j)
    {
        container.add(xcreator, getLayoutGrid(xcreator, i, j));
        LayoutUtils.layoutRootContainer(container);
    }

    public HoverPainter getPainter()
    {
        return new FRGridLayoutPainter(container);
    }

    public GroupModel getLayoutProperties()
    {
        return new FRGridLayoutPropertiesGroupModel(container);
    }

    private Point getLayoutGrid(XCreator xcreator, int i, int j)
    {
        FRGridLayout frgridlayout = (FRGridLayout)container.getLayout();
        int k = container.getWidth() / frgridlayout.getColumns();
        int l = container.getHeight() / frgridlayout.getRows();
        return new Point(i / k, j / l);
    }
}
