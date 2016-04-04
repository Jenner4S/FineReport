// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.designer.beans.location;

import com.fr.design.mainframe.FormDesigner;
import java.awt.Rectangle;

// Referenced classes of package com.fr.design.designer.beans.location:
//            AccessDirection

public class Bottom extends AccessDirection
{

    public Bottom()
    {
    }

    public Rectangle getDraggedBounds(int i, int j, Rectangle rectangle, FormDesigner formdesigner, Rectangle rectangle1)
    {
        rectangle.height = sorption(0, rectangle1.height + j + rectangle1.y, rectangle, formdesigner)[1] - rectangle1.y;
        return rectangle;
    }

    public int getCursor()
    {
        return 9;
    }

    public int getActual()
    {
        return 2;
    }
}
