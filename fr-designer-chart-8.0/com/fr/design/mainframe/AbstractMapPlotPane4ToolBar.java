// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe;

import com.fr.chart.base.MapSvgXMLHelper;
import com.fr.chart.chartattr.Chart;
import com.fr.chart.chartattr.ChartCollection;
import com.fr.chart.chartattr.Plot;
import com.fr.chart.chartattr.Title;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.mainframe.chart.ChartDesignEditPane;
import com.fr.design.mainframe.chart.ChartEditPane;
import com.fr.form.ui.ChartBook;
import com.fr.general.ComparatorUtils;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Iterator;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

// Referenced classes of package com.fr.design.mainframe:
//            ChartDesigner

public abstract class AbstractMapPlotPane4ToolBar extends JPanel
{

    protected static final int COM_HEIGHT = 22;
    protected static final int COM_GAP = 14;
    protected static final int COMBOX_WIDTH = 230;
    protected ChartDesigner chartDesigner;
    protected UIComboBox mapTypeComboBox;
    protected ItemListener mapTypeListener;

    public AbstractMapPlotPane4ToolBar(ChartDesigner chartdesigner)
    {
        mapTypeListener = new ItemListener() {

            final AbstractMapPlotPane4ToolBar this$0;

            public void itemStateChanged(ItemEvent itemevent)
            {
                calculateDetailMaps(mapTypeComboBox.getSelectedIndex());
            }

            
            {
                this$0 = AbstractMapPlotPane4ToolBar.this;
                super();
            }
        }
;
        chartDesigner = chartdesigner;
        setLayout(new FlowLayout(0, 14, 0));
        setBorder(new EmptyBorder(2, 0, 2, 0));
        mapTypeComboBox = new UIComboBox(getMapTypes()) {

            final AbstractMapPlotPane4ToolBar this$0;

            public Dimension getPreferredSize()
            {
                return new Dimension(230, 22);
            }

            
            {
                this$0 = AbstractMapPlotPane4ToolBar.this;
                super(aobj);
            }
        }
;
        mapTypeComboBox.addItemListener(mapTypeListener);
        add(mapTypeComboBox);
    }

    protected abstract void calculateDetailMaps(int i);

    public void populateMapPane(String s)
    {
        mapTypeComboBox.removeItemListener(mapTypeListener);
        String as[] = getMapTypes();
        int i = as.length;
label0:
        for(int j = 0; j < i; j++)
        {
            String s1 = as[j];
            java.util.List list = MapSvgXMLHelper.getInstance().getNamesListWithCateName(s1);
            Iterator iterator = list.iterator();
            Object obj;
            do
            {
                if(!iterator.hasNext())
                    continue label0;
                obj = iterator.next();
            } while(!ComparatorUtils.equals(obj, s));
            mapTypeComboBox.setSelectedItem(s1);
        }

        mapTypeComboBox.addItemListener(mapTypeListener);
    }

    public abstract String[] getMapTypes();

    public void fireChange()
    {
        ChartCollection chartcollection = (ChartCollection)((ChartBook)chartDesigner.getTarget()).getChartCollection();
        Chart chart = chartcollection.getSelectedChart();
        if(chart.getPlot().getPlotStyle() != 0)
            resetChart(chart);
        chart.switchPlot(getSelectedClonedPlot());
        if(chart.getPlot().getPlotStyle() != 0)
            resetChart(chart);
        chartDesigner.fireTargetModified();
        ChartDesignEditPane.getInstance().populateSelectedTabPane();
    }

    protected void resetChart(Chart chart)
    {
        chart.setTitle(new Title(chart.getTitle().getTextObject()));
        chart.setBorderStyle(0);
        chart.setBorderColor(new Color(150, 150, 150));
        chart.setBackground(null);
    }

    protected abstract Plot getSelectedClonedPlot();

    public void fireTargetModified()
    {
        chartDesigner.fireTargetModified();
    }
}
