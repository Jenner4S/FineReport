// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.chart;

import com.fr.base.FRContext;
import com.fr.chart.chartattr.*;
import com.fr.design.mainframe.chart.gui.style.axis.*;
import com.fr.general.ComparatorUtils;
import com.fr.general.FRLogger;
import java.util.HashMap;
import java.util.Map;

public class ChartAxisFactory
{

    private static final String PERCENT = "Percent";
    private static final String SECOND = "Second";
    private static Map map;

    private ChartAxisFactory()
    {
    }

    public static ChartAxisUsePane createAxisStylePane(Axis axis, String s)
    {
        String s1 = axis.getClass().getName();
        if(axis.isPercentage())
            s1 = (new StringBuilder()).append(s1).append("Percent").toString();
        else
        if(ComparatorUtils.equals(s, "secondAxis"))
            s1 = (new StringBuilder()).append(s1).append("Second").toString();
        Class class1 = (Class)map.get(s1);
        try
        {
            return (ChartAxisUsePane)class1.newInstance();
        }
        catch(InstantiationException instantiationexception)
        {
            FRContext.getLogger().error(instantiationexception.getMessage(), instantiationexception);
        }
        catch(IllegalAccessException illegalaccessexception)
        {
            FRContext.getLogger().error(illegalaccessexception.getMessage(), illegalaccessexception);
        }
        return new ChartCategoryPane();
    }

    static 
    {
        map = new HashMap();
        map.put(com/fr/chart/chartattr/ValueAxis.getName(), com/fr/design/mainframe/chart/gui/style/axis/ChartValuePane);
        map.put(com/fr/chart/chartattr/RadarAxis.getName(), com/fr/design/mainframe/chart/gui/style/axis/ChartRadarPane);
        map.put(com/fr/chart/chartattr/CategoryAxis.getName(), com/fr/design/mainframe/chart/gui/style/axis/ChartCategoryPane);
        map.put((new StringBuilder()).append(com/fr/chart/chartattr/ValueAxis.getName()).append("Percent").toString(), com/fr/design/mainframe/chart/gui/style/axis/ChartPercentValuePane);
        map.put((new StringBuilder()).append(com/fr/chart/chartattr/ValueAxis.getName()).append("Second").toString(), com/fr/design/mainframe/chart/gui/style/axis/ChartSecondValuePane);
    }
}
