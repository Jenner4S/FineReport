// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.widget.renderer;

import com.fr.base.*;
import com.fr.form.ui.WidgetManager;
import com.fr.form.ui.WidgetManagerProvider;
import com.fr.general.FRLogger;
import java.awt.*;

// Referenced classes of package com.fr.design.mainframe.widget.renderer:
//            GenericCellRenderer

public class IconCellRenderer extends GenericCellRenderer
{

    private Image img;

    public IconCellRenderer()
    {
    }

    public void setValue(Object obj)
    {
        try
        {
            Icon icon = WidgetManager.getProviderInstance().getIconManager().getIcon(obj);
            setImage(icon != null ? icon.getImage() : null);
        }
        catch(CloneNotSupportedException clonenotsupportedexception)
        {
            setImage(null);
            FRContext.getLogger().error(clonenotsupportedexception.getMessage(), clonenotsupportedexception);
        }
    }

    private void setImage(Image image)
    {
        img = image;
        repaint();
    }

    public void paintComponent(Graphics g)
    {
        int i = getWidth();
        int j = getHeight();
        g.setColor(getBackground());
        g.fillRect(0, 0, i, j);
        Graphics2D graphics2d = (Graphics2D)g;
        if(img != null)
            graphics2d.drawImage(img, 4, (getHeight() - 16) / 2, 16, 16, null);
    }
}
