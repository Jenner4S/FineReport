// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.widget;

import com.fr.design.gui.controlpane.NameableCreator;
import com.fr.design.gui.controlpane.ObjectJControlPane;
import com.fr.design.gui.frpane.ListenerUpdatePane;
import com.fr.design.javascript.JavaScriptActionPane;
import com.fr.design.mainframe.*;
import com.fr.design.write.submit.DBManipulationPane;
import com.fr.design.write.submit.SmartInsertDBManipulationInWidgetEventPane;
import com.fr.form.event.Listener;
import com.fr.form.ui.Widget;
import com.fr.general.Inter;
import com.fr.general.NameObject;
import java.util.ArrayList;
import java.util.List;

// Referenced classes of package com.fr.design.widget:
//            EventCreator

public class WidgetEventPane extends ObjectJControlPane
{
    public static class WidgetEventListenerUpdatePane extends ListenerUpdatePane
    {

        private ElementCasePane epane;

        private DBManipulationPane autoCreateDBManipulationInWidgetEventPane()
        {
            JTemplate jtemplate = DesignerContext.getDesignerFrame().getSelectedJTemplate();
            return jtemplate.createDBManipulationPaneInWidget();
        }

        protected JavaScriptActionPane createJavaScriptActionPane()
        {
            return new JavaScriptActionPane() {

                final WidgetEventListenerUpdatePane this$0;

                protected DBManipulationPane createDBManipulationPane()
                {
                    if(epane == null)
                        return autoCreateDBManipulationInWidgetEventPane();
                    else
                        return new SmartInsertDBManipulationInWidgetEventPane(epane);
                }

                protected String title4PopupWindow()
                {
                    return Inter.getLocText("Set_Callback_Function");
                }

                protected boolean isForm()
                {
                    return false;
                }

                protected String[] getDefaultArgs()
                {
                    return new String[0];
                }

                
                {
                    this$0 = WidgetEventListenerUpdatePane.this;
                    super();
                }
            }
;
        }

        protected boolean supportCellAction()
        {
            return false;
        }



        public WidgetEventListenerUpdatePane()
        {
            this(null);
        }

        public WidgetEventListenerUpdatePane(ElementCasePane elementcasepane)
        {
            epane = elementcasepane;
            super.initComponents();
        }
    }


    public WidgetEventPane()
    {
        this(null);
    }

    public WidgetEventPane(ElementCasePane elementcasepane)
    {
        super(elementcasepane);
        setNameListEditable(false);
    }

    public NameableCreator[] createNameableCreators()
    {
        return (new NameableCreator[] {
            EventCreator.STATECHANGE
        });
    }

    protected String title4PopupWindow()
    {
        return Inter.getLocText("Event");
    }

    public void populate(Widget widget)
    {
        if(widget == null)
            return;
        refreshNameableCreator(EventCreator.createEventCreator(widget.supportedEvents()));
        ArrayList arraylist = new ArrayList();
        int i = 0;
        for(int j = widget.getListenerSize(); i < j; i++)
        {
            Listener listener = widget.getListener(i);
            if(!listener.isDefault())
                arraylist.add(new NameObject((new StringBuilder()).append(EventCreator.switchLang(listener.getEventName())).append(i + 1).toString(), listener));
        }

        populate((com.fr.stable.Nameable[])arraylist.toArray(new NameObject[arraylist.size()]));
    }

    public Listener[] updateListeners()
    {
        com.fr.stable.Nameable anameable[] = update();
        Listener alistener[] = new Listener[anameable.length];
        int i = 0;
        for(int j = anameable.length; i < j; i++)
            alistener[i] = (Listener)((NameObject)anameable[i]).getObject();

        return alistener;
    }
}
