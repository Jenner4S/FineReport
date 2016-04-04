// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.designer.beans.adapters.layout;

import com.fr.design.beans.GroupModel;
import com.fr.design.designer.beans.HoverPainter;
import com.fr.design.designer.beans.painters.FRParameterLayoutPainter;
import com.fr.design.designer.creator.*;
import com.fr.design.form.parameter.RootDesignGroupModel;
import com.fr.form.ui.container.WParameterLayout;

// Referenced classes of package com.fr.design.designer.beans.adapters.layout:
//            FRAbsoluteLayoutAdapter

public class FRParameterLayoutAdapter extends FRAbsoluteLayoutAdapter
{

    private HoverPainter painter;

    public FRParameterLayoutAdapter(XLayoutContainer xlayoutcontainer)
    {
        super(xlayoutcontainer);
        painter = new FRParameterLayoutPainter(xlayoutcontainer);
    }

    public HoverPainter getPainter()
    {
        return painter;
    }

    public GroupModel getLayoutProperties()
    {
        return new RootDesignGroupModel((XWParameterLayout)container);
    }

    public void fix(XCreator xcreator)
    {
        super.fix(xcreator);
        WParameterLayout wparameterlayout = (WParameterLayout)container.toData();
        wparameterlayout.refreshTagList();
    }
}
