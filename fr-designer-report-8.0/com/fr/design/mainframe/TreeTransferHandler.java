// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe;

import com.fr.base.FRContext;
import com.fr.design.designer.beans.LayoutAdapter;
import com.fr.design.designer.creator.*;
import com.fr.design.utils.gui.LayoutUtils;
import com.fr.general.FRLogger;
import java.awt.*;
import java.awt.datatransfer.Transferable;
import javax.swing.*;
import javax.swing.tree.TreePath;

// Referenced classes of package com.fr.design.mainframe:
//            ComponentTree

public class TreeTransferHandler extends TransferHandler
{

    private static int PAD = 4;

    public TreeTransferHandler()
    {
        super("selectionPath");
    }

    public boolean canImport(javax.swing.TransferHandler.TransferSupport transfersupport)
    {
        java.awt.datatransfer.DataFlavor adataflavor[];
        ComponentTree componenttree;
        if(!transfersupport.isDrop())
            return false;
        adataflavor = transfersupport.getDataFlavors();
        if(adataflavor == null || adataflavor.length <= 0)
            break MISSING_BLOCK_LABEL_170;
        componenttree = (ComponentTree)transfersupport.getComponent();
        Object obj;
        obj = transfersupport.getTransferable().getTransferData(adataflavor[0]);
        if(!(obj instanceof TreePath))
            return false;
        XCreator xcreator;
        Point point;
        TreePath treepath1;
        TreePath treepath = (TreePath)obj;
        xcreator = (XCreator)treepath.getLastPathComponent();
        javax.swing.TransferHandler.DropLocation droplocation = transfersupport.getDropLocation();
        point = droplocation.getDropPoint();
        treepath1 = componenttree.getPathForLocation(point.x, point.y);
        if(treepath1 != null)
            break MISSING_BLOCK_LABEL_141;
        TreePath treepath2 = componenttree.getClosestPathForLocation(point.x, point.y);
        if(treepath2 != null)
            return canPathAccept(componenttree, treepath2, xcreator, point);
        try
        {
            return false;
        }
        catch(Exception exception)
        {
            FRContext.getLogger().error(exception.getMessage(), exception);
        }
        break MISSING_BLOCK_LABEL_168;
        return canPathAccept(componenttree, treepath1, xcreator, point);
        return false;
        return false;
    }

    private boolean canPathAccept(ComponentTree componenttree, TreePath treepath, XCreator xcreator, Point point)
    {
        XCreator xcreator1 = (XCreator)treepath.getLastPathComponent();
        if(SwingUtilities.isDescendingFrom(xcreator1, xcreator))
            return false;
        if(SwingUtilities.isDescendingFrom(xcreator, xcreator1))
            if(xcreator1 instanceof XLayoutContainer)
                return ((XLayoutContainer)xcreator1).getLayoutAdapter().canAcceptMoreComponent();
            else
                return false;
        if(xcreator1 instanceof XLayoutContainer)
            return ((XLayoutContainer)xcreator1).getLayoutAdapter().canAcceptMoreComponent();
        Rectangle rectangle = componenttree.getRowBounds(componenttree.getRowForLocation(point.x, point.y));
        if(rectangle == null)
            return false;
        rectangle.y += PAD;
        rectangle.height -= 2 * PAD;
        if(rectangle.contains(point))
        {
            return false;
        } else
        {
            XLayoutContainer xlayoutcontainer = XCreatorUtils.getParentXLayoutContainer(xcreator1);
            return xlayoutcontainer.getLayoutAdapter().canAcceptMoreComponent();
        }
    }

    public int getSourceActions(JComponent jcomponent)
    {
        return 1;
    }

    public boolean importData(javax.swing.TransferHandler.TransferSupport transfersupport)
    {
        if(canImport(transfersupport))
        {
            java.awt.datatransfer.DataFlavor adataflavor[] = transfersupport.getDataFlavors();
            ComponentTree componenttree = (ComponentTree)transfersupport.getComponent();
            try
            {
                TreePath treepath = (TreePath)transfersupport.getTransferable().getTransferData(adataflavor[0]);
                XCreator xcreator = (XCreator)treepath.getLastPathComponent();
                javax.swing.TransferHandler.DropLocation droplocation = transfersupport.getDropLocation();
                Point point = droplocation.getDropPoint();
                TreePath treepath1 = componenttree.getPathForLocation(point.x, point.y);
                if(treepath1 == null)
                    treepath1 = componenttree.getClosestPathForLocation(point.x, point.y);
                accept(componenttree, treepath1, xcreator, point);
                componenttree.refreshUI();
                componenttree.fireTreeChanged();
                return true;
            }
            catch(Exception exception)
            {
                FRContext.getLogger().error(exception.getMessage(), exception);
            }
            return false;
        } else
        {
            return false;
        }
    }

    private void accept(ComponentTree componenttree, TreePath treepath, XCreator xcreator, Point point)
    {
        XCreator xcreator1 = (XCreator)treepath.getLastPathComponent();
        if(SwingUtilities.isDescendingFrom(xcreator, xcreator1))
        {
            if(XCreatorUtils.getParentXLayoutContainer(xcreator) != xcreator1)
            {
                removeComponent(xcreator);
                ((XLayoutContainer)xcreator1).getLayoutAdapter().addNextComponent(xcreator);
            }
        } else
        if(xcreator1 instanceof XLayoutContainer)
        {
            removeComponent(xcreator);
            ((XLayoutContainer)xcreator1).getLayoutAdapter().addNextComponent(xcreator);
        } else
        {
            Rectangle rectangle = componenttree.getRowBounds(componenttree.getRowForLocation(point.x, point.y));
            XLayoutContainer xlayoutcontainer = XCreatorUtils.getParentXLayoutContainer(xcreator);
            LayoutAdapter layoutadapter = xlayoutcontainer.getLayoutAdapter();
            if(point.y < rectangle.y + PAD)
            {
                removeComponent(xcreator);
                layoutadapter.addBefore(xcreator1, xcreator);
            } else
            if(point.y > (rectangle.y + rectangle.height) - PAD)
            {
                removeComponent(xcreator);
                layoutadapter.addAfter(xcreator1, xcreator);
            }
        }
    }

    private void removeComponent(Component component)
    {
        Container container = component.getParent();
        container.remove(component);
        container.invalidate();
        LayoutUtils.layoutRootContainer(container);
    }

}
