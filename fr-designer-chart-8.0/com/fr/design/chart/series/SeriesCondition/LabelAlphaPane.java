// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.chart.series.SeriesCondition;

import com.fr.chart.base.AttrAlpha;
import com.fr.chart.base.DataSeriesCondition;
import com.fr.design.condition.ConditionAttrSingleConditionPane;
import com.fr.design.condition.ConditionAttributesPane;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.style.AlphaPane;
import com.fr.general.Inter;

public class LabelAlphaPane extends ConditionAttrSingleConditionPane
{

    private static final int ALPHASIZE = 100;
    private UILabel nameLabel;
    private AlphaPane alphaPane;
    private AttrAlpha attrAlpha;

    public LabelAlphaPane(ConditionAttributesPane conditionattributespane)
    {
        super(conditionattributespane, true);
        attrAlpha = new AttrAlpha();
        nameLabel = new UILabel(Inter.getLocText("ChartF-Alpha"));
        alphaPane = new AlphaPane();
        add(nameLabel);
        add(alphaPane);
    }

    public String nameForPopupMenuItem()
    {
        return Inter.getLocText("ChartF-Alpha");
    }

    protected String title4PopupWindow()
    {
        return nameForPopupMenuItem();
    }

    public void populate(DataSeriesCondition dataseriescondition)
    {
        if(dataseriescondition instanceof AttrAlpha)
        {
            attrAlpha = (AttrAlpha)dataseriescondition;
            alphaPane.populate((int)(attrAlpha.getAlpha() * 100F));
        }
    }

    public DataSeriesCondition update()
    {
        attrAlpha.setAlpha(alphaPane.update());
        return attrAlpha;
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
