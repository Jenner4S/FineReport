// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.designer.creator;

import com.fr.design.designer.beans.LayoutAdapter;
import com.fr.design.designer.beans.adapters.layout.FRHorizontalLayoutAdapter;
import com.fr.design.form.layout.FRHorizontalLayout;
import com.fr.form.ui.AbstractBorderStyleWidget;
import com.fr.form.ui.Widget;
import com.fr.form.ui.container.WHorizontalBoxLayout;
import com.fr.form.ui.container.WLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ContainerEvent;

// Referenced classes of package com.fr.design.designer.creator:
//            XLayoutContainer, XWidgetCreator, XCreator

public class XWHorizontalBoxLayout extends XLayoutContainer
{

    public XWHorizontalBoxLayout(WHorizontalBoxLayout whorizontalboxlayout, Dimension dimension)
    {
        super(whorizontalboxlayout, dimension);
    }

    protected String getIconName()
    {
        return "boxlayout_h_16.png";
    }

    public String createDefaultName()
    {
        return "hBox";
    }

    public Dimension initEditorSize()
    {
        return new Dimension(200, 100);
    }

    public WHorizontalBoxLayout toData()
    {
        return (WHorizontalBoxLayout)data;
    }

    protected void initLayoutManager()
    {
        setLayout(new FRHorizontalLayout(toData().getAlignment(), toData().getHgap(), toData().getVgap()));
    }

    public void componentAdded(ContainerEvent containerevent)
    {
        if(isRefreshing)
            return;
        XWidgetCreator xwidgetcreator = (XWidgetCreator)containerevent.getChild();
        WHorizontalBoxLayout whorizontalboxlayout = toData();
        Widget widget = xwidgetcreator.toData();
        int i = 0;
        for(int j = getComponentCount(); i < j; i++)
            if(xwidgetcreator == getComponent(i))
            {
                whorizontalboxlayout.addWidget(widget, i);
                whorizontalboxlayout.setWidthAtWidget(widget, xwidgetcreator.getWidth());
            }

        recalculateChildrenPreferredSize();
    }

    protected Dimension calculatePreferredSize(Widget widget)
    {
        return new Dimension(toData().getWidthAtWidget(widget), 0);
    }

    public Component add(Component component, int i)
    {
        super.add(component, i);
        if(component == null)
        {
            return null;
        } else
        {
            XCreator xcreator = (XCreator)component;
            xcreator.setDirections(new int[] {
                3, 4
            });
            return component;
        }
    }

    public LayoutAdapter getLayoutAdapter()
    {
        return new FRHorizontalLayoutAdapter(this);
    }

    public volatile WLayout toData()
    {
        return toData();
    }

    public volatile AbstractBorderStyleWidget toData()
    {
        return toData();
    }

    public volatile Widget toData()
    {
        return toData();
    }
}
