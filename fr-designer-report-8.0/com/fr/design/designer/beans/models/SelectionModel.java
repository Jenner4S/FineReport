// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.designer.beans.models;

import com.fr.design.designer.beans.AdapterBus;
import com.fr.design.designer.beans.LayoutAdapter;
import com.fr.design.designer.beans.events.CreatorEventListenerTable;
import com.fr.design.designer.beans.location.Direction;
import com.fr.design.designer.beans.location.Location;
import com.fr.design.designer.creator.*;
import com.fr.design.mainframe.*;
import com.fr.design.utils.gui.LayoutUtils;
import com.fr.stable.ArrayUtils;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class SelectionModel
{

    private static final int DELTA_X_Y = 20;
    private static FormSelection CLIP_BOARD = new FormSelection();
    private FormDesigner designer;
    private FormSelection selection;
    private Rectangle hotspot_bounds;

    public SelectionModel(FormDesigner formdesigner)
    {
        designer = formdesigner;
        selection = new FormSelection();
    }

    public void reset()
    {
        selection.reset();
        hotspot_bounds = null;
    }

    public static boolean isEmpty()
    {
        return CLIP_BOARD.isEmpty();
    }

    public void selectACreatorAtMouseEvent(MouseEvent mouseevent)
    {
        if(!mouseevent.isControlDown() && !mouseevent.isShiftDown())
            selection.reset();
        XCreator xcreator = designer.getComponentAt(mouseevent);
        if(xcreator != designer.getRootComponent() && xcreator != designer.getParaComponent())
        {
            XCreator xcreator1 = (XCreator)xcreator.getParent();
            xcreator = xcreator1.isDedicateContainer() ? xcreator1 : xcreator;
        }
        if(selection.removeSelectedCreator(xcreator) || selection.addSelectedCreator(xcreator))
        {
            designer.getEditListenerTable().fireCreatorModified(xcreator, 7);
            designer.repaint();
        }
    }

    public void cutSelectedCreator2ClipBoard()
    {
        if(hasSelectionComponent())
        {
            selection.cut2ClipBoard(CLIP_BOARD);
            designer.getEditListenerTable().fireCreatorModified(3);
            designer.repaint();
        }
    }

    public void copySelectedCreator2ClipBoard()
    {
        if(!selection.isEmpty())
            selection.copy2ClipBoard(CLIP_BOARD);
    }

    public boolean pasteFromClipBoard()
    {
        if(!CLIP_BOARD.isEmpty())
        {
            Object obj = null;
            if(!hasSelectionComponent())
            {
                FormSelectionUtils.paste2Container(designer, designer.getRootComponent(), CLIP_BOARD, 20, 20);
            } else
            {
                XLayoutContainer xlayoutcontainer = XCreatorUtils.getParentXLayoutContainer(selection.getSelectedCreator());
                if(xlayoutcontainer != null)
                {
                    Rectangle rectangle = selection.getSelctionBounds();
                    FormSelectionUtils.paste2Container(designer, xlayoutcontainer, CLIP_BOARD, rectangle.x + 20, rectangle.y + 20);
                }
            }
        } else
        {
            Toolkit.getDefaultToolkit().beep();
        }
        return false;
    }

    public FormSelection getSelection()
    {
        return selection;
    }

    public void deleteSelection()
    {
        XCreator axcreator[] = selection.getSelectedCreators();
        if(axcreator.length > 0)
        {
            XCreator axcreator1[] = axcreator;
            int i = axcreator1.length;
            for(int j = 0; j < i; j++)
            {
                XCreator xcreator = axcreator1[j];
                if(xcreator.acceptType(new Class[] {
    com/fr/design/designer/creator/XWParameterLayout
}))
                    designer.removeParaComponent();
                removeCreatorFromContainer(xcreator, xcreator.getWidth(), xcreator.getHeight());
                xcreator.removeAll();
                selection.reset();
            }

            setSelectedCreator(designer.getRootComponent());
            designer.getEditListenerTable().fireCreatorModified(2);
            designer.repaint();
        }
    }

    public void removeCreator(XCreator xcreator, int i, int j)
    {
        selection.removeCreator(xcreator);
        removeCreatorFromContainer(xcreator, i, j);
        designer.repaint();
    }

    public void setHotspotBounds(Rectangle rectangle)
    {
        hotspot_bounds = rectangle;
    }

    public Rectangle getHotspotBounds()
    {
        return hotspot_bounds;
    }

    private void removeCreatorFromContainer(XCreator xcreator, int i, int j)
    {
        XLayoutContainer xlayoutcontainer = XCreatorUtils.getParentXLayoutContainer(xcreator);
        if(xlayoutcontainer == null)
            return;
        boolean flag = xcreator.shouldScaleCreator() || xcreator.hasTitleStyle();
        if(xlayoutcontainer.acceptType(new Class[] {
    com/fr/design/designer/creator/XWFitLayout
}) && flag)
            xcreator = (XCreator)xcreator.getParent();
        xlayoutcontainer.getLayoutAdapter().removeBean(xcreator, i, j);
        xlayoutcontainer.remove(xcreator);
        LayoutManager layoutmanager = xlayoutcontainer.getLayout();
        if(layoutmanager != null)
            LayoutUtils.layoutContainer(xlayoutcontainer);
    }

    public boolean hasSelectionComponent()
    {
        return !selection.isEmpty() && selection.getSelectedCreator().getParent() != null;
    }

    public void move(int i, int j)
    {
        XCreator axcreator[] = selection.getSelectedCreators();
        int k = axcreator.length;
        for(int l = 0; l < k; l++)
        {
            XCreator xcreator = axcreator[l];
            xcreator.setLocation(xcreator.getX() + i, xcreator.getY() + j);
            LayoutAdapter layoutadapter = AdapterBus.searchLayoutAdapter(designer, xcreator);
            if(layoutadapter != null)
                layoutadapter.fix(xcreator);
        }

        designer.getEditListenerTable().fireCreatorModified(selection.getSelectedCreator(), 7);
    }

    public void releaseDragging()
    {
        designer.setPainter(null);
        selection.fixCreator(designer);
        designer.getEditListenerTable().fireCreatorModified(selection.getSelectedCreator(), 6);
    }

    public Direction getDirectionAt(MouseEvent mouseevent)
    {
        if(mouseevent.isControlDown() || mouseevent.isShiftDown())
        {
            XCreator xcreator = designer.getComponentAt(mouseevent.getX(), mouseevent.getY(), selection.getSelectedCreators());
            if(xcreator != designer.getRootComponent() && selection.addedable(xcreator))
                return Location.add;
        }
        Object obj;
        if(hasSelectionComponent())
        {
            int i = mouseevent.getX() + designer.getArea().getHorizontalValue();
            int j = mouseevent.getY() + designer.getArea().getVerticalValue();
            obj = getDirection(selection.getRelativeBounds(), i, j);
            if(selection.size() == 1 && !ArrayUtils.contains(selection.getSelectedCreator().getDirections(), ((Direction) (obj)).getActual()))
                obj = Location.outer;
        } else
        {
            obj = Location.outer;
        }
        if(designer.getDesignerMode().isFormParameterEditor() && obj == Location.outer)
            obj = designer.getLoc2Root(mouseevent);
        return ((Direction) (obj));
    }

    private Direction getDirection(Rectangle rectangle, int i, int j)
    {
        if(i < rectangle.x - 5)
            return Location.outer;
        if(i >= rectangle.x - 5 && i <= rectangle.x)
        {
            if(j < rectangle.y - 5)
                return Location.outer;
            if(j >= rectangle.y - 5 && j <= rectangle.y)
                return Location.left_top;
            if(j > rectangle.y && j < rectangle.y + rectangle.height)
                return Location.left;
            if(j >= rectangle.y + rectangle.height && j <= rectangle.y + rectangle.height + 5)
                return Location.left_bottom;
            else
                return Location.outer;
        }
        if(i > rectangle.x && i < rectangle.x + rectangle.width)
        {
            if(j < rectangle.y - 5)
                return Location.outer;
            if(j >= rectangle.y - 5 && j <= rectangle.y)
                return Location.top;
            if(j > rectangle.y && j < rectangle.y + rectangle.height)
                return Location.inner;
            if(j >= rectangle.y + rectangle.height && j <= rectangle.y + rectangle.height + 5)
                return Location.bottom;
            else
                return Location.outer;
        }
        if(i >= rectangle.x + rectangle.width && i <= rectangle.x + rectangle.width + 5)
        {
            if(j < rectangle.y - 5)
                return Location.outer;
            if(j >= rectangle.y - 5 && j <= rectangle.y)
                return Location.right_top;
            if(j > rectangle.y && j < rectangle.y + rectangle.height)
                return Location.right;
            if(j >= rectangle.y + rectangle.height && j <= rectangle.y + rectangle.height + 5)
                return Location.right_bottom;
            else
                return Location.outer;
        } else
        {
            return Location.outer;
        }
    }

    private void fireCreatorSelected()
    {
        designer.getEditListenerTable().fireCreatorModified(selection.getSelectedCreator(), 7);
    }

    public void setSelectedCreator(XCreator xcreator)
    {
        selection.setSelectedCreator(xcreator);
        fireCreatorSelected();
    }

    public void setSelectedCreators(ArrayList arraylist)
    {
        selection.setSelectedCreators(arraylist);
        fireCreatorSelected();
    }

}
