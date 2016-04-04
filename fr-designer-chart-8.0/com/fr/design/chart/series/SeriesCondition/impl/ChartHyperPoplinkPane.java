// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.chart.series.SeriesCondition.impl;

import com.fr.base.chart.BaseChartCollection;
import com.fr.chart.chartattr.*;
import com.fr.chart.web.ChartHyperPoplink;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.chart.gui.ChartComponent;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.mainframe.chart.ChartEditPane;
import com.fr.design.mainframe.chart.ChartHyperEditPane;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.general.Inter;
import java.awt.Dimension;

public class ChartHyperPoplinkPane extends BasicBeanPane
{
    public static class CHART_METER extends ChartHyperPoplinkPane
    {

        protected int getChartParaType()
        {
            return 10;
        }

        public volatile void updateBean(Object obj)
        {
            updateBean((ChartHyperPoplink)obj);
        }

        public volatile Object updateBean()
        {
            return updateBean();
        }

        public volatile void populateBean(Object obj)
        {
            populateBean((ChartHyperPoplink)obj);
        }

        public CHART_METER()
        {
        }
    }

    public static class CHART_GANTT extends ChartHyperPoplinkPane
    {

        protected int getChartParaType()
        {
            return 9;
        }

        public volatile void updateBean(Object obj)
        {
            updateBean((ChartHyperPoplink)obj);
        }

        public volatile Object updateBean()
        {
            return updateBean();
        }

        public volatile void populateBean(Object obj)
        {
            populateBean((ChartHyperPoplink)obj);
        }

        public CHART_GANTT()
        {
        }
    }

    public static class CHART_STOCK extends ChartHyperPoplinkPane
    {

        protected int getChartParaType()
        {
            return 6;
        }

        public volatile void updateBean(Object obj)
        {
            updateBean((ChartHyperPoplink)obj);
        }

        public volatile Object updateBean()
        {
            return updateBean();
        }

        public volatile void populateBean(Object obj)
        {
            populateBean((ChartHyperPoplink)obj);
        }

        public CHART_STOCK()
        {
        }
    }

    public static class CHART_BUBBLE extends ChartHyperPoplinkPane
    {

        protected int getChartParaType()
        {
            return 5;
        }

        public volatile void updateBean(Object obj)
        {
            updateBean((ChartHyperPoplink)obj);
        }

        public volatile Object updateBean()
        {
            return updateBean();
        }

        public volatile void populateBean(Object obj)
        {
            populateBean((ChartHyperPoplink)obj);
        }

        public CHART_BUBBLE()
        {
        }
    }

    public static class CHART_XY extends ChartHyperPoplinkPane
    {

        protected int getChartParaType()
        {
            return 4;
        }

        public volatile void updateBean(Object obj)
        {
            updateBean((ChartHyperPoplink)obj);
        }

        public volatile Object updateBean()
        {
            return updateBean();
        }

        public volatile void populateBean(Object obj)
        {
            populateBean((ChartHyperPoplink)obj);
        }

        public CHART_XY()
        {
        }
    }

    public static class CHART_PIE extends ChartHyperPoplinkPane
    {

        protected int getChartParaType()
        {
            return 3;
        }

        public volatile void updateBean(Object obj)
        {
            updateBean((ChartHyperPoplink)obj);
        }

        public volatile Object updateBean()
        {
            return updateBean();
        }

        public volatile void populateBean(Object obj)
        {
            populateBean((ChartHyperPoplink)obj);
        }

        public CHART_PIE()
        {
        }
    }

    public static class CHART_GIS extends ChartHyperPoplinkPane
    {

        protected int getChartParaType()
        {
            return 8;
        }

        public volatile void updateBean(Object obj)
        {
            updateBean((ChartHyperPoplink)obj);
        }

        public volatile Object updateBean()
        {
            return updateBean();
        }

        public volatile void populateBean(Object obj)
        {
            populateBean((ChartHyperPoplink)obj);
        }

        public CHART_GIS()
        {
        }
    }

    public static class CHART_MAP extends ChartHyperPoplinkPane
    {

