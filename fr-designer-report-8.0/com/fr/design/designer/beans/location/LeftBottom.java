// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.designer.beans.location;

import com.fr.design.mainframe.FormDesigner;
import java.awt.Rectangle;

// Referenced classes of package com.fr.design.designer.beans.location:
//            AccessDirection

public class LeftBottom extends AccessDirection
{

    public LeftBottom()
    {
    }

    public Rectangle getDraggedBounds(int i, int j, Rectangle rectangle, FormDesigner formdesigner, Rectangle rectangle1)
    {
        int ai[] = sorption(rectangle1.x + i, rectangle1.y + j + rectangle1.height, rectangle, formdesigner);
        rectangle.x = ai[0];
        rectangle.width = (rectangle1.width - rectangle.x) + rectangle1.x;
        rectangle.height = ai[1] - rectangle1.y;
        return rectangle;
    }

    public int getCursor()
    {
        return 4;
    }

    public int getActual()
    {
        return 6;
    }
}
