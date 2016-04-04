// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.grid;

import com.fr.base.GraphHelper;
import com.fr.design.mainframe.ElementCasePane;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import javax.swing.ActionMap;
import javax.swing.InputMap;

// Referenced classes of package com.fr.grid:
//            BaseGridComponent, GridCornerMouseHandler, GridColumn, GridRow

public class GridCorner extends BaseGridComponent
{

    public GridCorner()
    {
        setOpaque(true);
        getInputMap().clear();
        getActionMap().clear();
        GridCornerMouseHandler gridcornermousehandler = new GridCornerMouseHandler(this);
        addMouseListener(gridcornermousehandler);
        addMouseMotionListener(gridcornermousehandler);
    }

    public void paintComponent(Graphics g)
    {
        Graphics2D graphics2d = (Graphics2D)g;
        ElementCasePane elementcasepane = getElementCasePane();
        Dimension dimension = getSize();
        java.awt.geom.Rectangle2D.Double double1 = new java.awt.geom.Rectangle2D.Double(0.0D, 0.0D, dimension.getWidth(), dimension.getHeight());
        if(getBackground() != null)
        {
            graphics2d.setPaint(getBackground());
            GraphHelper.fill(graphics2d, double1);
        } else
        {
            graphics2d.setPaint(elementcasepane.getBackground());
            GraphHelper.fill(graphics2d, double1);
        }
        paintArc(graphics2d, dimension);
        graphics2d.setColor(elementcasepane.getGridColumn().getSeparatorLineColor());
        GraphHelper.drawLine(graphics2d, 0.0D, 0.0D, 0.0D, dimension.getHeight());
        graphics2d.setColor(elementcasepane.getGridRow().getSeparatorLineColor());
        GraphHelper.drawLine(graphics2d, 0.0D, 0.0D, dimension.getWidth(), 0.0D);
    }

    public Dimension getPreferredSize()
    {
        ElementCasePane elementcasepane = getElementCasePane();
        if(!elementcasepane.isColumnHeaderVisible() || !elementcasepane.isRowHeaderVisible())
            return new Dimension(0, 0);
        else
            return new Dimension(elementcasepane.getGridRow().getPreferredSize().width, elementcasepane.getGridColumn().getPreferredSize().height);
    }

    public Color getBackground()
    {
        return super.getBackground();
    }

    private void paintArc(Graphics2D graphics2d, Dimension dimension)
    {
        graphics2d.setColor(Color.gray);
        int i = dimension.width - 16;
        int j = i / 3;
        int k = dimension.height - 8;
        int l = k / 3;
        for(int i1 = 0; i1 < 3; i1++)
        {
            for(int j1 = 0; j1 < 3; j1++)
                GraphHelper.fillArc(graphics2d, 8 + i1 * j, 4 + j1 * l, 3D, 3D, 0.0D, 360D);

        }

    }
}
