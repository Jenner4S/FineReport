// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.actions.columnrow;

import com.fr.design.mainframe.ElementCasePane;
import com.fr.general.Inter;
import com.fr.grid.selection.CellSelection;
import com.fr.report.elementcase.ElementCase;
import com.fr.stable.unit.UNIT;

// Referenced classes of package com.fr.design.actions.columnrow:
//            ColumnRowSizingAction

public class ColumnWidthAction extends ColumnRowSizingAction
{

    public ColumnWidthAction(ElementCasePane elementcasepane, int i)
    {
        super(elementcasepane, i);
        setName((new StringBuilder()).append(Inter.getLocText("Utils-Column_Width")).append("...").toString());
        setMnemonic('C');
    }

    protected String title4UnitInputPane()
    {
        return Inter.getLocText("Utils-Column_Width");
    }

    protected void updateAction(ElementCase elementcase, UNIT unit, CellSelection cellselection)
    {
        int ai[] = cellselection.getSelectedColumns();
        for(int i = 0; i < ai.length; i++)
            elementcase.setColumnWidth(ai[i], unit);

    }

    protected UNIT getIndexLen(int i, ElementCase elementcase)
    {
        return elementcase.getColumnWidth(i);
    }

    protected UNIT getShowLen(ElementCase elementcase, CellSelection cellselection)
    {
        int ai[] = cellselection.getSelectedColumns();
        return getSelectedCellsLen(ai, elementcase);
    }
}
