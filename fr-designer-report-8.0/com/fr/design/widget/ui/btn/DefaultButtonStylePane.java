// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.widget.ui.btn;

import com.fr.design.dialog.BasicPane;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.widget.IconDefinePane;
import com.fr.form.ui.Button;
import com.fr.general.Inter;
import java.awt.BorderLayout;
import java.awt.Component;
import javax.swing.JPanel;

public class DefaultButtonStylePane extends BasicPane
{

    private UITextField buttonNameTextField;
    private IconDefinePane iconPane;
    private UIComboBox buttonStyleComboBox;

    public DefaultButtonStylePane()
    {
        initComponents();
    }

    protected void initComponents()
    {
        setLayout(new BorderLayout());
        JPanel jpanel = FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();
        iconPane = new IconDefinePane();
        jpanel.add(iconPane);
        Component acomponent[][] = {
            {
                new UILabel((new StringBuilder()).append(Inter.getLocText("Text")).append(":").toString()), buttonNameTextField = new UITextField(20)
            }, {
                new UILabel((new StringBuilder()).append(Inter.getLocText("Icon")).append(":").toString()), jpanel
            }
        };
        JPanel jpanel1 = TableLayoutHelper.createTableLayoutPane(acomponent, new double[] {
            -2D, -2D
        }, new double[] {
            -2D, -2D
        });
        add(jpanel1, "Center");
    }

    public void populate(Button button)
    {
        buttonNameTextField.setText(button.getText());
        iconPane.populate(button.getIconName());
    }

    public Button update(Button button)
    {
        button.setText(buttonNameTextField.getText());
        button.setIconName(iconPane.update());
        return button;
    }

    protected String title4PopupWindow()
    {
        return "default";
    }
}
