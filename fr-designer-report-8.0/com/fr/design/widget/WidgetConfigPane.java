// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.widget;

import com.fr.design.gui.controlpane.*;
import com.fr.design.gui.core.WidgetConstants;
import com.fr.form.ui.*;
import com.fr.general.NameObject;
import java.util.*;

// Referenced classes of package com.fr.design.widget:
//            UserDefinedWidgetConfigPane

public class WidgetConfigPane extends JControlPane
{

    public WidgetConfigPane()
    {
    }

    public NameableCreator[] createNameableCreators()
    {
        NameObjectCreator nameobjectcreator = new NameObjectCreator(WidgetConstants.USER_DEFINED_WIDGETCONFIG, "/com/fr/design/images/data/user_widget.png", com/fr/form/ui/UserDefinedWidgetConfig, com/fr/design/widget/UserDefinedWidgetConfigPane);
        return (new NameableCreator[] {
            nameobjectcreator
        });
    }

    protected String title4PopupWindow()
    {
        return "config";
    }

    public void populate(WidgetManagerProvider widgetmanagerprovider)
    {
        Iterator iterator = widgetmanagerprovider.getWidgetConfigNameIterator();
        ArrayList arraylist = new ArrayList();
        String s;
        for(; iterator.hasNext(); arraylist.add(new NameObject(s, widgetmanagerprovider.getWidgetConfig(s))))
            s = (String)iterator.next();

        populate((com.fr.stable.Nameable[])arraylist.toArray(new NameObject[arraylist.size()]));
    }

    public void update(WidgetManagerProvider widgetmanagerprovider)
    {
        com.fr.stable.Nameable anameable[] = update();
        NameObject anameobject[] = new NameObject[anameable.length];
        Arrays.asList(anameable).toArray(anameobject);
        widgetmanagerprovider.clearAllWidgetConfig();
        for(int i = 0; i < anameobject.length; i++)
        {
            NameObject nameobject = anameobject[i];
            widgetmanagerprovider.putWidgetConfig(nameobject.getName(), (WidgetConfig)nameobject.getObject());
        }

    }
}
