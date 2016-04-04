// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.actions;

import com.fr.design.gui.imenu.UICheckBoxMenuItem;
import com.fr.design.gui.imenu.UIMenuItem;
import com.fr.design.mainframe.ElementCasePane;

// Referenced classes of package com.fr.design.actions:
//            ElementCaseAction

public abstract class PresentCheckBoxAction extends ElementCaseAction
{

    protected PresentCheckBoxAction(ElementCasePane elementcasepane)
    {
        super(elementcasepane);
    }

    public UICheckBoxMenuItem createMenuItem()
    {
        Object obj = getValue(com/fr/design/gui/imenu/UICheckBoxMenuItem.getName());
        if(obj == null)
        {
            obj = createCheckBoxMenuItem(this);
            putValue(com/fr/design/gui/imenu/UICheckBoxMenuItem.getName(), obj);
        }
        ((UICheckBoxMenuItem)obj).setSelected(isSelected());
        return (UICheckBoxMenuItem)obj;
    }

    public abstract boolean isSelected();

    public volatile UIMenuItem createMenuItem()
    {
        return createMenuItem();
    }
}
