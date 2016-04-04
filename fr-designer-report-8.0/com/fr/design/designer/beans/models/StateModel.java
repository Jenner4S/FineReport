// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.designer.beans.models;

import com.fr.design.beans.location.Absorptionline;
import com.fr.design.designer.beans.*;
import com.fr.design.designer.beans.events.CreatorEventListenerTable;
import com.fr.design.designer.beans.location.Direction;
import com.fr.design.designer.beans.location.Location;
import com.fr.design.designer.creator.*;
import com.fr.design.mainframe.*;
import com.fr.design.utils.ComponentUtils;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

// Referenced classes of package com.fr.design.designer.beans.models:
//            SelectionModel

public class StateModel
{

    private SelectionModel selectionModel;
    private Direction driection;
    private int current_x;
    private int current_y;
    private Point startPoint;
    private Point currentPoint;
    private Absorptionline lineInX;
    private Absorptionline lineInY;
    private boolean selecting;
    private boolean dragging;
    private boolean addable;
    private FormDesigner designer;

    public StateModel(FormDesigner formdesigner)
    {
        startPoint = new Point();
        currentPoint = new Point();
        designer = formdesigner;
        selectionModel = formdesigner.getSelectionModel();
    }

    public Direction getDirection()
    {
        return driection;
    }

    public boolean isSelecting()
    {
        return selecting;
    }

    public boolean dragable()
    {
        return driection != Location.outer && !selecting;
    }

    private void checkAddable(MouseEvent mouseevent)
    {
        addable = false;
        designer.setPainter(null);
        if(driection != Location.inner)
            return;
        XCreator xcreator = designer.getComponentAt(mouseevent.getX(), mouseevent.getY(), selectionModel.getSelection().getSelectedCreators());
        XLayoutContainer xlayoutcontainer = XCreatorUtils.getHotspotContainer(xcreator);
        XCreator xcreator1 = selectionModel.getSelection().getSelectedCreator();
        XLayoutContainer xlayoutcontainer1 = XCreatorUtils.getParentXLayoutContainer(xcreator1);
        if(xlayoutcontainer1 != null && xlayoutcontainer1 != xlayoutcontainer && (selectionModel.getSelection().size() == 1 || (xlayoutcontainer instanceof XWAbsoluteLayout)))
        {
            HoverPainter hoverpainter = AdapterBus.getContainerPainter(designer, xlayoutcontainer);
            designer.setPainter(hoverpainter);
            if(hoverpainter != null)
            {
                Rectangle rectangle = ComponentUtils.getRelativeBounds(xlayoutcontainer);
                rectangle.x -= designer.getArea().getHorizontalValue();
                rectangle.y -= designer.getArea().getVerticalValue();
                hoverpainter.setRenderingBounds(rectangle);
                hoverpainter.setHotspot(new Point(mouseevent.getX(), mouseevent.getY()));
                hoverpainter.setCreator(xcreator1);
            }
            addable = true;
        }
    }

    private boolean addBean(XLayoutContainer xlayoutcontainer, int i, int j)
    {
        LayoutAdapter layoutadapter = xlayoutcontainer.getLayoutAdapter();
        Rectangle rectangle = ComponentUtils.getRelativeBounds(xlayoutcontainer);
        if(selectionModel.getSelection().size() == 1)
            return layoutadapter.addBean(selectionModel.getSelection().getSelectedCreator(), (i + designer.getArea().getHorizontalValue()) - rectangle.x, (j + designer.getArea().getVerticalValue()) - rectangle.y);
        XCreator axcreator[] = selectionModel.getSelection().getSelectedCreators();
        int k = axcreator.length;
        for(int l = 0; l < k; l++)
        {
            XCreator xcreator = axcreator[l];
            layoutadapter.addBean(xcreator, (i + designer.getArea().getHorizontalValue()) - rectangle.x, (j + designer.getArea().getVerticalValue()) - rectangle.y);
        }

        return true;
    }

    private void adding(int i, int j)
    {
        XCreator xcreator = designer.getComponentAt(i, j, selectionModel.getSelection().getSelectedCreators());
        XLayoutContainer xlayoutcontainer = XCreatorUtils.getHotspotContainer(xcreator);
        boolean flag = false;
        if(xlayoutcontainer != null)
            flag = addBean(xlayoutcontainer, i, j);
        if(flag)
        {
            FormSelectionUtils.rebuildSelection(designer);
            designer.getEditListenerTable().fireCreatorModified(selectionModel.getSelection().getSelectedCreator(), 1);
        } else
        {
            Toolkit.getDefaultToolkit().beep();
        }
        designer.setPainter(null);
    }

    public boolean isDragging()
    {
        return dragging;
    }

    public boolean prepareForDrawLining()
    {
        return startPoint != null;
    }

    public void setStartPoint(Point point)
    {
        startPoint = point;
    }

    public Point getStartPoint()
    {
        return startPoint;
    }

    public Point getEndPoint()
    {
        return currentPoint;
    }

    public void startSelecting(MouseEvent mouseevent)
    {
        selecting = true;
        selectionModel.setHotspotBounds(new Rectangle());
        current_x = getMouseXY(mouseevent).x;
        current_y = getMouseXY(mouseevent).y;
    }

    public void startResizing(MouseEvent mouseevent)
    {
        if(!selectionModel.getSelection().isEmpty())
            driection.backupBounds(designer);
        current_x = getMouseXY(mouseevent).x;
        current_y = getMouseXY(mouseevent).y;
    }

