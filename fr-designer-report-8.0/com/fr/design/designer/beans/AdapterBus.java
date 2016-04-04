// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.designer.beans;

import com.fr.design.designer.beans.adapters.component.CompositeComponentAdapter;
import com.fr.design.designer.beans.painters.NullLayoutPainter;
import com.fr.design.designer.creator.*;
import com.fr.design.mainframe.FormDesigner;
import com.fr.design.utils.ComponentUtils;
import java.awt.Component;
import javax.swing.JComponent;
import javax.swing.RootPaneContainer;

// Referenced classes of package com.fr.design.designer.beans:
//            ComponentAdapter, LayoutAdapter, HoverPainter

public class AdapterBus
{

    public static final String CLIENT_PROPERTIES = "component.adapter";

    public AdapterBus()
    {
    }

    public static JComponent getJComponent(Component component)
    {
        JComponent jcomponent;
        if(component instanceof JComponent)
            jcomponent = (JComponent)component;
        else
        if(component instanceof RootPaneContainer)
            jcomponent = (JComponent)((RootPaneContainer)component).getContentPane();
        else
            return null;
        return jcomponent;
    }

    public static ComponentAdapter getComponentAdapter(FormDesigner formdesigner, JComponent jcomponent)
    {
        JComponent jcomponent1 = getJComponent(jcomponent);
        Object obj = (ComponentAdapter)jcomponent1.getClientProperty("component.adapter");
        if(obj == null)
        {
            obj = new CompositeComponentAdapter(formdesigner, jcomponent);
            jcomponent1.putClientProperty("component.adapter", obj);
        }
        return ((ComponentAdapter) (obj));
    }

    public static XCreator getFirstInvisibleParent(XCreator xcreator)
    {
        Object obj;
        for(obj = xcreator; obj != null && ((XCreator) (obj)).isVisible(); obj = XCreatorUtils.getParentXLayoutContainer(((XCreator) (obj))));
        return ((XCreator) (obj));
    }

    public static LayoutAdapter searchLayoutAdapter(FormDesigner formdesigner, XCreator xcreator)
    {
        if(ComponentUtils.isRootComponent(xcreator))
            return null;
        else
            return XCreatorUtils.getParentXLayoutContainer(xcreator).getLayoutAdapter();
    }

    public static HoverPainter getContainerPainter(FormDesigner formdesigner, XLayoutContainer xlayoutcontainer)
    {
        LayoutAdapter layoutadapter = xlayoutcontainer.getLayoutAdapter();
        HoverPainter hoverpainter = layoutadapter.getPainter();
        if(hoverpainter != null)
            return hoverpainter;
        else
            return new NullLayoutPainter(xlayoutcontainer);
    }
}
