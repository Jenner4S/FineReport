// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.report;

import com.fr.design.dialog.BasicPane;
import com.fr.design.gui.ibutton.UIRadioButton;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.general.Inter;
import javax.swing.*;

public class RowColumnPane extends BasicPane
{

    private UILabel titleLabel;
    private UIRadioButton entireRowRadioButton;
    private UIRadioButton entireColRadioButton;

    public RowColumnPane()
    {
        setLayout(FRGUIPaneFactory.createBorderLayout());
        JPanel jpanel = FRGUIPaneFactory.createBorderLayout_S_Pane();
        add(jpanel, "North");
        titleLabel = new UILabel(Inter.getLocText("Delete"));
        jpanel.add(titleLabel, "West");
        JPanel jpanel1 = FRGUIPaneFactory.createBorderLayout_S_Pane();
        jpanel1.add(new JSeparator());
        jpanel.add(jpanel1, "Center");
        JPanel jpanel2 = FRGUIPaneFactory.createNColumnGridInnerContainer_S_Pane(2);
        add(jpanel2, "Center");
        jpanel2.setBorder(BorderFactory.createEmptyBorder(6, 12, 6, 8));
        entireRowRadioButton = new UIRadioButton(Inter.getLocText("EditRC-Entire_row"));
        entireRowRadioButton.setMnemonic('r');
        entireColRadioButton = new UIRadioButton(Inter.getLocText("EditRC-Entire_column"));
        entireColRadioButton.setMnemonic('c');
        entireRowRadioButton.setSelected(true);
        ButtonGroup buttongroup = new ButtonGroup();
        buttongroup.add(entireRowRadioButton);
        buttongroup.add(entireColRadioButton);
        jpanel2.add(entireRowRadioButton);
        jpanel2.add(entireColRadioButton);
    }

    public void setTitle(String s)
    {
        titleLabel.setText(s);
    }

    protected String title4PopupWindow()
    {
        return titleLabel.getText();
    }

    public boolean isEntireRow()
    {
        return entireRowRadioButton.isSelected();
    }
}
