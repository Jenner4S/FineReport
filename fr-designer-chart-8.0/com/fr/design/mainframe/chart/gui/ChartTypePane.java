// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.chart.gui;

import com.fr.base.FRContext;
import com.fr.chart.chartattr.Chart;
import com.fr.chart.chartattr.ChartCollection;
import com.fr.chart.chartattr.Plot;
import com.fr.chart.charttypes.ChartTypeManager;
import com.fr.design.ChartTypeInterfaceManager;
import com.fr.design.beans.FurtherBasicBeanPane;
import com.fr.design.gui.frpane.UIComboBoxPane;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.mainframe.chart.AbstractChartAttrPane;
import com.fr.design.mainframe.chart.ChartEditPane;
import com.fr.design.mainframe.chart.PaneTitleConstants;
import com.fr.design.mainframe.chart.gui.type.AbstractChartTypePane;
import com.fr.general.FRLogger;
import java.awt.BorderLayout;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

// Referenced classes of package com.fr.design.mainframe.chart.gui:
//            ChartTypeButtonPane

public class ChartTypePane extends AbstractChartAttrPane
{
    class ComboBoxPane extends UIComboBoxPane
    {

        final ChartTypePane this$0;

        protected java.util.List initPaneList()
        {
            ArrayList arraylist = new ArrayList();
            ChartTypeInterfaceManager.getInstance().addPlotTypePaneList(arraylist);
            return arraylist;
        }

        protected String title4PopupWindow()
        {
            return null;
        }

        public void updateBean(Chart chart)
        {
            int i = editPane.getSelectedChartIndex(chart);
            try
            {
                Chart chart1 = (Chart)((AbstractChartTypePane)cards.get(jcb.getSelectedIndex())).getDefaultChart().clone();
                if(!chart.accept(chart1.getClass()))
                {
                    editingCollection.removeNameObject(editingCollection.getSelectedIndex());
                    editingCollection.addChart(chart1);
                    chart = chart1;
                }
            }
            catch(CloneNotSupportedException clonenotsupportedexception)
            {
                FRContext.getLogger().error(clonenotsupportedexception.getMessage(), clonenotsupportedexception);
            }
            ((AbstractChartTypePane)cards.get(jcb.getSelectedIndex())).updateBean(chart);
            Plot plot = chart.getPlot();
            if(plot != null)
            {
                String s = plot.getPlotID();
                chart.setWrapperName(ChartTypeManager.getInstance().getWrapperName(s));
                chart.setChartImagePath(ChartTypeManager.getInstance().getChartImagePath(s));
                boolean flag = ChartTypeInterfaceManager.getInstance().isUseDefaultPane(s);
                if(editPane.isDefaultPane() != flag || !flag && i != jcb.getSelectedIndex())
                    editPane.reLayout(chart);
            }
        }

        public volatile void updateBean(Object obj)
        {
            updateBean((Chart)obj);
        }

        ComboBoxPane()
        {
            this$0 = ChartTypePane.this;
            super();
        }
    }


    private ComboBoxPane chartTypePane;
    private ChartTypeButtonPane buttonPane;
    private ChartEditPane editPane;
    private ChartCollection editingCollection;

    public ChartTypePane()
    {
    }

    protected JPanel createContentPane()
    {
        JPanel jpanel = new JPanel(new BorderLayout());
        buttonPane = new ChartTypeButtonPane();
        jpanel.add(buttonPane, "North");
        chartTypePane = new ComboBoxPane();
        chartTypePane.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));
        jpanel.add(chartTypePane, "Center");
        buttonPane.setEditingChartPane(chartTypePane);
        return jpanel;
    }

    public String getIconPath()
    {
        return "com/fr/design/images/chart/ChartType.png";
    }

    public String title4PopupWindow()
    {
        return PaneTitleConstants.CHART_TYPE_TITLE;
    }

    public void populate(ChartCollection chartcollection)
    {
        Chart chart = chartcollection.getSelectedChart();
        chartTypePane.populateBean(chart);
        buttonPane.populateBean(chartcollection);
    }

    public void update(ChartCollection chartcollection)
    {
        editingCollection = chartcollection;
        buttonPane.update(chartcollection);
        Chart chart = chartcollection.getSelectedChart();
        chartTypePane.updateBean(chart);
    }

    public FurtherBasicBeanPane[] getPaneList()
    {
        return (FurtherBasicBeanPane[])chartTypePane.getCards().toArray(new FurtherBasicBeanPane[0]);
    }

    public int getSelectedIndex()
    {
        return chartTypePane.getSelectedIndex();
    }

    public int getSelectedChartIndex()
    {
        return chartTypePane.getSelectedIndex();
    }

    public void registerChartEditPane(ChartEditPane charteditpane)
    {
        editPane = charteditpane;
    }


}
