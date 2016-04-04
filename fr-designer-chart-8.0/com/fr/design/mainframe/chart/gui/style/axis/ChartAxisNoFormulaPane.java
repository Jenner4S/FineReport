// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.chart.gui.style.axis;

import com.fr.chart.chartattr.Plot;
import com.fr.design.chart.ChartPlotFactory;
import com.fr.design.chart.axis.AxisStyleObject;
import com.fr.design.chart.axis.ChartStyleAxisPane;
import com.fr.design.gui.frpane.AbstractAttrNoScrollPane;
import com.fr.design.mainframe.chart.gui.ChartStylePane;
import com.fr.design.mainframe.chart.gui.style.ThirdTabPane;
import java.util.ArrayList;
import java.util.List;

// Referenced classes of package com.fr.design.mainframe.chart.gui.style.axis:
//            ChartAxisPane, ChartAxisUsePane

public class ChartAxisNoFormulaPane extends ChartAxisPane
{

    public ChartAxisNoFormulaPane(Plot plot, ChartStylePane chartstylepane)
    {
        super(plot, chartstylepane);
    }

    protected List initPaneList(Plot plot, AbstractAttrNoScrollPane abstractattrnoscrollpane)
    {
        ArrayList arraylist = new ArrayList();
        ChartStyleAxisPane chartstyleaxispane = ChartPlotFactory.createChartStyleAxisPaneByPlot(plot);
        AxisStyleObject aaxisstyleobject[] = chartstyleaxispane.createAxisStyleObjects(plot);
        for(int i = 0; i < aaxisstyleobject.length; i++)
        {
            ChartAxisUsePane chartaxisusepane = aaxisstyleobject[i].getAxisStylePane();
            chartaxisusepane = ChartPlotFactory.getNoFormulaPane(chartaxisusepane);
            if(i == 0)
                first = chartaxisusepane;
            else
            if(i == 1)
                second = chartaxisusepane;
            else
            if(i == 2)
                third = chartaxisusepane;
            arraylist.add(new com.fr.design.mainframe.chart.gui.style.ThirdTabPane.NamePane(aaxisstyleobject[i].getName(), chartaxisusepane));
        }

        return arraylist;
    }
}
