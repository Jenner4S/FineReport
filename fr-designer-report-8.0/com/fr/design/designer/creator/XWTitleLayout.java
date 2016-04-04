// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.designer.creator;

import com.fr.design.designer.beans.LayoutAdapter;
import com.fr.design.designer.beans.adapters.layout.FRTitleLayoutAdapter;
import com.fr.design.form.layout.FRTitleLayout;
import com.fr.form.ui.*;
import com.fr.form.ui.container.*;
import com.fr.general.ComparatorUtils;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ContainerEvent;

// Referenced classes of package com.fr.design.designer.creator:
//            DedicateLayoutContainer, XWidgetCreator, XCreator, XCreatorUtils

public class XWTitleLayout extends DedicateLayoutContainer
{

    private static final long serialVersionUID = 0x49330bb81d8a33fdL;

    public XWTitleLayout()
    {
        super(new WTitleLayout("titlePane"), new Dimension());
    }

    public XWTitleLayout(WTitleLayout wtitlelayout, Dimension dimension)
    {
        super(wtitlelayout, dimension);
    }

    protected void initLayoutManager()
    {
        setLayout(new FRTitleLayout());
    }

    public LayoutAdapter getLayoutAdapter()
    {
        return new FRTitleLayoutAdapter(this);
    }

    public WTitleLayout toData()
    {
        return (WTitleLayout)data;
    }

    public void resetCreatorName(String s)
    {
        super.resetCreatorName(s);
        if(getXCreatorCount() > 1)
            getTitleCreator().toData().setWidgetName((new StringBuilder()).append("Title_").append(s).toString());
    }

    public String createDefaultName()
    {
        return "titlePanel";
    }

    public XCreator getTitleCreator()
    {
        for(int i = 0; i < getXCreatorCount(); i++)
        {
            XCreator xcreator = getXCreator(i);
            if(!xcreator.hasTitleStyle())
                return xcreator;
        }

        return null;
    }

    public void convert()
    {
        isRefreshing = true;
        WTitleLayout wtitlelayout = toData();
        removeAll();
        int i = 0;
        for(int j = wtitlelayout.getWidgetCount(); i < j; i++)
        {
            com.fr.form.ui.container.WAbsoluteLayout.BoundsWidget boundswidget = (com.fr.form.ui.container.WAbsoluteLayout.BoundsWidget)wtitlelayout.getWidget(i);
            if(boundswidget != null)
            {
                Rectangle rectangle = boundswidget.getBounds();
                XWidgetCreator xwidgetcreator = (XWidgetCreator)XCreatorUtils.createXCreator(boundswidget.getWidget());
                String s = boundswidget.getWidget().acceptType(new Class[] {
                    com/fr/form/ui/Label
                }) ? "Title" : "Body";
                add(xwidgetcreator, s);
                xwidgetcreator.setBounds(rectangle);
            }
        }

        isRefreshing = false;
    }

    public void componentAdded(ContainerEvent containerevent)
    {
        if(isRefreshing)
            return;
        WTitleLayout wtitlelayout = toData();
        XWidgetCreator xwidgetcreator = (XWidgetCreator)containerevent.getChild();
        FRTitleLayout frtitlelayout = (FRTitleLayout)getLayout();
        Object obj = frtitlelayout.getConstraints(xwidgetcreator);
        if(ComparatorUtils.equals("Title", obj))
            wtitlelayout.addTitle(xwidgetcreator.toData(), xwidgetcreator.getBounds());
        else
        if(ComparatorUtils.equals("Body", obj))
            wtitlelayout.addBody(xwidgetcreator.toData(), xwidgetcreator.getBounds());
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
            WTitleLayout wtitlelayout = toData();
            wtitlelayout.removeWidget(widget);
            return;
        }
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
