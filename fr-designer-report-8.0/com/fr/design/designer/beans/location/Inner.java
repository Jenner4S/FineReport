// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.designer.beans.location;

import com.fr.design.beans.location.Absorptionline;
import com.fr.design.beans.location.MoveUtils;
import com.fr.design.designer.beans.models.SelectionModel;
import com.fr.design.designer.beans.models.StateModel;
import com.fr.design.designer.creator.*;
import com.fr.design.mainframe.FormDesigner;
import com.fr.design.mainframe.FormSelection;
import com.fr.form.main.Form;
import com.fr.form.ui.container.WAbsoluteLayout;
import com.fr.stable.ArrayUtils;
import java.awt.Point;
import java.awt.Rectangle;

// Referenced classes of package com.fr.design.designer.beans.location:
//            AccessDirection

public class Inner extends AccessDirection
{

    public Inner()
    {
    }

    public int getCursor()
    {
        return 13;
    }

    public int getActual()
    {
        return 0;
    }

    protected Point getRelativePoint(int i, int j, Rectangle rectangle, FormDesigner formdesigner)
    {
        if(i < 0)
            i = 0;
        else
        if((double)i + rectangle.getWidth() > (double)formdesigner.getRootComponent().getWidth() && formdesigner.getSelectionModel().hasSelectionComponent())
            i = formdesigner.getRootComponent().getWidth() - rectangle.width;
        if(j < 0)
            j = 0;
        else
        if((double)j + rectangle.getHeight() > (double)formdesigner.getRootComponent().getHeight() && formdesigner.getSelectionModel().hasSelectionComponent())
            j = formdesigner.getRootComponent().getHeight() - rectangle.height;
        return new Point(i, j);
    }

    protected void sorptionPoint(Point point, Rectangle rectangle, final FormDesigner designer)
    {
        com.fr.design.beans.location.MoveUtils.RectangleDesigner rectangledesigner = new com.fr.design.beans.location.MoveUtils.RectangleDesigner() {

            final FormDesigner val$designer;
            final Inner this$0;

            public void setXAbsorptionline(Absorptionline absorptionline)
            {
                designer.getStateModel().setXAbsorptionline(absorptionline);
            }

            public void setYAbsorptionline(Absorptionline absorptionline)
            {
                designer.getStateModel().setYAbsorptionline(absorptionline);
            }

            public int[] getHorizontalLine()
            {
                return ArrayUtils.EMPTY_INT_ARRAY;
            }

            public int[] getVerticalLine()
            {
                return ArrayUtils.EMPTY_INT_ARRAY;
            }

            public com.fr.design.beans.location.MoveUtils.RectangleIterator createRectangleIterator()
            {
                return getRectangleIterator(designer);
            }

            
            {
                this$0 = Inner.this;
                designer = formdesigner;
                super();
            }
        }
;
        point.setLocation(MoveUtils.sorption(point.x, point.y, rectangle.width, rectangle.height, rectangledesigner));
    }

    private com.fr.design.beans.location.MoveUtils.RectangleIterator getRectangleIterator(final FormDesigner designer)
    {
        return new com.fr.design.beans.location.MoveUtils.RectangleIterator() {

            private int i;
            private WAbsoluteLayout layout;
            private int count;
            private FormSelection selection;
            final FormDesigner val$designer;
            final Inner this$0;

            public boolean hasNext()
            {
                if(i >= count)
                    return false;
                for(com.fr.form.ui.container.WAbsoluteLayout.BoundsWidget boundswidget = (com.fr.form.ui.container.WAbsoluteLayout.BoundsWidget)layout.getWidget(i); !boundswidget.isVisible() || selection.contains(boundswidget.getWidget()); boundswidget = (com.fr.form.ui.container.WAbsoluteLayout.BoundsWidget)layout.getWidget(i))
                    if(++i >= count)
                        return false;

                return true;
            }

            public int[] getHorizontalLine()
            {
                return ArrayUtils.EMPTY_INT_ARRAY;
            }

            public int[] getVerticalLine()
            {
                return ArrayUtils.EMPTY_INT_ARRAY;
            }

            public Rectangle nextRectangle()
            {
                com.fr.form.ui.container.WAbsoluteLayout.BoundsWidget boundswidget = (com.fr.form.ui.container.WAbsoluteLayout.BoundsWidget)layout.getWidget(i++);
                return boundswidget.getBounds();
            }

            
            {
                this$0 = Inner.this;
                designer = formdesigner;
                super();
                layout = getLayout(designer);
                count = layout.getWidgetCount();
                selection = designer.getSelectionModel().getSelection();
            }
        }
;
    }

    private WAbsoluteLayout getLayout(FormDesigner formdesigner)
    {
        XLayoutContainer xlayoutcontainer = (XLayoutContainer)XCreatorUtils.createXCreator(((Form)formdesigner.getTarget()).getContainer());
        WAbsoluteLayout wabsolutelayout;
        if(xlayoutcontainer.acceptType(new Class[] {
    com/fr/design/designer/creator/XWBorderLayout
}))
            wabsolutelayout = (WAbsoluteLayout)formdesigner.getParaComponent().toData();
        else
            wabsolutelayout = (WAbsoluteLayout)((Form)formdesigner.getTarget()).getContainer();
        return wabsolutelayout;
    }

    public Rectangle getDraggedBounds(int i, int j, Rectangle rectangle, FormDesigner formdesigner, Rectangle rectangle1)
    {
        int ai[] = sorption(rectangle1.x + i, rectangle1.y + j, rectangle, formdesigner);
        rectangle.x = ai[0];
        rectangle.y = ai[1];
        return rectangle;
    }


}
