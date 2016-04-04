// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.chart.series.SeriesCondition;

import com.fr.design.chart.comp.BorderAttriPane;
import com.fr.design.condition.ConditionAttrSingleConditionPane;
import com.fr.design.condition.ConditionAttributesPane;
import com.fr.design.gui.ilable.UILabel;

public abstract class LabelBorderAttrPane extends ConditionAttrSingleConditionPane
{

    protected UILabel nameLabel;
    protected BorderAttriPane linePane;
    private String labelName;

    public LabelBorderAttrPane(ConditionAttributesPane conditionattributespane)
    {
        this(conditionattributespane, false, "");
    }

    public LabelBorderAttrPane(ConditionAttributesPane conditionattributespane, boolean flag, String s)
    {
        super(conditionattributespane, flag);
        nameLabel = new UILabel(s);
        linePane = new BorderAttriPane();
        if(flag)
            add(nameLabel);
        add(linePane);
        labelName = s;
    }

    public String nameForPopupMenuItem()
    {
        return labelName;
    }
}
