// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.condition;

import com.fr.design.gui.ispinner.UIBasicSpinner;
import com.fr.general.Inter;
import com.fr.report.cell.cellattr.highlight.HighlightAction;
import com.fr.report.cell.cellattr.highlight.RowHeightHighlightAction;
import com.fr.stable.unit.UNIT;

// Referenced classes of package com.fr.design.condition:
//            WHPane, ConditionAttributesPane

public class RowHeightPane extends WHPane
{

    private UIBasicSpinner rowHeightSpinner;

    public RowHeightPane(ConditionAttributesPane conditionattributespane)
    {
        super(conditionattributespane, Inter.getLocText("Utils-Row_Height"));
    }

    protected UNIT getUnit(HighlightAction highlightaction)
    {
        return ((RowHeightHighlightAction)highlightaction).getRowHeight();
    }

    protected HighlightAction returnAction(UNIT unit)
    {
        return new RowHeightHighlightAction(unit);
    }
}
