// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.chart.gui;

import com.fr.base.chart.Glyph;
import com.fr.chart.chartglyph.*;
import com.fr.design.chart.gui.active.ActiveGlyph;
import com.fr.design.chart.gui.active.AlertValueActiveGlyph;
import com.fr.design.chart.gui.active.CategoryAxisActiveGlyph;
import com.fr.design.chart.gui.active.ChartActiveGlyph;
import com.fr.design.chart.gui.active.DataLabelActiveGlyph;
import com.fr.design.chart.gui.active.DataSeriesActiveGlyph;
import com.fr.design.chart.gui.active.DataSheetActiveGlyph;
import com.fr.design.chart.gui.active.DateAxisActiveGlyph;
import com.fr.design.chart.gui.active.LegendActiveGlyph;
import com.fr.design.chart.gui.active.PlotActiveGlyph;
import com.fr.design.chart.gui.active.RadarAxisActiveGlyph;
import com.fr.design.chart.gui.active.RangeAxisActiveGlyph;
import com.fr.design.chart.gui.active.TextActiveGlyph;
import com.fr.design.chart.gui.active.TrendLineActiveGlyph;
import com.fr.design.chart.gui.active.ValueAxisActiveGlyph;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

// Referenced classes of package com.fr.design.chart.gui:
//            ChartComponent

public class ActiveGlyphFactory
{

    private static Map glyphMap;

    private ActiveGlyphFactory()
    {
    }

    public static ActiveGlyph createActiveGlyph(ChartComponent chartcomponent, Object obj)
    {
        return createActiveGlyph(chartcomponent, obj, null);
    }

    public static ActiveGlyph createActiveGlyph(ChartComponent chartcomponent, Object obj, Glyph glyph)
    {
        if(obj == null)
            return null;
        String s = obj.getClass().getName();
        Object obj1 = (Class)glyphMap.get(s);
        Object obj2 = obj.getClass();
        if(obj1 == null)
            if(s.endsWith("PlotGlyph"))
            {
                obj1 = com/fr/design/chart/gui/active/PlotActiveGlyph;
                obj2 = com/fr/chart/chartglyph/PlotGlyph;
            } else
            if(s.endsWith("DataSeries4Area"))
            {
                obj1 = com/fr/design/chart/gui/active/DataSeriesActiveGlyph;
                obj2 = com/fr/chart/chartglyph/DataSeries;
            } else
            {
                obj1 = com/fr/design/chart/gui/active/ChartActiveGlyph;
                obj2 = com/fr/chart/chartglyph/ChartGlyph;
            }
        try
        {
            Class aclass[] = {
                com/fr/design/chart/gui/ChartComponent, obj2, com/fr/base/chart/Glyph
            };
            return (ActiveGlyph)((Class) (obj1)).getConstructor(aclass).newInstance(new Object[] {
                chartcomponent, obj, glyph
            });
        }
        catch(Exception exception)
        {
            return null;
        }
    }

    static 
    {
        glyphMap = new HashMap();
        glyphMap.put(com/fr/chart/chartglyph/DataSeries.getName(), com/fr/design/chart/gui/active/DataSeriesActiveGlyph);
        glyphMap.put(com/fr/chart/chartglyph/RadarAxisGlyph.getName(), com/fr/design/chart/gui/active/RadarAxisActiveGlyph);
        glyphMap.put(com/fr/chart/chartglyph/RangeAxisGlyph.getName(), com/fr/design/chart/gui/active/RangeAxisActiveGlyph);
        glyphMap.put(com/fr/chart/chartglyph/TitleGlyph.getName(), com/fr/design/chart/gui/active/TextActiveGlyph);
        glyphMap.put(com/fr/chart/chartglyph/DateAxisGlyph.getName(), com/fr/design/chart/gui/active/DateAxisActiveGlyph);
        glyphMap.put(com/fr/chart/chartglyph/ValueAxisGlyph.getName(), com/fr/design/chart/gui/active/ValueAxisActiveGlyph);
        glyphMap.put(com/fr/chart/chartglyph/CategoryAxisGlyph.getName(), com/fr/design/chart/gui/active/CategoryAxisActiveGlyph);
        glyphMap.put(com/fr/chart/chartglyph/ChartGlyph.getName(), com/fr/design/chart/gui/active/ChartActiveGlyph);
        glyphMap.put(com/fr/chart/chartglyph/DataSheetGlyph.getName(), com/fr/design/chart/gui/active/DataSheetActiveGlyph);
        glyphMap.put(com/fr/chart/chartglyph/LegendGlyph.getName(), com/fr/design/chart/gui/active/LegendActiveGlyph);
        glyphMap.put(com/fr/chart/chartglyph/TextGlyph.getName(), com/fr/design/chart/gui/active/DataLabelActiveGlyph);
        glyphMap.put(com/fr/chart/chartglyph/TrendLineGlyph.getName(), com/fr/design/chart/gui/active/TrendLineActiveGlyph);
        glyphMap.put(com/fr/chart/chartglyph/ChartAlertValueGlyph.getName(), com/fr/design/chart/gui/active/AlertValueActiveGlyph);
    }
}
