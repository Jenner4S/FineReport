// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.widget;

import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.mainframe.ElementCasePane;
import com.fr.design.mainframe.widget.editors.WidgetValueEditor;
import com.fr.form.ui.*;
import com.fr.general.Inter;
import javax.swing.JPanel;

// Referenced classes of package com.fr.design.widget:
//            WidgetPane

public class ValueWidgetPane extends WidgetPane
{

    private JPanel widgetValuePane;
    private WidgetValueEditor widgetValueEditor;
    private UILabel label;

    public ValueWidgetPane()
    {
    }

    protected void initComponents(ElementCasePane elementcasepane)
    {
        super.initComponents(elementcasepane);
        label = new UILabel((new StringBuilder()).append("     ").append(Inter.getLocText(new String[] {
            "Widget", "Value"
        })).append(":").toString());
        northPane.add(label);
        label.setVisible(false);
        widgetValuePane = new JPanel();
        widgetValuePane.setLayout(FRGUIPaneFactory.createBorderLayout());
        northPane.add(widgetValuePane);
    }

    public void populate(Widget widget)
    {
        super.populate(widget);
        populateWidgetValue(widget);
    }

    protected void populateWidgetConfig(Widget widget)
    {
        super.populateWidgetConfig(widget);
        populateWidgetValue(widget);
    }

    private void populateWidgetValue(Widget widget)
    {
        widgetValuePane.removeAll();
        widgetValueEditor = null;
        label.setVisible(false);
        if(widget instanceof DataControl)
        {
            label.setVisible(true);
            widgetValueEditor = new WidgetValueEditor(widget, true);
            widgetValueEditor.setValue(((DataControl)widget).getWidgetValue());
            widgetValuePane.add(widgetValueEditor.getCustomEditor());
        }
    }

    public Widget update()
    {
        Widget widget = super.update();
        if((widget instanceof DataControl) && widgetValueEditor != null)
            ((DataControl)widget).setWidgetValue((WidgetValue)widgetValueEditor.getValue());
        return widget;
    }
}
