// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.actions.columnrow;

import com.fr.base.BaseUtils;
import com.fr.design.actions.cell.AbstractCellElementAction;
import com.fr.design.data.DesignTableDataManager;
import com.fr.design.dialog.BasicPane;
import com.fr.design.dscolumn.DSColumnConditionsPane;
import com.fr.design.mainframe.ElementCasePane;
import com.fr.general.Inter;
import com.fr.report.cell.TemplateCellElement;

public class DSColumnConditionAction extends AbstractCellElementAction
{

    private boolean returnValue;
    private TemplateCellElement editCellElement;

    public DSColumnConditionAction(ElementCasePane elementcasepane)
    {
        super(elementcasepane);
        returnValue = false;
        setName(Inter.getLocText("Filter"));
        setMnemonic('E');
        setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/expand/cellAttr.gif"));
    }

    protected BasicPane populateBasicPane(TemplateCellElement templatecellelement)
    {
        DSColumnConditionsPane dscolumnconditionspane = new DSColumnConditionsPane();
        dscolumnconditionspane.populate(DesignTableDataManager.getEditingTableDataSource(), templatecellelement);
        return dscolumnconditionspane;
    }

    protected void updateBasicPane(BasicPane basicpane, TemplateCellElement templatecellelement)
    {
        ((DSColumnConditionsPane)basicpane).update(templatecellelement);
    }
}
