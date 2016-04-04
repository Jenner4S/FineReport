// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.chart.gui.type;

import com.fr.base.FRContext;
import com.fr.chart.chartattr.Axis;
import com.fr.chart.chartattr.Chart;
import com.fr.chart.chartattr.ChartCollection;
import com.fr.chart.chartattr.Plot;
import com.fr.design.mainframe.ChartDesigner;
import com.fr.design.mainframe.chart.ChartDesignEditPane;
import com.fr.design.mainframe.chart.ChartEditPane;
import com.fr.form.ui.ChartBook;
import com.fr.general.ComparatorUtils;
import com.fr.general.FRLogger;
import com.fr.js.NameJavaScriptGroup;
import java.awt.Color;
import java.awt.FlowLayout;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

// Referenced classes of package com.fr.design.mainframe.chart.gui.type:
//            ChartDesignerImagePane

public abstract class PlotPane4ToolBar extends JPanel
{

    private static final int COM_GAP = 14;
    protected java.util.List typeDemo;
    private int selectedIndex;
    private ChartDesigner chartDesigner;
    private ChangeListener changeListener;

    protected abstract String getTypeIconPath();

    protected abstract java.util.List initDemoList();

    public PlotPane4ToolBar(ChartDesigner chartdesigner)
    {
        selectedIndex = 0;
        changeListener = new ChangeListener() {

            final PlotPane4ToolBar this$0;

            public void stateChanged(ChangeEvent changeevent)
            {
                fireChange();
            }

            
            {
                this$0 = PlotPane4ToolBar.this;
                super();
            }
        }
;
        chartDesigner = chartdesigner;
        typeDemo = initDemoList();
        setLayout(new FlowLayout(0, 14, 0));
        for(int i = 0; i < typeDemo.size(); i++)
        {
            ChartDesignerImagePane chartdesignerimagepane = (ChartDesignerImagePane)typeDemo.get(i);
            chartdesignerimagepane.registeChangeListener(changeListener);
            add(chartdesignerimagepane);
        }

        setSelectedIndex(0);
    }

    public void clearChoose()
    {
        if(typeDemo == null)
            return;
        for(int i = 0; i < typeDemo.size(); i++)
        {
            ((ChartDesignerImagePane)typeDemo.get(i)).setSelected(false);
            repaint();
        }

    }

    public void setSelectedIndex(int i)
    {
        clearChoose();
        selectedIndex = i;
        ((ChartDesignerImagePane)typeDemo.get(i)).setSelected(true);
    }

    public int getSelectedIndex()
    {
        return selectedIndex;
    }

    protected Plot getSelectedClonedPlot()
    {
        return null;
    }

    public void fireChange()
    {
        ChartCollection chartcollection = (ChartCollection)((ChartBook)chartDesigner.getTarget()).getChartCollection();
        Chart chart = chartcollection.getSelectedChart();
        chart.switchPlot(getSelectedClonedPlot());
        resetChart(chart);
        chartDesigner.clearToolBarStyleChoose();
        chartDesigner.fireTargetModified();
        ChartDesignEditPane.getInstance().populateSelectedTabPane();
    }

    protected void resetChart(Chart chart)
    {
        chart.setBorderStyle(0);
        chart.setBorderColor(new Color(150, 150, 150));
        chart.setBackground(null);
    }

    public Plot setSelectedClonedPlotWithCondition(Plot plot)
    {
        Plot plot1 = getSelectedClonedPlot();
        if(plot != null && ComparatorUtils.equals(plot1.getClass(), plot.getClass()))
        {
            if(plot.getHotHyperLink() != null)
            {
                NameJavaScriptGroup namejavascriptgroup = plot.getHotHyperLink();
                try
                {
                    plot1.setHotHyperLink((NameJavaScriptGroup)namejavascriptgroup.clone());
                }
                catch(CloneNotSupportedException clonenotsupportedexception)
                {
                    FRContext.getLogger().error("Error in Hyperlink, Please Check it.", clonenotsupportedexception);
                }
            }
            plot1.setConditionCollection(plot.getConditionCollection());
            plot1.setSeriesDragEnable(plot.isSeriesDragEnable());
            if(plot1.isSupportZoomCategoryAxis() && plot1.getxAxis() != null)
                plot1.getxAxis().setZoom(plot.getxAxis().isZoom());
            if(plot1.isSupportTooltipInInteractivePane())
                plot1.setHotTooltipStyle(plot.getHotTooltipStyle());
            if(plot1.isSupportAutoRefresh())
                plot1.setAutoRefreshPerSecond(plot.getAutoRefreshPerSecond());
        }
        return plot1;
    }
}
