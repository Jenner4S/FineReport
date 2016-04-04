// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.widget.renderer;

import com.fr.design.designer.properties.Encoder;
import com.fr.design.mainframe.widget.wrappers.FontWrapper;
import com.fr.general.FRFont;
import java.awt.FontMetrics;
import java.awt.Graphics;
import javax.swing.border.Border;

// Referenced classes of package com.fr.design.mainframe.widget.renderer:
//            GenericCellRenderer

public class FontCellRenderer extends GenericCellRenderer
{

    private static Encoder wrapper = new FontWrapper();
    private FRFont fontValue;

    public FontCellRenderer()
    {
    }

    public void paint(Graphics g)
    {
        int i = getWidth();
        int j = getHeight();
        g.setColor(getBackground());
        g.fillRect(0, 0, i, j);
        g.setColor(getForeground());
        FontMetrics fontmetrics = g.getFontMetrics();
        int k = (j - fontmetrics.getHeight()) / 2 + fontmetrics.getAscent();
        g.drawString(getFontText(), 0, k);
        if(getBorder() != null)
            getBorder().paintBorder(this, g, 0, 0, i, j);
    }

    private String getFontText()
    {
        return fontValue != null ? wrapper.encode(fontValue) : "";
    }

    public void setValue(Object obj)
    {
        FRFont frfont = (FRFont)obj;
        if(frfont != null)
            fontValue = frfont;
        else
            fontValue = FRFont.getInstance();
    }

}
