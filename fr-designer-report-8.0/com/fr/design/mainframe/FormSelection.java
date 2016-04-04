// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe;

import com.fr.base.FRContext;
import com.fr.design.designer.beans.AdapterBus;
import com.fr.design.designer.beans.LayoutAdapter;
import com.fr.design.designer.beans.location.Direction;
import com.fr.design.designer.creator.*;
import com.fr.design.utils.ComponentUtils;
import com.fr.design.utils.gui.LayoutUtils;
import com.fr.form.ui.Widget;
import com.fr.general.FRLogger;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Iterator;

// Referenced classes of package com.fr.design.mainframe:
//            FormDesigner, FormArea

public class FormSelection
{

    private ArrayList selection;
    private Rectangle backupBounds;
    private ArrayList recs;

    public FormSelection()
    {
        recs = new ArrayList();
        selection = new ArrayList();
    }

    public void reset()
    {
        selection.clear();
    }

    public boolean isEmpty()
    {
        return selection.isEmpty();
    }

    public int size()
    {
        return selection.size();
    }

    public void removeCreator(XCreator xcreator)
    {
        selection.remove(xcreator);
    }

    public boolean removeSelectedCreator(XCreator xcreator)
    {
        if(selection.size() > 1 && selection.contains(xcreator))
        {
            removeCreator(xcreator);
            return true;
        } else
        {
            return false;
        }
    }

    public boolean addSelectedCreator(XCreator xcreator)
    {
        if(addedable(xcreator))
        {
            selection.add(xcreator);
            return true;
        } else
        {
            return false;
        }
    }

    public boolean addedable(XCreator xcreator)
    {
        if(selection.isEmpty())
            return true;
        XLayoutContainer xlayoutcontainer = XCreatorUtils.getParentXLayoutContainer(xcreator);
        if(!(xlayoutcontainer instanceof XWAbsoluteLayout))
            return false;
        for(Iterator iterator = selection.iterator(); iterator.hasNext();)
        {
            XCreator xcreator1 = (XCreator)iterator.next();
            if(xcreator1 == xcreator || XCreatorUtils.getParentXLayoutContainer(xcreator1) != xlayoutcontainer)
                return false;
        }

        return true;
    }

    public XCreator getSelectedCreator()
    {
        return selection.isEmpty() ? null : (XCreator)selection.get(0);
    }

    public XCreator[] getSelectedCreators()
    {
        return (XCreator[])selection.toArray(new XCreator[selection.size()]);
    }

    public Widget[] getSelectedWidgets()
    {
        Widget awidget[] = new Widget[selection.size()];
        for(int i = 0; i < selection.size(); i++)
            awidget[i] = ((XCreator)selection.get(i)).toData();

        return awidget;
    }

    public void setSelectedCreator(XCreator xcreator)
    {
        reset();
        selection.add(xcreator);
    }

    public void setSelectedCreators(ArrayList arraylist)
    {
        reset();
        Iterator iterator = arraylist.iterator();
        do
        {
            if(!iterator.hasNext())
                break;
            XCreator xcreator = (XCreator)iterator.next();
            if(addedable(xcreator))
                selection.add(xcreator);
        } while(true);
    }

    public boolean contains(Widget widget)
    {
        for(Iterator iterator = selection.iterator(); iterator.hasNext();)
        {
            XCreator xcreator = (XCreator)iterator.next();
            if(xcreator.toData() == widget)
                return true;
        }

        return false;
    }

    public int[] getDirections()
    {
        if(selection.size() > 1)
            return Direction.ALL;
        if(selection.size() == 1)
            return ((XCreator)selection.get(0)).getDirections();
        else
            return new int[0];
    }

    public void backupBounds()
    {
        backupBounds = getRelativeBounds();
        recs.clear();
        XComponent xcomponent;
        for(Iterator iterator = selection.iterator(); iterator.hasNext(); recs.add(xcomponent.getBounds()))
            xcomponent = (XComponent)iterator.next();

    }

    public Rectangle getBackupBounds()
    {
        return backupBounds;
    }

    public Rectangle getRelativeBounds()
    {
        Rectangle rectangle = getSelctionBounds();
        XLayoutContainer xlayoutcontainer = XCreatorUtils.getParentXLayoutContainer((XCreator)selection.get(0));
        if(xlayoutcontainer == null)
        {
            return rectangle;
        } else
        {
            Rectangle rectangle1 = ComponentUtils.getRelativeBounds(xlayoutcontainer);
            rectangle.x += rectangle1.x;
            rectangle.y += rectangle1.y;
            return rectangle;
        }
    }

