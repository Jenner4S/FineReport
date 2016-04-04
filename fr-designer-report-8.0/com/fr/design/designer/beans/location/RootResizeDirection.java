// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.designer.beans.location;

import com.fr.design.designer.creator.XLayoutContainer;
import com.fr.design.mainframe.FormDesigner;
import com.fr.design.utils.gui.LayoutUtils;
import java.awt.Cursor;
import java.awt.Rectangle;

// Referenced classes of package com.fr.design.designer.beans.location:
//            Direction

public abstract class RootResizeDirection
    implements Direction
{

    public static RootResizeDirection BOTTOM_RESIZE = new RootResizeDirection(2) {

        public Cursor getCursor()
        {
            return Cursor.getPredefinedCursor(9);
        }

        public void resizeRootBounds(Rectangle rectangle, int i, int j)
        {
            rectangle.height += j;
        }

    }
;
    public static RootResizeDirection RIGHT_RESIZE = new RootResizeDirection(4) {

        public Cursor getCursor()
        {
            return Cursor.getPredefinedCursor(11);
        }

        public void resizeRootBounds(Rectangle rectangle, int i, int j)
        {
            rectangle.width += i;
        }

    }
;
    public static RootResizeDirection RIGHT_BOTTOM_RESIZE = new RootResizeDirection(8) {

        public Cursor getCursor()
        {
            return Cursor.getPredefinedCursor(5);
        }

        public void resizeRootBounds(Rectangle rectangle, int i, int j)
        {
            rectangle.height += j;
            rectangle.width += i;
        }

    }
;
    private int actual;
    private Rectangle oldBounds;

    private RootResizeDirection(int i)
    {
        actual = i;
    }

    public void drag(int i, int j, FormDesigner formdesigner)
    {
        Rectangle rectangle = new Rectangle(oldBounds);
        if(actual == 2)
            rectangle.height += j;
        else
        if(actual == 4)
            rectangle.width += i;
        else
        if(actual == 8)
        {
            rectangle.height += j;
            rectangle.width += i;
        }
        formdesigner.getRootComponent().setBounds(rectangle);
        formdesigner.populateRootSize();
        LayoutUtils.layoutRootContainer(formdesigner.getRootComponent());
    }

    protected abstract void resizeRootBounds(Rectangle rectangle, int i, int j);

    protected abstract Cursor getCursor();

    public int getActual()
    {
        return actual;
    }

    public void updateCursor(FormDesigner formdesigner)
    {
        formdesigner.setCursor(getCursor());
    }

    public void backupBounds(FormDesigner formdesigner)
    {
        oldBounds = formdesigner.getRootComponent().getBounds();
    }


}
