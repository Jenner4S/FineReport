// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.actions.insert.cell;

import com.fr.base.BaseUtils;
import com.fr.design.mainframe.ElementCasePane;
import com.fr.design.menu.MenuKeySet;
import com.fr.general.ComparatorUtils;
import com.fr.general.Inter;
import com.fr.report.cell.cellattr.core.SubReport;
import javax.swing.KeyStroke;

// Referenced classes of package com.fr.design.actions.insert.cell:
//            AbstractCellAction

public class SubReportCellAction extends AbstractCellAction
{

    public static final MenuKeySet INSERT_SUB_REPORT = new MenuKeySet() {

        public char getMnemonic()
        {
            return 'S';
        }

        public String getMenuName()
        {
            return Inter.getLocText("M_Insert-Sub_Report");
        }

        public KeyStroke getKeyStroke()
        {
            return null;
        }

    }
;

    public SubReportCellAction(ElementCasePane elementcasepane)
    {
        super(elementcasepane);
        setMenuKeySet(INSERT_SUB_REPORT);
        setName((new StringBuilder()).append(getMenuKeySet().getMenuKeySetName()).append("...").toString());
        setMnemonic(getMenuKeySet().getMnemonic());
        setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/m_insert/subReport.png"));
    }

    public boolean equals(Object obj)
    {
        if(this == obj)
            return true;
        if(!(obj instanceof SubReportCellAction))
            return false;
        else
            return ComparatorUtils.equals(getEditingComponent(), ((SubReportCellAction)obj).getEditingComponent());
    }

    public Class getCellValueClass()
    {
        return com/fr/report/cell/cellattr/core/SubReport;
    }

}
