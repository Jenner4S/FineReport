// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.chart.gui.style.area;

import com.fr.chart.chartattr.Chart;
import com.fr.chart.chartattr.Plot;
import com.fr.design.file.HistoryTemplateListPane;
import com.fr.design.gui.frpane.AbstractAttrNoScrollPane;
import com.fr.design.gui.ibutton.UIHeadGroup;
import com.fr.design.mainframe.JTemplate;
import com.fr.design.mainframe.chart.PaneTitleConstants;
import com.fr.design.mainframe.chart.gui.ChartStylePane;
import com.fr.design.mainframe.chart.gui.style.ThirdTabPane;
import com.fr.design.mainframe.chart.gui.style.legend.AutoSelectedPane;
import com.fr.general.ComparatorUtils;
import java.util.ArrayList;
import java.util.List;

// Referenced classes of package com.fr.design.mainframe.chart.gui.style.area:
//            ChartWholeAreaPane, ChartPlotAreaPane

public class ChartAreaPane extends ThirdTabPane
    implements AutoSelectedPane
{

    private static final long serialVersionUID = 0xcef8ac397f73c00eL;
    private static final int PRE_WIDTH = 220;
    private ChartWholeAreaPane areaPane;
    private ChartPlotAreaPane plotPane;

    public ChartAreaPane(Plot plot, ChartStylePane chartstylepane)
    {
        super(plot, chartstylepane);
    }

    public String title4PopupWindow()
    {
        return PaneTitleConstants.CHART_STYLE_AREA_TITLE;
    }

    protected List initPaneList(Plot plot, AbstractAttrNoScrollPane abstractattrnoscrollpane)
    {
        ArrayList arraylist = new ArrayList();
        areaPane = new ChartWholeAreaPane();
        plotPane = new ChartPlotAreaPane();
        if(abstractattrnoscrollpane instanceof ChartStylePane)
            plotPane.setParentPane((ChartStylePane)abstractattrnoscrollpane);
        JTemplate jtemplate = HistoryTemplateListPane.getInstance().getCurrentEditingTemplate();
        if(jtemplate.isJWorkBook() || jtemplate.getEditingReportIndex() == 1)
            arraylist.add(new com.fr.design.mainframe.chart.gui.style.ThirdTabPane.NamePane(areaPane.title4PopupWindow(), areaPane));
        else
        if(jtemplate.isChartBook())
            arraylist.add(new com.fr.design.mainframe.chart.gui.style.ThirdTabPane.NamePane(areaPane.title4PopupWindow(), areaPane));
        if(plot.isSupportPlotBackground())
            arraylist.add(new com.fr.design.mainframe.chart.gui.style.ThirdTabPane.NamePane(plotPane.title4PopupWindow(), plotPane));
        return arraylist;
    }

    protected int getContentPaneWidth()
    {
        return 220;
    }

    public void populateBean(Chart chart)
    {
        areaPane.populateBean(chart);
        plotPane.populateBean(chart);
    }

    public void updateBean(Chart chart)
    {
        areaPane.updateBean(chart);
        plotPane.updateBean(chart);
    }

    public void setSelectedIndex(String s)
    {
        int i = 0;
        do
        {
            if(i >= paneList.size())
                break;
            if(ComparatorUtils.equals(s, nameArray[i]))
            {
                tabPane.setSelectedIndex(i);
                break;
            }
            i++;
        } while(true);
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
