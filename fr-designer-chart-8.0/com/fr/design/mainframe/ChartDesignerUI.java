// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe;

import com.fr.base.BaseUtils;
import com.fr.base.ScreenResolution;
import com.fr.base.chart.BaseChartGlyph;
import com.fr.chart.chartattr.*;
import com.fr.chart.charttypes.ColumnIndependentChart;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.mainframe.chart.ChartDesignEditPane;
import com.fr.design.mainframe.chart.ChartEditPane;
import com.fr.form.ui.ChartBook;
import com.fr.general.FRLogger;
import java.awt.*;
import java.awt.event.MouseEvent;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;

// Referenced classes of package com.fr.design.mainframe:
//            ChartDesigner, ChartArea, ToolTip4Chart

public class ChartDesignerUI extends ComponentUI
{

    private static final Icon ADD = BaseUtils.readIcon("/com/fr/design/images/add.png");
    private static final Icon DEL = BaseUtils.readIcon("/com/fr/design/images/del.png");
    private static final int ICON_SIZE = 22;
    private static final int H_GAP = 2;
    private static final int V_GAP = 6;
    private Rectangle iconLocations[];
    private Rectangle add;
    private Rectangle del;
    private UILabel tooltipLabel;
    private int overIndex;
    private ChartDesigner designer;

    public ChartDesignerUI()
    {
        overIndex = -1;
    }

    public void installUI(JComponent jcomponent)
    {
        designer = (ChartDesigner)jcomponent;
    }

    public void paint(Graphics g, JComponent jcomponent)
    {
        ChartCollection chartcollection = (ChartCollection)((ChartBook)designer.getTarget()).getChartCollection();
        Chart chart = chartcollection.getSelectedChart();
        BaseChartGlyph basechartglyph = null;
        if(chart != null && chart.getPlot() != null)
            basechartglyph = chart.createGlyph(chart.defaultChartData());
        int i = designer.getSize().width;
        int j = designer.getSize().height;
        int k = designer.getArea().getCustomWidth();
        int l = designer.getArea().getCustomHeight();
        Graphics g1 = g.create(-designer.getArea().getHorizontalValue(), -designer.getArea().getVerticalValue(), i + designer.getArea().getHorizontalValue(), j + designer.getArea().getVerticalValue());
        g1 = g1.create(1, 1, designer.getArea().getCustomWidth(), designer.getArea().getCustomHeight());
        g.setColor(Color.white);
        g.fillRect(0, 0, k, l);
        basechartglyph.setUseChangeChart(true);
        java.awt.Image image = basechartglyph.toImage(k, l, ScreenResolution.getScreenResolution());
        g1.drawImage(image, 0, 0, k, l, null);
        paintChange(g1, jcomponent);
    }

    private void paintChange(Graphics g, JComponent jcomponent)
    {
        int i = designer.getArea().getCustomWidth();
        ChartCollection chartcollection = (ChartCollection)((ChartBook)designer.getTarget()).getChartCollection();
        int j = chartcollection.getChartCount();
        iconLocations = new Rectangle[j];
        int k = i - 6 - 22;
        if(j == 1)
        {
            ADD.paintIcon(jcomponent, g, k, 2);
            add = new Rectangle(k, 2, 22, 22);
            del = null;
        } else
        {
            DEL.paintIcon(jcomponent, g, k, 2);
            del = new Rectangle(k, 2, 22, 22);
            k -= 28;
            ADD.paintIcon(jcomponent, g, k, 2);
            add = new Rectangle(k, 2, 22, 22);
        }
        for(int l = j - 1; l >= 0; l--)
        {
            Plot plot = chartcollection.getChart(l).getPlot();
            if(plot == null)
                continue;
            if(chartcollection.getSelectedIndex() == l)
            {
                Icon icon = BaseUtils.readIcon((new StringBuilder()).append(plot.getPlotSmallIconPath()).append("_normal.png").toString());
                if(icon != null)
                {
                    k -= 28;
                    icon.paintIcon(jcomponent, g, k, 2);
                }
            } else
            if(overIndex == l)
            {
                Icon icon1 = BaseUtils.readIcon((new StringBuilder()).append(plot.getPlotSmallIconPath()).append("_over.png").toString());
                if(icon1 != null)
                {
                    k -= 28;
                    icon1.paintIcon(jcomponent, g, k, 2);
                }
            } else
            {
                Icon icon2 = BaseUtils.readIcon((new StringBuilder()).append(plot.getPlotSmallIconPath()).append("_gray.png").toString());
                if(icon2 != null)
                {
                    k -= 28;
                    icon2.paintIcon(jcomponent, g, k, 2);
                }
            }
            iconLocations[l] = new Rectangle(k, 2, 22, 22);
        }

    }

    public void mouseClicked(MouseEvent mouseevent)
    {
        Point point = new Point(mouseevent.getPoint().x + designer.getArea().getHorizontalValue(), mouseevent.getPoint().y + designer.getArea().getVerticalValue());
        ChartCollection chartcollection = (ChartCollection)((ChartBook)designer.getTarget()).getChartCollection();
        for(int i = 0; i < iconLocations.length; i++)
            if(iconLocations[i].contains(point))
                if(i == chartcollection.getSelectedIndex())
                {
                    return;
                } else
                {
                    chartcollection.setSelectedIndex(i);
                    designer.repaint();
                    ChartDesignEditPane.getInstance().populateSelectedTabPane();
                    return;
                }

        if(add.contains(point))
        {
            Chart achart[] = ColumnIndependentChart.columnChartTypes;
            try
            {
                Chart chart = (Chart)achart[0].clone();
                int k = chartcollection.getSelectedIndex();
                chartcollection.addNamedChartAtIndex(chart.getTitle().getTextObject().toString(), chart, k + 1);
                chartcollection.setSelectedIndex(k + 1);
                ChartDesignEditPane.getInstance().populateSelectedTabPane();
            }
            catch(CloneNotSupportedException clonenotsupportedexception)
            {
                FRLogger.getLogger().error("Error in Clone");
            }
            designer.fireTargetModified();
            ChartDesignEditPane.getInstance().populateSelectedTabPane();
            return;
        }
        if(del != null && del.contains(point))
        {
            int j = chartcollection.getSelectedIndex();
            chartcollection.removeNameObject(j);
            if(j > 0)
                chartcollection.setSelectedIndex(j - 1);
            else
                chartcollection.setSelectedIndex(0);
            designer.fireTargetModified();
            ChartDesignEditPane.getInstance().populateSelectedTabPane();
        }
    }

    public void mouseMoved(MouseEvent mouseevent)
    {
        Point point = new Point(mouseevent.getPoint().x + designer.getArea().getHorizontalValue(), mouseevent.getPoint().y + designer.getArea().getVerticalValue());
        if(point.getY() < 2D || point.getY() > 24D)
        {
            ToolTip4Chart.getInstance().hideToolTip();
            overIndex = -1;
            return;
        }
        ChartCollection chartcollection = (ChartCollection)((ChartBook)designer.getTarget()).getChartCollection();
        for(int i = 0; i < iconLocations.length; i++)
            if(iconLocations[i].contains(point))
            {
                overIndex = i;
                String s = chartcollection.getChartName(i);
                ToolTip4Chart.getInstance().showToolTip(s, mouseevent.getXOnScreen(), mouseevent.getYOnScreen());
                return;
            }

        ToolTip4Chart.getInstance().hideToolTip();
        overIndex = -1;
    }

}
