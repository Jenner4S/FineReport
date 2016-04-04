// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.widget.ui;

import com.fr.base.FRContext;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.form.ui.NameWidget;
import com.fr.general.*;
import java.awt.Font;

// Referenced classes of package com.fr.design.widget.ui:
//            AbstractDataModify

public class UserEditorDefinePane extends AbstractDataModify
{

    private NameWidget nWidget;

    public UserEditorDefinePane()
    {
        initComponents();
    }

    private void initComponents()
    {
        setLayout(FRGUIPaneFactory.createBorderLayout());
        UILabel uilabel = new UILabel();
        FRFont frfont = FRContext.getDefaultValues().getFRFont();
        uilabel.setFont(new Font(frfont.getFamily(), 1, 24));
        uilabel.setText((new StringBuilder()).append(Inter.getLocText("Widget-User_Defined_Editor")).append(".").toString());
        uilabel.setHorizontalAlignment(0);
        add(uilabel, "Center");
    }

    protected String title4PopupWindow()
    {
        return "name";
    }

    public void populateBean(NameWidget namewidget)
    {
        nWidget = namewidget;
    }

    public NameWidget updateBean()
    {
        return nWidget;
    }

    public volatile Object updateBean()
    {
        return updateBean();
    }

    public volatile void populateBean(Object obj)
    {
        populateBean((NameWidget)obj);
    }
}
