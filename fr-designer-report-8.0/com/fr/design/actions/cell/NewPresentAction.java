// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.actions.cell;

import com.fr.base.present.Present;
import com.fr.design.actions.PresentCheckBoxAction;
import com.fr.design.mainframe.CellElementPropertyPane;
import com.fr.design.mainframe.ElementCasePane;
import com.fr.general.*;
import com.fr.grid.selection.CellSelection;
import com.fr.grid.selection.Selection;
import com.fr.report.cell.TemplateCellElement;
import com.fr.report.elementcase.TemplateElementCase;
import com.fr.stable.StableUtils;

public class NewPresentAction extends PresentCheckBoxAction
{

    private String itemName;

    public NewPresentAction(ElementCasePane elementcasepane, String s, String s1)
    {
        super(elementcasepane);
        itemName = null;
        setName(s);
        itemName = s1;
    }

    public boolean executeActionReturnUndoRecordNeeded()
    {
        if(!ComparatorUtils.equals(itemName, "NOPRESENT"))
        {
            CellElementPropertyPane.getInstance().GoToPane(new String[] {
                Inter.getLocText("Present"), itemName
            });
        } else
        {
            TemplateCellElement templatecellelement = getSelectedCellElement();
            if(templatecellelement != null && templatecellelement.getPresent() != null)
            {
                templatecellelement.setPresent(null);
                return true;
            } else
            {
                return false;
            }
        }
        return false;
    }

    public boolean isSelected()
    {
        return hasCurrentPresentSet();
    }

    private TemplateCellElement getSelectedCellElement()
    {
        ElementCasePane elementcasepane = (ElementCasePane)getEditingComponent();
        Selection selection = elementcasepane.getSelection();
        if(selection instanceof CellSelection)
            return (TemplateCellElement)elementcasepane.getEditingElementCase().getCellElement(((CellSelection)selection).getColumn(), ((CellSelection)selection).getRow());
        else
            return null;
    }

    public void update()
    {
        ElementCasePane elementcasepane = (ElementCasePane)getEditingComponent();
        Selection selection = elementcasepane.getSelection();
        if(selection instanceof CellSelection)
            setEnabled(true);
        else
            setEnabled(false);
    }

    public boolean hasCurrentPresentSet()
    {
        TemplateCellElement templatecellelement = getSelectedCellElement();
        if(templatecellelement != null)
        {
            Present present = templatecellelement.getPresent();
            try
            {
                Class class1 = GeneralUtils.classForName(itemName);
                if(itemName.equals(present.getClass().getName()))
                    return StableUtils.classInstanceOf(present.getClass(), class1);
            }
            catch(Exception exception)
            {
                return "NOPRESENT".equals(itemName) && present == null;
            }
        }
        return false;
    }
}
