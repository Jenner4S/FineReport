// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.chart.gui;

import com.fr.chart.chartattr.Chart;
import com.fr.chart.chartattr.ChartCollection;
import com.fr.chart.chartattr.Plot;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.dialog.BasicPane;
import com.fr.design.gui.frpane.AttributeChangeListener;
import com.fr.design.gui.ibutton.UITabGroup;
import com.fr.design.mainframe.chart.AbstractChartAttrPane;
import com.fr.design.mainframe.chart.PaneTitleConstants;
import com.fr.design.mainframe.chart.gui.style.analysisline.ChartAnalysisLinePane;
import com.fr.design.mainframe.chart.gui.style.area.ChartAreaPane;
import com.fr.design.mainframe.chart.gui.style.axis.ChartAxisPane;
import com.fr.design.mainframe.chart.gui.style.datalabel.ChartDataLabelPane;
import com.fr.design.mainframe.chart.gui.style.datalabel.ChartLabelFontPane;
import com.fr.design.mainframe.chart.gui.style.datasheet.ChartDatasheetPane;
import com.fr.design.mainframe.chart.gui.style.legend.ChartLegendPane;
import com.fr.design.mainframe.chart.gui.style.series.ChartSeriesPane;
import com.fr.design.mainframe.chart.gui.style.title.ChartTitlePane;
import com.fr.design.mainframe.chart.gui.type.ChartTabPane;
import java.awt.BorderLayout;
import java.util.ArrayList;
import javax.swing.JPanel;

public class ChartStylePane extends AbstractChartAttrPane
{
    class TabPane extends ChartTabPane
    {

        final ChartStylePane this$0;

        protected void tabChanged()
        {
            removeAttributeChangeListener();
            ((BasicBeanPane)paneList.get(tabPane.getSelectedIndex())).populateBean(chart);
            addAttributeChangeListener(listener);
        }

        protected java.util.List initPaneList()
        {
            return getPaneList();
        }

        public void populateBean(Chart chart1)
        {
            if(chart1 == null || stylePane.getSelectedIndex() == -1)
            {
                return;
            } else
            {
                ((BasicBeanPane)paneList.get(stylePane.getSelectedIndex())).populateBean(chart1);
                return;
            }
        }

        public Chart updateBean()
        {
            if(chart == null)
            {
                return null;
            } else
            {
                ((BasicBeanPane)paneList.get(stylePane.getSelectedIndex())).updateBean(chart);
                return chart;
            }
        }

        public void updateBean(Chart chart1)
        {
            ((BasicBeanPane)paneList.get(stylePane.getSelectedIndex())).updateBean(chart1);
        }

        public volatile void updateBean(Object obj)
        {
            updateBean((Chart)obj);
        }

        public volatile void populateBean(Object obj)
        {
            populateBean((Chart)obj);
        }

        public volatile Object updateBean()
        {
            return updateBean();
        }

        TabPane()
        {
            this$0 = ChartStylePane.this;
            super();
        }
    }


    private static final long serialVersionUID = 0x7bbd92e9ae0082fbL;
    private TabPane stylePane;
    private Chart chart;
    private AttributeChangeListener listener;
    private BasicPane chartAxisPane;

    protected Chart getChart()
    {
        return chart;
    }

    public ChartStylePane()
    {
    }

    public ChartStylePane(AttributeChangeListener attributechangelistener)
    {
        listener = attributechangelistener;
    }

    public ChartStylePane(AttributeChangeListener attributechangelistener, boolean flag)
    {
        listener = attributechangelistener;
    }

    protected JPanel createContentPane()
    {
        JPanel jpanel = new JPanel(new BorderLayout());
        if(chart == null)
        {
            return jpanel;
        } else
        {
            stylePane = new TabPane();
            jpanel.add(stylePane, "Center");
            return jpanel;
        }
    }

    public void populate(ChartCollection chartcollection)
    {
        chart = chartcollection.getSelectedChart();
        remove(leftContentPane);
        initContentPane();
        removeAttributeChangeListener();
        stylePane.populateBean(chart);
        addAttributeChangeListener(listener);
        initAllListeners();
    }

    public void update(ChartCollection chartcollection)
    {
        stylePane.updateBean(chartcollection.getSelectedChart());
    }

    protected java.util.List getPaneList()
    {
        ArrayList arraylist = new ArrayList();
        Plot plot = chart.getPlot();
        arraylist.add(new ChartTitlePane());
        if(plot.isSupportLegend())
            arraylist.add(new ChartLegendPane());
        else
            plot.setLegend(null);
        if(plot.isSupportDataSeriesAttr())
            arraylist.add(createChartSeriesPane());
        if(plot.isSupportDataLabelAttr())
            arraylist.add(new ChartDataLabelPane(this));
        if(plot.isMeterPlot())
            arraylist.add(new ChartLabelFontPane());
        if(plot.isHaveAxis())
        {
            arraylist.add(createAxisPane());
            arraylist.add(createAreaPane());
            if(plot.isSupportDataSheet())
                arraylist.add(createDataSheetPane());
        } else
        {
            arraylist.add(createAreaPane());
        }
        if(plot.needAnalysisLinePane())
            arraylist.add(new ChartAnalysisLinePane(this));
        return arraylist;
    }

    protected ChartSeriesPane createChartSeriesPane()
    {
        return new ChartSeriesPane(this);
    }

    private BasicPane createDataSheetPane()
    {
        ChartDatasheetPane chartdatasheetpane = new ChartDatasheetPane();
        chartdatasheetpane.useWithAxis((ChartAxisPane)chartAxisPane);
        return chartdatasheetpane;
    }

    private BasicPane createAxisPane()
    {
        return chartAxisPane = new ChartAxisPane(chart.getPlot(), this);
    }

    private BasicPane createAreaPane()
    {
        return new ChartAreaPane(chart.getPlot(), this);
    }

    public String getIconPath()
    {
        return "com/fr/design/images/chart/ChartStyle.png";
    }

    public String title4PopupWindow()
    {
        return PaneTitleConstants.CHART_STYLE_TITLE;
    }

    public transient void setSelectedByIds(int i, String as[])
    {
        stylePane.setSelectedByIds(i, as);
    }




}
