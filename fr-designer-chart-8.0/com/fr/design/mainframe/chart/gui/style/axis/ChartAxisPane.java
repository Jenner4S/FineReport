// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.chart.gui.style.axis;

import com.fr.chart.chartattr.Chart;
import com.fr.chart.chartattr.Plot;
import com.fr.chart.chartglyph.DataSheet;
import com.fr.design.chart.ChartPlotFactory;
import com.fr.design.chart.axis.AxisStyleObject;
import com.fr.design.chart.axis.ChartStyleAxisPane;
import com.fr.design.gui.frpane.AbstractAttrNoScrollPane;
import com.fr.design.gui.ibutton.UIHeadGroup;
import com.fr.design.mainframe.chart.PaneTitleConstants;
import com.fr.design.mainframe.chart.gui.ChartStylePane;
import com.fr.design.mainframe.chart.gui.style.ThirdTabPane;
import com.fr.design.mainframe.chart.gui.style.legend.AutoSelectedPane;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.general.ComparatorUtils;
import java.util.ArrayList;
import java.util.List;

// Referenced classes of package com.fr.design.mainframe.chart.gui.style.axis:
//            ChartCategoryPane, ChartAxisUsePane

public class ChartAxisPane extends ThirdTabPane
    implements AutoSelectedPane
{

    private static final long serialVersionUID = 0x5ca684bc6dab2edeL;
    private static final int PANE_WIDTH = 220;
    protected ChartAxisUsePane first;
    protected ChartAxisUsePane second;
    protected ChartAxisUsePane third;

    public ChartAxisPane(Plot plot, ChartStylePane chartstylepane)
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

    public void checkUseWithDataSheet(boolean flag)
    {
        if(first instanceof ChartCategoryPane)
            GUICoreUtils.setEnabled(first, flag);
    }

    protected int getContentPaneWidth()
    {
        return 220;
    }

    public String title4PopupWindow()
    {
        return PaneTitleConstants.CHART_STYLE_AXIS_TITLE;
    }

    protected int getOffset4TabPane()
    {
        return 3;
    }

    public void populateBean(Chart chart)
    {
        if(chart == null)
            return;
        Plot plot = chart.getPlot();
        if(plot == null)
            return;
        if(first != null)
        {
            if(first instanceof ChartCategoryPane)
                GUICoreUtils.setEnabled(first, !plot.isSupportDataSheet() || plot.getDataSheet() == null || !plot.getDataSheet().isVisible());
            first.populateBean(plot.getxAxis(), plot);
        }
        if(second != null)
            second.populateBean(plot.getyAxis(), plot);
        if(third != null)
            third.populateBean(plot.getSecondAxis(), plot);
    }

    public void updateBean(Chart chart)
    {
        if(chart == null || chart.getPlot() == null)
            return;
        Plot plot = chart.getPlot();
        if(first != null)
            first.updateBean(plot.getxAxis(), plot);
        if(second != null)
            second.updateBean(plot.getyAxis(), plot);
        if(third != null)
            third.updateBean(plot.getSecondAxis(), plot);
    }

    public void setSelectedIndex(String s)
    {
        byte byte0 = 0;
        if(ComparatorUtils.equals(s, "yAxis"))
            byte0 = 1;
        else
        if(ComparatorUtils.equals(s, "secondAxis"))
            byte0 = 2;
        if(paneList.size() > byte0 && tabPane != null)
            tabPane.setSelectedIndex(byte0);
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
