// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.chart.series.SeriesCondition;

import com.fr.chart.base.AttrBorder;
import com.fr.chart.base.DataSeriesCondition;
import com.fr.design.chart.comp.BorderAttriPane;
import com.fr.design.condition.ConditionAttributesPane;
import com.fr.general.Inter;

// Referenced classes of package com.fr.design.chart.series.SeriesCondition:
//            LabelBorderAttrPane

public class LabelBorderPane extends LabelBorderAttrPane
{

    private AttrBorder attrBorder;

    public LabelBorderPane(ConditionAttributesPane conditionattributespane)
    {
        super(conditionattributespane, true, Inter.getLocText(new String[] {
            "Border", "Format"
        }));
        attrBorder = new AttrBorder();
    }

    public void populate(DataSeriesCondition dataseriescondition)
    {
        if(dataseriescondition instanceof AttrBorder)
        {
            attrBorder = (AttrBorder)dataseriescondition;
            linePane.setLineColor(attrBorder.getBorderColor());
            linePane.setLineStyle(attrBorder.getBorderStyle());
        }
    }

    public DataSeriesCondition update()
    {
        attrBorder.setBorderColor(linePane.getLineColor());
        attrBorder.setBorderStyle(linePane.getLineStyle());
        return attrBorder;
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
