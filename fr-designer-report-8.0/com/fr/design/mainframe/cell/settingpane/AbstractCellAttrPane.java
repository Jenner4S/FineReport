// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.cell.settingpane;

import com.fr.design.mainframe.AbstractAttrPane;
import com.fr.design.mainframe.ElementCasePane;
import com.fr.grid.selection.CellSelection;
import com.fr.grid.selection.FloatSelection;
import com.fr.report.cell.TemplateCellElement;
import com.fr.report.elementcase.TemplateElementCase;

public abstract class AbstractCellAttrPane extends AbstractAttrPane
{

    protected TemplateCellElement cellElement;
    protected ElementCasePane elementCasePane;
    protected CellSelection cs;

    public AbstractCellAttrPane()
    {
    }

    protected abstract void populateBean();

    public abstract void updateBeans();

    public abstract void updateBean(TemplateCellElement templatecellelement);

    public void populateBean(TemplateCellElement templatecellelement, ElementCasePane elementcasepane)
    {
        if(elementcasepane == null || templatecellelement == null)
            return;
        removeAttributeChangeListener();
        cellElement = templatecellelement;
        elementCasePane = elementcasepane;
        if(elementCasePane.getSelection() instanceof FloatSelection)
        {
            return;
        } else
        {
            cs = (CellSelection)elementCasePane.getSelection();
            populateBean();
            return;
        }
    }

    public void updateBean()
    {
        updateBean(cellElement);
        TemplateElementCase templateelementcase = elementCasePane.getEditingElementCase();
        templateelementcase.addCellElement(cellElement);
    }
}
