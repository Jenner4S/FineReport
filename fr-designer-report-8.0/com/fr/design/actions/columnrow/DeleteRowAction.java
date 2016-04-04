// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.actions.columnrow;

import com.fr.base.BaseUtils;
import com.fr.design.actions.CellSelectionAction;
import com.fr.design.mainframe.ElementCasePane;
import com.fr.design.menu.MenuKeySet;
import com.fr.general.Inter;
import com.fr.grid.selection.CellSelection;
import com.fr.report.elementcase.ElementCase;
import javax.swing.KeyStroke;

public class DeleteRowAction extends CellSelectionAction
{

    public static final MenuKeySet DELETE_ROW = new MenuKeySet() {

        public char getMnemonic()
        {
            return 'D';
        }

        public String getMenuName()
        {
            return Inter.getLocText("Row");
        }

        public KeyStroke getKeyStroke()
        {
            return null;
        }

    }
;

    public DeleteRowAction(ElementCasePane elementcasepane)
    {
        this(elementcasepane, DELETE_ROW.getMenuName());
    }

    public DeleteRowAction(ElementCasePane elementcasepane, String s)
    {
        super(elementcasepane);
        setName(s);
        setMnemonic(DELETE_ROW.getMnemonic());
        setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/m_report/deleteRow.png"));
    }

    protected boolean executeActionReturnUndoRecordNeededWithCellSelection(CellSelection cellselection)
    {
        ElementCasePane elementcasepane = (ElementCasePane)getEditingComponent();
        com.fr.report.elementcase.TemplateElementCase templateelementcase = elementcasepane.getEditingElementCase();
        int ai[] = cellselection.getSelectedRows();
        for(int i = 0; i < ai.length; i++)
            templateelementcase.removeRow(ai[i] - i);

        return true;
    }

}
