// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.widget;

import com.fr.base.FRContext;
import com.fr.design.gui.controlpane.NameableSelfCreator;
import com.fr.design.gui.controlpane.UnrepeatedNameHelper;
import com.fr.design.gui.ilist.ListModelElement;
import com.fr.form.event.Listener;
import com.fr.general.*;
import com.fr.stable.Nameable;

// Referenced classes of package com.fr.design.widget:
//            WidgetEventPane

public class EventCreator extends NameableSelfCreator
{

    private String eventName;
    public static final EventCreator BEFOREEDIT = new EventCreator("beforeedit");
    public static final EventCreator AFTEREDIT = new EventCreator("afteredit");
    public static final EventCreator CHANGE = new EventCreator("change");
    public static final EventCreator CLICK = new EventCreator("click");
    public static final EventCreator SUCCESS = new EventCreator("success");
    public static final EventCreator AFTERINIT = new EventCreator("afterinit");
    public static final EventCreator STOPEDIT = new EventCreator("stopedit");
    public static final EventCreator STATECHANGE = new EventCreator("statechange");
    public static final EventCreator CALLBACK = new EventCreator("callback");

    public EventCreator(String s)
    {
        super(switchLang(s), com/fr/form/event/Listener, com/fr/design/widget/WidgetEventPane$WidgetEventListenerUpdatePane);
        eventName = s;
    }

    public Nameable createNameable(UnrepeatedNameHelper unrepeatednamehelper)
    {
        return new NameObject(unrepeatednamehelper.createUnrepeatedName(menuName()), new Listener(eventName));
    }

    public static EventCreator[] createEventCreator(String as[])
    {
        EventCreator aeventcreator[] = new EventCreator[as.length];
        for(int i = 0; i < as.length; i++)
            aeventcreator[i] = new EventCreator(as[i]);

        return aeventcreator;
    }

    public static final String switchLang(String s)
    {
        try
        {
            return Inter.getLocText((new StringBuilder()).append("Event-").append(s).toString());
        }
        catch(Exception exception)
        {
            FRContext.getLogger().error(exception.getMessage(), exception);
        }
        return s;
    }

    public void saveUpdatedBean(ListModelElement listmodelelement, Object obj)
    {
        ((NameObject)listmodelelement.wrapper).setObject(obj);
    }

    public String createTooltip()
    {
        return null;
    }

}
