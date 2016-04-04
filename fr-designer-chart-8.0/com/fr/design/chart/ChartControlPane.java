// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.chart;

import com.fr.chart.chartattr.*;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.gui.controlpane.*;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.general.Inter;
import com.fr.general.NameObject;
import java.util.HashMap;

// Referenced classes of package com.fr.design.chart:
//            ChartTypePane

public class ChartControlPane extends JControlPane
{
    public static class ChartTypeUpdatePane extends BasicBeanPane
    {

        private static final long serialVersionUID = 0x9e0bb50b4df0ead1L;
        private Chart editing;
        private ChartTypePane typePane;

        protected String title4PopupWindow()
        {
            return "Chart Type";
        }

        public void populateBean(Chart chart)
        {
            editing = chart;
            typePane.populate(chart);
        }

        public Chart updateBean()
        {
            typePane.update(editing);
            return editing;
        }

        public volatile Object updateBean()
        {
            return updateBean();
        }

        public volatile void populateBean(Object obj)
        {
            populateBean((Chart)obj);
        }

        public ChartTypeUpdatePane()
        {
            setLayout(FRGUIPaneFactory.createBorderLayout());
            typePane = new ChartTypePane();
            add(typePane, "Center");
        }
    }


    private static final long serialVersionUID = 0x65cfab6fe7cb6400L;

    public ChartControlPane()
    {
    }

    public NameableCreator[] createNameableCreators()
    {
        return (new NameableCreator[] {
            new NameObjectCreator(Inter.getLocText("Chart"), com/fr/chart/chartattr/Chart, com/fr/design/chart/ChartControlPane$ChartTypeUpdatePane)
        });
    }

    protected String title4PopupWindow()
    {
        return Inter.getLocText("Chart-Manage_Chart_Type");
    }

    public void populate(ChartCollection chartcollection)
    {
        if(chartcollection == null)
            return;
        NameObject anameobject[] = new NameObject[chartcollection.getChartCount()];
        for(int i = 0; i < anameobject.length; i++)
            anameobject[i] = new NameObject(chartcollection.getChartName(i), chartcollection.getChart(i));

        populate(((com.fr.stable.Nameable []) (anameobject)));
        String s = chartcollection.getChartName(chartcollection.getSelectedIndex() >= chartcollection.getChartCount() ? 0 : chartcollection.getSelectedIndex());
        setSelectedName(s);
    }

    public void update(ChartCollection chartcollection)
    {
        HashMap hashmap = new HashMap();
        for(int i = 0; i < chartcollection.getChartCount(); i++)
            try
            {
                hashmap.put(chartcollection.getChartName(i), chartcollection.getChart(i).clone());
            }
            catch(CloneNotSupportedException clonenotsupportedexception) { }

        com.fr.stable.Nameable anameable[] = update();
        if(anameable.length == 0 || chartcollection == null)
            return;
        chartcollection.removeAllNameObject();
        String s = getSelectedName();
        for(int j = 0; j < anameable.length; j++)
        {
            if(!(anameable[j] instanceof NameObject) || !(((NameObject)anameable[j]).getObject() instanceof Chart))
                continue;
            NameObject nameobject = (NameObject)anameable[j];
            String s1 = nameobject.getName();
            Chart chart = (Chart)nameobject.getObject();
            if(hashmap.containsKey(s1))
            {
                Chart chart1 = (Chart)hashmap.get(s1);
                if(chart.getPlot() != null && chart1.getPlot() != null && chart.getPlot().match4GUI(chart1.getPlot()))
                    chart = chart1;
            }
            chartcollection.addNamedChart(s1, chart);
            if(nameobject.getName().equals(s))
                chartcollection.setSelectedIndex(j);
        }

    }
}
