// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe;

import com.fr.base.GraphHelper;
import com.fr.base.background.ColorBackground;
import com.fr.stable.CoreConstants;
import java.awt.*;
import java.awt.geom.Dimension2D;
import java.awt.geom.RoundRectangle2D;
import javax.swing.JPanel;
import javax.swing.JWindow;

public class ToolTip4Chart extends JWindow
{
    private class ToolTipStringPane extends JPanel
    {

        String text;
        final ToolTip4Chart this$0;

        public void paintComponent(Graphics g)
        {
            super.paintComponent(g);
            if(!isOpaque())
            {
                return;
            } else
            {
                g.setFont(font);
                Rectangle rectangle = new Rectangle(0, 0, getWidth(), getHeight());
                ColorBackground colorbackground = ColorBackground.getInstance(Color.white);
                colorbackground.paint(g, new java.awt.geom.RoundRectangle2D.Double(rectangle.getX(), rectangle.getY(), rectangle.getWidth(), rectangle.getHeight(), 5D, 5D));
                Graphics2D graphics2d = (Graphics2D)g;
                graphics2d.drawString(text, 5, getHeight() - 5);
                return;
            }
        }

        public ToolTipStringPane()
        {
            this$0 = ToolTip4Chart.this;
            super();
        }
    }


    private static ToolTip4Chart instance = new ToolTip4Chart();
    private static final int HGAP = 5;
    private static final int VGAP = 3;
    private static final int FONT_SIZE = 12;
    private ToolTipStringPane stringPane;
    private Font font;

    public ToolTip4Chart()
    {
        font = new Font("Dialog", 0, 12);
        stringPane = new ToolTipStringPane();
        getContentPane().add(stringPane);
    }

    public static ToolTip4Chart getInstance()
    {
        if(instance == null)
            instance = new ToolTip4Chart();
        return instance;
    }

    public void showToolTip(String s, int i, int j)
    {
        stringPane.text = s.trim();
        Dimension2D dimension2d = GraphHelper.stringDimensionWithRotation(s, font, 0, CoreConstants.DEFAULT_FRC);
        setSize(new Dimension((int)dimension2d.getWidth() + 10, (int)dimension2d.getHeight() + 6));
        stringPane.setPreferredSize(new Dimension((int)dimension2d.getWidth(), (int)dimension2d.getHeight()));
        if(!isVisible())
        {
            setVisible(true);
            if(i + getWidth() > Toolkit.getDefaultToolkit().getScreenSize().width)
                i -= getWidth();
            setLocation(i, j + 10);
        }
    }

    public void hideToolTip()
    {
        setVisible(false);
    }


}
