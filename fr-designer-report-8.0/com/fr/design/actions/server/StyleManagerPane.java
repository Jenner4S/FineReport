// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.actions.server;

import com.fr.base.*;
import com.fr.design.dialog.BasicPane;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.present.StyleArrayPane;
import com.fr.general.Inter;
import java.io.File;
import javax.swing.JPanel;

public class StyleManagerPane extends BasicPane
{

    private UITextField StyleTextField;
    private StyleArrayPane styleArrayPane;

    public StyleManagerPane()
    {
        setLayout(FRGUIPaneFactory.createBorderLayout());
        JPanel jpanel = FRGUIPaneFactory.createBorderLayout_L_Pane();
        add(jpanel, "North");
        jpanel.add(new UILabel((new StringBuilder()).append(Inter.getLocText("FR-Designer_Save_Path")).append(":").toString()), "West");
        StyleTextField = new UITextField();
        jpanel.add(StyleTextField, "Center");
        StyleTextField.setEditable(false);
        styleArrayPane = new StyleArrayPane();
        add(styleArrayPane, "Center");
    }

    protected String title4PopupWindow()
    {
        return Inter.getLocText("ServerM-Predefined_Styles");
    }

    public void populate(ConfigManagerProvider configmanagerprovider)
    {
        StyleTextField.setText((new StringBuilder()).append(FRContext.getCurrentEnv().getPath()).append(File.separator).append("resources").append(File.separator).append(configmanagerprovider.fileName()).toString());
        styleArrayPane.populate(configmanagerprovider);
    }

    public void update(ConfigManagerProvider configmanagerprovider)
    {
        styleArrayPane.update(configmanagerprovider);
    }
}
