// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.chart.series.SeriesCondition;

import com.fr.chart.base.AttrTrendLine;
import com.fr.chart.base.ConditionTrendLine;
import com.fr.design.gui.controlpane.*;
import com.fr.general.Inter;
import com.fr.general.NameObject;
import java.util.*;

// Referenced classes of package com.fr.design.chart.series.SeriesCondition:
//            ConditionTrendLinePane

public class TrendLineControlPane extends JControlPane
{

    public TrendLineControlPane()
    {
    }

    public NameableCreator[] createNameableCreators()
    {
        return (new NameableCreator[] {
            new NameObjectCreator(Inter.getLocText("Chart_TrendLine"), com/fr/chart/base/ConditionTrendLine, com/fr/design/chart/series/SeriesCondition/ConditionTrendLinePane)
        });
    }

    protected String title4PopupWindow()
    {
        return Inter.getLocText(new String[] {
            "Edit", "Chart_TrendLine"
        });
    }

    public void populate(AttrTrendLine attrtrendline)
    {
        ArrayList arraylist = new ArrayList();
        for(int i = 0; i < attrtrendline.size(); i++)
        {
            ConditionTrendLine conditiontrendline = attrtrendline.get(i);
            arraylist.add(new NameObject(conditiontrendline.getPaneName(), conditiontrendline));
        }

        if(arraylist.size() > 0)
            populate((com.fr.stable.Nameable[])arraylist.toArray(new NameObject[arraylist.size()]));
    }

    public void update(AttrTrendLine attrtrendline)
    {
        com.fr.stable.Nameable anameable[] = update();
        NameObject anameobject[] = new NameObject[anameable.length];
        Arrays.asList(anameable).toArray(anameobject);
        attrtrendline.clear();
        if(anameobject.length < 1)
            return;
        for(int i = 0; i < anameobject.length; i++)
        {
            NameObject nameobject = anameobject[i];
            ConditionTrendLine conditiontrendline = (ConditionTrendLine)nameobject.getObject();
            conditiontrendline.setPaneName(nameobject.getName());
            attrtrendline.add(conditiontrendline);
        }

    }
}
