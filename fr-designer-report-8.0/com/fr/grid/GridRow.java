// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.grid;

import com.fr.base.GraphHelper;
import com.fr.design.mainframe.ElementCasePane;
import java.awt.Dimension;
import java.awt.FontMetrics;

// Referenced classes of package com.fr.grid:
//            GridHeader, GridRowMouseHandler, GridRowUI, Grid

public class GridRow extends GridHeader
{

    public GridRow()
    {
    }

    protected void initByConstructor()
    {
        GridRowMouseHandler gridrowmousehandler = new GridRowMouseHandler(this);
        addMouseListener(gridrowmousehandler);
        addMouseMotionListener(gridrowmousehandler);
        updateUI();
    }

    public Integer getDisplay(int i)
    {
        return new Integer(i + 1);
    }

    public void updateUI()
    {
        setUI(new GridRowUI());
    }

    public Dimension getPreferredSize()
    {
        ElementCasePane elementcasepane = getElementCasePane();
        if(!elementcasepane.isRowHeaderVisible())
        {
            return new Dimension(0, 0);
        } else
        {
            int i = caculateMaxCharNumber(elementcasepane);
            return new Dimension(i * GraphHelper.getFontMetrics(getFont()).charWidth('M'), super.getPreferredSize().height);
        }
    }

    private int caculateMaxCharNumber(ElementCasePane elementcasepane)
    {
        int i = 5;
        i = Math.max(i, (new StringBuilder()).append("").append(elementcasepane.getGrid().getVerticalValue() + elementcasepane.getGrid().getVerticalExtent()).toString().length() + 1);
        return i;
    }

    public volatile Object getDisplay(int i)
    {
        return getDisplay(i);
    }
}
