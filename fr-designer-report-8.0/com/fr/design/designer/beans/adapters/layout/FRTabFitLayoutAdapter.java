// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.designer.beans.adapters.layout;

import com.fr.design.beans.GroupModel;
import com.fr.design.designer.creator.*;
import com.fr.design.designer.creator.cardlayout.XWCardLayout;
import com.fr.design.designer.creator.cardlayout.XWTabFitLayout;
import com.fr.design.designer.properties.FRTabFitLayoutPropertiesGroupModel;
import com.fr.design.utils.ComponentUtils;
import com.fr.form.ui.LayoutBorderStyle;
import com.fr.form.ui.container.WCardLayout;
import com.fr.general.ComparatorUtils;
import java.awt.Rectangle;

// Referenced classes of package com.fr.design.designer.beans.adapters.layout:
//            FRFitLayoutAdapter

public class FRTabFitLayoutAdapter extends FRFitLayoutAdapter
{

    private static int TAB_HEIGHT = 40;

    public FRTabFitLayoutAdapter(XLayoutContainer xlayoutcontainer)
    {
        super(xlayoutcontainer);
    }

    public GroupModel getLayoutProperties()
    {
        XWTabFitLayout xwtabfitlayout = (XWTabFitLayout)container;
        return new FRTabFitLayoutPropertiesGroupModel(xwtabfitlayout);
    }

    public boolean addBean(XCreator xcreator, int i, int j)
    {
        XLayoutContainer xlayoutcontainer = container;
        Rectangle rectangle = ComponentUtils.getRelativeBounds(container);
        int k = i - rectangle.x;
        int l = j - rectangle.y;
        if(!accept(xcreator, k, l))
            return false;
        if(intersectsEdge(k, l, xlayoutcontainer))
        {
            if(!ComparatorUtils.equals(xlayoutcontainer.getOuterLayout(), xlayoutcontainer.getBackupParent()))
            {
                XWTabFitLayout xwtabfitlayout = (XWTabFitLayout)xlayoutcontainer;
                j = adjustY(j, xwtabfitlayout);
            }
            addComp(xcreator, i, j);
            ((XWidgetCreator)xcreator).recalculateChildrenSize();
            return true;
        } else
        {
            addComp(xcreator, k, l);
            ((XWidgetCreator)xcreator).recalculateChildrenSize();
            return true;
        }
    }

    private int adjustY(int i, XWTabFitLayout xwtabfitlayout)
    {
        XWCardLayout xwcardlayout = (XWCardLayout)xwtabfitlayout.getBackupParent();
        LayoutBorderStyle layoutborderstyle = xwcardlayout.toData().getBorderStyle();
        if(ComparatorUtils.equals(Integer.valueOf(layoutborderstyle.getType()), Integer.valueOf(1)))
            i -= 36;
        return i;
    }

}
