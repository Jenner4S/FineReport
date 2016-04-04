// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.grid;

import com.fr.base.GraphHelper;
import com.fr.design.mainframe.ElementCasePane;
import com.fr.stable.StableUtils;
import java.awt.Dimension;
import java.awt.FontMetrics;

// Referenced classes of package com.fr.grid:
//            GridHeader, GridColumnMouseHandler, GridColumnUI

public class GridColumn extends GridHeader
{

    public GridColumn()
    {
    }

    protected void initByConstructor()
    {
        GridColumnMouseHandler gridcolumnmousehandler = new GridColumnMouseHandler(this);
        addMouseListener(gridcolumnmousehandler);
        addMouseMotionListener(gridcolumnmousehandler);
        updateUI();
    }

    public String getDisplay(int i)
    {
        return StableUtils.convertIntToABC(i + 1);
    }

    public void updateUI()
    {
        setUI(new GridColumnUI());
    }

    public Dimension getPreferredSize()
    {
        ElementCasePane elementcasepane = getElementCasePane();
        if(!elementcasepane.isColumnHeaderVisible())
            return new Dimension(0, 0);
        else
            return new Dimension(super.getPreferredSize().width, GraphHelper.getFontMetrics(getFont()).getHeight() + 4);
    }

    public volatile Object getDisplay(int i)
    {
        return getDisplay(i);
    }
}
