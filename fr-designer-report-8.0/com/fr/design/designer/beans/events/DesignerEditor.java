// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.designer.beans.events;

import com.fr.design.utils.gui.LayoutUtils;
import com.fr.stable.core.PropertyChangeAdapter;
import com.fr.stable.core.PropertyChangeListener;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.JComponent;

public class DesignerEditor
    implements PropertyChangeListener
{

    private ArrayList propertyChangeListenerList;
    private ArrayList stopEditListenerList;
    private JComponent comp;
    private boolean changed;

    public DesignerEditor(JComponent jcomponent)
    {
        propertyChangeListenerList = new ArrayList();
        stopEditListenerList = new ArrayList();
        comp = jcomponent;
    }

    public void addStopEditingListener(PropertyChangeAdapter propertychangeadapter)
    {
        int i = stopEditListenerList.indexOf(propertychangeadapter);
        if(i == -1)
            stopEditListenerList.add(propertychangeadapter);
        else
            stopEditListenerList.set(i, propertychangeadapter);
    }

    public void fireEditStoped()
    {
        if(changed)
        {
            PropertyChangeAdapter propertychangeadapter;
            for(Iterator iterator = stopEditListenerList.iterator(); iterator.hasNext(); propertychangeadapter.propertyChange())
                propertychangeadapter = (PropertyChangeAdapter)iterator.next();

            changed = false;
        }
    }

    public void addPropertyChangeListener(PropertyChangeAdapter propertychangeadapter)
    {
        int i = propertyChangeListenerList.indexOf(propertychangeadapter);
        if(i == -1)
            propertyChangeListenerList.add(propertychangeadapter);
        else
            propertyChangeListenerList.set(i, propertychangeadapter);
    }

    public void propertyChange()
    {
        PropertyChangeAdapter propertychangeadapter;
        for(Iterator iterator = propertyChangeListenerList.iterator(); iterator.hasNext(); propertychangeadapter.propertyChange())
            propertychangeadapter = (PropertyChangeAdapter)iterator.next();

        changed = true;
    }

    public void propertyChange(JComponent jcomponent)
    {
    }

    public transient void propertyChange(JComponent ajcomponent[])
    {
    }

    public void reset()
    {
        changed = false;
    }

    public void paintEditor(Graphics g, Dimension dimension)
    {
        if(comp != null)
        {
            comp.setSize(new Dimension(dimension.width - 2, dimension.height - 2));
            LayoutUtils.layoutContainer(comp);
            Graphics g1 = g.create(1, 1, dimension.width, dimension.height);
            comp.paint(g1);
        }
    }

    public JComponent getEditorTarget()
    {
        return comp;
    }

    public volatile void propertyChange(Object aobj[])
    {
        propertyChange((JComponent[])aobj);
    }

    public volatile void propertyChange(Object obj)
    {
        propertyChange((JComponent)obj);
    }
}
