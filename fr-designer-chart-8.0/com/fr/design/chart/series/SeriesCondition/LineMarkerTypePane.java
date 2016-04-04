// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.chart.series.SeriesCondition;

import com.fr.chart.base.AttrMarkerType;
import com.fr.chart.base.DataSeriesCondition;
import com.fr.chart.chartglyph.Marker;
import com.fr.chart.chartglyph.MarkerFactory;
import com.fr.design.condition.ConditionAttrSingleConditionPane;
import com.fr.design.condition.ConditionAttributesPane;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.xcombox.MarkerComboBox;
import com.fr.general.Inter;

public class LineMarkerTypePane extends ConditionAttrSingleConditionPane
{

    private UILabel nameLabel;
    private MarkerComboBox markerBox;
    private AttrMarkerType markerType;

    public LineMarkerTypePane(ConditionAttributesPane conditionattributespane)
    {
        this(conditionattributespane, true);
    }

    public LineMarkerTypePane(ConditionAttributesPane conditionattributespane, boolean flag)
    {
        super(conditionattributespane, flag);
        markerType = new AttrMarkerType();
        nameLabel = new UILabel(Inter.getLocText(new String[] {
            "ChartF-Marker", "FS_Report_Type"
        }));
        markerBox = new MarkerComboBox(MarkerFactory.getMarkerArray());
        if(flag)
            add(nameLabel);
        add(markerBox);
    }

    public String nameForPopupMenuItem()
    {
        return Inter.getLocText(new String[] {
            "ChartF-Marker", "FS_Report_Type"
        });
    }

    protected String title4PopupWindow()
    {
        return nameForPopupMenuItem();
    }

    public void populate(DataSeriesCondition dataseriescondition)
    {
        if(dataseriescondition instanceof AttrMarkerType)
        {
            markerType = (AttrMarkerType)dataseriescondition;
            markerBox.setSelectedMarker(MarkerFactory.createMarker(markerType.getMarkerType()));
        }
    }

    public DataSeriesCondition update()
    {
        markerType.setMarkerType(markerBox.getSelectedMarkder().getMarkerType());
        return markerType;
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
