// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.chart.gui.style.axis;

import com.fr.chart.chartattr.Axis;
import com.fr.design.mainframe.chart.gui.style.ChartAxisTitleNoFormulaPane;
import javax.swing.JPanel;

// Referenced classes of package com.fr.design.mainframe.chart.gui.style.axis:
//            ChartCategoryPane

public class ChartCategoryNoFormulaPane extends ChartCategoryPane
{

    public ChartCategoryNoFormulaPane()
    {
    }

    protected JPanel getAxisTitlePane()
    {
        return axisTitleNoFormulaPane;
    }

    protected void updateAxisTitle(Axis axis)
    {
        axisTitleNoFormulaPane.update(axis);
    }

    protected void populateAxisTitle(Axis axis)
    {
        axisTitleNoFormulaPane.populate(axis);
    }
}
