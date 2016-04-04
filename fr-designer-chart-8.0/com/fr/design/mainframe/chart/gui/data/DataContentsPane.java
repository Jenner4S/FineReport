// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.chart.gui.data;

import com.fr.design.mainframe.chart.AbstractChartAttrPane;
import com.fr.design.mainframe.chart.PaneTitleConstants;

public abstract class DataContentsPane extends AbstractChartAttrPane
{

    public DataContentsPane()
    {
    }

    public String getIconPath()
    {
        return "com/fr/design/images/chart/ChartData.png";
    }

    public String title4PopupWindow()
    {
        return PaneTitleConstants.CHART_DATA_TITLE;
    }

    public abstract void setSupportCellData(boolean flag);
}
