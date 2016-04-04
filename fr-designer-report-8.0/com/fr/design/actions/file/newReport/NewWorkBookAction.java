// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.actions.file.newReport;

import com.fr.base.BaseUtils;
import com.fr.design.actions.UpdateAction;
import com.fr.design.mainframe.*;
import com.fr.design.menu.MenuKeySet;
import com.fr.general.Inter;
import java.awt.event.ActionEvent;
import javax.swing.Icon;
import javax.swing.KeyStroke;

public class NewWorkBookAction extends UpdateAction
{

    public static final MenuKeySet NEW_WORK_BOOK = new MenuKeySet() {

        public char getMnemonic()
        {
            return 'N';
        }

        public String getMenuName()
        {
            return Inter.getLocText("M-New_WorkBook");
        }

        public KeyStroke getKeyStroke()
        {
            return KeyStroke.getKeyStroke(78, 2);
        }

    }
;

    public NewWorkBookAction()
    {
        setMenuKeySet(NEW_WORK_BOOK);
        setName(getMenuKeySet().getMenuKeySetName());
        setMnemonic(getMenuKeySet().getMnemonic());
        setSmallIcon(icon());
        setAccelerator(getMenuKeySet().getKeyStroke());
    }

    protected Icon icon()
    {
        return BaseUtils.readIcon("/com/fr/design/images/buttonicon/newcpts.png");
    }

    public void actionPerformed(ActionEvent actionevent)
    {
        DesignerContext.getDesignerFrame().addAndActivateJTemplate(new JWorkBook());
    }

}
