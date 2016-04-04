// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.designer.beans.models;

import com.fr.design.designer.beans.ComponentAdapter;
import com.fr.design.designer.beans.LayoutAdapter;
import com.fr.design.designer.beans.adapters.component.CompositeComponentAdapter;
import com.fr.design.designer.creator.*;
import com.fr.design.mainframe.FormArea;
import com.fr.design.mainframe.FormDesigner;
import com.fr.design.utils.ComponentUtils;
import com.fr.form.main.Form;
import com.fr.form.ui.Widget;
import com.fr.general.ComparatorUtils;
import java.awt.Rectangle;

public class AddingModel
{

    private XCreator creator;
    private int current_x;
    private int current_y;
    private boolean added;

    public AddingModel(FormDesigner formdesigner, XCreator xcreator)
    {
        String s = getXCreatorName(formdesigner, xcreator);
        creator = xcreator;
        instantiateCreator(formdesigner, s);
        current_x = -creator.getWidth();
        current_y = -creator.getHeight();
    }

    public void instantiateCreator(FormDesigner formdesigner, String s)
    {
        creator.toData().setWidgetName(s);
        CompositeComponentAdapter compositecomponentadapter = new CompositeComponentAdapter(formdesigner, creator);
        compositecomponentadapter.initialize();
        creator.addNotify();
        creator.putClientProperty("component.adapter", compositecomponentadapter);
    }

    public AddingModel(XCreator xcreator, int i, int j)
    {
        creator = xcreator;
        creator.backupCurrentSize();
        creator.backupParent();
        creator.setSize(xcreator.initEditorSize());
        current_x = i - xcreator.getWidth() / 2;
        current_y = j - xcreator.getHeight() / 2;
    }

    public void reset()
    {
        current_x = -creator.getWidth();
        current_y = -creator.getHeight();
    }

    public String getXCreatorName(FormDesigner formdesigner, XCreator xcreator)
    {
        String s = xcreator.createDefaultName();
        if(xcreator.acceptType(new Class[] {
    com/fr/design/designer/creator/XWParameterLayout
}))
            return s;
        int i;
        for(i = 0; ((Form)formdesigner.getTarget()).isNameExist((new StringBuilder()).append(s).append(i).toString()); i++);
        return (new StringBuilder()).append(s).append(i).toString();
    }

    public int getCurrentX()
    {
        return current_x;
    }

    public int getCurrentY()
    {
        return current_y;
    }

    public void moveTo(int i, int j)
    {
        current_x = i - creator.getWidth() / 2;
        current_y = j - creator.getHeight() / 2;
    }

    public XCreator getXCreator()
    {
        return creator;
    }

    public boolean isCreatorAdded()
    {
        return added;
    }

    public boolean add2Container(FormDesigner formdesigner, XLayoutContainer xlayoutcontainer, int i, int j)
    {
        Rectangle rectangle = ComponentUtils.getRelativeBounds(xlayoutcontainer);
        if(!ComparatorUtils.equals(xlayoutcontainer.getOuterLayout(), xlayoutcontainer.getBackupParent()))
            return added = xlayoutcontainer.getLayoutAdapter().addBean(creator, i, j);
        else
            return added = xlayoutcontainer.getLayoutAdapter().addBean(creator, (i + formdesigner.getArea().getHorizontalValue()) - rectangle.x, (j + formdesigner.getArea().getVerticalValue()) - rectangle.y);
    }
}
