// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.actions.cell.style;

import com.fr.base.BaseUtils;
import com.fr.base.Style;
import com.fr.base.chart.BaseChartCollection;
import com.fr.design.actions.ButtonGroupAction;
import com.fr.design.actions.utils.ReportActionUtils;
import com.fr.design.gui.ibutton.UIButtonGroup;
import com.fr.design.gui.ibutton.UIToggleButton;
import com.fr.design.mainframe.ElementCasePane;
import com.fr.general.Inter;
import com.fr.grid.selection.FloatSelection;
import com.fr.grid.selection.Selection;
import com.fr.report.cell.FloatElement;
import com.fr.report.elementcase.TemplateElementCase;
import javax.swing.Icon;
import javax.swing.JComponent;

// Referenced classes of package com.fr.design.actions.cell.style:
//            StyleActionInterface

public class AlignmentAction extends ButtonGroupAction
    implements StyleActionInterface
{

    public AlignmentAction(ElementCasePane elementcasepane, Icon aicon[], Integer ainteger[])
    {
        super(elementcasepane, aicon, ainteger);
    }

    public Style executeStyle(Style style, Style style1)
    {
        return style.deriveHorizontalAlignment(getSelectedValue().intValue());
    }

    public void updateStyle(Style style)
    {
        setSelectedIndex(BaseUtils.getAlignment4Horizontal(style));
    }

    public boolean executeActionReturnUndoRecordNeeded()
    {
        ElementCasePane elementcasepane = (ElementCasePane)getEditingComponent();
        if(elementcasepane == null)
            return false;
        else
            return ReportActionUtils.executeAction(this, elementcasepane);
    }

    public void update()
    {
        super.update();
        if(!isEnabled())
            return;
        ElementCasePane elementcasepane = (ElementCasePane)getEditingComponent();
        if(elementcasepane == null)
        {
            setEnabled(false);
            return;
        }
        Selection selection = elementcasepane.getSelection();
        TemplateElementCase templateelementcase = elementcasepane.getEditingElementCase();
        if(templateelementcase != null && (selection instanceof FloatSelection))
        {
            FloatElement floatelement = templateelementcase.getFloatElement(((FloatSelection)selection).getSelectedFloatName());
            Object obj = floatelement.getValue();
            if(obj instanceof BaseChartCollection)
            {
                setEnabled(false);
                return;
            }
        }
        updateStyle(ReportActionUtils.getCurrentStyle(elementcasepane));
    }

    public UIButtonGroup createToolBarComponent()
    {
        UIButtonGroup uibuttongroup = super.createToolBarComponent();
        if(uibuttongroup != null)
        {
            uibuttongroup.setForToolBarButtonGroup(true);
            uibuttongroup.setAllToolTips(new String[] {
                Inter.getLocText("StyleAlignment-Left"), Inter.getLocText("Center"), Inter.getLocText("StyleAlignment-Right")
            });
        }
        for(int i = 0; i < 3; i++)
        {
            uibuttongroup.getButton(i).setRoundBorder(true, 0);
            uibuttongroup.getButton(i).setBorderPainted(true);
        }

        return uibuttongroup;
    }

    public volatile JComponent createToolBarComponent()
    {
        return createToolBarComponent();
    }
}