        protected int getChartParaType()
        {
            return 2;
        }

        public volatile void updateBean(Object obj)
        {
            updateBean((ChartHyperPoplink)obj);
        }

        public volatile Object updateBean()
        {
            return updateBean();
        }

        public volatile void populateBean(Object obj)
        {
            populateBean((ChartHyperPoplink)obj);
        }

        public CHART_MAP()
        {
        }
    }

    public static class CHART_NO_RENAME extends ChartHyperPoplinkPane
    {

        protected boolean needRenamePane()
        {
            return false;
        }

        public volatile void updateBean(Object obj)
        {
            updateBean((ChartHyperPoplink)obj);
        }

        public volatile Object updateBean()
        {
            return updateBean();
        }

        public volatile void populateBean(Object obj)
        {
            populateBean((ChartHyperPoplink)obj);
        }

        public CHART_NO_RENAME()
        {
        }
    }


    private static final long serialVersionUID = 0x22440fe03f0346e2L;
    private UITextField itemNameTextField;
    private ChartHyperEditPane hyperEditPane;
    private ChartComponent chartComponent;

    public ChartHyperPoplinkPane()
    {
        setLayout(FRGUIPaneFactory.createM_BorderLayout());
        if(needRenamePane())
        {
            itemNameTextField = new UITextField();
            add(GUICoreUtils.createNamedPane(itemNameTextField, (new StringBuilder()).append(Inter.getLocText("FR-Chart-Use_Name")).append(":").toString()), "North");
        }
        hyperEditPane = new ChartHyperEditPane(getChartParaType());
        add(hyperEditPane, "Center");
        ChartCollection chartcollection = new ChartCollection();
        chartcollection.addChart(new Chart(new Bar2DPlot()));
        chartComponent = new ChartComponent();
        chartComponent.setPreferredSize(new Dimension(220, 170));
        chartComponent.setSupportEdit(false);
        chartComponent.populate(chartcollection);
        add(chartComponent, "East");
        hyperEditPane.populate(chartcollection);
        hyperEditPane.useChartComponent(chartComponent);
    }

    protected int getChartParaType()
    {
        return 1;
    }

    protected boolean needRenamePane()
    {
        return true;
    }

    protected String title4PopupWindow()
    {
        return Inter.getLocText("FR-Chart-Pop_Chart");
    }

    public void populateBean(ChartHyperPoplink charthyperpoplink)
    {
        if(itemNameTextField != null)
            itemNameTextField.setText(charthyperpoplink.getItemName());
        Object obj = charthyperpoplink.getChartCollection();
        if(obj == null || ((BaseChartCollection) (obj)).getChartCount() < 1)
        {
            obj = new ChartCollection();
            ((BaseChartCollection) (obj)).addChart(new Chart(ChartFactory.createBar2DPlot()));
            charthyperpoplink.setChartCollection(((BaseChartCollection) (obj)));
        }
        hyperEditPane.populateHyperLink(charthyperpoplink);
        chartComponent.populate(((BaseChartCollection) (obj)));
    }

    public ChartHyperPoplink updateBean()
    {
        ChartHyperPoplink charthyperpoplink = new ChartHyperPoplink();
        updateBean(charthyperpoplink);
        if(itemNameTextField != null)
            charthyperpoplink.setItemName(itemNameTextField.getText());
        return charthyperpoplink;
    }

    public void updateBean(ChartHyperPoplink charthyperpoplink)
    {
        hyperEditPane.updateHyperLink(charthyperpoplink);
        charthyperpoplink.setChartCollection(chartComponent.update());
        ChartEditPane.getInstance().fire();
        if(itemNameTextField != null)
            charthyperpoplink.setItemName(itemNameTextField.getText());
    }

    public volatile void updateBean(Object obj)
    {
        updateBean((ChartHyperPoplink)obj);
    }

    public volatile Object updateBean()
    {
        return updateBean();
    }

    public volatile void populateBean(Object obj)
    {
        populateBean((ChartHyperPoplink)obj);
    }
}
