// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.form.layout;

import com.fr.design.gui.itextfield.UITextField;
import java.awt.*;
import java.io.PrintStream;
import javax.swing.JFrame;
import javax.swing.JPanel;

// Referenced classes of package com.fr.design.form.layout:
//            FRSplitLayout

public class FRVerticalSplitLayout extends FRSplitLayout
{

    public FRVerticalSplitLayout()
    {
    }

    public FRVerticalSplitLayout(double d)
    {
        super(d);
    }

    public FRVerticalSplitLayout(double d, int i, int j)
    {
        super(d, i, j);
    }

    protected Dimension calculateAppropriateSize(Container container, Dimension dimension, Dimension dimension1)
    {
        Dimension dimension2 = new Dimension(0, 0);
        Insets insets = container.getInsets();
        dimension2.width += insets.left + insets.right + hgap * 2;
        dimension2.height += insets.top + insets.bottom + vgap;
        int i = 0;
        if(dimension != null)
            i = (int)(dimension.getHeight() / ratio);
        if(dimension1 != null)
        {
            int j = (int)(dimension1.getHeight() / (1.0D - ratio));
            if(j > i)
                i = j;
        }
        dimension2.height += i;
        return dimension2;
    }

    public void layoutContainer(Container container)
    {
        Insets insets = container.getInsets();
        int i = container.getWidth() - insets.left - insets.right - hgap * 2;
        int j = container.getHeight() - insets.top - insets.bottom - vgap;
        int k = 0;
        if(aside != null)
        {
            k = (int)((double)j * ratio);
            aside.setBounds(insets.left + hgap, insets.top + vgap, i, k);
        }
        if(center != null)
            center.setBounds(insets.left + hgap, insets.top + k + vgap, i, j - k);
    }

    public static void main(String args[])
    {
        JFrame jframe = new JFrame("垂直均分布局测试");
        JPanel jpanel = (JPanel)jframe.getContentPane();
        jpanel.setLayout(new FRVerticalSplitLayout(0.20000000000000001D, 2, 2));
        UITextField uitextfield = new UITextField("上边");
        jpanel.add(uitextfield, "aside");
        UITextField uitextfield1 = new UITextField("下边");
        jpanel.add(uitextfield1, "center");
        jframe.setSize(300, 210);
        jframe.setVisible(true);
        System.out.println(uitextfield.getSize());
        System.out.println(uitextfield1.getSize());
    }
}
