// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.designer.creator;

import com.fr.design.designer.beans.LayoutAdapter;
import com.fr.design.designer.beans.adapters.layout.FRAbsoluteLayoutAdapter;
import com.fr.design.designer.beans.location.Direction;
import com.fr.design.form.layout.FRAbsoluteLayout;
import com.fr.form.ui.*;
import com.fr.form.ui.container.WAbsoluteLayout;
import com.fr.form.ui.container.WLayout;
import java.awt.*;
import java.awt.event.ContainerEvent;
import java.util.HashMap;

// Referenced classes of package com.fr.design.designer.creator:
//            XLayoutContainer, XConnector, XWidgetCreator, XWParameterLayout, 
//            XCreator, XWFitLayout, XCreatorUtils

public class XWAbsoluteLayout extends XLayoutContainer
{

    private HashMap xConnectorMap;

    public XWAbsoluteLayout()
    {
        this(new WAbsoluteLayout(), new Dimension());
    }

    public XWAbsoluteLayout(WAbsoluteLayout wabsolutelayout)
    {
        this(wabsolutelayout, new Dimension());
    }

    public XWAbsoluteLayout(WAbsoluteLayout wabsolutelayout, Dimension dimension)
    {
        super(wabsolutelayout, dimension);
        xConnectorMap = new HashMap();
        for(int i = 0; i < wabsolutelayout.connectorCount(); i++)
        {
            Connector connector = wabsolutelayout.getConnectorIndex(i);
            xConnectorMap.put(connector, new XConnector(connector, this));
        }

    }

    public void addConnector(Connector connector)
    {
        xConnectorMap.put(connector, new XConnector(connector, this));
        ((WAbsoluteLayout)data).addConnector(connector);
    }

    public XConnector getXConnector(Connector connector)
    {
        return (XConnector)xConnectorMap.get(connector);
    }

    public void removeConnector(Connector connector)
    {
        ((WAbsoluteLayout)data).removeConnector(connector);
        xConnectorMap.remove(connector);
    }

    public WAbsoluteLayout toData()
    {
        return (WAbsoluteLayout)data;
    }

    protected String getIconName()
    {
        return "layout_absolute.png";
    }

    public String createDefaultName()
    {
        return "absolute";
    }

    protected void initLayoutManager()
    {
        setLayout(new FRAbsoluteLayout());
    }

    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        WAbsoluteLayout wabsolutelayout = (WAbsoluteLayout)data;
        Connector aconnector[] = wabsolutelayout.getConnector();
        int i = 0;
        for(int j = aconnector.length; i < j; i++)
            aconnector[i].draw(g);

    }

    public void convert()
    {
        isRefreshing = true;
        WAbsoluteLayout wabsolutelayout = toData();
        removeAll();
        int i = 0;
        for(int j = wabsolutelayout.getWidgetCount(); i < j; i++)
        {
            com.fr.form.ui.container.WAbsoluteLayout.BoundsWidget boundswidget = (com.fr.form.ui.container.WAbsoluteLayout.BoundsWidget)wabsolutelayout.getWidget(i);
            if(boundswidget == null)
                continue;
            Rectangle rectangle = boundswidget.getBounds();
            XWidgetCreator xwidgetcreator = (XWidgetCreator)XCreatorUtils.createXCreator(boundswidget.getWidget());
            if(!xwidgetcreator.acceptType(new Class[] {
    com/fr/design/designer/creator/XWParameterLayout
}))
                xwidgetcreator.setDirections(Direction.ALL);
            add(xwidgetcreator);
            xwidgetcreator.setBounds(rectangle);
        }

        isRefreshing = false;
    }

    public XCreator replace(Widget widget, XCreator xcreator)
    {
        int i = getComponentZOrder(xcreator);
        if(i != -1)
        {
            toData().replace(new com.fr.form.ui.container.WAbsoluteLayout.BoundsWidget(widget, xcreator.getBounds()), new com.fr.form.ui.container.WAbsoluteLayout.BoundsWidget(xcreator.toData(), xcreator.getBounds()));
            convert();
            return (XCreator)getComponent(i);
        } else
        {
            return null;
        }
    }

    public void componentAdded(ContainerEvent containerevent)
    {
        if(isRefreshing)
            return;
        XWidgetCreator xwidgetcreator = (XWidgetCreator)containerevent.getChild();
        WAbsoluteLayout wabsolutelayout = toData();
        if(!xwidgetcreator.acceptType(new Class[] {
    com/fr/design/designer/creator/XWFitLayout
}))
            xwidgetcreator.setDirections(Direction.ALL);
        wabsolutelayout.addWidget(new com.fr.form.ui.container.WAbsoluteLayout.BoundsWidget(xwidgetcreator.toData(), xwidgetcreator.getBounds()));
    }

    public void componentRemoved(ContainerEvent containerevent)
    {
        if(isRefreshing)
        {
            return;
        } else
        {
            WAbsoluteLayout wabsolutelayout = toData();
            XWidgetCreator xwidgetcreator = (XWidgetCreator)containerevent.getChild();
            Widget widget = xwidgetcreator.toData();
            com.fr.form.ui.container.WAbsoluteLayout.BoundsWidget boundswidget = new com.fr.form.ui.container.WAbsoluteLayout.BoundsWidget(widget, xwidgetcreator.getBounds());
            wabsolutelayout.removeWidget(boundswidget);
            return;
        }
    }

    public Dimension getMinimumSize()
    {
        return toData().getMinDesignSize();
    }

    public LayoutAdapter getLayoutAdapter()
    {
        return new FRAbsoluteLayoutAdapter(this);
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
