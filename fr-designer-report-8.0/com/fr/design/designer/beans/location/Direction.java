// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.designer.beans.location;

import com.fr.design.mainframe.FormDesigner;

public interface Direction
{

    public static final int TOP = 1;
    public static final int BOTTOM = 2;
    public static final int LEFT = 3;
    public static final int RIGHT = 4;
    public static final int LEFT_TOP = 5;
    public static final int LEFT_BOTTOM = 6;
    public static final int RIGHT_TOP = 7;
    public static final int RIGHT_BOTTOM = 8;
    public static final int INNER = 0;
    public static final int OUTER = -1;
    public static final int ALL[] = {
        1, 2, 3, 4, 5, 6, 7, 8, 0
    };
    public static final int TOP_BOTTOM_LEFT_RIGHT[] = {
        1, 2, 3, 4
    };

    public abstract void drag(int i, int j, FormDesigner formdesigner);

    public abstract void updateCursor(FormDesigner formdesigner);

    public abstract int getActual();

    public abstract void backupBounds(FormDesigner formdesigner);

}
