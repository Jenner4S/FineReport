// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.widget.renderer;

import com.fr.design.designer.properties.Encoder;
import java.awt.FontMetrics;
import java.awt.Graphics;
import javax.swing.border.Border;

// Referenced classes of package com.fr.design.mainframe.widget.renderer:
//            GenericCellRenderer

public class EncoderCellRenderer extends GenericCellRenderer
{

    private static int LEFT = 1;
    protected Encoder encoder;
    protected Object value;

    public EncoderCellRenderer(Encoder encoder1)
    {
        encoder = encoder1;
    }

    public void paint(Graphics g)
    {
        int i = getWidth();
        int j = getHeight();
        g.setColor(getBackground());
        g.fillRect(0, 0, i, j);
        int k = LEFT;
        g.setColor(getForeground());
        FontMetrics fontmetrics = g.getFontMetrics();
        int l = (j - fontmetrics.getHeight()) / 2 + fontmetrics.getAscent();
        String s = getValueText();
        if(s != null)
            g.drawString(s, k, l);
        if(getBorder() != null)
            getBorder().paintBorder(this, g, 0, 0, i, j);
    }

    public void setValue(Object obj)
    {
        value = obj;
    }

    private String getValueText()
    {
        return encoder.encode(value);
    }

}
