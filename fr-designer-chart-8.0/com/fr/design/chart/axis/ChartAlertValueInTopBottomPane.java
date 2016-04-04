// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.chart.axis;

import com.fr.general.Inter;

// Referenced classes of package com.fr.design.chart.axis:
//            ChartAlertValuePane

public class ChartAlertValueInTopBottomPane extends ChartAlertValuePane
{

    public ChartAlertValueInTopBottomPane()
    {
    }

    protected String getLeftName()
    {
        return Inter.getLocText("Chart_Alert_Bottom");
    }

    protected String getRightName()
    {
        return Inter.getLocText("Chart_Alert_Top");
    }
}
