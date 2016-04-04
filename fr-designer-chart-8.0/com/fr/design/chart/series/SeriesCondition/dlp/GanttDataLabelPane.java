// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.chart.series.SeriesCondition.dlp;

import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.general.Inter;
import java.awt.Component;

// Referenced classes of package com.fr.design.chart.series.SeriesCondition.dlp:
//            DataLabelPane

public class GanttDataLabelPane extends DataLabelPane
{

    private static final long serialVersionUID = 0x4b13a227a428efd5L;

    public GanttDataLabelPane()
    {
    }

    protected Component[] createComponents4ShowCategoryName()
    {
        if(showCategoryNameCB == null)
            showCategoryNameCB = new UICheckBox(Inter.getLocText("Chart_Step_Name"));
        return (new Component[] {
            null, showCategoryNameCB
        });
    }

    protected Component[] createComponents4Value()
    {
        return new Component[0];
    }
}
