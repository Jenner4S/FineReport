// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.widget.ui;

import com.fr.base.FRContext;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.form.ui.Radio;
import com.fr.general.*;
import java.awt.Font;

// Referenced classes of package com.fr.design.widget.ui:
//            AbstractDataModify

public class RadioDefinePane extends AbstractDataModify
{

    public RadioDefinePane()
    {
        iniComoponents();
    }

    private void iniComoponents()
    {
        setLayout(FRGUIPaneFactory.createBorderLayout());
        UILabel uilabel = new UILabel();
        FRFont frfont = FRContext.getDefaultValues().getFRFont();
        uilabel.setFont(new Font(frfont.getFamily(), 1, 24));
        uilabel.setText((new StringBuilder()).append(Inter.getLocText("No_Editor_Property_Definition")).append(".").toString());
        uilabel.setHorizontalAlignment(0);
        add(uilabel, "Center");
    }

    protected String title4PopupWindow()
    {
        return "radio";
    }

    public void populateBean(Radio radio)
    {
    }

    public Radio updateBean()
    {
        return new Radio();
    }

    public volatile Object updateBean()
    {
        return updateBean();
    }

    public volatile void populateBean(Object obj)
    {
        populateBean((Radio)obj);
    }
}
