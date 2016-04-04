// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.chart;

import com.fr.chart.chartattr.Chart;
import com.fr.chart.chartattr.ChartCollection;

// Referenced classes of package com.fr.design.chart:
//            ChartWizardPane

public abstract class ChartCommonWizardPane extends ChartWizardPane
{

    private static final long serialVersionUID = 0x223ffbad171644c2L;

    public ChartCommonWizardPane()
    {
    }

    public void populate(ChartCollection chartcollection)
    {
        if(chartcollection == null)
        {
            return;
        } else
        {
            populate(chartcollection.getSelectedChart());
            return;
        }
    }

    public abstract void populate(Chart chart);

    public abstract void update(Chart chart);
}
