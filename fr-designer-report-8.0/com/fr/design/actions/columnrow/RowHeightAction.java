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

public class RowHeightAction extends ColumnRowSizingAction
{

    public RowHeightAction(ElementCasePane elementcasepane, int i)
    {
        super(elementcasepane, i);
        setName((new StringBuilder()).append(Inter.getLocText("Utils-Row_Height")).append("...").toString());
        setMnemonic('R');
    }

    protected String title4UnitInputPane()
    {
        return Inter.getLocText("Utils-Row_Height");
    }

    protected void updateAction(ElementCase elementcase, UNIT unit, CellSelection cellselection)
    {
        int ai[] = cellselection.getSelectedRows();
        for(int i = 0; i < ai.length; i++)
            elementcase.setRowHeight(ai[i], unit);

    }

    protected UNIT getShowLen(ElementCase elementcase, CellSelection cellselection)
    {
        int ai[] = cellselection.getSelectedRows();
        return getSelectedCellsLen(ai, elementcase);
    }

    protected UNIT getIndexLen(int i, ElementCase elementcase)
    {
        return elementcase.getRowHeight(i);
    }
}
