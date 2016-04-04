// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.actions.columnrow;

import com.fr.base.BaseUtils;
import com.fr.design.actions.cell.AbstractCellElementAction;
import com.fr.design.dialog.BasicPane;
import com.fr.design.dscolumn.DSColumnAdvancedPane;
import com.fr.design.mainframe.ElementCasePane;
import com.fr.general.Inter;
import com.fr.report.cell.TemplateCellElement;

public class DSColumnAdvancedAction extends AbstractCellElementAction
{

    private boolean returnValue;
    private TemplateCellElement editCellElement;

    public DSColumnAdvancedAction(ElementCasePane elementcasepane)
    {
        super(elementcasepane);
        returnValue = false;
        setName(Inter.getLocText("Advanced"));
        setMnemonic('A');
        setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/expand/cellAttr.gif"));
    }

    protected BasicPane populateBasicPane(TemplateCellElement templatecellelement)
    {
        editCellElement = templatecellelement;
        DSColumnAdvancedPane dscolumnadvancedpane = new DSColumnAdvancedPane();
        dscolumnadvancedpane.populate(templatecellelement);
        return dscolumnadvancedpane;
    }

    protected void updateBasicPane(BasicPane basicpane, TemplateCellElement templatecellelement)
    {
        ((DSColumnAdvancedPane)basicpane).update(editCellElement);
    }
}
