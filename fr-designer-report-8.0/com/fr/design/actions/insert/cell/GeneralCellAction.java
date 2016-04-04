// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.actions.insert.cell;

import com.fr.base.BaseUtils;
import com.fr.design.mainframe.ElementCasePane;
import com.fr.design.menu.MenuKeySet;
import com.fr.general.Inter;
import javax.swing.KeyStroke;

// Referenced classes of package com.fr.design.actions.insert.cell:
//            AbstractCellAction

public class GeneralCellAction extends AbstractCellAction
{

    public static final MenuKeySet INSERT_TEXT = new MenuKeySet() {

        public char getMnemonic()
        {
            return 'T';
        }

        public String getMenuName()
        {
            return Inter.getLocText("M_Insert-Text");
        }

        public KeyStroke getKeyStroke()
        {
            return null;
        }

    }
;

    public GeneralCellAction(ElementCasePane elementcasepane)
    {
        super(elementcasepane);
        setMenuKeySet(INSERT_TEXT);
        setName((new StringBuilder()).append(getMenuKeySet().getMenuKeySetName()).append("...").toString());
        setMnemonic(getMenuKeySet().getMnemonic());
        setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/m_insert/text.png"));
    }

    public Class getCellValueClass()
    {
        return java/lang/String;
    }

}
