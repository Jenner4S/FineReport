// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.cell.bar;

import com.fr.design.gui.iscrollbar.UISBChooser;
import java.awt.*;
import java.awt.event.MouseEvent;
import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;

// Referenced classes of package com.fr.design.cell.bar:
//            DynamicScrollButton, ScrollBarUIConstant

public class DynamicScrollBarUI extends BasicScrollBarUI
{
    private class ScrollBarTrackListener extends javax.swing.plaf.basic.BasicScrollBarUI.TrackListener
    {

        final DynamicScrollBarUI this$0;

        public void mouseReleased(MouseEvent mouseevent)
        {
            super.mouseReleased(mouseevent);
            ScrollBarTrackListener.repaint();
        }

        public void mousePressed(MouseEvent mouseevent)
        {
            super.mousePressed(mouseevent);
            ScrollBarTrackListener.repaint();
        }

        public void mouseEntered(MouseEvent mouseevent)
        {
            isRollover = false;
            wasRollover = false;
            if(getThumbBounds().contains(mouseevent.getPoint()))
            {
                isRollover = true;
                wasRollover = isRollover;
                ScrollBarTrackListener.repaint();
            }
        }

        public void mouseExited(MouseEvent mouseevent)
        {
            isRollover = false;
            if(isRollover != wasRollover)
            {
                wasRollover = isRollover;
                ScrollBarTrackListener.repaint();
            }
        }

        public void mouseDragged(MouseEvent mouseevent)
        {
            if(getThumbBounds().contains(mouseevent.getPoint()))
                true.TrackListener = mouseevent;
            super.mouseDragged(mouseevent);
        }

        public void mouseMoved(MouseEvent mouseevent)
        {
            if(getThumbBounds().contains(mouseevent.getPoint()))
            {
                isRollover = true;
                if(isRollover != wasRollover)
                {
                    ScrollBarTrackListener.repaint();
                    wasRollover = isRollover;
                }
            } else
            {
                isRollover = false;
                if(isRollover != wasRollover)
                {
                    ScrollBarTrackListener.repaint();
                    wasRollover = isRollover;
                }
            }
        }

        private ScrollBarTrackListener()
        {
            this$0 = DynamicScrollBarUI.this;
            super(DynamicScrollBarUI.this);
        }

    }


    private boolean isRollover;
    private boolean wasRollover;

    public DynamicScrollBarUI()
    {
        isRollover = false;
        wasRollover = false;
    }

    public boolean isThumbVisible()
    {
        if(scrollbar.getOrientation() == 1)
            return getThumbBounds().height > 0;
        else
            return getThumbBounds().width > 0;
    }

    public void paintThumb(Graphics g, JComponent jcomponent, Rectangle rectangle)
    {
        paintXP(g, rectangle);
    }

    private void paintXP(Graphics g, Rectangle rectangle)
    {
        javax.swing.plaf.ColorUIResource coloruiresource = null;
        if(isDragging && isRollover)
            coloruiresource = ScrollBarUIConstant.PRESS_SCROLL_BAR_COLOR;
        else
        if(isRollover)
            coloruiresource = ScrollBarUIConstant.ROLL_OVER_SCROLL_BAR_COLOR;
        else
            coloruiresource = ScrollBarUIConstant.NORMAL_SCROLL_BAR_COLOR;
        g.setColor(coloruiresource);
        int i = rectangle.x + 1;
        int j = rectangle.y + 1;
        int k = (rectangle.x + rectangle.width) - 1;
        int l = (rectangle.y + rectangle.height) - 1;
        paintScrollBar(g, i, j, k, l, rectangle);
    }

    private void drawHrizontal(Rectangle rectangle, int i, Graphics g, Color color)
    {
        int j = (rectangle.x + rectangle.width / 2) - 4;
        i = Math.min(j + 8, (rectangle.x + rectangle.width) - 5);
        int k = j + 1;
        g.setColor(UISBChooser.getAdjustedColor(color, 0, 71));
        for(; k < i; k += 2)
            g.drawLine(k, 5, k, 11);

        k = j;
        g.setColor(UISBChooser.getAdjustedColor(color, 0, -13));
        for(; k < i; k += 2)
            g.drawLine(k, 6, k, 12);

    }

    private void drawVertical(Rectangle rectangle, int i, Graphics g, Color color)
    {
        int j = (rectangle.y + rectangle.height / 2) - 4;
        i = Math.min(j + 8, (rectangle.y + rectangle.height) - 5);
        int k = j;
        g.setColor(UISBChooser.getAdjustedColor(color, 0, 71));
        for(; k < i; k += 2)
            g.drawLine(5, k, 11, k);

        k = j + 1;
        g.setColor(UISBChooser.getAdjustedColor(color, 0, -13));
        for(; k < i; k += 2)
            g.drawLine(6, k, 12, k);

    }

    private void paintScrollBar(Graphics g, int i, int j, int k, int l, Rectangle rectangle)
    {
        switch(scrollbar.getOrientation())
        {
        default:
            break;

        case 1: // '\001'
            Graphics2D graphics2d = (Graphics2D)g;
            GradientPaint gradientpaint = null;
            if(isRollover)
                gradientpaint = new GradientPaint(i, j, ScrollBarUIConstant.ROLL_OVER_SCROLL_BAR_COLOR, k, j, ScrollBarUIConstant.ROLL_OVER_SCROLL_BAR_COLOR);
            else
                gradientpaint = new GradientPaint(i, j, ScrollBarUIConstant.NORMAL_SCROLL_BAR_COLOR, k, j, ScrollBarUIConstant.NORMAL_SCROLL_BAR_COLOR);
            graphics2d.setPaint(gradientpaint);
            graphics2d.fillRoundRect(i, j, rectangle.width - 2, rectangle.height - 2, 0, 0);
            break;

        case 0: // '\0'
            Graphics2D graphics2d1 = (Graphics2D)g;
            GradientPaint gradientpaint1;
            if(isRollover)
                gradientpaint1 = new GradientPaint(i, j, ScrollBarUIConstant.ROLL_OVER_SCROLL_BAR_COLOR, i, l, ScrollBarUIConstant.ROLL_OVER_SCROLL_BAR_COLOR);
            else
                gradientpaint1 = new GradientPaint(i, j, ScrollBarUIConstant.NORMAL_SCROLL_BAR_COLOR, i, l, ScrollBarUIConstant.NORMAL_SCROLL_BAR_COLOR);
            graphics2d1.setPaint(gradientpaint1);
            graphics2d1.fillRoundRect(i, j, rectangle.width - 2, rectangle.height - 2, 0, 0);
            break;
        }
    }

    protected JButton createDecreaseButton(int i)
    {
        return new DynamicScrollButton(i, this);
    }

    protected JButton createIncreaseButton(int i)
    {
        return new DynamicScrollButton(i, this);
    }

    protected javax.swing.plaf.basic.BasicScrollBarUI.TrackListener createTrackListener()
    {
        return new ScrollBarTrackListener();
    }














}