    public Rectangle getSelctionBounds()
    {
        if(selection.isEmpty())
            return new Rectangle();
        Rectangle rectangle = ((XCreator)selection.get(0)).getBounds();
        int i = 1;
        for(int j = selection.size(); i < j; i++)
            rectangle = rectangle.union(((XCreator)selection.get(i)).getBounds());

        return rectangle;
    }

    public void setSelectionBounds(Rectangle rectangle, FormDesigner formdesigner)
    {
        XLayoutContainer xlayoutcontainer = XCreatorUtils.getParentXLayoutContainer((XCreator)selection.get(0));
        Rectangle rectangle1 = new Rectangle(backupBounds);
        if(xlayoutcontainer != null)
        {
            Rectangle rectangle2 = ComponentUtils.getRelativeBounds(xlayoutcontainer);
            rectangle.x -= rectangle2.x;
            rectangle.y -= rectangle2.y;
            rectangle1.x -= rectangle2.x;
            rectangle1.y -= rectangle2.y;
        }
        int i = selection.size();
        if(i == 1)
        {
            XCreator xcreator = (XCreator)selection.get(0);
            xcreator.setBounds(rectangle);
            if(xcreator.acceptType(new Class[] {
    com/fr/design/designer/creator/XWParameterLayout
}))
            {
                formdesigner.setParaHeight((int)rectangle.getHeight());
                formdesigner.getArea().doLayout();
            }
            LayoutUtils.layoutContainer(xcreator);
        } else
        if(i > 1)
        {
            for(int j = 0; j < selection.size(); j++)
            {
                Rectangle rectangle3 = new Rectangle((Rectangle)recs.get(j));
                rectangle3.x = rectangle.x + ((rectangle3.x - rectangle1.x) * rectangle.width) / rectangle1.width;
                rectangle3.y = rectangle.y + ((rectangle3.y - rectangle1.y) * rectangle.height) / rectangle1.height;
                rectangle3.width = (rectangle.width * rectangle3.width) / rectangle1.width;
                rectangle3.height = (rectangle.height * rectangle3.height) / rectangle1.height;
                XCreator xcreator1 = (XCreator)selection.get(j);
                xcreator1.setBounds(rectangle3);
                if(xcreator1.acceptType(new Class[] {
    com/fr/design/designer/creator/XWParameterLayout
}))
                {
                    formdesigner.setParaHeight((int)rectangle.getHeight());
                    formdesigner.getArea().doLayout();
                }
            }

            LayoutUtils.layoutRootContainer(formdesigner.getRootComponent());
        }
    }

    public void fixCreator(FormDesigner formdesigner)
    {
        Iterator iterator = selection.iterator();
        do
        {
            if(!iterator.hasNext())
                break;
            XCreator xcreator = (XCreator)iterator.next();
            LayoutAdapter layoutadapter = AdapterBus.searchLayoutAdapter(formdesigner, xcreator);
            if(layoutadapter != null)
            {
                xcreator.setBackupBound(backupBounds);
                layoutadapter.fix(xcreator);
            }
        } while(true);
    }

    private void removeCreatorFromContainer(XCreator xcreator)
    {
        XLayoutContainer xlayoutcontainer = XCreatorUtils.getParentXLayoutContainer(xcreator);
        if(xlayoutcontainer == null)
            return;
        xlayoutcontainer.remove(xcreator);
        LayoutManager layoutmanager = xlayoutcontainer.getLayout();
        if(layoutmanager != null)
            LayoutUtils.layoutContainer(xlayoutcontainer);
    }

    public void cut2ClipBoard(FormSelection formselection)
    {
        formselection.reset();
        formselection.selection.addAll(selection);
        XCreator xcreator;
        for(Iterator iterator = selection.iterator(); iterator.hasNext(); removeCreatorFromContainer(xcreator))
            xcreator = (XCreator)iterator.next();

        reset();
    }

    public void copy2ClipBoard(FormSelection formselection)
    {
        formselection.reset();
        for(Iterator iterator = selection.iterator(); iterator.hasNext();)
        {
            XCreator xcreator = (XCreator)iterator.next();
            try
            {
                XCreator xcreator1 = XCreatorUtils.createXCreator((Widget)xcreator.toData().clone());
                xcreator1.setBounds(xcreator.getBounds());
                formselection.selection.add(xcreator1);
            }
            catch(CloneNotSupportedException clonenotsupportedexception)
            {
                FRContext.getLogger().error(clonenotsupportedexception.getMessage(), clonenotsupportedexception);
            }
        }

    }
}
