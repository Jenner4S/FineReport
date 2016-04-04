// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.designer.beans.location;

import com.fr.design.mainframe.FormDesigner;
import java.awt.Rectangle;

// Referenced classes of package com.fr.design.designer.beans.location:
//            AccessDirection

public class RightTop extends AccessDirection
{

    public RightTop()
    {
    }

    public Rectangle getDraggedBounds(int i, int j, Rectangle rectangle, FormDesigner formdesigner, Rectangle rectangle1)
    {
        int ai[] = sorption(rectangle1.x + i + rectangle1.width, j + rectangle1.y, rectangle, formdesigner);
        rectangle.y = ai[1];
        rectangle.height = (rectangle1.height - rectangle.y) + rectangle1.y;
        rectangle.width = ai[0] - rectangle1.x;
        return rectangle;
    }

    public int getCursor()
    {
        return 7;
    }

    public int getActual()
    {
        return 7;
    }
}
