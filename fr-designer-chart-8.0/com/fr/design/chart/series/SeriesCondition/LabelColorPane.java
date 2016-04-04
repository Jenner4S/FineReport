// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.chart.series.SeriesCondition;

import com.fr.chart.base.AttrColor;
import com.fr.chart.base.DataSeriesCondition;
import com.fr.design.condition.ConditionAttrSingleConditionPane;
import com.fr.design.condition.ConditionAttributesPane;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.style.color.ColorSelectBox;
import com.fr.general.Inter;

public class LabelColorPane extends ConditionAttrSingleConditionPane
{

    protected UILabel nameLabel;
    private ColorSelectBox colorSelectionPane;
    private AttrColor attrColor;

    public LabelColorPane(ConditionAttributesPane conditionattributespane)
    {
        this(conditionattributespane, true);
    }

    public LabelColorPane(ConditionAttributesPane conditionattributespane, boolean flag)
    {
        super(conditionattributespane, flag);
        attrColor = new AttrColor();
        nameLabel = new UILabel(Inter.getLocText("ChartF-Background_Color"));
        colorSelectionPane = new ColorSelectBox(80);
        if(flag)
            add(nameLabel);
        add(colorSelectionPane);
    }

    public String nameForPopupMenuItem()
    {
        return Inter.getLocText("Color");
    }

    protected String title4PopupWindow()
    {
        return nameForPopupMenuItem();
    }

    public void populate(DataSeriesCondition dataseriescondition)
    {
        if(dataseriescondition instanceof AttrColor)
        {
            attrColor = (AttrColor)dataseriescondition;
            colorSelectionPane.setSelectObject(attrColor.getSeriesColor());
        }
    }

    public DataSeriesCondition update()
    {
        attrColor.setSeriesColor(colorSelectionPane.getSelectObject());
        return attrColor;
    }

    public volatile Object update()
    {
        return update();
    }

    public volatile void populate(Object obj)
    {
        populate((DataSeriesCondition)obj);
    }
}
