// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.cell.bar;

import com.fr.general.ComparatorUtils;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import javax.swing.ButtonModel;
import javax.swing.plaf.basic.BasicArrowButton;

// Referenced classes of package com.fr.design.cell.bar:
//            DynamicScrollBarUI, ScrollBarUIConstant

public class DynamicScrollButton extends BasicArrowButton
{
    static class ScrollButtonKey
    {

        private boolean vertical;
        private Color c;
        private boolean pressed;
        private boolean enabled;
        private boolean rollover;

        public boolean equals(Object obj)
        {
            if(obj == null)
                return false;
            if(!(obj instanceof ScrollButtonKey))
            {
                return false;
            } else
            {
                ScrollButtonKey scrollbuttonkey = (ScrollButtonKey)obj;
                return vertical == scrollbuttonkey.vertical && pressed == scrollbuttonkey.pressed && enabled == scrollbuttonkey.enabled && rollover == scrollbuttonkey.rollover && ComparatorUtils.equals(c, scrollbuttonkey.c);
            }
        }

        public int hashCode()
        {
            return c.hashCode() * (pressed ? 1 : 2) * (enabled ? 4 : 8) * (rollover ? 16 : 32) * (vertical ? '@' : 128);
        }

        ScrollButtonKey(boolean flag, Color color, boolean flag1, boolean flag2, boolean flag3)
        {
            vertical = flag;
            c = color;
            pressed = flag1;
            enabled = flag2;
            rollover = flag3;
        }
    }


    private DynamicScrollBarUI scrollbarUI;
    static HashMap cache = new HashMap();
    private Dimension buttonSize;

    public DynamicScrollButton(int i, DynamicScrollBarUI dynamicscrollbarui)
    {
        super(i);
        buttonSize = new Dimension(17, 17);
        scrollbarUI = dynamicscrollbarui;
        setBorder(null);
        setRolloverEnabled(true);
        setMargin(new Insets(0, 0, 0, 0));
        setSize(buttonSize);
    }

    public void paint(Graphics g)
    {
        javax.swing.plaf.ColorUIResource coloruiresource = null;
        if(!scrollbarUI.isThumbVisible())
            coloruiresource = ScrollBarUIConstant.DISABLE_SCROLL_BAR_COLOR;
        else
        if(getModel().isPressed())
            coloruiresource = ScrollBarUIConstant.PRESS_SCROLL_BAR_COLOR;
        else
        if(getModel().isRollover())
            coloruiresource = ScrollBarUIConstant.ROLL_OVER_SCROLL_BAR_COLOR;
        else
            coloruiresource = ScrollBarUIConstant.NORMAL_SCROLL_BAR_COLOR;
        g.setColor(coloruiresource);
        paintButton(g, getSize(), coloruiresource);
        if(!scrollbarUI.isThumbVisible())
            g.setColor(ScrollBarUIConstant.ARROW_DISABLED_COLOR);
        else
            g.setColor(ScrollBarUIConstant.SCROLL_ARROW_COLOR);
        paintArrow(g, getSize());
    }

