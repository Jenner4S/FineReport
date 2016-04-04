// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.designer.beans.location;

import com.fr.design.beans.location.Absorptionline;
import com.fr.design.designer.beans.models.SelectionModel;
import com.fr.design.designer.beans.models.StateModel;
import com.fr.design.designer.creator.*;
import com.fr.design.mainframe.FormDesigner;
import com.fr.design.mainframe.FormSelection;
import com.fr.form.main.Form;
import com.fr.form.ui.container.WAbsoluteLayout;
import java.awt.*;

// Referenced classes of package com.fr.design.designer.beans.location:
//            Direction

public abstract class AccessDirection
    implements Direction
{

    private static final int MINHEIGHT = 21;
    private static final int MINWIDTH = 36;
    private int ymin;
    private int xmin;

    public AccessDirection()
    {
    }

    abstract int getCursor();

    protected abstract Rectangle getDraggedBounds(int i, int j, Rectangle rectangle, FormDesigner formdesigner, Rectangle rectangle1);

    protected int[] sorption(int i, int j, Rectangle rectangle, FormDesigner formdesigner)
    {
        if(!formdesigner.hasWAbsoluteLayout())
            return (new int[] {
                i, j
            });
        int k = rectangle.y;
        if(k >= formdesigner.getParaHeight() && !formdesigner.isFormParaDesigner())
        {
            return (new int[] {
                i, j
            });
        } else
        {
            Point point = getRelativePoint(i, j, rectangle, formdesigner);
            sorptionPoint(point, rectangle, formdesigner);
            return (new int[] {
                point.x, point.y
            });
        }
    }

    protected Point getRelativePoint(int i, int j, Rectangle rectangle, FormDesigner formdesigner)
    {
        if(i < 0)
            i = 0;
        else
        if(i > formdesigner.getRootComponent().getWidth() && formdesigner.getSelectionModel().hasSelectionComponent())
            i = formdesigner.getRootComponent().getWidth();
        if(j < 0)
            j = 0;
        else
        if(j > formdesigner.getRootComponent().getHeight() && formdesigner.getSelectionModel().hasSelectionComponent() && !formdesigner.getSelectionModel().getSelection().getSelectedCreator().acceptType(new Class[] {
    com/fr/design/designer/creator/XWParameterLayout
}))
            j = formdesigner.getRootComponent().getHeight();
        return new Point(i, j);
    }

    protected void sorptionPoint(Point point, Rectangle rectangle, FormDesigner formdesigner)
    {
        boolean flag = rectangle.getWidth() <= 5D;
        boolean flag1 = rectangle.getHeight() <= 5D;
        WAbsoluteLayout wabsolutelayout = getLayout(formdesigner);
        FormSelection formselection = formdesigner.getSelectionModel().getSelection();
        int i = 0;
        for(int j = wabsolutelayout.getWidgetCount(); i < j; i++)
        {
            com.fr.form.ui.container.WAbsoluteLayout.BoundsWidget boundswidget = (com.fr.form.ui.container.WAbsoluteLayout.BoundsWidget)wabsolutelayout.getWidget(i);
            if(!boundswidget.isVisible() || formselection.contains(boundswidget.getWidget()))
                continue;
            Rectangle rectangle1 = boundswidget.getBounds();
            if(!flag)
            {
                int k = rectangle1.x;
                if(Math.abs(k - point.x) <= 5)
                {
                    point.x = k;
                    flag = true;
                }
                int i1 = rectangle1.x + rectangle1.width;
                if(Math.abs(i1 - point.x) <= 5)
                {
                    point.x = i1;
                    flag = true;
                }
            }
            if(!flag1)
            {
                int l = rectangle1.y;
                if(Math.abs(l - point.y) <= 5)
                {
                    point.y = l;
                    flag1 = true;
                }
                int j1 = rectangle1.y + rectangle1.height;
                if(Math.abs(j1 - point.y) <= 5)
                {
                    point.y = j1;
                    flag1 = true;
                }
            }
            if(flag && flag1)
                break;
        }

        formdesigner.getStateModel().setXAbsorptionline(!flag || rectangle.getWidth() <= 5D ? null : Absorptionline.createXAbsorptionline(point.x));
        formdesigner.getStateModel().setYAbsorptionline(!flag1 || rectangle.getHeight() <= 5D ? null : Absorptionline.createYAbsorptionline(point.y));
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

    public void drag(int i, int j, FormDesigner formdesigner)
    {
        Rectangle rectangle = getDraggedBounds(i, j, formdesigner.getSelectionModel().getSelection().getRelativeBounds(), formdesigner, formdesigner.getSelectionModel().getSelection().getBackupBounds());
        if(rectangle.height == 21)
            ymin = rectangle.y;
        if(rectangle.height == 20)
            ymin = ymin != rectangle.y ? rectangle.y - 1 : rectangle.y;
        if(rectangle.height < 21)
        {
            rectangle.height = 21;
            rectangle.y = ymin;
        }
        if(rectangle.width == 36)
            xmin = rectangle.x;
        if(rectangle.width == 35)
            xmin = xmin != rectangle.x ? rectangle.x - 1 : rectangle.x;
        if(rectangle.width < 36)
        {
            rectangle.width = 36;
            rectangle.x = xmin;
        }
        if(rectangle != null)
            formdesigner.getSelectionModel().getSelection().setSelectionBounds(rectangle, formdesigner);
    }

    public void updateCursor(FormDesigner formdesigner)
    {
        int i = getCursor();
        if(i != formdesigner.getCursor().getType())
            formdesigner.setCursor(Cursor.getPredefinedCursor(i));
    }

    public void backupBounds(FormDesigner formdesigner)
    {
        formdesigner.getSelectionModel().getSelection().backupBounds();
    }
}
