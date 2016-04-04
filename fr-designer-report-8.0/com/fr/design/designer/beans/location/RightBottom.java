// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.designer.beans.location;

import com.fr.design.mainframe.FormDesigner;
import java.awt.Rectangle;

// Referenced classes of package com.fr.design.designer.beans.location:
//            AccessDirection

public class RightBottom extends AccessDirection
{

    public RightBottom()
    {
    }

    public Rectangle getDraggedBounds(int i, int j, Rectangle rectangle, FormDesigner formdesigner, Rectangle rectangle1)
    {
        int ai[] = sorption(rectangle1.x + i + rectangle1.width, rectangle1.height + j + rectangle1.y, rectangle, formdesigner);
        rectangle.width = ai[0] - rectangle1.x;
        rectangle.height = ai[1] - rectangle1.y;
        return rectangle;
    }

    public int getCursor()
    {
        return 5;
    }

    public int getActual()
    {
        return 8;
    }
}
