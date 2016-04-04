// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe;

import com.fr.base.GraphHelper;
import com.fr.design.designer.beans.ConnectorCreator;
import com.fr.design.designer.beans.models.StateModel;
import com.fr.design.designer.creator.XWAbsoluteLayout;
import com.fr.form.main.Form;
import com.fr.form.ui.Connector;
import com.fr.form.ui.container.WAbsoluteLayout;
import com.fr.form.ui.container.WLayout;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.io.PrintStream;
import java.util.ArrayList;

// Referenced classes of package com.fr.design.mainframe:
//            FormDesigner, FormArea

public class ConnectorHelper
{

    public static final int NEAR = 5;
    private static double ratio = 0.5D;
    private ArrayList drawingPoint;
    private FormDesigner designer;
    private boolean drawing;

    public ConnectorHelper(FormDesigner formdesigner)
    {
        designer = formdesigner;
    }

    public void resetConnector(Connector connector)
    {
        ConnectorCreator connectorcreator = new ConnectorCreator(((Form)designer.getTarget()).getContainer(), connector.getStartPoint(), connector.getEndPoint());
        connector.addAll(connectorcreator.createPointList());
    }

    public boolean drawLining()
    {
        return drawing;
    }

    public void setDrawLine(boolean flag)
    {
        drawing = flag;
    }

    private boolean near(Point point, Point point1)
    {
        return point.x - point1.x < 5 && point1.x - point.x < 5 && point.y - point1.y < 5 && point1.y - point.y < 5;
    }

    private Point getNearPoint(MouseEvent mouseevent, Rectangle rectangle)
    {
        Point point = new Point((int)((double)rectangle.x + rectangle.getWidth() * ratio), rectangle.y);
        Point point1 = new Point((int)((double)rectangle.x + rectangle.getWidth()), (int)((double)rectangle.y + rectangle.getHeight() * ratio));
        Point point2 = new Point((int)((double)rectangle.x + rectangle.getWidth() * (1.0D - ratio)), (int)((double)rectangle.y + rectangle.getHeight()));
        Point point3 = new Point(rectangle.x, (int)((double)rectangle.y + rectangle.getHeight() * (1.0D - ratio)));
        Point point4 = new Point(mouseevent.getX() + designer.getArea().getHorizontalValue(), mouseevent.getY() + designer.getArea().getVerticalValue());
        if(near(point4, point))
            return point;
        if(near(point4, point1))
            return point1;
        if(near(point4, point2))
            return point2;
        if(near(point4, point3))
            return point3;
        else
            return null;
    }

    private ArrayList createDefalutNode(Point point, Point point1)
    {
        long l = System.currentTimeMillis();
        ConnectorCreator connectorcreator = new ConnectorCreator(((Form)designer.getTarget()).getContainer(), new Point(point), new Point(point1));
        ArrayList arraylist = connectorcreator.createPointList();
        long l1 = System.currentTimeMillis();
        System.out.println((new StringBuilder()).append("useTime:").append(l1 - l).toString());
        return arraylist;
    }

    public void drawAuxiliaryLine(Graphics g)
    {
        Point point = designer.getStateModel().getStartPoint();
        Point point1 = designer.getStateModel().getEndPoint();
        drawingPoint = createDefalutNode(point, point1);
        Point apoint[] = (Point[])drawingPoint.toArray(new Point[drawingPoint.size()]);
        g.setColor(Color.green);
        for(int i = 0; i < apoint.length - 1; i++)
            GraphHelper.drawLine(g, apoint[i].x - designer.getArea().getHorizontalValue(), apoint[i].y - designer.getArea().getVerticalValue(), apoint[i + 1].x - designer.getArea().getHorizontalValue(), apoint[i + 1].y - designer.getArea().getVerticalValue(), 4);

    }

    public void createDefalutLine()
    {
        if(drawingPoint != null && drawingPoint.size() > 1 && ConnectorCreator.getMinimumDistance((Point)drawingPoint.get(0), (Point)drawingPoint.get(drawingPoint.size() - 1)) > 15)
            ((XWAbsoluteLayout)designer.getRootComponent()).addConnector((new Connector()).addAll(drawingPoint));
        drawingPoint = null;
    }

    public Point getNearWidgetPoint(MouseEvent mouseevent)
    {
        Point point = null;
        int i = 0;
        int j = ((Form)designer.getTarget()).getContainer().getWidgetCount();
        do
        {
            if(i >= j)
                break;
            com.fr.form.ui.container.WAbsoluteLayout.BoundsWidget boundswidget = (com.fr.form.ui.container.WAbsoluteLayout.BoundsWidget)((Form)designer.getTarget()).getContainer().getWidget(i);
            if(boundswidget.isVisible() && (point = getNearPoint(mouseevent, boundswidget.getBounds())) != null)
                break;
            i++;
        } while(true);
        return point;
    }

}
