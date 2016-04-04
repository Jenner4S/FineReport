// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.chart.series.PlotSeries;

import java.awt.Point;
import java.awt.Shape;
import java.util.ArrayList;

public class MapSelectShape
{

    private java.util.List selectPoint;
    private java.util.List selectShape;
    private int selectType;

    public MapSelectShape(Shape ashape[], Point apoint[], int i)
    {
        selectPoint = new ArrayList();
        selectShape = new ArrayList();
        for(int j = 0; j < ashape.length; j++)
            selectShape.add(ashape[j]);

        for(int k = 0; k < apoint.length; k++)
            selectPoint.add(apoint[k]);

        selectType = i;
    }

    public boolean containsPoint(Point point)
    {
        boolean flag = false;
        int i = 0;
        do
        {
            if(selectShape == null || i >= selectShape.size())
                break;
            Shape shape = (Shape)selectShape.get(i);
            flag = shape.contains(point);
            if(flag)
                break;
            i++;
        } while(true);
        return flag;
    }

    public void addSelectValue(Point point, Shape shape)
    {
        if(!selectShape.contains(shape))
            selectShape.add(shape);
        if(!selectPoint.contains(point))
            selectPoint.add(point);
    }

    public Point[] getSelectPoints()
    {
        return (Point[])(Point[])selectPoint.toArray(new Point[selectPoint.size()]);
    }

    public Shape[] getSelectShapes()
    {
        return (Shape[])(Shape[])selectShape.toArray(new Shape[selectShape.size()]);
    }

    public void setSelectType(int i)
    {
        selectType = i;
    }

    public int getSelectType()
    {
        return selectType;
    }
}
