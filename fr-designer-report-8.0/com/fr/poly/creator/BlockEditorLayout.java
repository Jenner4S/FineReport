// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.poly.creator;

import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.ilable.UILabel;
import java.awt.*;
import java.io.Serializable;
import javax.swing.*;

public class BlockEditorLayout
    implements LayoutManager, Serializable
{

    public static final String CENTER = "center";
    public static final String LEFTBOTTOM = "leftbottom";
    public static final String RIGHTTOP = "righttop";
    public static final String BOTTOMCORNER = "bottomcorner";
    public static final String TOP = "top";
    private Component center;
    private Component lb;
    private Component rt;
    private Component bc;
    private Component tp;

    public BlockEditorLayout()
    {
    }

    public void addLayoutComponent(String s, Component component)
    {
        if("center".equals(s))
            center = component;
        else
        if("leftbottom".equals(s))
            lb = component;
        else
        if("righttop".equals(s))
            rt = component;
        else
        if("bottomcorner".equals(s))
            bc = component;
        else
        if("top".equals(s))
            tp = component;
    }

    public void removeLayoutComponent(Component component)
    {
    }

    public Dimension preferredLayoutSize(Container container)
    {
        Object obj = container.getTreeLock();
        JVM INSTR monitorenter ;
        Insets insets = container.getInsets();
        Dimension dimension = new Dimension();
        Dimension dimension1 = center.getPreferredSize();
        Dimension dimension2 = lb.getPreferredSize();
        Dimension dimension3 = rt.getPreferredSize();
        Dimension dimension4 = tp.getPreferredSize();
        dimension.height = insets.top + insets.bottom + dimension1.height + dimension2.height + dimension4.height;
        dimension.width = insets.left + insets.right + dimension1.width + dimension3.width;
        return dimension;
        Exception exception;
        exception;
        throw exception;
    }

    public Dimension minimumLayoutSize(Container container)
    {
        Object obj = container.getTreeLock();
        JVM INSTR monitorenter ;
        Insets insets = container.getInsets();
        Dimension dimension = new Dimension();
        Dimension dimension1 = center.getMinimumSize();
        Dimension dimension2 = lb.getMinimumSize();
        Dimension dimension3 = rt.getMinimumSize();
        Dimension dimension4 = tp.getMinimumSize();
        dimension.height = insets.top + insets.bottom + dimension1.height + dimension2.height + dimension4.height;
        dimension.width = insets.left + insets.right + dimension1.width + dimension3.width;
        return dimension;
        Exception exception;
        exception;
        throw exception;
    }

    public void layoutContainer(Container container)
    {
        synchronized(container.getTreeLock())
        {
            Insets insets = container.getInsets();
            int i = insets.top;
            int j = container.getHeight() - insets.bottom;
            int k = insets.left;
            int l = container.getWidth() - insets.right;
            Dimension dimension = lb.getPreferredSize();
            Dimension dimension1 = rt.getPreferredSize();
            if(tp == null)
            {
                lb.setBounds(k, j - dimension.height, dimension.width, dimension.height);
                rt.setBounds(l - dimension1.width, i, dimension1.width, dimension1.height);
                center.setBounds(k, i, l - dimension1.width, j - dimension.height);
            } else
            {
                Dimension dimension2 = tp.getPreferredSize();
                tp.setBounds(k, i, dimension2.width, dimension2.height);
                center.setBounds(k, i + dimension2.height, l - dimension1.width, j - dimension.height - dimension2.height);
                lb.setBounds(k, j - dimension.height, dimension.width, dimension.height);
                rt.setBounds(l - dimension1.width, i + dimension2.height, dimension1.width, dimension1.height);
            }
            if(bc != null)
                bc.setBounds(l - dimension1.width * 2, j - dimension.height * 2, dimension1.width * 2, dimension.height * 2);
        }
    }

    public static transient void main(String args[])
    {
        JFrame jframe = new JFrame();
        JPanel jpanel = (JPanel)jframe.getContentPane();
        jpanel.setLayout(new BlockEditorLayout());
        UILabel uilabel = new UILabel("111");
        uilabel.setBorder(BorderFactory.createLineBorder(Color.red));
        jpanel.add("center", uilabel);
        UIButton uibutton = new UIButton();
        uibutton.setPreferredSize(new Dimension(30, 15));
        jpanel.add("leftbottom", uibutton);
        UIButton uibutton1 = new UIButton();
        uibutton1.setPreferredSize(new Dimension(15, 30));
        jpanel.add("righttop", uibutton1);
        UIButton uibutton2 = new UIButton();
        jpanel.add("bottomcorner", uibutton2);
        UIButton uibutton3 = new UIButton();
        jpanel.add("top", uibutton3);
        jframe.setSize(300, 200);
        jframe.setVisible(true);
    }
}
