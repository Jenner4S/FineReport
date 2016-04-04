// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.grid;

import java.awt.Color;
import javax.swing.ActionMap;
import javax.swing.InputMap;

// Referenced classes of package com.fr.grid:
//            BaseGridComponent

public abstract class GridHeader extends BaseGridComponent
{

    public static final int SIZE_ADJUST = 4;
    private Color separatorLineColor;
    private Color selectedForeground;
    private Color selectedBackground;

    public GridHeader()
    {
        separatorLineColor = new Color(172, 168, 153);
        selectedForeground = Color.black;
        selectedBackground = new Color(253, 216, 153);
        getInputMap().clear();
        getActionMap().clear();
        setFocusable(false);
        setOpaque(true);
        initByConstructor();
    }

    protected abstract void initByConstructor();

    protected abstract Object getDisplay(int i);

    public Color getSeparatorLineColor()
    {
        return separatorLineColor;
    }

    public void setSeparatorLineColor(Color color)
    {
        Color color1 = separatorLineColor;
        separatorLineColor = color;
        firePropertyChange("separatorLineColor", color1, separatorLineColor);
        repaint();
    }

    public Color getSelectedForeground()
    {
        return selectedForeground;
    }

    public void setSelectedForeground(Color color)
    {
        Color color1 = selectedForeground;
        selectedForeground = color;
        firePropertyChange("selectedForeground", color1, selectedForeground);
        repaint();
    }

    public Color getSelectedBackground()
    {
        return selectedBackground;
    }

    public void setSelectedBackground(Color color)
    {
        Color color1 = selectedBackground;
        selectedBackground = color;
        firePropertyChange("selectedBackground", color1, selectedBackground);
        repaint();
    }
}
