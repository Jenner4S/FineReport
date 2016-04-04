// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.form.layout;

import com.fr.design.gui.itextfield.UITextField;
import java.awt.*;
import javax.swing.JFrame;
import javax.swing.JPanel;

// Referenced classes of package com.fr.design.form.layout:
//            FRSplitLayout

public class FRHorizontalSplitLayout extends FRSplitLayout
{

    public FRHorizontalSplitLayout()
    {
    }

    public FRHorizontalSplitLayout(double d)
    {
        super(d);
    }

    public FRHorizontalSplitLayout(double d, int i, int j)
    {
        super(d, i, j);
    }

    protected Dimension calculateAppropriateSize(Container container, Dimension dimension, Dimension dimension1)
    {
        Dimension dimension2 = new Dimension(0, 0);
        Insets insets = container.getInsets();
        dimension2.width += insets.left + insets.right + hgap;
        dimension2.height += insets.top + insets.bottom + vgap * 2;
        int i = 0;
        if(dimension != null)
            i = (int)(dimension.getWidth() / ratio);
        if(dimension1 != null)
        {
            int j = (int)(dimension1.getWidth() / (1.0D - ratio));
            if(j > i)
                i = j;
        }
        dimension2.width += i;
        return dimension2;
    }

    public void layoutContainer(Container container)
    {
        synchronized(container.getTreeLock())
        {
            Insets insets = container.getInsets();
            int i = container.getWidth() - insets.left - insets.right - hgap;
            int j = container.getHeight() - insets.top - insets.bottom - 2 * vgap;
            int k = 0;
            if(aside != null)
            {
                k = (int)((double)i * ratio);
                aside.setBounds(insets.left, insets.top + vgap, k, j);
            }
            if(center != null)
                center.setBounds(insets.left + k + hgap, insets.top + vgap, i - k, j);
        }
    }

    public static void main(String args[])
    {
        JFrame jframe = new JFrame("水平均分布局测试");
        JPanel jpanel = (JPanel)jframe.getContentPane();
        jpanel.setLayout(new FRHorizontalSplitLayout(0.80000000000000004D, 2, 2));
        UITextField uitextfield = new UITextField("左边");
        jpanel.add(uitextfield, "aside");
        UITextField uitextfield1 = new UITextField("右边");
        jpanel.add(uitextfield1, "center");
        jframe.setSize(300, 200);
        jframe.setVisible(true);
    }
}