    private void paintArrow(Graphics g, Dimension dimension)
    {
        switch(direction)
        {
        case 1: // '\001'
            g.drawLine(8, 5, 8, 5);
            g.drawLine(7, 6, 9, 6);
            g.drawLine(6, 7, 10, 7);
            g.drawLine(5, 8, 7, 8);
            g.drawLine(9, 8, 11, 8);
            g.drawLine(4, 9, 6, 9);
            g.drawLine(10, 9, 12, 9);
            g.drawLine(5, 10, 5, 10);
            g.drawLine(11, 10, 11, 10);
            break;

        case 5: // '\005'
            g.drawLine(5, 6, 5, 6);
            g.drawLine(11, 6, 11, 6);
            g.drawLine(4, 7, 6, 7);
            g.drawLine(10, 7, 12, 7);
            g.drawLine(5, 8, 7, 8);
            g.drawLine(9, 8, 11, 8);
            g.drawLine(6, 9, 10, 9);
            g.drawLine(7, 10, 9, 10);
            g.drawLine(8, 11, 8, 11);
            break;

        case 3: // '\003'
            g.drawLine(6, 5, 6, 5);
            g.drawLine(6, 11, 6, 11);
            g.drawLine(7, 4, 7, 6);
            g.drawLine(7, 10, 7, 12);
            g.drawLine(8, 5, 8, 7);
            g.drawLine(8, 9, 8, 11);
            g.drawLine(9, 6, 9, 10);
            g.drawLine(10, 7, 10, 9);
            g.drawLine(11, 8, 11, 8);
            break;

        case 7: // '\007'
            g.drawLine(4, 8, 4, 8);
            g.drawLine(5, 7, 5, 9);
            g.drawLine(6, 6, 6, 10);
            g.drawLine(7, 5, 7, 7);
            g.drawLine(7, 9, 7, 11);
            g.drawLine(8, 4, 8, 6);
            g.drawLine(8, 10, 8, 12);
            g.drawLine(9, 5, 9, 5);
            g.drawLine(9, 11, 9, 11);
            break;
        }
    }

    private void paintButton(Graphics g, Dimension dimension, Color color)
    {
        boolean flag = scrollbarUI.isThumbVisible();
        boolean flag1 = getModel().isPressed();
        boolean flag2 = getModel().isRollover();
        ScrollButtonKey scrollbuttonkey = new ScrollButtonKey(direction == 1 || direction == 5, color, flag1, flag, flag2);
        Object obj = cache.get(scrollbuttonkey);
        if(obj != null)
        {
            g.drawImage((Image)obj, 0, 0, this);
            return;
        }
        BufferedImage bufferedimage = new BufferedImage(17, 17, 2);
        Graphics g1 = bufferedimage.getGraphics();
        switch(direction)
        {
        case 1: // '\001'
        case 3: // '\003'
        case 5: // '\005'
        case 7: // '\007'
            javax.swing.plaf.ColorUIResource coloruiresource = ScrollBarUIConstant.SCROLL_BORDER_COLOR;
            Graphics2D graphics2d = (Graphics2D)g1;
            GradientPaint gradientpaint = null;
            if(!scrollbarUI.isThumbVisible())
                gradientpaint = new GradientPaint(1.0F, 1.0F, ScrollBarUIConstant.NORMAL_SCROLL_BUTTON_COLOR, 1.0F, 16F, ScrollBarUIConstant.NORMAL_SCROLL_BUTTON_COLOR);
            else
            if(getModel().isPressed())
                gradientpaint = new GradientPaint(1.0F, 1.0F, ScrollBarUIConstant.PRESSED_SCROLL_BUTTON_COLOR, 1.0F, 16F, ScrollBarUIConstant.PRESSED_SCROLL_BUTTON_COLOR);
            else
            if(getModel().isRollover())
                gradientpaint = new GradientPaint(1.0F, 1.0F, ScrollBarUIConstant.PRESSED_SCROLL_BUTTON_COLOR, 1.0F, 16F, ScrollBarUIConstant.PRESSED_SCROLL_BUTTON_COLOR);
            else
                gradientpaint = new GradientPaint(1.0F, 1.0F, ScrollBarUIConstant.NORMAL_SCROLL_BUTTON_COLOR, 1.0F, 16F, ScrollBarUIConstant.NORMAL_SCROLL_BUTTON_COLOR);
            graphics2d.setPaint(gradientpaint);
            graphics2d.fillRoundRect(1, 1, 16, 16, 0, 0);
            // fall through

        case 2: // '\002'
        case 4: // '\004'
        case 6: // '\006'
        default:
            g1.dispose();
            g.drawImage(bufferedimage, 0, 0, this);
            cache.put(scrollbuttonkey, bufferedimage);
            return;
        }
    }

}
