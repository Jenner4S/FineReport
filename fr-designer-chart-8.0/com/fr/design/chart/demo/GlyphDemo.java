// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.chart.demo;

import com.fr.chart.chartattr.Chart;
import com.fr.chart.chartattr.ChartCollection;
import com.fr.chart.charttypes.*;
import com.fr.design.chart.gui.ChartComponent;
import com.fr.design.gui.frpane.UITabbedPane;
import com.fr.design.layout.FRGUIPaneFactory;
import java.awt.GridLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class GlyphDemo extends JFrame
{

    private static final long serialVersionUID = 0xd3f290fbbdbca351L;

    public static void main(String args[])
    {
        new GlyphDemo();
    }

    public GlyphDemo()
    {
        JPanel jpanel = (JPanel)getContentPane();
        jpanel.setLayout(FRGUIPaneFactory.createBorderLayout());
        UITabbedPane uitabbedpane = new UITabbedPane();
        jpanel.add(uitabbedpane, "Center");
        uitabbedpane.add("Line", createGlyphLinePanel());
        uitabbedpane.add("Bar", createBarPanel());
        uitabbedpane.add("Area", createAreaPanel());
        uitabbedpane.add("Meter", createMeterPanel());
        uitabbedpane.add("Pie", createPiePanel());
        uitabbedpane.add("Radar", createRadarPanel());
        uitabbedpane.add("Stock", createStockPanel());
        uitabbedpane.add("XYScatter", createXYScatterPanel());
        uitabbedpane.add("Range", createRangePanel());
        uitabbedpane.add("Mix", createCustomPanel());
        uitabbedpane.add("Gantt", createGanttPanel());
        uitabbedpane.add("Bubble", createBubblePane());
        setSize(1200, 600);
        setDefaultCloseOperation(3);
        setTitle("Line Glyph Demo");
        setVisible(true);
    }

    private JPanel createGlyphLinePanel()
    {
        Chart achart[] = LineIndependentChart.lineChartTypes;
        return contentPane(achart, 2, 4);
    }

    private JPanel createBarPanel()
    {
        Chart achart[] = ColumnIndependentChart.columnChartTypes;
        return contentPane(achart, 2, 4);
    }

    private JPanel createAreaPanel()
    {
        Chart achart[] = AreaIndependentChart.areaChartTypes;
        return contentPane(achart, 2, 4);
    }

    private JPanel createMeterPanel()
    {
        Chart achart[] = MeterIndependentChart.meterChartTypes;
        return contentPane(achart, 2, 4);
    }

    private JPanel createPiePanel()
    {
        Chart achart[] = PieIndependentChart.pieChartTypes;
        JPanel jpanel = contentPane(achart, 2, 4);
        return jpanel;
    }

    private JPanel createRadarPanel()
    {
        Chart achart[] = RadarIndependentChart.radarChartTypes;
        return contentPane(achart, 2, 4);
    }

    private JPanel createStockPanel()
    {
        Chart achart[] = StockIndependentChart.stockChartTypes;
        return contentPane(achart, 2, 4);
    }

    private JPanel createXYScatterPanel()
    {
        Chart achart[] = XYScatterIndependentChart.XYScatterChartTypes;
        return contentPane(achart, 2, 4);
    }

    private JPanel createRangePanel()
    {
        Chart achart[] = RangeIndependentChart.rangeChartTypes;
        return contentPane(achart, 2, 4);
    }

    private JPanel createCustomPanel()
    {
        Chart achart[] = CustomIndependentChart.combChartTypes;
        return contentPane(achart, 2, 4);
    }

    private JPanel createGanttPanel()
    {
        Chart achart[] = GanttIndependentChart.ganttChartTypes;
        return contentPane(achart, 2, 4);
    }

    private JPanel createBubblePane()
    {
        Chart achart[] = BubbleIndependentChart.bubbleChartTypes;
        return contentPane(achart, 2, 4);
    }

    private JPanel contentPane(Chart achart[], int i, int j)
    {
        JPanel jpanel = FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();
        jpanel.setLayout(new GridLayout(i, j));
        for(int k = 0; k < achart.length; k++)
            jpanel.add(new ChartComponent(new ChartCollection(achart[k])));

        return jpanel;
    }
}
