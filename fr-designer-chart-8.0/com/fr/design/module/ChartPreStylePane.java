// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.module;

import com.fr.base.ChartPreStyleManagerProvider;
import com.fr.base.ChartPreStyleServerManager;
import com.fr.chart.base.ChartPreStyle;
import com.fr.chart.chartattr.*;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.chart.gui.ChartComponent;
import com.fr.design.event.UIObserver;
import com.fr.design.event.UIObserverListener;
import com.fr.design.gui.ilable.BoldFontTextLabel;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.mainframe.chart.gui.style.ChartPreFillStylePane;
import com.fr.general.Inter;
import java.awt.*;
import javax.swing.JPanel;

public class ChartPreStylePane extends BasicBeanPane
{

    private ChartPreFillStylePane fillStylePane;
    private ChartComponent chartComponent;

    public ChartPreStylePane()
    {
        setLayout(FRGUIPaneFactory.createBorderLayout());
        fillStylePane = new ChartPreFillStylePane();
        JPanel jpanel = new JPanel();
        jpanel.setLayout(new FlowLayout(0));
        jpanel.add(new BoldFontTextLabel(Inter.getLocText("FR-Designer_Preview")));
        JPanel jpanel1 = new JPanel();
        jpanel1.setLayout(new BorderLayout());
        jpanel1.add(jpanel, "South");
        jpanel1.add(fillStylePane, "Center");
        add(jpanel1, "Center");
        ChartCollection chartcollection = new ChartCollection();
        chartcollection.addChart(new Chart(new Bar2DPlot()));
        chartComponent = new ChartComponent();
        chartComponent.populate(chartcollection);
        chartComponent.setPreferredSize(new Dimension(400, 300));
        chartComponent.setSupportEdit(false);
        add(chartComponent, "South");
        initListener(this);
    }

    private void initListener(Container container)
    {
        for(int i = 0; i < container.getComponentCount(); i++)
        {
            Component component = container.getComponent(i);
            if(component instanceof Container)
                initListener((Container)component);
            if(component instanceof UIObserver)
                ((UIObserver)component).registerChangeListener(new UIObserverListener() {

                    final ChartPreStylePane this$0;

                    public void doChange()
                    {
                        refreshWhenStyleChange(updateBean());
                    }

            
            {
                this$0 = ChartPreStylePane.this;
                super();
            }
                }
);
        }

    }

    private void refreshWhenStyleChange(ChartPreStyle chartprestyle)
    {
        ChartPreStyleManagerProvider chartprestylemanagerprovider = ChartPreStyleServerManager.getProviderInstance();
        chartprestylemanagerprovider.setStyleEditing(chartprestyle);
        if(chartComponent != null)
            chartComponent.reset();
    }

    public void populateBean(ChartPreStyle chartprestyle)
    {
        fillStylePane.populateBean(chartprestyle.getAttrFillStyle());
        refreshWhenStyleChange(chartprestyle);
    }

    public ChartPreStyle updateBean()
    {
        ChartPreStyle chartprestyle = new ChartPreStyle();
        chartprestyle.setAttrFillStyle(fillStylePane.updateBean());
        return chartprestyle;
    }

    protected String title4PopupWindow()
    {
        return Inter.getLocText("ServerM-Predefined_Styles");
    }

    public volatile Object updateBean()
    {
        return updateBean();
    }

    public volatile void populateBean(Object obj)
    {
        populateBean((ChartPreStyle)obj);
    }

}
