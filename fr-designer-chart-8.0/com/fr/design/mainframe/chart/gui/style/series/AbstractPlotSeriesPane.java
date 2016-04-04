// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.chart.gui.style.series;

import com.fr.chart.chartattr.*;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.chart.gui.ChartStylePane;
import com.fr.design.mainframe.chart.gui.style.ChartFillStylePane;
import com.fr.general.Inter;
import java.awt.BorderLayout;
import java.awt.Component;
import javax.swing.*;

public abstract class AbstractPlotSeriesPane extends BasicBeanPane
{

    protected ChartFillStylePane fillStylePane;
    protected Plot plot;
    protected ChartStylePane parentPane;
    protected Chart chart;

    public AbstractPlotSeriesPane(ChartStylePane chartstylepane, Plot plot1)
    {
        this(chartstylepane, plot1, false);
    }

    public AbstractPlotSeriesPane(ChartStylePane chartstylepane, Plot plot1, boolean flag)
    {
        plot = plot1;
        parentPane = chartstylepane;
        fillStylePane = getFillStylePane();
        double d = -2D;
        double d1 = -1D;
        double ad[] = {
            d1
        };
        double ad1[] = {
            d, d, d
        };
        Component acomponent[][] = new Component[3][1];
        if(flag)
        {
            if(!(plot1 instanceof Bar2DPlot))
            {
                acomponent[0] = (new Component[] {
                    getContentInPlotType()
                });
                acomponent[1] = (new Component[] {
                    new JSeparator()
                });
            }
            JPanel jpanel = TableLayoutHelper.createTableLayoutPane(acomponent, ad1, ad);
            JScrollPane jscrollpane = new JScrollPane();
            jscrollpane.setViewportView(jpanel);
            jscrollpane.setHorizontalScrollBarPolicy(31);
            setLayout(new BorderLayout());
            add(jscrollpane, "Center");
        } else
        {
            if(fillStylePane != null)
            {
                acomponent[0] = (new Component[] {
                    fillStylePane
                });
                acomponent[1] = (new Component[] {
                    new JSeparator()
                });
            }
            JPanel jpanel1 = getContentInPlotType();
            if(jpanel1 != null)
                acomponent[2] = (new Component[] {
                    jpanel1
                });
            JPanel jpanel2 = TableLayoutHelper.createTableLayoutPane(acomponent, ad1, ad);
            setLayout(new BorderLayout());
            add(jpanel2, "Center");
        }
    }

    protected abstract JPanel getContentInPlotType();

    protected ChartFillStylePane getFillStylePane()
    {
        return new ChartFillStylePane();
    }

    protected String title4PopupWindow()
    {
        return Inter.getLocText("FR-Chart-Data_Series");
    }

    public Plot updateBean()
    {
        return null;
    }

    public void populateBean(Plot plot1)
    {
        if(plot1 == null)
            return;
        if(fillStylePane != null)
            fillStylePane.populateBean(plot1.getPlotFillStyle());
    }

    public void updateBean(Plot plot1)
    {
        if(plot1 == null)
            return;
        if(fillStylePane != null)
            plot1.setPlotFillStyle(fillStylePane.updateBean());
    }

    public void setCurrentChart(Chart chart1)
    {
        chart = chart1;
    }

    public volatile void updateBean(Object obj)
    {
        updateBean((Plot)obj);
    }

    public volatile Object updateBean()
    {
        return updateBean();
    }

    public volatile void populateBean(Object obj)
    {
        populateBean((Plot)obj);
    }
}
