// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.grid.selection;

import com.fr.base.BaseUtils;
import com.fr.base.FRContext;
import com.fr.design.actions.cell.CleanAuthorityAction;
import com.fr.design.actions.cell.FloatStyleAction;
import com.fr.design.actions.core.ActionUtils;
import com.fr.design.actions.edit.*;
import com.fr.design.actions.utils.DeprecatedActionManager;
import com.fr.design.cell.clipboard.*;
import com.fr.design.designer.TargetComponent;
import com.fr.design.mainframe.ElementCasePane;
import com.fr.design.menu.MenuDef;
import com.fr.design.selection.QuickEditor;
import com.fr.design.utils.DesignUtils;
import com.fr.general.*;
import com.fr.report.cell.FloatElement;
import com.fr.report.elementcase.TemplateElementCase;
import com.fr.stable.ColumnRow;
import com.fr.stable.unit.FU;
import com.fr.stable.unit.OLDPIX;
import java.awt.Toolkit;
import javax.swing.JPopupMenu;

// Referenced classes of package com.fr.grid.selection:
//            Selection, CellSelection

public class FloatSelection extends Selection
{

    private String selectedFloatName;

    public FloatSelection(String s)
    {
        setFloatName(s);
    }

    public final void setFloatName(String s)
    {
        selectedFloatName = s;
    }

    public String getSelectedFloatName()
    {
        return selectedFloatName;
    }

    public void asTransferable(ElementsTransferable elementstransferable, ElementCasePane elementcasepane)
    {
        TemplateElementCase templateelementcase = elementcasepane.getEditingElementCase();
        FloatElement floatelement = templateelementcase.getFloatElement(selectedFloatName);
        if(floatelement != null)
            try
            {
                FloatElement floatelement1 = (FloatElement)floatelement.clone();
                elementstransferable.addObject(new FloatElementsClip(floatelement1));
            }
            catch(CloneNotSupportedException clonenotsupportedexception)
            {
                FRContext.getLogger().error(clonenotsupportedexception.getMessage(), clonenotsupportedexception);
            }
    }

    public boolean pasteCellElementsClip(CellElementsClip cellelementsclip, ElementCasePane elementcasepane)
    {
        Toolkit.getDefaultToolkit().beep();
        DesignUtils.errorMessage(Inter.getLocText(new String[] {
            "Only_selected_cell_can_paste_only", "M_Insert-Cell"
        }));
        return false;
    }

    public boolean pasteString(String s, ElementCasePane elementcasepane)
    {
        Toolkit.getDefaultToolkit().beep();
        DesignUtils.errorMessage(Inter.getLocText(new String[] {
            "Only_selected_cell_can_paste_only", "Character"
        }));
        return false;
    }

    public boolean pasteOtherType(Object obj, ElementCasePane elementcasepane)
    {
        TemplateElementCase templateelementcase = elementcasepane.getEditingElementCase();
        FloatElement floatelement = templateelementcase.getFloatElement(selectedFloatName);
        if(floatelement != null)
            floatelement.setValue(obj);
        return true;
    }

    public boolean canMergeCells(ElementCasePane elementcasepane)
    {
        return false;
    }

    public boolean mergeCells(ElementCasePane elementcasepane)
    {
        throw new UnsupportedOperationException();
    }

    public boolean canUnMergeCells(ElementCasePane elementcasepane)
    {
        return false;
    }

    public boolean unMergeCells(ElementCasePane elementcasepane)
    {
        throw new UnsupportedOperationException();
    }

