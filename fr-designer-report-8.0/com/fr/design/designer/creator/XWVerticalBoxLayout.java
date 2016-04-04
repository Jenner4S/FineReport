// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.designer.creator;

import com.fr.design.designer.beans.LayoutAdapter;
import com.fr.design.designer.beans.adapters.layout.FRVerticalLayoutAdapter;
import com.fr.design.form.layout.FRVerticalLayout;
import com.fr.form.ui.AbstractBorderStyleWidget;
import com.fr.form.ui.Widget;
import com.fr.form.ui.container.WLayout;
import com.fr.form.ui.container.WVerticalBoxLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ContainerEvent;

// Referenced classes of package com.fr.design.designer.creator:
//            XLayoutContainer, XWidgetCreator, XCreator

public class XWVerticalBoxLayout extends XLayoutContainer
{

    public XWVerticalBoxLayout(WVerticalBoxLayout wverticalboxlayout, Dimension dimension)
    {
        super(wverticalboxlayout, dimension);
    }

    public WVerticalBoxLayout toData()
    {
        return (WVerticalBoxLayout)data;
    }

    public Dimension initEditorSize()
    {
        return new Dimension(100, 200);
    }

    protected void initLayoutManager()
    {
        setLayout(new FRVerticalLayout(toData().getHgap(), toData().getVgap()));
    }

    public void componentAdded(ContainerEvent containerevent)
    {
        if(isRefreshing)
            return;
        XWidgetCreator xwidgetcreator = (XWidgetCreator)containerevent.getChild();
        WVerticalBoxLayout wverticalboxlayout = toData();
        Widget widget = xwidgetcreator.toData();
        int i = 0;
        for(int j = getComponentCount(); i < j; i++)
            if(xwidgetcreator == getComponent(i))
            {
                wverticalboxlayout.addWidget(widget, i);
                wverticalboxlayout.setHeightAtWidget(widget, xwidgetcreator.getHeight());
            }

        recalculateChildrenPreferredSize();
    }

    protected Dimension calculatePreferredSize(Widget widget)
    {
        return new Dimension(getSize().width, toData().getHeightAtWidget(widget));
    }

    protected String getIconName()
    {
        return "boxlayout_v_16.png";
    }

    public String createDefaultName()
    {
        return "vBox";
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
                1, 2
            });
            return component;
        }
    }

    public LayoutAdapter getLayoutAdapter()
    {
        return new FRVerticalLayoutAdapter(this);
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