    public void startDrawLine(Point point)
    {
        startPoint = point;
        if(point != null)
            try
            {
                designer.setCursor(XConnector.connectorCursor);
            }
            catch(Exception exception) { }
        else
            designer.setCursor(Cursor.getPredefinedCursor(0));
    }

    public void selectCreators(MouseEvent mouseevent)
    {
        int i = getMouseXY(mouseevent).x;
        int j = getMouseXY(mouseevent).y;
        Rectangle rectangle = createCurrentBounds(i, j);
        if(i != current_x || j != current_y)
            selectionModel.setSelectedCreators(getHotspotCreators(rectangle, designer.getRootComponent()));
        selectionModel.setHotspotBounds(null);
    }

    public void drawLine(MouseEvent mouseevent)
    {
        designer.getDrawLineHelper().setDrawLine(true);
        Point point = designer.getDrawLineHelper().getNearWidgetPoint(mouseevent);
        if(point != null)
        {
            currentPoint = point;
        } else
        {
            currentPoint.x = mouseevent.getX() + designer.getArea().getHorizontalValue();
            currentPoint.y = mouseevent.getY() + designer.getArea().getVerticalValue();
        }
    }

    private Rectangle createCurrentBounds(int i, int j)
    {
        Rectangle rectangle = new Rectangle();
        rectangle.x = Math.min(i, current_x);
        rectangle.y = Math.min(j, current_y);
        rectangle.width = Math.max(i, current_x) - rectangle.x;
        rectangle.height = Math.max(j, current_y) - rectangle.y;
        return rectangle;
    }

    private ArrayList getHotspotCreators(Rectangle rectangle, XCreator xcreator)
    {
        ArrayList arraylist = new ArrayList();
        if(!xcreator.isVisible() && !designer.isRoot(xcreator))
            return arraylist;
        if(xcreator instanceof XLayoutContainer)
        {
            XLayoutContainer xlayoutcontainer = (XLayoutContainer)xcreator;
            int i = xlayoutcontainer.getXCreatorCount();
            Rectangle rectangle1 = new Rectangle(rectangle);
            for(int j = i - 1; j >= 0; j--)
            {
                XCreator xcreator1 = xlayoutcontainer.getXCreator(j);
                if(rectangle.contains(xcreator1.getBounds()))
                {
                    arraylist.add(xcreator1);
                } else
                {
                    rectangle1.x = rectangle.x - xcreator1.getX();
                    rectangle1.y = rectangle.y - xcreator1.getY();
                    arraylist.addAll(getHotspotCreators(rectangle1, xcreator1));
                }
            }

        }
        return arraylist;
    }

    public void resetModel()
    {
        dragging = false;
        selecting = false;
    }

    public void reset()
    {
        driection = Location.outer;
        dragging = false;
        selecting = false;
    }

    public void draggingCancel()
    {
        designer.repaint();
        reset();
    }

    public void setDirection(Direction direction)
    {
        if(driection != direction)
        {
            driection = direction;
            driection.updateCursor(designer);
        }
    }

    public void setXAbsorptionline(Absorptionline absorptionline)
    {
        lineInX = absorptionline;
    }

    public void setYAbsorptionline(Absorptionline absorptionline)
    {
        lineInY = absorptionline;
    }

    public void paintAbsorptionline(Graphics g)
    {
        if(lineInX != null)
            lineInX.paint(g, designer.getArea());
        if(lineInY != null)
            lineInY.paint(g, designer.getArea());
    }

    public void dragging(MouseEvent mouseevent)
    {
        checkAddable(mouseevent);
        setDependLinePainter(mouseevent);
        driection.drag(getMouseXY(mouseevent).x - current_x, getMouseXY(mouseevent).y - current_y, designer);
        dragging = true;
    }

    private void setDependLinePainter(MouseEvent mouseevent)
    {
        XCreator xcreator = designer.getComponentAt(mouseevent.getX(), mouseevent.getY(), selectionModel.getSelection().getSelectedCreators());
        XLayoutContainer xlayoutcontainer = XCreatorUtils.getHotspotContainer(xcreator);
        XCreator xcreator1 = selectionModel.getSelection().getSelectedCreator();
        HoverPainter hoverpainter = AdapterBus.getContainerPainter(designer, xlayoutcontainer);
        designer.setPainter(hoverpainter);
        if(hoverpainter != null)
        {
            hoverpainter.setHotspot(new Point(mouseevent.getX(), mouseevent.getY()));
            hoverpainter.setCreator(xcreator1);
        }
    }

    public void releaseDragging(MouseEvent mouseevent)
    {
        dragging = false;
        if(addable)
            adding(mouseevent.getX(), mouseevent.getY());
        else
        if(!selectionModel.getSelection().isEmpty())
            selectionModel.releaseDragging();
        designer.repaint();
    }

    public void changeSelection(MouseEvent mouseevent)
    {
        Rectangle rectangle = createCurrentBounds(getMouseXY(mouseevent).x, getMouseXY(mouseevent).y);
        selectionModel.setHotspotBounds(rectangle);
    }

    public Point getMouseXY(MouseEvent mouseevent)
    {
        Point point = new Point(mouseevent.getX() + designer.getArea().getHorizontalValue(), mouseevent.getY() + designer.getArea().getVerticalValue());
        return point;
    }
}
