// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe;

import com.fr.design.constants.UIConstants;
import com.fr.design.designer.beans.AdapterBus;
import com.fr.design.designer.beans.ComponentAdapter;
import com.fr.design.designer.beans.events.DesignerEditListener;
import com.fr.design.designer.beans.events.DesignerEvent;
import com.fr.design.designer.beans.models.SelectionModel;
import com.fr.design.designer.creator.XCreator;
import com.fr.design.designer.creator.XLayoutContainer;
import com.fr.design.designer.treeview.ComponentTreeCellRenderer;
import com.fr.design.designer.treeview.ComponentTreeModel;
import com.fr.form.ui.Widget;
import com.fr.stable.StringUtils;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

// Referenced classes of package com.fr.design.mainframe:
//            TreeTransferHandler, FormDesigner, FormSelection

public class ComponentTree extends JTree
{
    private class TreeDesignerEditAdapter
        implements DesignerEditListener
    {

        final ComponentTree this$0;

        public void fireCreatorModified(DesignerEvent designerevent)
        {
            if(designerevent.getCreatorEventID() == 7)
            {
                TreePath atreepath[] = getSelectedTreePath();
                if(atreepath.length == 1)
                    setAndScrollSelectionPath(atreepath[0]);
                else
                    setSelectionPaths(atreepath);
            } else
            if(designerevent.getCreatorEventID() == 4)
            {
                refreshUI();
                TreePath atreepath1[] = getSelectedTreePath();
                if(atreepath1.length == 1)
                    setAndScrollSelectionPath(atreepath1[0]);
                else
                    setSelectionPaths(atreepath1);
                repaint();
            } else
            {
                refreshUI();
                repaint();
            }
        }

        public boolean equals(Object obj)
        {
            return obj.getClass() == getClass();
        }

        private TreeDesignerEditAdapter()
        {
            this$0 = ComponentTree.this;
            super();
        }

    }


    private FormDesigner designer;
    private ComponentTreeModel model;

    public ComponentTree(FormDesigner formdesigner)
    {
        designer = formdesigner;
        setBackground(UIConstants.NORMAL_BACKGROUND);
        setRootVisible(true);
        setCellRenderer(new ComponentTreeCellRenderer());
        getSelectionModel().setSelectionMode(4);
        setDragEnabled(false);
        setDropMode(DropMode.ON_OR_INSERT);
        setTransferHandler(new TreeTransferHandler());
        refreshTreeRoot();
        TreePath atreepath[] = getSelectedTreePath();
        addTreeSelectionListener(formdesigner);
        setSelectionPaths(atreepath);
        formdesigner.addDesignerEditListener(new TreeDesignerEditAdapter());
        addMouseListener(new MouseAdapter() {

            final ComponentTree this$0;

            public void mouseClicked(MouseEvent mouseevent)
            {
                if(mouseevent.isPopupTrigger())
                    popupMenu(mouseevent);
            }

            public void mousePressed(MouseEvent mouseevent)
            {
                if(mouseevent.isPopupTrigger())
                    popupMenu(mouseevent);
            }

            public void mouseReleased(MouseEvent mouseevent)
            {
                if(mouseevent.isPopupTrigger())
                    popupMenu(mouseevent);
            }

            
            {
                this$0 = ComponentTree.this;
                super();
            }
        }
);
        setEditable(true);
    }

    public ComponentTree(FormDesigner formdesigner, ComponentTreeModel componenttreemodel)
    {
        this(formdesigner);
        setModel(componenttreemodel);
    }

    public boolean isPathEditable(TreePath treepath)
    {
        Object obj = treepath.getLastPathComponent();
        if(obj == designer.getRootComponent())
            return false;
        else
            return super.isPathEditable(treepath);
    }

    public String convertValueToText(Object obj, boolean flag, boolean flag1, boolean flag2, int i, boolean flag3)
    {
        if(obj != null && (obj instanceof XCreator))
            return ((XCreator)obj).toData().getWidgetName();
        else
            return super.convertValueToText(obj, flag, flag1, flag2, i, flag3);
    }

    public void setAndScrollSelectionPath(TreePath treepath)
    {
        setSelectionPath(treepath);
        scrollPathToVisible(treepath);
    }

    private void popupMenu(MouseEvent mouseevent)
    {
        TreePath treepath = getSelectionPath();
        if(treepath == null)
            return;
        Component component = (Component)treepath.getLastPathComponent();
        if(!(component instanceof XCreator))
        {
            return;
        } else
        {
            ComponentAdapter componentadapter = AdapterBus.getComponentAdapter(designer, (XCreator)component);
            JPopupMenu jpopupmenu = componentadapter.getContextPopupMenu(mouseevent);
            jpopupmenu.show(this, mouseevent.getX(), mouseevent.getY());
            return;
        }
    }

    public void refreshUI()
    {
        updateUI();
    }

    public TreePath[] getSelectedTreePath()
    {
        XCreator axcreator[] = designer.getSelectionModel().getSelection().getSelectedCreators();
        TreePath atreepath[] = new TreePath[axcreator.length];
        for(int i = 0; i < atreepath.length; i++)
            atreepath[i] = buildTreePath(axcreator[i]);

        return atreepath;
    }

    public TreePath[] search(String s)
    {
        if(StringUtils.isNotEmpty(s))
            s = s.toLowerCase();
        ArrayList arraylist = new ArrayList();
        XLayoutContainer xlayoutcontainer = designer.getRootComponent();
        searchFormRoot(xlayoutcontainer, arraylist, s);
        TreePath atreepath[] = new TreePath[arraylist.size()];
        for(int i = 0; i < atreepath.length; i++)
            atreepath[i] = buildTreePath((Component)arraylist.get(i));

        if(atreepath.length > 0)
            setAndScrollSelectionPath(atreepath[0]);
        else
            setSelectionPath();
        return atreepath;
    }

    private void setSelectionPath()
    {
        super.setSelectionPath(null);
        clearSelection();
    }

    private void searchFormRoot(XLayoutContainer xlayoutcontainer, ArrayList arraylist, String s)
    {
        if(StringUtils.isEmpty(s))
            return;
        int i = 0;
        for(int j = xlayoutcontainer.getXCreatorCount(); i < j; i++)
        {
            XCreator xcreator = xlayoutcontainer.getXCreator(i);
            String s1 = xcreator.toData().getWidgetName();
            if(s1.toLowerCase().contains(s))
                arraylist.add(xcreator);
            if(xcreator instanceof XLayoutContainer)
                searchFormRoot((XLayoutContainer)xcreator, arraylist, s);
        }

    }

    public void fireTreeChanged()
    {
        designer.refreshDesignerUI();
    }

    public void refreshTreeRoot()
    {
        model = new ComponentTreeModel(designer, designer.getTopContainer());
        setModel(model);
        setDragEnabled(false);
        setDropMode(DropMode.ON_OR_INSERT);
        setTransferHandler(new TreeTransferHandler());
        repaint();
    }

    private TreePath buildTreePath(Component component)
    {
        ArrayList arraylist = new ArrayList();
        for(Object obj = component; obj != null; obj = ((Component) (obj)).getParent())
        {
            XCreator xcreator = (XCreator)obj;
            arraylist.add(0, obj);
            if(xcreator != component)
                xcreator.notShowInComponentTree(arraylist);
        }

        Object aobj[] = arraylist.toArray();
        return new TreePath(aobj);
    }

}
