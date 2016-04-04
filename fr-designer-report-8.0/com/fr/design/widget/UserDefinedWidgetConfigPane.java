// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.widget;

import com.fr.design.beans.BasicBeanPane;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.form.ui.UserDefinedWidgetConfig;

// Referenced classes of package com.fr.design.widget:
//            ValueWidgetPane

public class UserDefinedWidgetConfigPane extends BasicBeanPane
{

    private ValueWidgetPane editorDefPane;

    public UserDefinedWidgetConfigPane()
    {
        setLayout(FRGUIPaneFactory.createBorderLayout());
        editorDefPane = new ValueWidgetPane();
        add(editorDefPane, "Center");
    }

    protected String title4PopupWindow()
    {
        return "custom";
    }

    public void populateBean(UserDefinedWidgetConfig userdefinedwidgetconfig)
    {
        editorDefPane.populate(userdefinedwidgetconfig.toWidget());
    }

    public UserDefinedWidgetConfig updateBean()
    {
        UserDefinedWidgetConfig userdefinedwidgetconfig = new UserDefinedWidgetConfig();
        userdefinedwidgetconfig.setWidget(editorDefPane.update());
        return userdefinedwidgetconfig;
    }

    public volatile Object updateBean()
    {
        return updateBean();
    }

    public volatile void populateBean(Object obj)
    {
        populateBean((UserDefinedWidgetConfig)obj);
    }
}
