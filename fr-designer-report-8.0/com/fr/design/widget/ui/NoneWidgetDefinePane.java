// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.widget.ui;

import com.fr.form.ui.NoneWidget;

// Referenced classes of package com.fr.design.widget.ui:
//            AbstractDataModify

public class NoneWidgetDefinePane extends AbstractDataModify
{

    public NoneWidgetDefinePane()
    {
    }

    protected String title4PopupWindow()
    {
        return "none";
    }

    public void populateBean(NoneWidget nonewidget)
    {
    }

    public NoneWidget updateBean()
    {
        return new NoneWidget();
    }

    public volatile Object updateBean()
    {
        return updateBean();
    }

    public volatile void populateBean(Object obj)
    {
        populateBean((NoneWidget)obj);
    }
}
