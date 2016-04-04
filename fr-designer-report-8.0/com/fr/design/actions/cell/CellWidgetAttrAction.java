// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.actions.cell;

import com.fr.base.BaseUtils;
import com.fr.design.dialog.BasicPane;
import com.fr.design.mainframe.ElementCasePane;
import com.fr.design.menu.KeySetUtils;
import com.fr.design.menu.MenuKeySet;
import com.fr.design.present.CellWriteAttrPane;
import com.fr.form.ui.Widget;
import com.fr.general.FRLogger;
import com.fr.grid.selection.CellSelection;
import com.fr.grid.selection.Selection;
import com.fr.privilege.finegrain.WidgetPrivilegeControl;
import com.fr.report.cell.TemplateCellElement;

// Referenced classes of package com.fr.design.actions.cell:
//            AbstractCellElementAction

public class CellWidgetAttrAction extends AbstractCellElementAction
{

    public CellWidgetAttrAction(ElementCasePane elementcasepane)
    {
        super(elementcasepane);
        setMenuKeySet(KeySetUtils.CELL_WIDGET_ATTR);
        setName(getMenuKeySet().getMenuKeySetName());
        setMnemonic(getMenuKeySet().getMnemonic());
        setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/m_format/modified.png"));
    }

    protected BasicPane populateBasicPane(TemplateCellElement templatecellelement)
    {
        CellWriteAttrPane cellwriteattrpane = new CellWriteAttrPane((ElementCasePane)getEditingComponent());
        cellwriteattrpane.populate(templatecellelement);
        return cellwriteattrpane;
    }

    protected void updateBasicPane(BasicPane basicpane, TemplateCellElement templatecellelement)
    {
        CellWriteAttrPane cellwriteattrpane = (CellWriteAttrPane)basicpane;
        if(templatecellelement.getWidget() == null)
        {
            cellwriteattrpane.update(templatecellelement);
            return;
        }
        try
        {
            Widget widget = (Widget)templatecellelement.getWidget().clone();
            cellwriteattrpane.update(templatecellelement);
            Widget widget1 = templatecellelement.getWidget();
            if(widget1.getClass() == widget.getClass())
                widget1.setWidgetPrivilegeControl((WidgetPrivilegeControl)widget.getWidgetPrivilegeControl().clone());
        }
        catch(Exception exception)
        {
            FRLogger.getLogger().error(exception.getMessage());
        }
    }

    protected boolean isNeedShinkToFit()
    {
        return true;
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
}
