// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.chart.series.SeriesCondition;

import com.fr.chart.base.ChartConstants;
import com.fr.design.condition.DSColumnLiteConditionPane;
import javax.swing.JPanel;

public class ChartConditionPane extends DSColumnLiteConditionPane
{

    public ChartConditionPane()
    {
        conditonTypePane.setVisible(false);
        populateColumns(columns2Populate());
    }

    public String[] columns2Populate()
    {
        return (new String[] {
            ChartConstants.CATEGORY_INDEX, ChartConstants.CATEGORY_NAME, ChartConstants.SERIES_INDEX, ChartConstants.SERIES_NAME, ChartConstants.VALUE
        });
    }
}
