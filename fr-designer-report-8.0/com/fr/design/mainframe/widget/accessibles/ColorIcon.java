// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.widget.accessibles;

import java.awt.*;
import javax.swing.Icon;

public class ColorIcon
    implements Icon
{

    private static int BOX = 11;
    static Color BORDER_COLOR = new Color(172, 168, 153);
    private String name;
    private Color color;

    public ColorIcon(Color color1)
    {
        this((new StringBuilder()).append("[").append(color1.getRed()).append(", ").append(color1.getGreen()).append(", ").append(color1.getBlue()).append("]").toString(), color1);
    }

    public ColorIcon(String s, Color color1)
    {
        name = s;
        color = color1;
    }

    public Color getColor()
    {
        return color;
    }

    public String toString()
    {
        return name;
    }

    public void paintIcon(Component component, Graphics g, int i, int j)
    {
        g.setColor(color);
        g.fillRect(i, j, getIconWidth(), getIconHeight());
        g.setColor(BORDER_COLOR);
        g.drawRect(i, j, getIconWidth(), getIconHeight());
    }

    public int getIconWidth()
    {
        return BOX;
    }

    public int getIconHeight()
    {
        return BOX;
    }

}
