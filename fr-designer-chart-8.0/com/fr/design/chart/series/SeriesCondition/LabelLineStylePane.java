// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.chart.series.SeriesCondition;

import com.fr.chart.base.AttrLineStyle;
import com.fr.chart.base.DataSeriesCondition;
import com.fr.design.condition.ConditionAttrSingleConditionPane;
import com.fr.design.condition.ConditionAttributesPane;
import com.fr.design.gui.icombobox.LineComboBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.general.Inter;
import com.fr.stable.CoreConstants;

public class LabelLineStylePane extends ConditionAttrSingleConditionPane
{

    private UILabel nameLabel;
    private LineComboBox lineCombo;
    private AttrLineStyle attrLineStyle;

    public LabelLineStylePane(ConditionAttributesPane conditionattributespane)
    {
        this(conditionattributespane, true);
    }

    public LabelLineStylePane(ConditionAttributesPane conditionattributespane, boolean flag)
    {
        super(conditionattributespane, flag);
        attrLineStyle = new AttrLineStyle();
        nameLabel = new UILabel((new StringBuilder()).append(Inter.getLocText("Line-Style")).append(":").toString());
        if(flag)
            add(nameLabel);
        lineCombo = new LineComboBox(CoreConstants.STRIKE_LINE_STYLE_ARRAY_4_CHART);
        add(lineCombo);
    }

    public String nameForPopupMenuItem()
    {
        return Inter.getLocText("Line-Style");
    }

    protected String title4PopupWindow()
    {
        return nameForPopupMenuItem();
    }

    public void populate(DataSeriesCondition dataseriescondition)
    {
        if(dataseriescondition instanceof AttrLineStyle)
        {
            attrLineStyle = (AttrLineStyle)dataseriescondition;
            int i = attrLineStyle.getLineStyle();
            if(i != 5 && i != 1 && i != 2 && i != 0)
                i = 1;
            lineCombo.setSelectedLineStyle(i);
        }
    }

    public DataSeriesCondition update()
    {
        attrLineStyle.setLineStyle(lineCombo.getSelectedLineStyle());
        return attrLineStyle;
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
