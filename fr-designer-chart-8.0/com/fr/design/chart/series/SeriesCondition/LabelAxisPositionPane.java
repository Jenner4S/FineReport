// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.chart.series.SeriesCondition;

import com.fr.chart.base.*;
import com.fr.design.condition.ConditionAttrSingleConditionPane;
import com.fr.design.condition.ConditionAttributesPane;
import com.fr.design.gui.ibutton.UIButtonGroup;
import com.fr.design.gui.ilable.UILabel;
import com.fr.general.ComparatorUtils;
import com.fr.general.Inter;

public class LabelAxisPositionPane extends ConditionAttrSingleConditionPane
{

    private UILabel nameLabel;
    private AttrAxisPosition axisPosition;
    private UIButtonGroup positionGroup;

    public LabelAxisPositionPane(ConditionAttributesPane conditionattributespane)
    {
        this(conditionattributespane, true);
    }

    public LabelAxisPositionPane(ConditionAttributesPane conditionattributespane, boolean flag)
    {
        super(conditionattributespane, flag);
        axisPosition = new AttrAxisPosition();
        nameLabel = new UILabel(Inter.getLocText(new String[] {
            "ChartF-Axis", "Selection"
        }));
        if(flag)
            add(nameLabel);
        String as[] = {
            Inter.getLocText("ChartF-MainAxis"), Inter.getLocText("ChartF-SecondAxis")
        };
        String as1[] = {
            ChartAxisPosition.AXIS_LEFT.getAxisPosition(), ChartAxisPosition.AXIS_RIGHT.getAxisPosition()
        };
        positionGroup = new UIButtonGroup(as, as1);
        positionGroup.setAllToolTips(as);
        add(positionGroup);
        positionGroup.setSelectedItem(ChartAxisPosition.AXIS_LEFT.getAxisPosition());
    }

    public String nameForPopupMenuItem()
    {
        return Inter.getLocText(new String[] {
            "Owner", "time(s)", "ChartF-Axis"
        });
    }

    protected String title4PopupWindow()
    {
        return nameForPopupMenuItem();
    }

    public void populate(DataSeriesCondition dataseriescondition)
    {
        if(dataseriescondition instanceof AttrAxisPosition)
        {
            axisPosition = (AttrAxisPosition)dataseriescondition;
            if(ComparatorUtils.equals(axisPosition.getAxisPosition(), ChartAxisPosition.AXIS_LEFT))
                positionGroup.setSelectedItem(ChartAxisPosition.AXIS_LEFT.getAxisPosition());
            else
                positionGroup.setSelectedItem(ChartAxisPosition.AXIS_RIGHT.getAxisPosition());
        }
    }

    public DataSeriesCondition update()
    {
        axisPosition.setAxisPosition(ChartAxisPosition.parse((String)positionGroup.getSelectedItem()));
        return axisPosition;
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
