// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.designer.beans.adapters.layout;

import com.fr.design.beans.GroupModel;
import com.fr.design.designer.beans.ConstraintsGroupModel;
import com.fr.design.designer.beans.HoverPainter;
import com.fr.design.designer.beans.painters.FRVerticalLayoutPainter;
import com.fr.design.designer.creator.*;
import com.fr.design.designer.properties.VerticalBoxProperties;
import com.fr.design.designer.properties.VerticalLayoutConstraints;
import com.fr.design.utils.gui.LayoutUtils;
import com.fr.form.ui.container.WVerticalBoxLayout;
import java.awt.*;

// Referenced classes of package com.fr.design.designer.beans.adapters.layout:
//            AbstractLayoutAdapter

public class FRVerticalLayoutAdapter extends AbstractLayoutAdapter
{

    public FRVerticalLayoutAdapter(XLayoutContainer xlayoutcontainer)
    {
        super(xlayoutcontainer);
    }

    public boolean accept(XCreator xcreator, int i, int j)
    {
        return true;
    }

    protected void addComp(XCreator xcreator, int i, int j)
    {
        if(whetherUseBackupSize(xcreator))
            xcreator.useBackupSize();
        container.add(xcreator, getPlaceIndex(j));
        LayoutUtils.layoutRootContainer(container);
    }

    public boolean supportBackupSize()
    {
        return true;
    }

    private int getPlaceIndex(int i)
    {
        byte byte0 = -1;
        int j = container.getComponentCount();
        for(int k = 0; k < j; k++)
        {
            Rectangle rectangle = container.getComponent(k).getBounds();
            if(i < rectangle.y)
                return k;
        }

        if(byte0 == -1)
            return j;
        else
            return byte0;
    }

    public void fix(XCreator xcreator)
    {
        WVerticalBoxLayout wverticalboxlayout = ((XWVerticalBoxLayout)container).toData();
        com.fr.form.ui.Widget widget = ((XWidgetCreator)xcreator).toData();
        xcreator.setPreferredSize(new Dimension(0, xcreator.getHeight()));
        wverticalboxlayout.setHeightAtWidget(widget, xcreator.getHeight());
    }

    public HoverPainter getPainter()
    {
        return new FRVerticalLayoutPainter(container);
    }

    public ConstraintsGroupModel getLayoutConstraints(XCreator xcreator)
    {
        return new VerticalLayoutConstraints(container, xcreator);
    }

    public GroupModel getLayoutProperties()
    {
        XWVerticalBoxLayout xwverticalboxlayout = (XWVerticalBoxLayout)container;
        return new VerticalBoxProperties(xwverticalboxlayout);
    }
}
