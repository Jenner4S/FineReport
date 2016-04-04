// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.designer.creator;

import com.fr.design.designer.beans.LayoutAdapter;
import com.fr.design.designer.beans.adapters.layout.FRBorderLayoutAdapter;
import com.fr.design.form.layout.FRBorderLayout;
import com.fr.form.ui.AbstractBorderStyleWidget;
import com.fr.form.ui.Widget;
import com.fr.form.ui.container.WBorderLayout;
import com.fr.form.ui.container.WLayout;
import com.fr.general.ComparatorUtils;
import com.fr.general.Inter;
import java.awt.*;
import java.awt.event.ContainerEvent;
import java.beans.IntrospectionException;

// Referenced classes of package com.fr.design.designer.creator:
//            XLayoutContainer, CRPropertyDescriptor, XWidgetCreator, XCreator, 
//            XCreatorUtils

public class XWBorderLayout extends XLayoutContainer
{

    private int Num;

    public XWBorderLayout()
    {
        super(new WBorderLayout(), new Dimension(960, 540));
        Num = 5;
    }

    public XWBorderLayout(WBorderLayout wborderlayout, Dimension dimension)
    {
        super(wborderlayout, dimension);
        Num = 5;
    }

    protected String getIconName()
    {
        return "layout_border.png";
    }

    public String createDefaultName()
    {
        return "border";
    }

    public WBorderLayout toData()
    {
        return (WBorderLayout)data;
    }

    protected void initLayoutManager()
    {
        setLayout(new FRBorderLayout(toData().getHgap(), toData().getVgap()));
    }

    public Dimension initEditorSize()
    {
        return new Dimension(960, 540);
    }

    public CRPropertyDescriptor[] supportedDescriptor()
        throws IntrospectionException
    {
        return (new CRPropertyDescriptor[] {
            (new CRPropertyDescriptor("widgetName", data.getClass())).setI18NName(Inter.getLocText("Form-Widget_Name"))
        });
    }

    public void convert()
    {
        isRefreshing = true;
        WBorderLayout wborderlayout = toData();
        removeAll();
        String as[] = {
            "North", "South", "East", "West", "Center"
        };
        for(int i = 0; i < as.length; i++)
        {
            Widget widget = wborderlayout.getLayoutWidget(as[i]);
            if(widget != null)
            {
                XWidgetCreator xwidgetcreator = (XWidgetCreator)XCreatorUtils.createXCreator(widget, calculatePreferredSize(widget));
                add(xwidgetcreator, as[i]);
                xwidgetcreator.setBackupParent(this);
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
            BorderLayout borderlayout = (BorderLayout)getLayout();
            Object obj = borderlayout.getConstraints(xwidgetcreator);
            WBorderLayout wborderlayout = toData();
            Widget widget = xwidgetcreator.toData();
            add(wborderlayout, widget, obj);
            doResizePreferredSize(xwidgetcreator, calculatePreferredSize(widget));
            return;
        }
    }

    protected Dimension calculatePreferredSize(Widget widget)
    {
        WBorderLayout wborderlayout = toData();
        Object obj = wborderlayout.getConstraints(widget);
        Dimension dimension = new Dimension();
        if(ComparatorUtils.equals("North", obj))
            dimension.height = wborderlayout.getNorthSize();
        else
        if(ComparatorUtils.equals("South", obj))
            dimension.height = wborderlayout.getSouthSize();
        else
        if(ComparatorUtils.equals("East", obj))
            dimension.width = wborderlayout.getEastSize();
        else
        if(ComparatorUtils.equals("West", obj))
            dimension.width = wborderlayout.getWestSize();
        return dimension;
    }

    private void doResizePreferredSize(XWidgetCreator xwidgetcreator, Dimension dimension)
    {
        xwidgetcreator.setPreferredSize(dimension);
    }

    public static void add(WBorderLayout wborderlayout, Widget widget, Object obj)
    {
        if(ComparatorUtils.equals("North", obj))
            wborderlayout.addNorth(widget);
        else
        if(ComparatorUtils.equals("South", obj))
            wborderlayout.addSouth(widget);
        else
        if(ComparatorUtils.equals("East", obj))
            wborderlayout.addEast(widget);
        else
        if(ComparatorUtils.equals("West", obj))
            wborderlayout.addWest(widget);
        else
        if(ComparatorUtils.equals("Center", obj))
            wborderlayout.addCenter(widget);
    }

    public void recalculateChildrenSize()
    {
        Dimension dimension = getSize();
        WBorderLayout wborderlayout = toData();
        wborderlayout.setNorthSize(dimension.height / Num);
        wborderlayout.setSouthSize(dimension.height / Num);
        wborderlayout.setWestSize(dimension.width / Num);
        wborderlayout.setEastSize(dimension.width / Num);
    }

    public void add(Component component, Object obj)
    {
        super.add(component, obj);
        if(component == null)
            return;
        XCreator xcreator = (XCreator)component;
        if(ComparatorUtils.equals("North", obj))
            xcreator.setDirections(new int[] {
                2
            });
        else
        if(ComparatorUtils.equals("South", obj))
            xcreator.setDirections(new int[] {
                1
            });
        else
        if(ComparatorUtils.equals("East", obj))
            xcreator.setDirections(new int[] {
                3
            });
        else
        if(ComparatorUtils.equals("West", obj))
            xcreator.setDirections(new int[] {
                4
            });
    }

    public LayoutAdapter getLayoutAdapter()
    {
        return new FRBorderLayoutAdapter(this);
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
