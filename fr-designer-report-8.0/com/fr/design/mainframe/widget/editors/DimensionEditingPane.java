// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.widget.editors;

import com.fr.design.dialog.BasicPane;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.ispinner.UISpinner;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.general.Inter;
import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.JPanel;

public class DimensionEditingPane extends BasicPane
{

    private UILabel jLabel1;
    private UILabel jLabel2;
    private UISpinner spHeight;
    private UISpinner spWidth;

    public DimensionEditingPane()
    {
        initComponents();
    }

    private void initComponents()
    {
        setLayout(new GridLayout(2, 1));
        jLabel1 = new UILabel((new StringBuilder()).append(Inter.getLocText("Widget-Width")).append(":").toString());
        jLabel2 = new UILabel((new StringBuilder()).append(Inter.getLocText("Widget-Height")).append(":").toString());
        spWidth = new UISpinner(0.0D, 2147483647D, 1.0D, 0.0D);
        spHeight = new UISpinner(0.0D, 2147483647D, 1.0D, 0.0D);
        spWidth.setPreferredSize(new Dimension(29, 22));
        spHeight.setPreferredSize(new Dimension(29, 22));
        JPanel jpanel = FRGUIPaneFactory.createBorderLayout_S_Pane();
        add(jpanel);
        jpanel.add(jLabel1, "West");
        jpanel.add(spWidth, "Center");
        JPanel jpanel1 = FRGUIPaneFactory.createBorderLayout_S_Pane();
        add(jpanel1);
        jpanel1.add(jLabel2, "West");
        jpanel1.add(spHeight, "Center");
    }

    protected String title4PopupWindow()
    {
        return Inter.getLocText("Widget-Sizing");
    }

    public Dimension update()
    {
        return new Dimension(Double.valueOf(spWidth.getValue()).intValue(), Double.valueOf(spHeight.getValue()).intValue());
    }

    public void populate(Dimension dimension)
    {
        spWidth.setValue(dimension.width);
        spHeight.setValue(dimension.height);
    }
}
