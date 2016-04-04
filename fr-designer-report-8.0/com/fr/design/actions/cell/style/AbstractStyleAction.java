// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.actions.cell.style;

import com.fr.base.Style;
import com.fr.base.chart.BaseChartCollection;
import com.fr.design.actions.ElementCaseAction;
import com.fr.design.actions.utils.ReportActionUtils;
import com.fr.design.mainframe.ElementCasePane;
import com.fr.grid.selection.*;
import com.fr.report.cell.FloatElement;
import com.fr.report.elementcase.TemplateElementCase;

// Referenced classes of package com.fr.design.actions.cell.style:
//            StyleActionInterface

public abstract class AbstractStyleAction extends ElementCaseAction
    implements StyleActionInterface
{

    protected AbstractStyleAction(ElementCasePane elementcasepane)
    {
        super(elementcasepane);
    }

    public abstract Style executeStyle(Style style, Style style1);

    public boolean executeActionReturnUndoRecordNeeded()
    {
        ElementCasePane elementcasepane = (ElementCasePane)getEditingComponent();
        if(elementcasepane == null)
            return false;
        else
            return ReportActionUtils.executeAction(this, elementcasepane);
    }

    public void updateStyle(Style style)
    {
    }

    public boolean isFontStye()
    {
        return true;
    }

    public void update()
    {
        setEnabled(true);
        ElementCasePane elementcasepane = (ElementCasePane)getEditingComponent();
        if(elementcasepane == null)
        {
            setEnabled(false);
            return;
        }
        if(isFontStye())
        {
            Selection selection = elementcasepane.getSelection();
            TemplateElementCase templateelementcase = elementcasepane.getEditingElementCase();
            if(templateelementcase != null && (selection instanceof FloatSelection))
            {
                FloatElement floatelement = templateelementcase.getFloatElement(((FloatSelection)selection).getSelectedFloatName());
                if(floatelement == null)
                {
                    elementcasepane.setSelection(new CellSelection());
                    return;
                }
                Object obj = floatelement.getValue();
                if(obj instanceof BaseChartCollection)
                {
                    setEnabled(false);
                    return;
                }
            }
        }
        updateStyle(ReportActionUtils.getCurrentStyle(elementcasepane));
    }
}
