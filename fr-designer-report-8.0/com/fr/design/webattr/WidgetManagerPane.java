// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.webattr;

import com.fr.base.Env;
import com.fr.base.FRContext;
import com.fr.design.gui.frpane.LoadingBasicPane;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.widget.WidgetConfigPane;
import com.fr.form.ui.WidgetManagerProvider;
import com.fr.general.Inter;
import java.io.File;
import javax.swing.JPanel;

public class WidgetManagerPane extends LoadingBasicPane
{

    private UITextField widgetTextField;
    private WidgetConfigPane widgetConfigPane;

    public WidgetManagerPane()
    {
    }

    protected void initComponents(JPanel jpanel)
    {
        jpanel.setLayout(FRGUIPaneFactory.createBorderLayout());
        JPanel jpanel1 = FRGUIPaneFactory.createBorderLayout_L_Pane();
        jpanel.add(jpanel1, "North");
        jpanel1.add(new UILabel((new StringBuilder()).append(Inter.getLocText("FR-Designer_Save_Path")).append(":").toString()), "West");
        widgetTextField = new UITextField();
        jpanel1.add(widgetTextField, "Center");
        widgetTextField.setEditable(false);
        widgetConfigPane = new WidgetConfigPane();
        jpanel.add(widgetConfigPane, "Center");
    }

    protected String title4PopupWindow()
    {
        return Inter.getLocText("ServerM-Widget_Manager");
    }

    public void populate(WidgetManagerProvider widgetmanagerprovider)
    {
        widgetTextField.setText((new StringBuilder()).append(FRContext.getCurrentEnv().getPath()).append(File.separator).append("resources").append(File.separator).append(widgetmanagerprovider.fileName()).toString());
        widgetConfigPane.populate(widgetmanagerprovider);
    }

    public void update(WidgetManagerProvider widgetmanagerprovider)
    {
        widgetConfigPane.update(widgetmanagerprovider);
    }
}
