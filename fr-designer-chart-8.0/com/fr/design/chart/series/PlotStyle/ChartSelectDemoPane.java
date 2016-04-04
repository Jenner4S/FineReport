// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.chart.series.PlotStyle;

import com.fr.design.dialog.BasicPane;
import com.fr.design.event.UIObserver;
import com.fr.design.event.UIObserverListener;
import com.fr.stable.ArrayUtils;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class ChartSelectDemoPane extends BasicPane
    implements UIObserver, MouseListener
{

    private static final long serialVersionUID = 0x6b14a51f93ab9638L;
    public boolean isPressing;
    protected ChartSelectDemoPane demoList[];
    private boolean isRollOver;
    private ArrayList changeListeners;

    public ChartSelectDemoPane()
    {
        demoList = new ChartSelectDemoPane[0];
        changeListeners = new ArrayList();
    }

    public void setDemoGroup(ChartSelectDemoPane achartselectdemopane[])
    {
        demoList = achartselectdemopane;
    }

    protected String title4PopupWindow()
    {
        return "";
    }

    public void mouseClicked(MouseEvent mouseevent)
    {
        if(isEnabled())
        {
            for(int i = 0; i < ArrayUtils.getLength(demoList); i++)
            {
                demoList[i].isRollOver = false;
                demoList[i].isPressing = false;
            }

            isPressing = true;
            fireStateChange();
            for(int j = 0; j < ArrayUtils.getLength(demoList); j++)
            {
                demoList[j].checkBackground();
                demoList[j].repaint();
            }

        }
    }

    public void addChangeListener(ChangeListener changelistener)
    {
        changeListeners.add(changelistener);
    }

    private void fireStateChange()
    {
        for(int i = 0; i < changeListeners.size(); i++)
            ((ChangeListener)changeListeners.get(i)).stateChanged(new ChangeEvent(this));

    }

    public void mousePressed(MouseEvent mouseevent)
    {
    }

    public void mouseReleased(MouseEvent mouseevent)
    {
    }

    public void mouseEntered(MouseEvent mouseevent)
    {
        if(isEnabled())
        {
            for(int i = 0; i < ArrayUtils.getLength(demoList); i++)
                demoList[i].isRollOver = false;

            isRollOver = true;
            for(int j = 0; j < ArrayUtils.getLength(demoList); j++)
            {
                demoList[j].checkBackground();
                demoList[j].repaint();
            }

        }
    }

    public void mouseExited(MouseEvent mouseevent)
    {
        if(isEnabled())
        {
            int i = mouseevent.getX();
            int j = mouseevent.getY();
            Dimension dimension = getPreferredSize();
            if(inDimension(dimension, i, j))
                isRollOver = true;
            else
                isRollOver = false;
            for(int k = 0; k < ArrayUtils.getLength(demoList); k++)
            {
                demoList[k].checkBackground();
                demoList[k].repaint();
            }

        }
    }

    private boolean inDimension(Dimension dimension, int i, int j)
    {
        return (double)i < dimension.getWidth() && (double)j < dimension.getHeight() && i > 0 && j > 0;
    }

    public void registerChangeListener(final UIObserverListener listener)
    {
        changeListeners.add(new ChangeListener() {

            final UIObserverListener val$listener;
            final ChartSelectDemoPane this$0;

            public void stateChanged(ChangeEvent changeevent)
            {
                listener.doChange();
            }

            
            {
                this$0 = ChartSelectDemoPane.this;
                listener = uiobserverlistener;
                super();
            }
        }
);
    }

    public boolean shouldResponseChangeListener()
    {
        return true;
    }

    public void checkBackground()
    {
        if(!isRollOver && !isPressing)
            setBackground(null);
        else
        if(isRollOver && !isPressing)
            setBackground(new Color(182, 217, 253));
        else
            setBackground(new Color(164, 192, 220));
    }
}
