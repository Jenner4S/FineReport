// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.condition;

import com.fr.general.Inter;
import com.fr.report.cell.cellattr.highlight.ColWidthHighlightAction;
import com.fr.report.cell.cellattr.highlight.HighlightAction;
import com.fr.stable.unit.UNIT;

// Referenced classes of package com.fr.design.condition:
//            WHPane, ConditionAttributesPane

public class ColumnWidthPane extends WHPane
{

    public ColumnWidthPane(ConditionAttributesPane conditionattributespane)
    {
        super(conditionattributespane, Inter.getLocText("Utils-Column_Width"));
    }

    protected UNIT getUnit(HighlightAction highlightaction)
    {
        return ((ColWidthHighlightAction)highlightaction).getColumnWidth();
    }

    protected HighlightAction returnAction(UNIT unit)
    {
        return new ColWidthHighlightAction(unit);
    }
}
