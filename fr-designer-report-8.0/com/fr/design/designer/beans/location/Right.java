// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.designer.beans.location;

import com.fr.design.mainframe.FormDesigner;
import java.awt.Rectangle;

// Referenced classes of package com.fr.design.designer.beans.location:
//            AccessDirection

public class Right extends AccessDirection
{

    public Right()
    {
    }

    public Rectangle getDraggedBounds(int i, int j, Rectangle rectangle, FormDesigner formdesigner, Rectangle rectangle1)
    {
        rectangle.width = sorption(rectangle1.x + i + rectangle1.width, 0, rectangle, formdesigner)[0] - rectangle1.x;
        return rectangle;
    }

    public int getCursor()
    {
        return 11;
    }

    public int getActual()
    {
        return 4;
    }
}
