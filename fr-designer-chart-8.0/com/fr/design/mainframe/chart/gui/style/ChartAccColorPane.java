// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.chart.gui.style;

import com.fr.chart.base.ChartConstants;
import com.fr.design.dialog.BasicPane;
import com.fr.design.event.UIObserver;
import com.fr.design.event.UIObserverListener;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.style.color.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Rectangle2D;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class ChartAccColorPane extends BasicPane
    implements MouseListener, UIObserver, ColorSelectable
{

    private static final long serialVersionUID = 0x6897747520f25f53L;
    private static final int WIDTH = 16;
    private static final int ROWCOUNT = 8;
    private Color colors[];
    private int currentIndex;
    private ChangeListener changeListener;
    private UIObserverListener uiObserverListener;
    private Color color;

    public ChartAccColorPane()
    {
        colors = new Color[32];
        currentIndex = 0;
        changeListener = null;
        color = null;
        addMouseListener(this);
        Color acolor[] = ChartConstants.CHART_COLOR_ARRAY;
        for(int i = 0; i < acolor.length; i++)
            colors[i] = acolor[i];

        iniListener();
    }

    private void iniListener()
    {
        if(shouldResponseChangeListener())
            addChangeListener(new ChangeListener() {

                final ChartAccColorPane this$0;

                public void stateChanged(ChangeEvent changeevent)
                {
                    if(uiObserverListener == null)
                    {
                        return;
                    } else
                    {
                        uiObserverListener.doChange();
                        return;
                    }
                }

            
            {
                this$0 = ChartAccColorPane.this;
                super();
            }
            }
);
    }

    public void paintComponent(Graphics g)
    {
        Graphics2D graphics2d = (Graphics2D)g;
        super.repaint();
        java.awt.Rectangle rectangle = getBounds();
        if(rectangle == null)
            return;
        Paint paint = graphics2d.getPaint();
        graphics2d.setPaint(new Color(240, 240, 240));
        graphics2d.fillRect(0, 0, (int)rectangle.getWidth(), (int)rectangle.getHeight());
        graphics2d.setPaint(paint);
        int i = 0;
        boolean flag = false;
        for(int k = 0; k < colors.length; k++)
        {
            Color color1 = colors[k];
            graphics2d.setColor(color1 == null ? Color.white : color1);
            if(k % 8 == 0 && k != 0)
                i += 16;
            int j = k % 8;
            graphics2d.fillRect(j * 16, i, 16, 16);
        }

    }

    protected String title4PopupWindow()
    {
        return "";
    }

    private int getColorIndex(double d, double d1)
    {
        int i = (int)(d / 16D) % 8;
        int j = (int)(d1 / 16D) % 8;
        return i + j * 8;
    }

    public void mouseClicked(MouseEvent mouseevent)
    {
        int i = mouseevent.getX();
        int j = mouseevent.getY();
        if(i <= 0 || i >= 128)
            return;
        if(j <= 0 || j >= 64)
            return;
        int k = getColorIndex(mouseevent.getX(), mouseevent.getY());
        if(k < colors.length)
        {
            currentIndex = k;
            ColorSelectDetailPane colorselectdetailpane = new ColorSelectDetailPane(colors[currentIndex]);
            ColorSelectDialog.showDialog(DesignerContext.getDesignerFrame(), colorselectdetailpane, colors[currentIndex], this);
            Color color1 = getColor();
            if(color1 != null)
            {
                colors[currentIndex] = color1;
                ColorSelectConfigManager.getInstance().addToColorQueue(color1);
                stateChanged();
            }
            repaint();
        }
    }

    public void mouseEntered(MouseEvent mouseevent)
    {
    }

    public void mouseExited(MouseEvent mouseevent)
    {
    }

    public void mousePressed(MouseEvent mouseevent)
    {
    }

    public void mouseReleased(MouseEvent mouseevent)
    {
    }

    public void populateBean(Color acolor[])
    {
        for(int i = 0; i < colors.length; i++)
            if(i < acolor.length)
                colors[i] = acolor[i];
            else
                colors[i] = Color.white;

        repaint();
    }

    public Color[] updateBean()
    {
        return colors;
    }

    public void registerChangeListener(UIObserverListener uiobserverlistener)
    {
        uiObserverListener = uiobserverlistener;
    }

    public boolean shouldResponseChangeListener()
    {
        return true;
    }

    public void stateChanged()
    {
        if(changeListener != null)
            changeListener.stateChanged(null);
    }

    public void addChangeListener(ChangeListener changelistener)
    {
        changeListener = changelistener;
    }

    public void setColor(Color color1)
    {
        color = color1;
    }

    public Color getColor()
    {
        return color;
    }

    public void colorSetted(ColorCell colorcell)
    {
    }

}
