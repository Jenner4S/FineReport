// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.designer.beans;

import com.fr.form.ui.container.WAbsoluteLayout;
import com.fr.form.ui.container.WLayout;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.PriorityQueue;

public class ConnectorCreator
{
    class AssessedPoint
        implements Comparable
    {

        ArrayList pointList;
        Point p;
        Point parent;
        int distance;
        int g;
        final ConnectorCreator this$0;

        public void getS()
        {
            int i = pointList.size();
            if(i > 1)
            {
                Point point = (Point)pointList.get(i - 1);
                if(point.x == p.x)
                {
                    if(endPoint.x != point.x)
                        if(beyond)
                            pointList.add(new Point(point.x, endPoint.y));
                        else
                            point.x = p.x = endPoint.x;
                } else
                if(point.y == p.y && endPoint.y != point.y)
                    if(beyond)
                        pointList.add(new Point(endPoint.x, point.y));
                    else
                        point.y = p.y = endPoint.y;
            } else
            if(i == 1 && (startPoint.x != endPoint.x || startPoint.y != endPoint.y))
                pointList.add(new Point(startPoint.x, endPoint.y));
            pointList.add(endPoint);
        }

        public int compareTo(AssessedPoint assessedpoint)
        {
            return distance - assessedpoint.distance;
        }

        void pushInto()
        {
            for(int i = 0; i < ConnectorCreator.vector.length; i++)
            {
                Point point = new Point(p.x + ConnectorCreator.vector[i][0], p.y + ConnectorCreator.vector[i][1]);
                if(parent != null && parent.x == point.x && parent.y == point.y)
                    continue;
                AssessedPoint assessedpoint = new AssessedPoint(point, this, loss(point));
                if(check(point) && !open.contains(assessedpoint) && !close.contains(assessedpoint))
                    open.add(assessedpoint);
            }

        }

        boolean reCheck()
        {
            for(int i = 0; i < ConnectorCreator.vector.length; i++)
            {
                Point point = new Point(p.x + 2 * ConnectorCreator.vector[i][0], p.y + 2 * ConnectorCreator.vector[i][1]);
                AssessedPoint assessedpoint = new AssessedPoint(point, this, loss(point));
                if(check(point))
                    open.add(assessedpoint);
            }

            return open.size() != 0;
        }

        private boolean loss(Point point)
        {
            return parent != null && (p.x == parent.x && point.x != p.x || p.y == parent.y && point.y != p.y);
        }

        public boolean equals(Object obj)
        {
            return (obj instanceof AssessedPoint) && ((AssessedPoint)obj).p.x == p.x && ((AssessedPoint)obj).p.y == p.y;
        }

        public volatile int compareTo(Object obj)
        {
            return compareTo((AssessedPoint)obj);
        }

        AssessedPoint(Point point, AssessedPoint assessedpoint, boolean flag)
        {
            this$0 = ConnectorCreator.this;
            super();
            p = point;
            pointList = new ArrayList();
            if(assessedpoint != null)
            {
                g = assessedpoint.g + (flag ? 20 : 10);
                parent = assessedpoint.p;
                pointList.addAll(assessedpoint.pointList);
                if(flag)
                    pointList.add(assessedpoint.p);
            } else
            {
                pointList.add(point);
                g = 0;
            }
            distance = ConnectorCreator.getMinimumDistance(point, endPoint) + g;
        }
    }


    public static final int UNIT = 10;
    public static final int SIDE = 2;
    public static final int CORNER_LOSS = 20;
    public static final int vector[][] = {
        {
            10, 0
        }, {
            -10, 0
        }, {
            0, 10
        }, {
            0, -10
        }
    };
    private long timeOut;
    private boolean beyond;
    private WLayout container;
    private com.fr.form.ui.container.WAbsoluteLayout.BoundsWidget IgnoreLayout;
    private Point startPoint;
    private Point endPoint;
    private PriorityQueue open;
    private PriorityQueue close;

    public ConnectorCreator(WLayout wlayout, Point point, Point point1)
    {
        timeOut = 200L;
        open = new PriorityQueue();
        close = new PriorityQueue();
        container = wlayout;
        startPoint = point;
        endPoint = point1;
        if(getNearWidget(endPoint, 19) != null && (IgnoreLayout = getNearWidget(endPoint, -1)) == null)
            beyond = true;
    }

    private static int difference(int i, int j)
    {
        if(i < j)
        {
            int k = i;
            i = j;
            j = k;
        }
        return i - j;
    }

    public static int getMinimumDistance(Point point, Point point1)
    {
        return difference(point.x, point1.x) + difference(point.y, point1.y) + (point.x != point1.x && point.y != point1.y ? 20 : 0);
    }

    private com.fr.form.ui.container.WAbsoluteLayout.BoundsWidget getNearWidget(Point point, int i)
    {
        Rectangle arectangle[] = new Rectangle[container.getWidgetCount()];
        Rectangle rectangle = new Rectangle();
        int j = 0;
        for(int k = arectangle.length; j < k; j++)
        {
            com.fr.form.ui.container.WAbsoluteLayout.BoundsWidget boundswidget = (com.fr.form.ui.container.WAbsoluteLayout.BoundsWidget)container.getWidget(j);
            if(!boundswidget.isVisible())
                continue;
            arectangle[j] = boundswidget.getBounds();
            rectangle.setBounds(arectangle[j]);
            rectangle.grow(i, i);
            if(inside(point, rectangle))
                return boundswidget;
        }

        return null;
    }

    private boolean arrive(Point point, Point point1)
    {
        if(!beyond)
            return point.x - point1.x < 10 && point1.x - point.x < 10 && point.y - point1.y < 10 && point1.y - point.y < 10;
        else
            return point.x - point1.x < 30 && point1.x - point.x < 30 && point.y - point1.y < 30 && point1.y - point.y < 30;
    }

    private boolean inside(Point point, Rectangle rectangle)
    {
        return point.x >= rectangle.x && point.x <= rectangle.x + rectangle.width && point.y >= rectangle.y && point.y <= rectangle.y + rectangle.height;
    }

    private boolean check(Point point)
    {
        if(point.x <= 0 || point.y <= 0)
        {
            return false;
        } else
        {
            com.fr.form.ui.container.WAbsoluteLayout.BoundsWidget boundswidget = getNearWidget(point, 19);
            return boundswidget == IgnoreLayout || boundswidget == null;
        }
    }

    public ArrayList createPointList()
    {
        ArrayList arraylist = new ArrayList();
        AssessedPoint assessedpoint = new AssessedPoint(startPoint, null, false);
        long l = System.currentTimeMillis();
        open.add(assessedpoint);
        do
        {
            AssessedPoint assessedpoint1;
            if((assessedpoint1 = (AssessedPoint)open.poll()) == null && (!checkClose() || (assessedpoint1 = (AssessedPoint)open.poll()) == null))
                break;
            if(arrive(assessedpoint1.p, endPoint))
            {
                assessedpoint1.getS();
                arraylist.addAll(assessedpoint1.pointList);
                return arraylist;
            }
            close.add(assessedpoint1);
            assessedpoint1.pushInto();
        } while(System.currentTimeMillis() - l <= timeOut);
        arraylist.add(startPoint);
        arraylist.add(new Point(startPoint.x, endPoint.y));
        arraylist.add(endPoint);
        return arraylist;
    }

    private boolean checkClose()
    {
        if(close.size() == 1)
        {
            AssessedPoint assessedpoint = (AssessedPoint)close.poll();
            return assessedpoint.reCheck();
        } else
        {
            return false;
        }
    }







}
