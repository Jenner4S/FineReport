// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.webattr;

import com.fr.form.ui.ToolBar;
import com.fr.form.ui.Widget;
import com.fr.general.Background;
import java.util.ArrayList;
import java.util.List;

// Referenced classes of package com.fr.design.webattr:
//            ToolBarButton

public class FToolBar
{

    private List buttonlist;
    private Background background;
    private boolean isDefault;

    public FToolBar()
    {
        buttonlist = new ArrayList();
        background = null;
        isDefault = true;
    }

    public List getButtonlist()
    {
        return buttonlist;
    }

    public void setButtonlist(List list)
    {
        if(list == null || list.size() < 0)
            buttonlist = new ArrayList();
        else
            buttonlist = list;
    }

    public void addButton(ToolBarButton toolbarbutton)
    {
        buttonlist.add(toolbarbutton);
    }

    public void removeButton(ToolBarButton toolbarbutton)
    {
        buttonlist.remove(toolbarbutton);
    }

    public void clearButton()
    {
        buttonlist.clear();
    }

    public Background getBackground()
    {
        return background;
    }

    public void setBackground(Background background1)
    {
        background = background1;
    }

    public boolean isDefault()
    {
        return isDefault;
    }

    public void setDefault(boolean flag)
    {
        isDefault = flag;
    }

    public ToolBar getToolBar()
    {
        Widget awidget[] = new Widget[getButtonlist().size()];
        for(int i = 0; i < getButtonlist().size(); i++)
            awidget[i] = ((ToolBarButton)getButtonlist().get(i)).getWidget();

        ToolBar toolbar = new ToolBar(awidget);
        toolbar.setBackground(background);
        toolbar.setDefault(isDefault);
        return toolbar;
    }
}
