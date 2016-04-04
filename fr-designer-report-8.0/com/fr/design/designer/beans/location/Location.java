// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.designer.beans.location;

import com.fr.design.mainframe.FormDesigner;

// Referenced classes of package com.fr.design.designer.beans.location:
//            Outer, Add, Inner, LeftTop, 
//            Top, RightTop, Right, RightBottom, 
//            Bottom, LeftBottom, Left, Direction

public final class Location extends Enum
    implements Direction
{

    public static final Location outer;
    public static final Location add;
    public static final Location inner;
    public static final Location left_top;
    public static final Location top;
    public static final Location right_top;
    public static final Location right;
    public static final Location right_bottom;
    public static final Location bottom;
    public static final Location left_bottom;
    public static final Location left;
    private Direction direction;
    private static final Location $VALUES[];

    public static Location[] values()
    {
        return (Location[])$VALUES.clone();
    }

    public static Location valueOf(String s)
    {
        return (Location)Enum.valueOf(com/fr/design/designer/beans/location/Location, s);
    }

    private Location(String s, int i, Direction direction1)
    {
        super(s, i);
        direction = direction1;
    }

    public void drag(int i, int j, FormDesigner formdesigner)
    {
        direction.drag(i, j, formdesigner);
    }

    public int getActual()
    {
        return direction.getActual();
    }

    public void updateCursor(FormDesigner formdesigner)
    {
        direction.updateCursor(formdesigner);
    }

    public void backupBounds(FormDesigner formdesigner)
    {
        direction.backupBounds(formdesigner);
    }

    static 
    {
        outer = new Location("outer", 0, new Outer());
        add = new Location("add", 1, new Add());
        inner = new Location("inner", 2, new Inner());
        left_top = new Location("left_top", 3, new LeftTop());
        top = new Location("top", 4, new Top());
        right_top = new Location("right_top", 5, new RightTop());
        right = new Location("right", 6, new Right());
        right_bottom = new Location("right_bottom", 7, new RightBottom());
        bottom = new Location("bottom", 8, new Bottom());
        left_bottom = new Location("left_bottom", 9, new LeftBottom());
        left = new Location("left", 10, new Left());
        $VALUES = (new Location[] {
            outer, add, inner, left_top, top, right_top, right, right_bottom, bottom, left_bottom, 
            left
        });
    }
}
