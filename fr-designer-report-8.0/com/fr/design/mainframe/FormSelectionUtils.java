// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe;

import com.fr.base.FRContext;
import com.fr.design.designer.beans.LayoutAdapter;
import com.fr.design.designer.beans.events.CreatorEventListenerTable;
import com.fr.design.designer.beans.models.SelectionModel;
import com.fr.design.designer.creator.XCreator;
import com.fr.design.designer.creator.XCreatorUtils;
import com.fr.design.designer.creator.XLayoutContainer;
import com.fr.design.designer.creator.XWAbsoluteLayout;
import com.fr.form.main.ClonedWidgetCreator;
import com.fr.form.main.Form;
import com.fr.form.ui.Widget;
import com.fr.general.ComparatorUtils;
import com.fr.general.FRLogger;
import java.awt.Component;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

// Referenced classes of package com.fr.design.mainframe:
//            FormDesigner, FormSelection

public class FormSelectionUtils
{

    public FormSelectionUtils()
    {
    }

    public static void paste2Container(FormDesigner formdesigner, XLayoutContainer xlayoutcontainer, FormSelection formselection, int i, int j)
    {
        LayoutAdapter layoutadapter = xlayoutcontainer.getLayoutAdapter();
        if(formselection.size() == 1)
            try
            {
                XCreator xcreator = formselection.getSelectedCreator();
                Widget widget = (new ClonedWidgetCreator((Form)formdesigner.getTarget())).clonedWidgetWithNoRepeatName(xcreator.toData());
                XCreator xcreator1 = XCreatorUtils.createXCreator(widget, xcreator.getSize());
                if(layoutadapter.addBean(xcreator1, i + xcreator1.getWidth() / 2, j + xcreator1.getHeight() / 2))
                {
                    formdesigner.getSelectionModel().getSelection().setSelectedCreator(xcreator1);
                    formdesigner.getEditListenerTable().fireCreatorModified(xcreator1, 4);
                    return;
                }
            }
            catch(CloneNotSupportedException clonenotsupportedexception)
            {
                FRContext.getLogger().error(clonenotsupportedexception.getMessage(), clonenotsupportedexception);
            }
        else
        if(formselection.size() > 1 && (xlayoutcontainer instanceof XWAbsoluteLayout))
        {
            formdesigner.getSelectionModel().getSelection().reset();
            Rectangle rectangle = formselection.getSelctionBounds();
            XCreator axcreator[] = formselection.getSelectedCreators();
            int k = axcreator.length;
            for(int l = 0; l < k; l++)
            {
                XCreator xcreator2 = axcreator[l];
                try
                {
                    Widget widget1 = (new ClonedWidgetCreator((Form)formdesigner.getTarget())).clonedWidgetWithNoRepeatName(xcreator2.toData());
                    XCreator xcreator3 = XCreatorUtils.createXCreator(widget1, xcreator2.getSize());
                    layoutadapter.addBean(xcreator3, ((i + xcreator2.getX()) - rectangle.x) + xcreator3.getWidth() / 2, ((j + xcreator2.getY()) - rectangle.y) + xcreator3.getHeight() / 2);
                    formdesigner.getSelectionModel().getSelection().addSelectedCreator(xcreator3);
                }
                catch(CloneNotSupportedException clonenotsupportedexception1)
                {
                    FRContext.getLogger().error(clonenotsupportedexception1.getMessage(), clonenotsupportedexception1);
                }
            }

            formdesigner.getEditListenerTable().fireCreatorModified(formdesigner.getSelectionModel().getSelection().getSelectedCreator(), 4);
            return;
        }
        Toolkit.getDefaultToolkit().beep();
    }

    public static void rebuildSelection(FormDesigner formdesigner)
    {
        ArrayList arraylist = new ArrayList();
        ArrayList arraylist1 = new ArrayList();
        XCreator axcreator[] = formdesigner.getSelectionModel().getSelection().getSelectedCreators();
        int i = axcreator.length;
        for(int j = 0; j < i; j++)
        {
            XCreator xcreator = axcreator[j];
            arraylist1.add(xcreator.toData());
        }

        formdesigner.getSelectionModel().setSelectedCreators(rebuildSelection(((XCreator) (formdesigner.getRootComponent())), ((java.util.List) (arraylist1)), arraylist));
    }

    public static ArrayList rebuildSelection(XCreator xcreator, Widget awidget[])
    {
        ArrayList arraylist = new ArrayList();
        if(awidget != null)
            arraylist.addAll(Arrays.asList(awidget));
        return rebuildSelection(xcreator, ((java.util.List) (arraylist)), new ArrayList());
    }

    private static ArrayList rebuildSelection(XCreator xcreator, java.util.List list, ArrayList arraylist)
    {
        _rebuild(xcreator, list, arraylist);
        if(arraylist.isEmpty())
            arraylist.add(xcreator);
        return arraylist;
    }

    private static void _rebuild(XCreator xcreator, java.util.List list, java.util.List list1)
    {
        if(list.isEmpty())
            return;
        Iterator iterator = list.iterator();
        do
        {
            if(!iterator.hasNext())
                break;
            Widget widget = (Widget)iterator.next();
            if(!ComparatorUtils.equals(widget, xcreator.toData()))
                continue;
            if(!list1.contains(xcreator))
            {
                list1.add(xcreator);
                list.remove(widget);
            }
            break;
        } while(true);
        int i = xcreator.getComponentCount();
        for(int j = 0; j < i && !list.isEmpty(); j++)
        {
            Component component = xcreator.getComponent(j);
            if(!(component instanceof XCreator))
                continue;
            XCreator xcreator1 = (XCreator)component;
            Iterator iterator1 = list.iterator();
            do
            {
                if(!iterator1.hasNext())
                    break;
                Widget widget1 = (Widget)iterator1.next();
                if(!ComparatorUtils.equals(widget1, xcreator1.toData()))
                    continue;
                list1.add(xcreator1);
                list.remove(widget1);
                break;
            } while(true);
            if(component instanceof XLayoutContainer)
                _rebuild(((XCreator) ((XLayoutContainer)component)), list, list1);
        }

    }
}
