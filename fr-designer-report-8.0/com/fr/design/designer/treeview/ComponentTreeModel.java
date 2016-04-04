// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.designer.treeview;

import com.fr.base.FRContext;
import com.fr.design.designer.beans.events.CreatorEventListenerTable;
import com.fr.design.designer.creator.*;
import com.fr.design.mainframe.FormDesigner;
import com.fr.form.ui.Widget;
import com.fr.general.FRLogger;
import java.awt.Component;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

public class ComponentTreeModel
    implements TreeModel
{

    private ArrayList listeners;
    private Component root;
    private FormDesigner designer;

    public ComponentTreeModel(FormDesigner formdesigner, Component component)
    {
        listeners = new ArrayList();
        designer = formdesigner;
        root = component;
    }

    public Object getRoot()
    {
        return root;
    }

    public Object getChild(Object obj, int i)
    {
        if(obj != null && (obj instanceof XLayoutContainer))
        {
            XLayoutContainer xlayoutcontainer = (XLayoutContainer)obj;
            XCreator xcreator = xlayoutcontainer.getXCreator(i);
            return xcreator.getXCreator();
        } else
        {
            return null;
        }
    }

    public void setRoot(Component component)
    {
        root = component;
    }

    public int getChildCount(Object obj)
    {
        if(obj != null && (obj instanceof XLayoutContainer))
        {
            XLayoutContainer xlayoutcontainer = (XLayoutContainer)obj;
            return xlayoutcontainer.getXCreatorCount();
        } else
        {
            return 0;
        }
    }

    public boolean isLeaf(Object obj)
    {
        if(obj != null && (obj instanceof XCreator))
            return ((XCreator)obj).isComponentTreeLeaf();
        else
            return true;
    }

    public void valueForPathChanged(TreePath treepath, Object obj)
    {
        Object obj1 = treepath.getLastPathComponent();
        if(obj1 != obj)
        {
            if(obj instanceof String)
            {
                XCreator xcreator = (XCreator)obj1;
                Widget widget = ((XWidgetCreator)xcreator).toData();
                widget.setWidgetName((String)obj);
                designer.getEditListenerTable().fireCreatorModified(xcreator, 5);
            }
            TreeModelEvent treemodelevent = new TreeModelEvent(this, treepath.getPath());
            fireEvent("treeNodesChanged", treemodelevent);
        }
    }

    public int getIndexOfChild(Object obj, Object obj1)
    {
        if(obj != null && (obj instanceof XLayoutContainer))
            return ((XLayoutContainer)obj).getIndexOfChild(obj1);
        else
            return -1;
    }

    public void addTreeModelListener(TreeModelListener treemodellistener)
    {
        if(!listeners.contains(treemodellistener))
            listeners.add(treemodellistener);
    }

    public void removeTreeModelListener(TreeModelListener treemodellistener)
    {
        if(listeners.contains(treemodellistener))
            listeners.remove(treemodellistener);
    }

    protected void fireEvent(String s, TreeModelEvent treemodelevent)
    {
        try
        {
            Method method = javax/swing/event/TreeModelListener.getMethod(s, new Class[] {
                javax/swing/event/TreeModelEvent
            });
            TreeModelListener treemodellistener;
            for(Iterator iterator = listeners.iterator(); iterator.hasNext(); method.invoke(treemodellistener, new Object[] {
    treemodelevent
}))
                treemodellistener = (TreeModelListener)iterator.next();

        }
        catch(Exception exception)
        {
            FRContext.getLogger().error(exception.getMessage(), exception);
        }
    }
}
