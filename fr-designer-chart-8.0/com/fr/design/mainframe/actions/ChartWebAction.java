// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.actions;

import com.fr.base.BaseUtils;
import com.fr.design.actions.UpdateAction;
import com.fr.design.menu.MenuKeySet;
import com.fr.general.Inter;
import com.fr.start.StartServer;
import java.awt.event.ActionEvent;
import javax.swing.KeyStroke;

public class ChartWebAction extends UpdateAction
{

    public ChartWebAction()
    {
        setMenuKeySet(getSelfMenuKeySet());
        setName(getMenuKeySet().getMenuName());
        setMnemonic(getMenuKeySet().getMnemonic());
        setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/m_help/demo.png"));
    }

    private MenuKeySet getSelfMenuKeySet()
    {
        return new MenuKeySet() {

            final ChartWebAction this$0;

            public char getMnemonic()
            {
                return 'D';
            }

            public String getMenuName()
            {
                return Inter.getLocText("FR-Chart-Product_Demo");
            }

            public KeyStroke getKeyStroke()
            {
                return null;
            }

            
            {
                this$0 = ChartWebAction.this;
                super();
            }
        }
;
    }

    public void actionPerformed(ActionEvent actionevent)
    {
        StartServer.browerURLWithLocalEnv("http://www.vancharts.com/demo.html");
    }
}
