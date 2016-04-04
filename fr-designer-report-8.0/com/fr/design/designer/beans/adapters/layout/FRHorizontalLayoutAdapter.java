// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.designer.beans.adapters.layout;

import com.fr.design.beans.GroupModel;
import com.fr.design.designer.beans.ConstraintsGroupModel;
import com.fr.design.designer.beans.HoverPainter;
import com.fr.design.designer.beans.painters.FRHorizontalLayoutPainter;
import com.fr.design.designer.creator.*;
import com.fr.design.designer.properties.HorizontalLayoutConstraints;
import com.fr.design.designer.properties.HorizontalLayoutPropertiesGroupModel;
import com.fr.design.utils.gui.LayoutUtils;
import com.fr.form.ui.container.WHorizontalBoxLayout;
import java.awt.*;

// Referenced classes of package com.fr.design.designer.beans.adapters.layout:
//            AbstractLayoutAdapter

public class FRHorizontalLayoutAdapter extends AbstractLayoutAdapter
{

    public FRHorizontalLayoutAdapter(XLayoutContainer xlayoutcontainer)
    {
        super(xlayoutcontainer);
    }

    public boolean accept(XCreator xcreator, int i, int j)
    {
        return true;
    }

    public void addComp(XCreator xcreator, int i, int j)
    {
        if(whetherUseBackupSize(xcreator))
            xcreator.useBackupSize();
        container.add(xcreator, getPlaceIndex(i));
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
            if(i < rectangle.x)
                return k;
        }

        if(byte0 == -1)
            return j;
        else
            return byte0;
    }

    public void fix(XCreator xcreator)
    {
        WHorizontalBoxLayout whorizontalboxlayout = ((XWHorizontalBoxLayout)container).toData();
        com.fr.form.ui.Widget widget = ((XWidgetCreator)xcreator).toData();
        xcreator.setPreferredSize(new Dimension(xcreator.getWidth(), 0));
        whorizontalboxlayout.setWidthAtWidget(widget, xcreator.getWidth());
    }

    public HoverPainter getPainter()
    {
        return new FRHorizontalLayoutPainter(container);
    }

    public ConstraintsGroupModel getLayoutConstraints(XCreator xcreator)
    {
        return new HorizontalLayoutConstraints(container, xcreator);
    }

    public GroupModel getLayoutProperties()
    {
        return new HorizontalLayoutPropertiesGroupModel((XWHorizontalBoxLayout)container);
    }
}
