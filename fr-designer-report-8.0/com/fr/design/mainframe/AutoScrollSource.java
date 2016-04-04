// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe;

import java.awt.Rectangle;

// Referenced classes of package com.fr.design.mainframe:
//            FormDesigner, FormArea

public class AutoScrollSource
{
    public static interface AutoScrollChangeListener
    {

        public abstract void propertyChange(int i, int j);
    }


    private int x;
    private int y;
    private AutoScrollChangeListener l;

    public static int sab(int i, int j)
    {
        if(i > j)
            return i - j;
        if(i < 0)
            return i;
        else
            return 0;
    }

    public static AutoScrollSource creatAutoScrollSource(int i, int j, AutoScrollChangeListener autoscrollchangelistener, FormDesigner formdesigner)
    {
        AutoScrollSource autoscrollsource = new AutoScrollSource();
        autoscrollsource.y = sab(j, formdesigner.getHeight());
        autoscrollsource.x = sab(i, formdesigner.getWidth());
        autoscrollsource.l = autoscrollchangelistener;
        return autoscrollsource;
    }

    public static AutoScrollSource creatAutoScrollSource(Rectangle rectangle, int i, int j, AutoScrollChangeListener autoscrollchangelistener, FormDesigner formdesigner)
    {
        AutoScrollSource autoscrollsource = new AutoScrollSource();
        if(rectangle.x + i < formdesigner.getArea().getHorizontalValue())
            autoscrollsource.x = (rectangle.x + i) - formdesigner.getArea().getHorizontalValue();
        else
        if(rectangle.x + i + rectangle.width > formdesigner.getArea().getHorizontalValue() + formdesigner.getWidth())
            autoscrollsource.x = (rectangle.x + i + rectangle.width) - formdesigner.getArea().getHorizontalValue() - formdesigner.getWidth();
        if(rectangle.y + j < formdesigner.getArea().getVerticalValue())
            autoscrollsource.y = (rectangle.y + j) - formdesigner.getArea().getVerticalValue();
        else
        if(rectangle.y + j + rectangle.height > formdesigner.getArea().getVerticalValue() + formdesigner.getHeight())
            autoscrollsource.y = (rectangle.y + j + rectangle.height) - formdesigner.getArea().getVerticalValue() - formdesigner.getHeight();
        autoscrollsource.l = autoscrollchangelistener;
        return autoscrollsource;
    }

    private AutoScrollSource()
    {
        x = 0;
        y = 0;
    }

    public int getHorizontalExtent()
    {
        return x;
    }

    public int getVerticalExtent()
    {
        return y;
    }

    public void propertyChange()
    {
        if(l != null)
            l.propertyChange(x, y);
    }
}
