// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.chart.gui.type;

import com.fr.design.chart.series.PlotStyle.ChartSelectDemoPane;
import com.fr.design.constants.UIConstants;
import com.fr.design.gui.ilable.UILabel;
import java.awt.BorderLayout;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;

public class ChartImagePane extends ChartSelectDemoPane
{

    private static final long serialVersionUID = 0xd9593c9ddfbf0f65L;
    private boolean isDrawRightLine;
    public boolean isDoubleClicked;

    public ChartImagePane(String s, String s1)
    {
        isDrawRightLine = false;
        isDoubleClicked = false;
        UILabel uilabel = new UILabel(new ImageIcon(getClass().getResource(s)));
        setLayout(new BorderLayout());
        add(uilabel, "Center");
        addMouseListener(this);
        setToolTipText(s1);
        setBorder(BorderFactory.createMatteBorder(1, 1, 1, isDrawRightLine ? 1 : 0, UIConstants.LINE_COLOR));
    }

    public ChartImagePane(String s, String s1, boolean flag)
    {
        isDrawRightLine = false;
        isDoubleClicked = false;
        constructImagePane(s, s1, flag);
    }

    private void constructImagePane(String s, String s1, boolean flag)
    {
        UILabel uilabel = new UILabel(new ImageIcon(getClass().getResource(s)));
        setLayout(new BorderLayout());
        add(uilabel, "Center");
        addMouseListener(this);
        isDrawRightLine = flag;
        setToolTipText(s1);
        setBorder(BorderFactory.createMatteBorder(1, 1, 1, flag ? 1 : 0, UIConstants.LINE_COLOR));
    }

    public void mouseClicked(MouseEvent mouseevent)
    {
        if(isPressing)
            isDoubleClicked = true;
        else
            isDoubleClicked = false;
        super.mouseClicked(mouseevent);
    }
}
