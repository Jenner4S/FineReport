// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.designer.creator;

import com.fr.design.form.layout.FRSplitLayout;
import com.fr.form.ui.AbstractBorderStyleWidget;
import com.fr.form.ui.Widget;
import com.fr.form.ui.container.WLayout;
import com.fr.form.ui.container.WSplitLayout;
import java.awt.Dimension;
import java.awt.event.ContainerEvent;

// Referenced classes of package com.fr.design.designer.creator:
//            XLayoutContainer, XWidgetCreator, XCreatorUtils

public abstract class XAbstractSplitLayout extends XLayoutContainer
{

    public XAbstractSplitLayout(WSplitLayout wsplitlayout, Dimension dimension)
    {
        super(wsplitlayout, dimension);
    }

    public WSplitLayout toData()
    {
        return (WSplitLayout)data;
    }

    public void convert()
    {
        isRefreshing = true;
        WSplitLayout wsplitlayout = toData();
        removeAll();
        String as[] = {
            "center", "aside"
        };
        for(int i = 0; i < as.length; i++)
        {
            Widget widget = wsplitlayout.getLayoutWidget(as[i]);
            if(widget != null)
            {
                XWidgetCreator xwidgetcreator = (XWidgetCreator)XCreatorUtils.createXCreator(widget, calculatePreferredSize(widget));
                add(xwidgetcreator, as[i]);
            }
        }

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
            FRSplitLayout frsplitlayout = (FRSplitLayout)getLayout();
            Object obj = frsplitlayout.getConstraints(xwidgetcreator);
            WSplitLayout wsplitlayout = toData();
            Widget widget = xwidgetcreator.toData();
            wsplitlayout.addWidget(widget, obj);
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
