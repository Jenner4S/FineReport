// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.designer.creator;

import com.fr.design.designer.beans.LayoutAdapter;
import com.fr.design.designer.beans.adapters.layout.FRHorizontalSplitLayoutAdapter;
import com.fr.design.form.layout.FRHorizontalSplitLayout;
import com.fr.form.ui.AbstractBorderStyleWidget;
import com.fr.form.ui.Widget;
import com.fr.form.ui.container.*;
import java.awt.Dimension;

// Referenced classes of package com.fr.design.designer.creator:
//            XAbstractSplitLayout

public class XWHorizontalSplitLayout extends XAbstractSplitLayout
{

    public XWHorizontalSplitLayout(WHorizontalSplitLayout whorizontalsplitlayout, Dimension dimension)
    {
        super(whorizontalsplitlayout, dimension);
    }

    public WHorizontalSplitLayout toData()
    {
        return (WHorizontalSplitLayout)data;
    }

    protected void initLayoutManager()
    {
        setLayout(new FRHorizontalSplitLayout(toData().getRatio(), toData().getHgap(), toData().getVgap()));
    }

    protected String getIconName()
    {
        return "split_pane_16.png";
    }

    public String createDefaultName()
    {
        return "hsplit";
    }

    public LayoutAdapter getLayoutAdapter()
    {
        return new FRHorizontalSplitLayoutAdapter(this);
    }

    public volatile WSplitLayout toData()
    {
        return toData();
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