    public JPopupMenu createPopupMenu(ElementCasePane elementcasepane)
    {
        JPopupMenu jpopupmenu = new JPopupMenu();
        if(BaseUtils.isAuthorityEditing())
        {
            jpopupmenu.add((new CleanAuthorityAction(elementcasepane)).createMenuItem());
            return jpopupmenu;
        } else
        {
            jpopupmenu.add(DeprecatedActionManager.getCellMenu(elementcasepane).createJMenu());
            jpopupmenu.add((new FloatStyleAction(elementcasepane)).createMenuItem());
            jpopupmenu.add((new HyperlinkAction(elementcasepane)).createMenuItem());
            jpopupmenu.addSeparator();
            jpopupmenu.add((new CutAction(elementcasepane)).createMenuItem());
            jpopupmenu.add((new CopyAction(elementcasepane)).createMenuItem());
            jpopupmenu.add((new PasteAction(elementcasepane)).createMenuItem());
            jpopupmenu.add((new DeleteAction(elementcasepane)).createMenuItem());
            jpopupmenu.addSeparator();
            jpopupmenu.add(DeprecatedActionManager.getOrderMenu(elementcasepane));
            jpopupmenu.add((new EditFloatElementNameAction(elementcasepane)).createMenuItem());
            return jpopupmenu;
        }
    }

    public boolean clear(com.fr.design.mainframe.ElementCasePane.Clear clear1, ElementCasePane elementcasepane)
    {
        TemplateElementCase templateelementcase = elementcasepane.getEditingElementCase();
        FloatElement floatelement = templateelementcase.getFloatElement(selectedFloatName);
        if(floatelement != null)
        {
            templateelementcase.removeFloatElement(floatelement);
            elementcasepane.setSelection(new CellSelection(0, 0, 1, 1));
            return true;
        } else
        {
            return false;
        }
    }

    public int[] getSelectedColumns()
    {
        return new int[0];
    }

    public int[] getSelectedRows()
    {
        return new int[0];
    }

    public void moveLeft(ElementCasePane elementcasepane)
    {
        FloatElement floatelement = elementcasepane.getEditingElementCase().getFloatElement(selectedFloatName);
        if(floatelement.getLeftDistance().less_than_or_equal_zero())
            floatelement.setLeftDistance(FU.getInstance(0L));
        else
            floatelement.setLeftDistance(FU.getInstance(floatelement.getLeftDistance().toFU() - (new OLDPIX(1.0F)).toFU()));
    }

    public void moveRight(ElementCasePane elementcasepane)
    {
        FloatElement floatelement = elementcasepane.getEditingElementCase().getFloatElement(selectedFloatName);
        floatelement.setLeftDistance(FU.getInstance(floatelement.getLeftDistance().toFU() + (new OLDPIX(1.0F)).toFU()));
    }

    public void moveUp(ElementCasePane elementcasepane)
    {
        FloatElement floatelement = elementcasepane.getEditingElementCase().getFloatElement(selectedFloatName);
        if(floatelement.getTopDistance().less_than_or_equal_zero())
            floatelement.setTopDistance(FU.getInstance(0L));
        else
            floatelement.setTopDistance(FU.getInstance(floatelement.getTopDistance().toFU() - (new OLDPIX(1.0F)).toFU()));
    }

    public void moveDown(ElementCasePane elementcasepane)
    {
        FloatElement floatelement = elementcasepane.getEditingElementCase().getFloatElement(selectedFloatName);
        floatelement.setTopDistance(FU.getInstance(floatelement.getTopDistance().toFU() + (new OLDPIX(1.0F)).toFU()));
    }

    public boolean triggerDeleteAction(ElementCasePane elementcasepane)
    {
        return elementcasepane.clearAll();
    }

    public boolean containsColumnRow(ColumnRow columnrow)
    {
        return false;
    }

    public boolean isSelectedOneCell(ElementCasePane elementcasepane)
    {
        return false;
    }

    public boolean equals(Object obj)
    {
        return (obj instanceof FloatSelection) && ComparatorUtils.equals(getSelectedFloatName(), ((FloatSelection)obj).getSelectedFloatName());
    }

    public QuickEditor getQuickEditor(TargetComponent targetcomponent)
    {
        ElementCasePane elementcasepane = (ElementCasePane)targetcomponent;
        FloatElement floatelement = elementcasepane.getEditingElementCase().getFloatElement(selectedFloatName);
        Object obj = floatelement.getValue();
        obj = obj != null ? obj : "";
        obj = (obj instanceof Number) ? ((Object) (obj.toString())) : obj;
        QuickEditor quickeditor = ActionUtils.getFloatEditor(obj.getClass());
        quickeditor.populate(targetcomponent);
        return quickeditor;
    }
}
