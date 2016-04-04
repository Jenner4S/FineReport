// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.chart.gui.type;

import com.fr.base.BaseUtils;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import javax.swing.Icon;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

// Referenced classes of package com.fr.design.mainframe.chart.gui.type:
//            PlotPane4ToolBar

public class ChartDesignerImagePane extends JPanel
    implements MouseListener
{

    private static final int SIZE = 28;
    private static final String NOMAL = "normal";
    private static final String OVER = "over";
    private static final String PRESS = "normal";
    private String iconPath;
    private int chartType;
    private String state;
    private Icon mode;
    private boolean isSelected;
    private PlotPane4ToolBar parent;
    private ArrayList changeListeners;

    public ChartDesignerImagePane(String s, int i, String s1, PlotPane4ToolBar plotpane4toolbar)
    {
        state = "normal";
        changeListeners = new ArrayList();
        iconPath = s;
        chartType = i;
        isSelected = false;
        addMouseListener(this);
        setToolTipText(s1);
        parent = plotpane4toolbar;
    }

    public Dimension getPreferredSize()
    {
        return new Dimension(28, 28);
    }

    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        mode = BaseUtils.readIcon((new StringBuilder()).append(iconPath).append(chartType).append("_").append(state).append(".png").toString());
        if(isSelected)
        {
            Icon icon = BaseUtils.readIcon("com/fr/design/images/toolbar/border.png");
            icon.paintIcon(this, g, 0, 0);
        }
        mode.paintIcon(this, g, 3, 3);
    }

    public void setSelected(boolean flag)
    {
        isSelected = flag;
        state = flag ? "normal" : "normal";
    }

    public void mouseClicked(MouseEvent mouseevent)
    {
    }

    public void mousePressed(MouseEvent mouseevent)
    {
        parent.clearChoose();
        if(parent.getSelectedIndex() != chartType)
        {
            parent.setSelectedIndex(chartType);
            fireStateChange();
        }
        isSelected = true;
        state = "normal";
        repaint();
    }

    public void mouseReleased(MouseEvent mouseevent)
    {
    }

    public void mouseEntered(MouseEvent mouseevent)
    {
        if(isSelected)
            state = "normal";
        else
            state = "over";
        repaint();
    }

    public void mouseExited(MouseEvent mouseevent)
    {
        if(isSelected)
            state = "normal";
        else
            state = "normal";
        repaint();
    }

    private void fireStateChange()
    {
        for(int i = 0; i < changeListeners.size(); i++)
            ((ChangeListener)changeListeners.get(i)).stateChanged(new ChangeEvent(this));

    }

    public void registeChangeListener(ChangeListener changelistener)
    {
        changeListeners.add(changelistener);
    }
}
