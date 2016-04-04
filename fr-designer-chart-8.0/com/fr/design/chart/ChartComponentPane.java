// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.chart;

import com.fr.design.chart.gui.ChartComponent;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.stable.core.PropertyChangeAdapter;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import javax.swing.JPanel;
import javax.swing.border.AbstractBorder;
import javax.swing.border.Border;

public class ChartComponentPane extends JPanel
    implements MouseMotionListener, MouseListener
{

    private static final long serialVersionUID = 0xfeee1c42ae076457L;
    private static final int BORDER_STYLE = 10;
    private java.util.List listeners;
    private java.util.List slisteners;
    private ChartComponent chartComponent;
    private boolean select;
    private Border designBorder;

    public ChartComponentPane(ChartComponent chartcomponent)
    {
        listeners = new ArrayList();
        slisteners = new ArrayList();
        select = false;
        designBorder = new AbstractBorder() {

            private static final long serialVersionUID = 0x19052de87afde0b2L;
            final ChartComponentPane this$0;

            public void paintBorder(Component component, Graphics g, int i, int j, int k, int l)
            {
                Color color = g.getColor();
                g.setColor(new Color(157, 228, 245));
                for(int i1 = 0; i1 < 10; i1++)
                    g.drawRect(i + i1, j + i1, k - i1 - i1 - 1, l - i1 - i1 - 1);

                g.setColor(color);
            }

            public Insets getBorderInsets(Component component)
            {
                return new Insets(0, 0, 10, 10);
            }

            
            {
                this$0 = ChartComponentPane.this;
                super();
            }
        }
;
        initComponents(chartcomponent);
    }

    private void initComponents(ChartComponent chartcomponent)
    {
        chartComponent = chartcomponent;
        chartComponent.setLocation(getLocation());
        setBorder(designBorder);
        setLayout(FRGUIPaneFactory.createBorderLayout());
        chartComponent.addStopEditingListener(new PropertyChangeAdapter() {

            final ChartComponentPane this$0;

            public void propertyChange()
            {
                stopEditing();
            }

            
            {
                this$0 = ChartComponentPane.this;
                super();
            }
        }
);
        add(chartcomponent);
        addMouseListener(this);
        addMouseMotionListener(this);
    }

    public void addSizeChangedListener(PropertyChangeAdapter propertychangeadapter)
    {
        slisteners.add(propertychangeadapter);
    }

    public void addStopListener(PropertyChangeAdapter propertychangeadapter)
    {
        listeners.add(propertychangeadapter);
    }

    private void stopEditing()
    {
        fireChanged(listeners);
    }

    public void mouseDragged(MouseEvent mouseevent)
    {
        if(select)
            if(getCursor() == Cursor.getPredefinedCursor(10))
            {
                setSize(mouseevent.getX(), getSize().height);
                setPreferredSize(new Dimension(mouseevent.getX(), getSize().height));
            } else
            if(getCursor() == Cursor.getPredefinedCursor(9))
            {
                setSize(getSize().width, mouseevent.getY());
                setPreferredSize(new Dimension(getSize().width, mouseevent.getY()));
            } else
            if(getCursor() == Cursor.getPredefinedCursor(5))
            {
                setSize(mouseevent.getX(), mouseevent.getY());
                setPreferredSize(new Dimension(mouseevent.getX(), mouseevent.getY()));
            }
        fireSizeChanged();
        super.repaint();
    }

    public void mouseMoved(MouseEvent mouseevent)
    {
        Rectangle rectangle = getBounds();
        if(atCorner(mouseevent, rectangle))
            setCursor(Cursor.getPredefinedCursor(5));
        else
        if(atBottomLine(mouseevent, rectangle))
            setCursor(Cursor.getPredefinedCursor(9));
        else
        if(atRightLine(mouseevent, rectangle))
            setCursor(Cursor.getPredefinedCursor(10));
        else
            setCursor(Cursor.getPredefinedCursor(0));
    }

    private boolean atBottomLine(MouseEvent mouseevent, Rectangle rectangle)
    {
        return mouseevent.getY() < rectangle.height && mouseevent.getY() > rectangle.height - 10;
    }

    private boolean atRightLine(MouseEvent mouseevent, Rectangle rectangle)
    {
        return mouseevent.getX() < rectangle.width && mouseevent.getX() > rectangle.width - 10;
    }

    private boolean atCorner(MouseEvent mouseevent, Rectangle rectangle)
    {
        return atBottomLine(mouseevent, rectangle) && atRightLine(mouseevent, rectangle);
    }

    public void mouseClicked(MouseEvent mouseevent)
    {
    }

    public void mouseEntered(MouseEvent mouseevent)
    {
    }

    public void mouseExited(MouseEvent mouseevent)
    {
    }

    public void mousePressed(MouseEvent mouseevent)
    {
        if(getCursor() == Cursor.getPredefinedCursor(10) || getCursor() == Cursor.getPredefinedCursor(9) || getCursor() == Cursor.getPredefinedCursor(5))
            select = true;
    }

    public void mouseReleased(MouseEvent mouseevent)
    {
        select = false;
        repaint();
    }

    private void fireSizeChanged()
    {
        fireChanged(slisteners);
    }

    private void fireChanged(java.util.List list)
    {
        int i = list.size();
        for(int j = i; j > 0; j--)
            ((PropertyChangeAdapter)list.get(j - 1)).propertyChange();

    }

}
