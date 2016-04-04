// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe;

import com.fr.base.BaseUtils;
import com.fr.design.designer.beans.events.DesignerEditListener;
import com.fr.design.designer.beans.events.DesignerEvent;
import com.fr.design.designer.properties.EventPropertyTable;
import com.fr.design.designer.properties.WidgetPropertyTable;
import com.fr.design.gui.frpane.UITabbedPane;
import com.fr.design.gui.icontainer.UIScrollPane;
import com.fr.design.gui.itable.AbstractPropertyTable;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.general.Inter;
import javax.swing.Icon;
import javax.swing.JScrollPane;

// Referenced classes of package com.fr.design.mainframe:
//            FormDockView, FormDesigner, BaseWidgetPropertyPane, DockingView, 
//            BaseFormDesigner

public class WidgetPropertyPane extends FormDockView
    implements BaseWidgetPropertyPane
{
    public class EventPropertyDesignerAdapter
        implements DesignerEditListener
    {

        EventPropertyTable propertyTable;
        final WidgetPropertyPane this$0;

        public void fireCreatorModified(DesignerEvent designerevent)
        {
            if(designerevent.getCreatorEventID() == 5 || designerevent.getCreatorEventID() == 5 || designerevent.getCreatorEventID() == 7)
                propertyTable.refresh();
        }

        public boolean equals(Object obj)
        {
            return obj instanceof EventPropertyDesignerAdapter;
        }

        public EventPropertyDesignerAdapter(EventPropertyTable eventpropertytable)
        {
            this$0 = WidgetPropertyPane.this;
            super();
            propertyTable = eventpropertytable;
        }
    }

    public class WidgetPropertyDesignerAdapter
        implements DesignerEditListener
    {

        AbstractPropertyTable propertyTable;
        final WidgetPropertyPane this$0;

        public void fireCreatorModified(DesignerEvent designerevent)
        {
            if(designerevent.getCreatorEventID() == 5 || designerevent.getCreatorEventID() == 2 || designerevent.getCreatorEventID() == 7)
                propertyTable.initPropertyGroups(null);
            else
            if(designerevent.getCreatorEventID() == 6)
                repaint();
        }

        public boolean equals(Object obj)
        {
            return obj instanceof WidgetPropertyDesignerAdapter;
        }

        public WidgetPropertyDesignerAdapter(AbstractPropertyTable abstractpropertytable)
        {
            this$0 = WidgetPropertyPane.this;
            super();
            propertyTable = abstractpropertytable;
        }
    }

    private static class HOLDER
    {

        private static WidgetPropertyPane singleton = new WidgetPropertyPane();




        private HOLDER()
        {
        }
    }


    private WidgetPropertyTable propertyTable;
    private EventPropertyTable eventTable;

    public static WidgetPropertyPane getInstance()
    {
        if(HOLDER.singleton == null)
            HOLDER.singleton = new WidgetPropertyPane();
        return HOLDER.singleton;
    }

    public static WidgetPropertyPane getInstance(FormDesigner formdesigner)
    {
        HOLDER.singleton.setEditingFormDesigner(formdesigner);
        HOLDER.singleton.refreshDockingView();
        return HOLDER.singleton;
    }

    private WidgetPropertyPane()
    {
        setLayout(FRGUIPaneFactory.createBorderLayout());
    }

    public String getViewTitle()
    {
        return Inter.getLocText("Form-Widget_Property_Table");
    }

    public Icon getViewIcon()
    {
        return BaseUtils.readIcon("/com/fr/design/images/m_report/attributes.png");
    }

    public void refreshDockingView()
    {
        FormDesigner formdesigner = getEditingFormDesigner();
        removeAll();
        if(formdesigner == null)
        {
            clearDockingView();
            return;
        } else
        {
            propertyTable = new WidgetPropertyTable(formdesigner);
            formdesigner.addDesignerEditListener(new WidgetPropertyDesignerAdapter(propertyTable));
            propertyTable.setBorder(null);
            UIScrollPane uiscrollpane = new UIScrollPane(propertyTable);
            uiscrollpane.setBorder(null);
            eventTable = new EventPropertyTable(formdesigner);
            formdesigner.addDesignerEditListener(new EventPropertyDesignerAdapter(eventTable));
            eventTable.setBorder(null);
            UIScrollPane uiscrollpane1 = new UIScrollPane(eventTable);
            uiscrollpane1.setBorder(null);
            UITabbedPane uitabbedpane = new UITabbedPane();
            uitabbedpane.setOpaque(true);
            uitabbedpane.setBorder(null);
            uitabbedpane.setTabPlacement(3);
            uitabbedpane.addTab(Inter.getLocText("Form-Properties"), uiscrollpane);
            uitabbedpane.addTab(Inter.getLocText("Form-Events"), uiscrollpane1);
            add(uitabbedpane, "Center");
            propertyTable.initPropertyGroups(null);
            eventTable.refresh();
            return;
        }
    }

    public void setEditingFormDesigner(BaseFormDesigner baseformdesigner)
    {
        FormDesigner formdesigner = (FormDesigner)baseformdesigner;
        super.setEditingFormDesigner(formdesigner);
    }

    public void clearDockingView()
    {
        propertyTable = null;
        eventTable = null;
        JScrollPane jscrollpane = new JScrollPane();
        jscrollpane.setBorder(null);
        add(jscrollpane, "Center");
    }

    public DockingView.Location preferredLocation()
    {
        return DockingView.Location.WEST_BELOW;
    }

}
