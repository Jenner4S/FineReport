// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.actions.insert.cell;

import com.fr.base.BaseUtils;
import com.fr.design.mainframe.ElementCasePane;
import com.fr.design.menu.MenuKeySet;
import com.fr.general.ComparatorUtils;
import com.fr.general.Inter;
import com.fr.report.cell.cellattr.core.RichText;
import javax.swing.KeyStroke;

// Referenced classes of package com.fr.design.actions.insert.cell:
//            AbstractCellAction

public class RichTextCellAction extends AbstractCellAction
{

    private static final MenuKeySet INSERT_RICHTEXT = new MenuKeySet() {

        public char getMnemonic()
        {
            return 'R';
        }

        public String getMenuName()
        {
            return Inter.getLocText("FR-Designer_RichText");
        }

        public KeyStroke getKeyStroke()
        {
            return null;
        }

    }
;

    public RichTextCellAction(ElementCasePane elementcasepane)
    {
        super(elementcasepane);
        setMenuKeySet(INSERT_RICHTEXT);
        setName((new StringBuilder()).append(getMenuKeySet().getMenuKeySetName()).append("...").toString());
        setMnemonic(getMenuKeySet().getMnemonic());
        setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/m_insert/richtext.png"));
    }

    public Class getCellValueClass()
    {
        return com/fr/report/cell/cellattr/core/RichText;
    }

    public boolean equals(Object obj)
    {
        if(this == obj)
            return true;
        if(!(obj instanceof RichTextCellAction))
            return false;
        else
            return ComparatorUtils.equals(getEditingComponent(), ((RichTextCellAction)obj).getEditingComponent());
    }

}
