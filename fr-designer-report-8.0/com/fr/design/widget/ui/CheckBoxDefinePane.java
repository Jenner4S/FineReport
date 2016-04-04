// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.widget.ui;

import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.form.ui.CheckBox;
import com.fr.general.Inter;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

// Referenced classes of package com.fr.design.widget.ui:
//            AbstractDataModify

public class CheckBoxDefinePane extends AbstractDataModify
{

    private UITextField text;

    public CheckBoxDefinePane()
    {
        iniComoponents();
    }

    private void iniComoponents()
    {
        setLayout(FRGUIPaneFactory.createBorderLayout());
        JPanel jpanel = FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();
        jpanel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));
        jpanel.add(new UILabel((new StringBuilder()).append(Inter.getLocText("Text")).append(":").toString()));
        text = new UITextField(8);
        jpanel.add(text);
        add(jpanel);
    }

    protected String title4PopupWindow()
    {
        return "CheckBox";
    }

    public void populateBean(CheckBox checkbox)
    {
        text.setText(checkbox.getText());
    }

    public CheckBox updateBean()
    {
        CheckBox checkbox = new CheckBox();
        checkbox.setText(text.getText());
        return checkbox;
    }

    public volatile Object updateBean()
    {
        return updateBean();
    }

    public volatile void populateBean(Object obj)
    {
        populateBean((CheckBox)obj);
    }
}
