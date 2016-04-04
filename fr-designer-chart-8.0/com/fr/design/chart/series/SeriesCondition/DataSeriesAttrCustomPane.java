// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.chart.series.SeriesCondition;

import com.fr.chart.chartglyph.CustomAttr;
import com.fr.design.gui.controlpane.NameObjectCreator;
import com.fr.design.gui.controlpane.NameableCreator;
import com.fr.general.Inter;
import java.awt.Dimension;

// Referenced classes of package com.fr.design.chart.series.SeriesCondition:
//            DataSeriesAttrPane, DataSeriesCustomConditionPane

public class DataSeriesAttrCustomPane extends DataSeriesAttrPane
{

    private static final long serialVersionUID = 0x827614e315663774L;

    public DataSeriesAttrCustomPane()
    {
        setPreferredSize(new Dimension(640, 450));
    }

    public NameableCreator[] createNameableCreators()
    {
        return (new NameableCreator[] {
            new NameObjectCreator(Inter.getLocText("Condition_Attributes"), com/fr/chart/chartglyph/CustomAttr, com/fr/design/chart/series/SeriesCondition/DataSeriesCustomConditionPane)
        });
    }
}
