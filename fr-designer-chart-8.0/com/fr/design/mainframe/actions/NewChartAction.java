// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.actions;

import com.fr.base.BaseUtils;
import com.fr.design.actions.UpdateAction;
import com.fr.design.mainframe.*;
import com.fr.design.menu.MenuKeySet;
import com.fr.general.Inter;
import java.awt.event.ActionEvent;
import javax.swing.KeyStroke;

public class NewChartAction extends UpdateAction
{

    public static final MenuKeySet NEW_CHART = new MenuKeySet() {

        public char getMnemonic()
        {
            return 'F';
        }

        public String getMenuName()
        {
            return Inter.getLocText("M-New_ChartBook");
        }

        public KeyStroke getKeyStroke()
        {
            return KeyStroke.getKeyStroke(70, 2);
        }

    }
;

    public NewChartAction()
    {
        setMenuKeySet(NEW_CHART);
        setName(getMenuKeySet().getMenuKeySetName());
        setMnemonic(getMenuKeySet().getMnemonic());
        setSmallIcon(BaseUtils.readIcon("com/fr/design/images/newchart_normal.png"));
        setAccelerator(getMenuKeySet().getKeyStroke());
    }

    public void actionPerformed(ActionEvent actionevent)
    {
        DesignerContext.getDesignerFrame().addAndActivateJTemplate(new JChart());
    }

}
