// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.actions.cell;

import com.fr.base.BaseUtils;
import com.fr.base.CellBorderStyle;
import com.fr.design.actions.ElementCaseAction;
import com.fr.design.actions.core.ActionUtils;
import com.fr.design.mainframe.ElementCasePane;
import com.fr.design.style.BorderUtils;
import com.fr.general.Inter;
import javax.swing.JComponent;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

// Referenced classes of package com.fr.design.actions.cell:
//            UIToolbarBorderButton

public class BorderAction extends ElementCaseAction
    implements ChangeListener
{

    private CellBorderStyle oldCellBorderStyle;

    public BorderAction(ElementCasePane elementcasepane)
    {
        super(elementcasepane);
        setName(Inter.getLocText("M_Format_A-Border"));
    }

    public void stateChanged(ChangeEvent changeevent)
    {
        actionPerformedUndoable();
    }

    public boolean executeActionReturnUndoRecordNeeded()
    {
        Object obj = getValue(com/fr/design/actions/cell/UIToolbarBorderButton.getName());
        ElementCasePane elementcasepane = (ElementCasePane)getEditingComponent();
        if(elementcasepane == null)
            return false;
        if(obj != null && (obj instanceof UIToolbarBorderButton))
        {
            oldCellBorderStyle = ((UIToolbarBorderButton)obj).getCellBorderStyle();
            return update(elementcasepane);
        } else
        {
            return false;
        }
    }

    public JComponent createToolBarComponent()
    {
        Object obj = getValue(com/fr/design/actions/cell/UIToolbarBorderButton.getName());
        if(obj == null || !(obj instanceof UIToolbarBorderButton))
        {
            UIToolbarBorderButton uitoolbarborderbutton = new UIToolbarBorderButton(BaseUtils.readIcon("/com/fr/design/images/m_format/noboder.png"), (ElementCasePane)getEditingComponent());
            putValue(com/fr/design/actions/cell/UIToolbarBorderButton.getName(), uitoolbarborderbutton);
            uitoolbarborderbutton.setEnabled(isEnabled());
            uitoolbarborderbutton.set4Toolbar();
            uitoolbarborderbutton.setToolTipText(ActionUtils.createButtonToolTipText(this));
            uitoolbarborderbutton.setCellBorderStyle(new CellBorderStyle());
            uitoolbarborderbutton.addStyleChangeListener(this);
            return uitoolbarborderbutton;
        } else
        {
            return (JComponent)obj;
        }
    }

    public boolean update(ElementCasePane elementcasepane)
    {
        if(oldCellBorderStyle.isNoneBorderStyle())
            return BorderUtils.updateCellBorderStyle(elementcasepane, oldCellBorderStyle);
        else
            return BorderUtils.update(elementcasepane, oldCellBorderStyle);
    }

    public void update()
    {
        setEnabled(true);
        ElementCasePane elementcasepane = (ElementCasePane)getEditingComponent();
        if(elementcasepane == null)
        {
            setEnabled(false);
            return;
        } else
        {
            return;
        }
    }
}
