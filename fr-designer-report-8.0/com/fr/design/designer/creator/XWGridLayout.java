// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.designer.creator;

import com.fr.design.designer.beans.LayoutAdapter;
import com.fr.design.designer.beans.adapters.layout.FRGridLayoutAdapter;
import com.fr.design.form.layout.FRGridLayout;
import com.fr.form.ui.AbstractBorderStyleWidget;
import com.fr.form.ui.Widget;
import com.fr.form.ui.container.WGridLayout;
import com.fr.form.ui.container.WLayout;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ContainerEvent;
import java.util.Iterator;

// Referenced classes of package com.fr.design.designer.creator:
//            XLayoutContainer, XWidgetCreator, XCreatorUtils

public class XWGridLayout extends XLayoutContainer
{

    public XWGridLayout(WGridLayout wgridlayout, Dimension dimension)
    {
        super(wgridlayout, dimension);
    }

    protected String getIconName()
    {
        return "layout_grid.png";
    }

    public String createDefaultName()
    {
        return "grid";
    }

    public WGridLayout toData()
    {
        return (WGridLayout)data;
    }

    protected void initLayoutManager()
    {
        setLayout(new FRGridLayout(toData().getRows(), toData().getColumns(), toData().getHgap(), toData().getVgap()));
    }

    public void convert()
    {
        isRefreshing = true;
        WGridLayout wgridlayout = toData();
        removeAll();
        Iterator iterator = wgridlayout.iterator();
        do
        {
            if(!iterator.hasNext())
                break;
            Point point = (Point)iterator.next();
            Widget widget = wgridlayout.getWidget(point);
            if(widget != null)
            {
                XWidgetCreator xwidgetcreator = (XWidgetCreator)XCreatorUtils.createXCreator(widget, calculatePreferredSize(widget));
                add(xwidgetcreator, point);
            }
        } while(true);
        repaint();
        isRefreshing = false;
    }

    public void componentAdded(ContainerEvent containerevent)
    {
        if(isRefreshing)
        {
            return;
        } else
        {
            XWidgetCreator xwidgetcreator = (XWidgetCreator)containerevent.getChild();
            FRGridLayout frgridlayout = (FRGridLayout)getLayout();
            Point point = frgridlayout.getPoint(xwidgetcreator);
            WGridLayout wgridlayout = toData();
            Widget widget = xwidgetcreator.toData();
            wgridlayout.addWidget(widget, point);
            return;
        }
    }

    public LayoutAdapter getLayoutAdapter()
    {
        return new FRGridLayoutAdapter(this);
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
