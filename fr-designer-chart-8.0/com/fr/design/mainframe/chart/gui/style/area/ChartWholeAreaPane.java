// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.chart.gui.style.area;

import com.fr.chart.chartattr.Chart;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.chart.PaneTitleConstants;
import com.fr.design.mainframe.chart.gui.style.*;
import java.awt.BorderLayout;
import java.awt.Component;
import javax.swing.JPanel;

public class ChartWholeAreaPane extends AbstractChartTabPane
{
    private class ContentPane extends JPanel
    {

        private static final long serialVersionUID = 0x823be8010eefd215L;
        final ChartWholeAreaPane this$0;

        private void initComponents()
        {
            chartBorderPane = new ChartBorderPane();
            chargBackgroundPane = new ChartBackgroundPane();
            double d = -2D;
            double d1 = -1D;
            double ad[] = {
                d1
            };
            double ad1[] = {
                d, d, d
            };
            Component acomponent[][] = {
                {
                    chartBorderPane
                }, {
                    chargBackgroundPane
                }
            };
            JPanel jpanel = TableLayoutHelper.createTableLayoutPane(acomponent, ad1, ad);
            setLayout(new BorderLayout());
            add(jpanel, "Center");
        }

        public ContentPane()
        {
            this$0 = ChartWholeAreaPane.this;
            super();
            initComponents();
        }
    }


    private static final long serialVersionUID = 0x829fe2bb2ac02c5bL;
    private ChartBorderPane chartBorderPane;
    private ChartBackgroundPane chargBackgroundPane;

    public ChartWholeAreaPane()
    {
    }

    protected JPanel createContentPane()
    {
        return new ContentPane();
    }

    public String title4PopupWindow()
    {
        return PaneTitleConstants.CHART_STYLE_AREA_AREA_TITLE;
    }

    public void updateBean(Chart chart)
    {
        if(chart == null)
            chart = new Chart();
        chartBorderPane.update(chart);
        chargBackgroundPane.update(chart);
    }

    public void populateBean(Chart chart)
    {
        if(chart == null)
        {
            return;
        } else
        {
            chartBorderPane.populate(chart);
            chargBackgroundPane.populate(chart);
            return;
        }
    }

    public Chart updateBean()
    {
        return null;
    }

    public volatile Object updateBean()
    {
        return updateBean();
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
