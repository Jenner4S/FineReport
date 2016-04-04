// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.actions;

import com.fr.base.BaseUtils;
import com.fr.design.actions.UpdateAction;
import com.fr.design.mainframe.*;
import com.fr.design.menu.MenuKeySet;
import com.fr.general.Inter;
import com.fr.stable.bridge.StableFactory;
import java.awt.event.ActionEvent;
import javax.swing.KeyStroke;

public class NewFormAction extends UpdateAction
{

    public static final MenuKeySet NEW_FORM = new MenuKeySet() {

        public char getMnemonic()
        {
            return 'F';
        }

        public String getMenuName()
        {
            return Inter.getLocText("M-New_FormBook");
        }

        public KeyStroke getKeyStroke()
        {
            return KeyStroke.getKeyStroke(70, 2);
        }

    }
;

    public NewFormAction()
    {
        setMenuKeySet(NEW_FORM);
        setName(getMenuKeySet().getMenuKeySetName());
        setMnemonic(getMenuKeySet().getMnemonic());
        setSmallIcon(BaseUtils.readIcon("/com/fr/web/images/form/new_form3.png"));
        setAccelerator(getMenuKeySet().getKeyStroke());
    }

    public void actionPerformed(ActionEvent actionevent)
    {
        BaseJForm basejform = (BaseJForm)StableFactory.getMarkedInstanceObjectFromClass("JForm", com/fr/design/mainframe/BaseJForm);
        DesignerContext.getDesignerFrame().addAndActivateJTemplate((JTemplate)basejform);
    }

}
