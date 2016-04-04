// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.chart.gui.type;

import com.fr.chart.chartattr.Chart;
import java.util.List;

// Referenced classes of package com.fr.design.mainframe.chart.gui.type:
//            AbstractChartTypePane, ChartImagePane

public abstract class UserDefinedChartTypePane extends AbstractChartTypePane
{

    public UserDefinedChartTypePane()
    {
    }

    protected String[] getTypeLayoutPath()
    {
        return new String[0];
    }

    protected String[] getTypeLayoutTipName()
    {
        return new String[0];
    }

    protected String[] getTypeIconPath()
    {
        return (new String[] {
            "/com/fr/design/images/chart/default.png"
        });
    }

    protected String[] getTypeTipName()
    {
        return (new String[] {
            title4PopupWindow()
        });
    }

    public void updateBean(Chart chart)
    {
    }

    public void populateBean(Chart chart)
    {
        ((ChartImagePane)typeDemo.get(0)).isPressing = true;
        checkDemosBackground();
    }

    public String title4PopupWindow()
    {
        return "";
    }

    public volatile void updateBean(Object obj)
    {
        updateBean((Chart)obj);
    }

    public volatile void populateBean(Object obj)
    {
        populateBean((Chart)obj);
    }
}
