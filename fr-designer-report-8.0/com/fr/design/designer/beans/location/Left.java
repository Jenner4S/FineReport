// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.designer.beans.location;

import com.fr.design.mainframe.FormDesigner;
import java.awt.Rectangle;

// Referenced classes of package com.fr.design.designer.beans.location:
//            AccessDirection

public class Left extends AccessDirection
{

    public Left()
    {
    }

    public Rectangle getDraggedBounds(int i, int j, Rectangle rectangle, FormDesigner formdesigner, Rectangle rectangle1)
    {
        rectangle.x = sorption(rectangle1.x + i, 0, rectangle, formdesigner)[0];
        rectangle.width = (rectangle1.width - rectangle.x) + rectangle1.x;
        return rectangle;
    }

    public int getCursor()
    {
        return 10;
    }

    public int getActual()
    {
        return 3;
    }
}
