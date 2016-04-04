// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.designer.creator;

import com.fr.design.designer.beans.LayoutAdapter;
import com.fr.design.designer.beans.adapters.layout.FRScaleLayoutAdapter;
import com.fr.design.form.layout.FRScaleLayout;
import com.fr.form.ui.AbstractBorderStyleWidget;
import com.fr.form.ui.Widget;
import com.fr.form.ui.container.*;
import java.awt.Dimension;
import java.awt.event.ContainerEvent;

// Referenced classes of package com.fr.design.designer.creator:
//            DedicateLayoutContainer, XWidgetCreator, XCreatorUtils, XCreator

public class XWScaleLayout extends DedicateLayoutContainer
{

    private static final long serialVersionUID = 0x84e7107bf1523f14L;
    public static final int INDEX = 0;

    public XWScaleLayout()
    {
        this(new WScaleLayout("scalePanel"), new Dimension());
    }

    public XWScaleLayout(WScaleLayout wscalelayout, Dimension dimension)
    {
        super(wscalelayout, dimension);
    }

    protected void initLayoutManager()
    {
        setLayout(new FRScaleLayout());
    }

    public LayoutAdapter getLayoutAdapter()
    {
        return new FRScaleLayoutAdapter(this);
    }

    public WScaleLayout toData()
    {
        return (WScaleLayout)data;
    }

    public String createDefaultName()
    {
        return "scalePanel";
    }

    public void convert()
    {
        isRefreshing = true;
        WScaleLayout wscalelayout = toData();
        removeAll();
        int i = 0;
        for(int j = wscalelayout.getWidgetCount(); i < j; i++)
        {
            com.fr.form.ui.container.WAbsoluteLayout.BoundsWidget boundswidget = (com.fr.form.ui.container.WAbsoluteLayout.BoundsWidget)wscalelayout.getWidget(i);
            if(boundswidget != null)
            {
                java.awt.Rectangle rectangle = boundswidget.getBounds();
                XWidgetCreator xwidgetcreator = (XWidgetCreator)XCreatorUtils.createXCreator(boundswidget.getWidget());
                add(xwidgetcreator, boundswidget.getWidget().getWidgetName());
                xwidgetcreator.setBounds(rectangle);
            }
        }

        isRefreshing = false;
    }

    public void componentAdded(ContainerEvent containerevent)
    {
        if(isRefreshing)
            return;
        WScaleLayout wscalelayout = toData();
        wscalelayout.removeAll();
        int i = 0;
        for(int j = getComponentCount(); i < j; i++)
        {
            XWidgetCreator xwidgetcreator = (XWidgetCreator)getComponent(i);
            wscalelayout.addWidget(new com.fr.form.ui.container.WAbsoluteLayout.BoundsWidget(xwidgetcreator.toData(), xwidgetcreator.getBounds()));
        }

    }

    public void componentRemoved(ContainerEvent containerevent)
    {
        if(isRefreshing)
        {
            return;
        } else
        {
            XWidgetCreator xwidgetcreator = (XWidgetCreator)containerevent.getChild();
            Widget widget = xwidgetcreator.toData();
            com.fr.form.ui.container.WAbsoluteLayout.BoundsWidget boundswidget = new com.fr.form.ui.container.WAbsoluteLayout.BoundsWidget(widget, xwidgetcreator.getBounds());
            WScaleLayout wscalelayout = toData();
            wscalelayout.removeWidget(boundswidget);
            return;
        }
    }

    public XCreator getEditingChildCreator()
    {
        return getXCreator(0);
    }

    public void updateChildBound(int i)
    {
        XCreator xcreator = getXCreator(0);
        xcreator.setSize(getWidth(), i);
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
