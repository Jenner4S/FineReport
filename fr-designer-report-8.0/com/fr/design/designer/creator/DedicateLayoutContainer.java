// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.designer.creator;

import com.fr.form.ui.Widget;
import com.fr.form.ui.container.WLayout;
import java.awt.Dimension;
import java.beans.IntrospectionException;
import java.util.ArrayList;

// Referenced classes of package com.fr.design.designer.creator:
//            XLayoutContainer, CRPropertyDescriptor, XCreator

public abstract class DedicateLayoutContainer extends XLayoutContainer
{

    public DedicateLayoutContainer(WLayout wlayout, Dimension dimension)
    {
        super(wlayout, dimension);
    }

    public CRPropertyDescriptor[] supportedDescriptor()
        throws IntrospectionException
    {
        return new CRPropertyDescriptor[0];
    }

    public String getIconPath()
    {
        if(getXCreator(0) != null)
            return getXCreator(0).getIconPath();
        else
            return "/com/fr/web/images/form/resources/text_field_16.png";
    }

    public void notShowInComponentTree(ArrayList arraylist)
    {
        arraylist.remove(arraylist.size() - 1);
    }

    public void resetCreatorName(String s)
    {
        super.resetCreatorName(s);
        XCreator xcreator = getXCreator(0);
        xcreator.toData().setWidgetName(s);
    }

    public XCreator getPropertyDescriptorCreator()
    {
        return getXCreator(0);
    }

    public boolean isComponentTreeLeaf()
    {
        return true;
    }

    public boolean isDedicateContainer()
    {
        return true;
    }
}
