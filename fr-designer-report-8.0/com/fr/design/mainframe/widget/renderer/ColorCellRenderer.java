// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.widget.renderer;

import com.fr.design.designer.properties.Encoder;
import com.fr.design.mainframe.widget.wrappers.ColorWrapper;
import java.awt.*;
import javax.swing.border.Border;

// Referenced classes of package com.fr.design.mainframe.widget.renderer:
//            GenericCellRenderer

public class ColorCellRenderer extends GenericCellRenderer
{

    private static Encoder wrapper = new ColorWrapper();
    private static int BOX = 12;
    private static int LEFT = 4;
    private static int ICON_TEXT_PAD = 4;
    private Color color;

    public ColorCellRenderer()
    {
    }

    public void paint(Graphics g)
    {
        int i = getWidth();
        int j = getHeight();
        g.setColor(getBackground());
        g.fillRect(0, 0, i, j);
        int k = 0;
        int l = (j - BOX) / 2;
        if(color != null)
        {
            k += LEFT;
            g.setColor(color);
            g.fillRect(k, l, BOX, BOX);
            g.setColor(getForeground());
            g.drawRect(k, l, BOX, BOX);
            k += BOX + ICON_TEXT_PAD;
        } else
        {
            g.setColor(getForeground());
        }
        FontMetrics fontmetrics = g.getFontMetrics();
        l = (j - fontmetrics.getHeight()) / 2 + fontmetrics.getAscent();
        String s = getColorText();
        g.drawString(s, k, l);
        if(getBorder() != null)
            getBorder().paintBorder(this, g, 0, 0, i, j);
    }

    private String getColorText()
    {
        return wrapper.encode(color);
    }

    public void setValue(Object obj)
    {
        Color color1 = (Color)obj;
        color = color1;
    }

}
