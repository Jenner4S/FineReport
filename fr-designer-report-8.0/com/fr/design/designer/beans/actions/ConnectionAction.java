// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.designer.beans.actions;

import com.fr.base.BaseUtils;
import com.fr.design.actions.ToggleButtonUpdateAction;
import com.fr.design.actions.UpdateAction;
import com.fr.design.gui.ibutton.UIToggleButton;
import com.fr.design.mainframe.FormDesigner;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.general.Inter;
import java.awt.event.ActionEvent;
import javax.swing.JComponent;

public class ConnectionAction extends UpdateAction
    implements ToggleButtonUpdateAction
{

    private FormDesigner fd;

    public ConnectionAction(FormDesigner formdesigner)
    {
        fd = formdesigner;
        setName(Inter.getLocText("Connectionline"));
        setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/toolbarbtn/connector.png"));
    }

    public void actionPerformed(ActionEvent actionevent)
    {
        UIToggleButton uitogglebutton = createToolBarComponent();
        fd.setDrawLineMode(uitogglebutton.isSelected());
    }

    public UIToggleButton createToolBarComponent()
    {
        return GUICoreUtils.createToolBarComponent(this);
    }

    public volatile JComponent createToolBarComponent()
    {
        return createToolBarComponent();
    }
}
