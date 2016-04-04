// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.chart.series.SeriesCondition;

import com.fr.chart.chartattr.Plot;
import com.fr.chart.chartglyph.ConditionAttr;
import com.fr.chart.chartglyph.ConditionCollection;
import com.fr.design.chart.series.SeriesCondition.impl.DataSeriesConditionPaneFactory;
import com.fr.design.gui.controlpane.*;
import com.fr.general.Inter;
import com.fr.general.NameObject;
import java.util.*;

public class DataSeriesAttrPane extends JControlPane
{

    private static final long serialVersionUID = 0x9b2c26b6a5199373L;
    private Plot plot;

    public DataSeriesAttrPane()
    {
    }

    public DataSeriesAttrPane(Plot plot1)
    {
        plot = plot1;
        initComponentPane();
    }

    public NameableCreator[] createNameableCreators()
    {
        return (new NameableCreator[] {
            new NameObjectCreator(Inter.getLocText("Condition_Attributes"), com/fr/chart/chartglyph/ConditionAttr, DataSeriesConditionPaneFactory.findConfitionPane4DataSeries(plot))
        });
    }

    protected String title4PopupWindow()
    {
        return Inter.getLocText(new String[] {
            "Condition", "Display"
        });
    }

    public void populate(Plot plot1)
    {
        ArrayList arraylist = new ArrayList();
        ConditionCollection conditioncollection = plot1.getConditionCollection();
        int i = 0;
        for(int j = conditioncollection.getConditionAttrSize(); i < j; i++)
        {
            ConditionAttr conditionattr = conditioncollection.getConditionAttr(i);
            arraylist.add(new NameObject(conditionattr.getName(), conditionattr));
        }

        if(arraylist.size() > 0)
            populate((com.fr.stable.Nameable[])arraylist.toArray(new NameObject[arraylist.size()]));
    }

    public void update(Plot plot1)
    {
        com.fr.stable.Nameable anameable[] = update();
        NameObject anameobject[] = new NameObject[anameable.length];
        Arrays.asList(anameable).toArray(anameobject);
        ConditionCollection conditioncollection = plot1.getConditionCollection();
        conditioncollection.clearConditionAttr();
        if(anameobject.length < 1)
            return;
        for(int i = 0; i < anameobject.length; i++)
        {
            NameObject nameobject = anameobject[i];
            ConditionAttr conditionattr = (ConditionAttr)nameobject.getObject();
            conditionattr.setName(nameobject.getName());
            conditioncollection.addConditionAttr(conditionattr);
        }

    }
}
